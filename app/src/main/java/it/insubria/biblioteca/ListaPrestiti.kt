package it.insubria.biblioteca

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ListaPrestiti : Fragment() {
    private lateinit var prestitiUtente: ArrayList<Prestito>
    private lateinit var libriPrestito: ArrayList<Libro>
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prestitiUtente = arrayListOf<Prestito>()
        libriPrestito = arrayListOf<Libro>()
        auth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_prestiti, container, false)
        recyclerView = view.findViewById(R.id.itemListPrestiti)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        trovaPrestiti()
        return view
    }

    private fun trovaPrestiti() {
        val ref = FirebaseDatabase.getInstance().getReference("prestiti")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (prestito in snapshot.children) {
                        val p = prestito.getValue(Prestito::class.java)
                        prestitiUtente.add(p!!)
                    }
                    associaLibri(prestitiUtente) // Sposta qui la chiamata
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "errore ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun associaLibri(prestitiUtente: ArrayList<Prestito>) {
        val ref = FirebaseDatabase.getInstance().getReference("books")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (prestito in prestitiUtente) {
                    val l = snapshot.child(prestito.IdLibro!!).getValue(Libro::class.java)
                    libriPrestito.add(l!!)
                }

                val prestitiLibri = ArrayList<PrestitoLibro>()
                for (i in prestitiUtente.indices) {
                    val p = prestitiUtente[i]
                    val l = libriPrestito[i]
                    val pl = PrestitoLibro(p.IdPrestito,p.IdLibro,p.IdUtente,p.dataInizio,p.dataScadenza,p.dataRestituzione,l.ID,l.isbn,l.titolo,l.autore,l.genere,l.disponibilità,l.copertina,l.data,l.descrizione)
                    prestitiLibri.add(pl)
                }

                recyclerView.adapter = AdapterGestionePrestiti(prestitiLibri).apply {
                    setOnItemClickListener(object : AdapterGestionePrestiti.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val bundle = Bundle().apply {
                                putParcelable("prestitolibro", prestitiLibri[position])
                            }
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, DettagliPrestito().apply {
                                    arguments = bundle
                                })
                                .addToBackStack(null)
                                .commit()
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "errore ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}