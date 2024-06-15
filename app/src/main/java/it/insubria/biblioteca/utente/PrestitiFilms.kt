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
import it.insubria.biblioteca.adapter.AdapterPrestitiFilms
import it.insubria.biblioteca.dataClass.Film
import it.insubria.biblioteca.dataClass.Prestito
import it.insubria.biblioteca.dataClass.PrestitoFilm


class PrestitiFilms : Fragment() {
    private lateinit var prestitiUtente: ArrayList<Prestito>
    private lateinit var filmsPrestito: ArrayList<Film>
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prestitiUtente = arrayListOf<Prestito>()
        filmsPrestito = arrayListOf<Film>()
        auth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_films, container, false)
        recyclerView = view.findViewById(R.id.itemListNovitàFilm)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        trovaPrestiti()
        return view
    }

    private fun trovaPrestiti() {
        val ref = FirebaseDatabase.getInstance().getReference("prestitiFilms")
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
                associaFilms(prestitiUtente)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"errore ${error.message}",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun associaFilms(prestitiUtente: ArrayList<Prestito>) {
        var iterazioni =0
        val ref = FirebaseDatabase.getInstance().getReference("films")
        for (prestito in prestitiUtente)
        {
            ref.child(prestito.idArticolo!!).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val f = snapshot.getValue(Film::class.java)
                    filmsPrestito.add(f!!)
                    iterazioni++
                    if (iterazioni == prestitiUtente.size) {
                        val prestitiFilms = ArrayList<PrestitoFilm>()
                        for (i in prestitiUtente.indices) {
                            val p = prestitiUtente[i]
                            val f = filmsPrestito[i]
                            val pf = PrestitoFilm(p.IdPrestito,p.idArticolo,p.IdUtente,p.dataInizio,p.dataScadenza,p.dataRestituzione,f.ID,f.disponibilità,f.titolo,f.copertina,f.dataInserimento,f.attori,f.annoUscita,f.regista,f.trama,f.genere)
                            prestitiFilms.add(pf)
                        }
                        recyclerView.adapter = AdapterPrestitiFilms(prestitiFilms).apply {
                            setOnItemClickListener(object : AdapterPrestitiFilms.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val bundle = Bundle().apply {
                                        putParcelable("prestitofilm", prestitiFilms[position])
                                    }
                                    parentFragmentManager.beginTransaction()
                                        .replace(R.id.fragmentContainerView4, DettagliPrestitoFilm().apply {
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