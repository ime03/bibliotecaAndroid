package it.insubria.biblioteca.utente

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import it.insubria.biblioteca.R
import it.insubria.biblioteca.dataClass.Libro
import it.insubria.biblioteca.dataClass.Prestito
import java.time.LocalDate

class DettagliLibroUtente : Fragment() {

    private var libro: Libro? = null
    private lateinit var auth:FirebaseAuth
    private lateinit var listaPrestiti: ArrayList<Prestito>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        libro = arguments?.getParcelable("libro") ?: Libro()
        listaPrestiti = arrayListOf<Prestito>()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dettagli_libro_u, container, false)
        libro?.let {
            view.findViewById<TextView>(R.id.descrizione_dettagli).text = it.isbn
            view.findViewById<TextView>(R.id.titolo_dettagli).text = it.titolo
            view.findViewById<TextView>(R.id.autore_dettagli).text = it.autore
            view.findViewById<TextView>(R.id.genere_dettagli).text = it.genere
            view.findViewById<TextView>(R.id.descrizione_dettagli).text = it.descrizione


            val copertinaImageView = view.findViewById<ImageView>(R.id.copertina_dettagli)
            Glide.with(requireContext())
                .load(it.copertina)
                .into(copertinaImageView)

            val but_prestito = view.findViewById<Button>(R.id.btn_ins_prestito)
            view.findViewById<TextView>(R.id.disponibilita_dettagli).text = it.disponibilità.toString()
            cercaPrestito(libro?.ID!!){valido->
                if (valido==true)
                {
                    but_prestito.isEnabled = false
                    but_prestito.text = getString(R.string.prestincorso_text)
                }
                else
                {
                    if(it.disponibilità==0)
                    {
                        but_prestito.isEnabled = false
                    }
                    else
                    {
                        but_prestito.isEnabled = true
                    }
                }
            }
            but_prestito.setOnClickListener {
                nuovoPrestito()
            }
        }
        return view
    }

    private fun nuovoPrestito() {
        val ref = FirebaseDatabase.getInstance().getReference("prestitiLibri")
        val idLibro = libro?.ID
        val disponibilità = libro?.disponibilità
        val idPrestito = ref.push().key.toString()
        val emailUtente = auth.currentUser?.email ?: "" // Ottieni l'email dell'utente
        val p = Prestito(
            idPrestito, idLibro,
            emailUtente, // Utilizza l'email dell'utente
            LocalDate.now().toString(), LocalDate.now().plusDays(30).toString(), ""
        )
        ref.child(idPrestito).setValue(p).addOnSuccessListener {
            Toast.makeText(context, getString(R.string.prestagg_text), Toast.LENGTH_SHORT).show()
            val btn_prestito = view?.findViewById<Button>(R.id.btn_ins_prestito)
            btn_prestito?.setText(getString(R.string.prestagg_text))
            btn_prestito?.isEnabled = false
            aggiornaDisponilibità(idLibro!!, disponibilità!!)
            val intent = Intent(context, UserHome::class.java)
            startActivity(intent)
            requireActivity().finish()

        }.addOnFailureListener {
            Toast.makeText(context, "Errore", Toast.LENGTH_SHORT).show()
        }
    }

    private fun aggiornaDisponilibità(idLibro: String, disponibilità: Int) {
        val ref = FirebaseDatabase.getInstance().getReference("books").child(idLibro)
        ref.child("disponibilità").setValue(disponibilità-1)
        view?.findViewById<TextView>(R.id.disponibilita_dettagli)?.text = (disponibilità-1).toString()

    }

    private fun cercaPrestito(idL: String, callback: (Boolean) -> Unit){
        var x:Boolean = false
        val ref = FirebaseDatabase.getInstance().getReference("prestitiLibri")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    for (prestito in snapshot.children)
                    {
                        val dataOdierna = LocalDate.now()
                        val dataFine = LocalDate.parse(prestito.child("dataScadenza").value.toString())
                        val idUtente = prestito.child("idUtente").value.toString()
                        /*val giorni: Long = if (dataFine.isAfter(dataOdierna)) {
                            ChronoUnit.DAYS.between(dataOdierna, dataFine)
                        } else {
                            ChronoUnit.DAYS.between(dataFine, dataOdierna)
                        }*/
                        val dataRestituzione = prestito.child("dataRestituzione").value.toString()
                        if(idUtente.equals(auth.currentUser?.email.toString()) && prestito.child("idArticolo").value.toString().equals(idL) && dataRestituzione.equals(""))
                        {
                            x=true
                            break
                        }
                    }
                }
                callback(x)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })

    }
}