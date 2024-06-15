package it.insubria.biblioteca.utente

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
import it.insubria.biblioteca.dataClass.PrestitoLibro
import it.insubria.biblioteca.R
import it.insubria.biblioteca.adapter.AdapterPrestitiLibri
import it.insubria.biblioteca.dataClass.Libro
import it.insubria.biblioteca.dataClass.Prestito


class PrestitiLibri : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_libri, container, false)
        recyclerView = view.findViewById(R.id.itemListNovitàLibri)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        trovaPrestiti()
        return view
    }

    private fun trovaPrestiti() {
        val ref = FirebaseDatabase.getInstance().getReference("prestitiLibri")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    for (prestito in snapshot.children)
                    {
                        val emailUtente = prestito.child("idUtente").getValue(String::class.java)
                        if (emailUtente == auth.currentUser?.email)
                        {
                            val p = prestito.getValue(Prestito::class.java)
                            prestitiUtente.add(p!!)
                           // Log.v("IDLIBRO",prestito.child("idLibro").value.toString())
                        }
                    }
                }
                associaLibri(prestitiUtente)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"errore ${error.message}",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun associaLibri(prestitiUtente: ArrayList<Prestito>) {
        var iterazioni =0
        val ref = FirebaseDatabase.getInstance().getReference("books")
        for (prestito in prestitiUtente)
        {
            ref.child(prestito.idArticolo!!).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val l = snapshot.getValue(Libro::class.java)
                    libriPrestito.add(l!!)
                    iterazioni++
                    if (iterazioni == prestitiUtente.size) {
                        val prestitiLibri = ArrayList<PrestitoLibro>()
                        for (i in prestitiUtente.indices) {
                            val p = prestitiUtente[i]
                            val l = libriPrestito[i]
                            val pl = PrestitoLibro(p.IdPrestito,p.idArticolo,p.IdUtente,p.dataInizio,p.dataScadenza,p.dataRestituzione,l.ID,l.isbn,l.titolo,l.autore,l.genere,l.disponibilità,l.copertina,l.data,l.descrizione)
                            prestitiLibri.add(pl)
                        }
                        recyclerView.adapter = AdapterPrestitiLibri(prestitiLibri).apply {
                            setOnItemClickListener(object : AdapterPrestitiLibri.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val bundle = Bundle().apply {
                                        putParcelable("prestitolibro", prestitiLibri[position])
                                    }
                                    parentFragmentManager.beginTransaction()
                                        .replace(R.id.fragmentContainerView4, DettagliPrestitoLibro().apply {
                                            arguments = bundle
                                        })
                                        .addToBackStack(null)
                                        .commit()

                                }
                            })
                        }

                    }

                }



                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }



}