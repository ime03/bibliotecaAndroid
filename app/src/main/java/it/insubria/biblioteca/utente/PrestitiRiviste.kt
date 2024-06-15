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
import it.insubria.biblioteca.R
import it.insubria.biblioteca.adapter.AdapterPrestitiRiviste
import it.insubria.biblioteca.dataClass.Prestito
import it.insubria.biblioteca.dataClass.PrestitoRivista
import it.insubria.biblioteca.dataClass.Rivista


class PrestitiRiviste : Fragment() {
    private lateinit var prestitiUtente: ArrayList<Prestito>
    private lateinit var rivistePrestito: ArrayList<Rivista>
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prestitiUtente = arrayListOf<Prestito>()
        rivistePrestito = arrayListOf<Rivista>()
        auth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_riviste, container, false)
        recyclerView = view.findViewById(R.id.itemListNovitàRiviste)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        trovaPrestiti()
        return view
    }

    private fun trovaPrestiti() {
        val ref = FirebaseDatabase.getInstance().getReference("prestitiRiviste")
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
                associaRiviste(prestitiUtente)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"errore ${error.message}",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun associaRiviste(prestitiUtente: ArrayList<Prestito>) {
        var iterazioni =0
        val ref = FirebaseDatabase.getInstance().getReference("riviste")
        for (prestito in prestitiUtente)
        {
            ref.child(prestito.idArticolo!!).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val r = snapshot.getValue(Rivista::class.java)
                    rivistePrestito.add(r!!)
                    iterazioni++
                    if (iterazioni == prestitiUtente.size) {
                        val prestitiRiviste = ArrayList<PrestitoRivista>()
                        for (i in prestitiUtente.indices) {
                            val p = prestitiUtente[i]
                            val r = rivistePrestito[i]
                            val pr = PrestitoRivista(p.IdPrestito,p.idArticolo,p.IdUtente,p.dataInizio,p.dataScadenza,p.dataRestituzione,r.ID,r.titolo,r.dataPubblicazione,r.genere,r.periodicità,r.disponibilità,r.copertina,r.dataInserimento,r.descrizione)
                            prestitiRiviste.add(pr)
                        }
                        recyclerView.adapter = AdapterPrestitiRiviste(prestitiRiviste).apply {
                            setOnItemClickListener(object : AdapterPrestitiRiviste.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val bundle = Bundle().apply {
                                        putParcelable("prestitorivista", prestitiRiviste[position])
                                    }
                                    parentFragmentManager.beginTransaction()
                                        .replace(R.id.fragmentContainerView4, DettagliPrestitoRivista().apply {
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