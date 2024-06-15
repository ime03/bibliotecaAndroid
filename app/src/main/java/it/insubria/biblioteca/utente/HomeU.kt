package it.insubria.biblioteca.utente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import it.insubria.biblioteca.R
import it.insubria.biblioteca.dataClass.Libro


class HomeU : Fragment() {
    private lateinit var bookList: ArrayList<Libro>
    //private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bookList = arrayListOf<Libro>()
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        /*recyclerView = view.findViewById(R.id.itemListNovitàLibri)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        getData()*/
        val btn_libri = view.findViewById<Button>(R.id.libriButton)
        val btnRiv = view.findViewById<Button>(R.id.rivButton)
        val btnFilm = view.findViewById<Button>(R.id.filmButton)
        val btnMusica = view.findViewById<Button>(R.id.musicaButton)
        val btnPercorsi = view.findViewById<Button>(R.id.perclettButton)
        btn_libri.setOnClickListener {
            replaceFragment(Libri())
        }
        btnRiv.setOnClickListener {
            replaceFragment(Riviste())
        }
        btnFilm.setOnClickListener {
            replaceFragment(Films())
        }
        btnMusica.setOnClickListener {
            replaceFragment(Albums())
        }
        btnPercorsi.setOnClickListener {
            replaceFragment(PercorsiLettura())
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment){
        //supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainerView3, fragment).addToBackStack(null).commit()
    }


    /*private fun getData(){
        val ref = FirebaseDatabase.getInstance().getReference("books")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (booksSnapshot in snapshot.children)
                    {
                        val oggi = LocalDate.now()
                        val data = LocalDate.parse(booksSnapshot.child("data").getValue(String::class.java))
                        val giorni = ChronoUnit.DAYS.between(data, oggi)
                        if(giorni<30)
                        {
                            val book = booksSnapshot.getValue(Libro::class.java)
                            bookList.add(book!!)

                        }


                    }
                    recyclerView.adapter = Adapter(bookList).apply {
                        setOnItemClickListener(object : Adapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val bundle = Bundle().apply {
                                    putParcelable("libro", bookList[position])
                                }
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, DettagliLibroUtente().apply {
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
    }*/
}

