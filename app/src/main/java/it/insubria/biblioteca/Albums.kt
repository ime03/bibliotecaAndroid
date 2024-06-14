package it.insubria.biblioteca

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Albums : Fragment() {
    private lateinit var albums: ArrayList<Album>
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_albums, container, false)
        albums = arrayListOf<Album>()
        recyclerView = view.findViewById(R.id.itemListNovitàAlbum)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        getData()
        return view
    }


    private fun getData(){
        val ref = FirebaseDatabase.getInstance().getReference("albums")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (album in snapshot.children)
                    {
                        val oggi = LocalDate.now()
                        val data = LocalDate.parse(album.child("dataInserimento").getValue(String::class.java))
                        val giorni = ChronoUnit.DAYS.between(data, oggi)
                        if(giorni<30)
                        {
                            val a = album.getValue(Album::class.java)
                            albums.add(a!!)

                        }


                    }
                    recyclerView.adapter = AdapterAlbum(albums).apply {
                        setOnItemClickListener(object : AdapterAlbum.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val bundle = Bundle().apply {
                                    putParcelable("album", albums[position])
                                }
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainerView3, DettagliAlbumUtente().apply {
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