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


class Films : Fragment() {
    private lateinit var films: ArrayList<Film>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_films, container, false)
        films = arrayListOf<Film>()
        recyclerView = view.findViewById(R.id.itemListNovitàFilm)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        getData()
        return view
    }

    private fun getData(){
        val ref = FirebaseDatabase.getInstance().getReference("films")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (film in snapshot.children)
                    {
                        val oggi = LocalDate.now()
                        val data = LocalDate.parse(film.child("dataInserimento").getValue(String::class.java))
                        val giorni = ChronoUnit.DAYS.between(data, oggi)
                        if(giorni<30)
                        {
                            val f = film.getValue(Film::class.java)
                            films.add(f!!)

                        }


                    }
                    recyclerView.adapter = AdapterFilm(films).apply {
                        setOnItemClickListener(object : AdapterFilm.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val bundle = Bundle().apply {
                                    putParcelable("film", films[position])
                                }
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainerView3, DettagliFilmUtente().apply {
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