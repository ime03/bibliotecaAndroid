package it.insubria.biblioteca.utente

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import it.insubria.biblioteca.dataClass.PrestitoLibro
import it.insubria.biblioteca.R
import java.time.LocalDate

class DettagliPrestitoLibro : Fragment() {
    private var prestito: PrestitoLibro? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        prestito = arguments?.getParcelable("prestitolibro") ?: PrestitoLibro()
        val view = inflater.inflate(R.layout.fragment_dettagli_prestito, container, false)

        // Imposta le date del prestito
        view.findViewById<TextView>(R.id.DataInizioDettagli).text = prestito!!.dataInizio.toString()
        view.findViewById<TextView>(R.id.DataScadenzaDettagli).text = prestito!!.dataScadenza.toString()

        // Imposta il pulsante di restituzione
        val btnRestituisci = view.findViewById<Button>(R.id.btn_res_prestito)

        // Controllo sicuro per dataRestituzione
        if (!prestito!!.dataRestituzione.isNullOrEmpty()) {
            view.findViewById<TextView>(R.id.textViewRestituito).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.RestituzioneDettagli).text = prestito!!.dataRestituzione
            btnRestituisci.isEnabled = false
            btnRestituisci.text = "LIBRO GIA' RESTITUITO"
        }

        val copertinaImageView = view.findViewById<ImageView>(R.id.copertinaPrestito)
        val libroId = prestito?.IdArticolo

        // Verifica che libroId non sia null prima di procedere
        if (!libroId.isNullOrEmpty()) {
            val libroRef = FirebaseDatabase.getInstance().getReference("books").child(libroId)
            libroRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val urlCopertina = snapshot.child("copertina").getValue(String::class.java)

                    // Carica l'immagine di copertina utilizzando Glide
                    if (!urlCopertina.isNullOrEmpty()) {
                        Glide.with(this@DettagliPrestitoLibro)
                            .load(urlCopertina)
                            .placeholder(R.drawable.placeholder_image) // Immagine di placeholder
                            .into(copertinaImageView)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Errore nel recupero della copertina: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Imposta l'azione del pulsante di restituzione
        btnRestituisci.setOnClickListener {
            aggiornaDisponibilità(prestito!!.ID!!)
            impostaRestituzione(prestito!!.IdPrestito!!)
            Toast.makeText(context, "Restituzione avvenuta correttamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, UserHome::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

    private fun impostaRestituzione(idPrestito: String) {
        val dataOdierna = LocalDate.now()
        val ref = FirebaseDatabase.getInstance().getReference("prestitiLibri").child(idPrestito)
        ref.child("dataRestituzione").setValue(dataOdierna.toString())
    }

    private fun aggiornaDisponibilità(idLibro: String) {
        val ref = FirebaseDatabase.getInstance().getReference("books").child(idLibro)
        ref.child("disponibilità").get().addOnSuccessListener {
            ref.child("disponibilità").setValue(it.getValue(Int::class.java)!! + 1)
        }
    }
}