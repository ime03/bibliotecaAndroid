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
import it.insubria.biblioteca.adapter.AdapterPrestitiAlbums
import it.insubria.biblioteca.dataClass.Album
import it.insubria.biblioteca.dataClass.Prestito
import it.insubria.biblioteca.dataClass.PrestitoAlbum


class PrestitiAlbums : Fragment() {
    private lateinit var prestitiUtente: ArrayList<Prestito>
    private lateinit var albumsPrestito: ArrayList<Album>
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prestitiUtente = arrayListOf<Prestito>()
        albumsPrestito = arrayListOf<Album>()
        auth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_albums, container, false)
        recyclerView = view.findViewById(R.id.itemListNovitàAlbum)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        trovaPrestiti()
        return view
    }

    private fun trovaPrestiti() {
        val ref = FirebaseDatabase.getInstance().getReference("prestitiAlbums")
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
                associaAlbums(prestitiUtente)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"errore ${error.message}",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun associaAlbums(prestitiUtente: ArrayList<Prestito>) {
        var iterazioni =0
        val ref = FirebaseDatabase.getInstance().getReference("albums")
        for (prestito in prestitiUtente)
        {
            ref.child(prestito.idArticolo!!).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val a = snapshot.getValue(Album::class.java)
                    albumsPrestito.add(a!!)
                    iterazioni++
                    if (iterazioni == prestitiUtente.size) {
                        val prestitiAlbums = ArrayList<PrestitoAlbum>()
                        for (i in prestitiUtente.indices) {
                            val p = prestitiUtente[i]
                            val a = albumsPrestito[i]
                            val pa = PrestitoAlbum(p.IdPrestito,p.idArticolo,p.IdUtente,p.dataInizio,p.dataScadenza,p.dataRestituzione,a.ID,a.pubblicazione,a.artista,a.titolo,a.dataInserimento,a.disponibilità,a.copertina,a.descrizione,a.genere)
                            prestitiAlbums.add(pa)
                        }
                        recyclerView.adapter = AdapterPrestitiAlbums(prestitiAlbums).apply {
                            setOnItemClickListener(object : AdapterPrestitiAlbums.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val bundle = Bundle().apply {
                                        putParcelable("prestitoalbum", prestitiAlbums[position])
                                    }
                                    parentFragmentManager.beginTransaction()
                                        .replace(R.id.fragmentContainerView4, DettagliPrestitoAlbum().apply {
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