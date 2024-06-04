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


class HomeU : Fragment() {
    private lateinit var bookList: ArrayList<Libro>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bookList = arrayListOf<Libro>()
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.itemListNovità)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        getData()
        return view
    }


    private fun getData(){
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
    }
}

