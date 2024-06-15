package it.insubria.biblioteca.admin

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
import it.insubria.biblioteca.R
import it.insubria.biblioteca.adapter.Adapter
import it.insubria.biblioteca.admin.DettagliLibroAdmin
import it.insubria.biblioteca.dataClass.Libro


class HomeA : Fragment() {
    private lateinit var bookList: ArrayList<Libro>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bookList = arrayListOf<Libro>()
        val view = inflater.inflate(R.layout.fragment_home_a, container, false)
        recyclerView = view.findViewById(R.id.itemListLibri)
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
                            val book = booksSnapshot.getValue(Libro::class.java)
                            bookList.add(book!!)

                    }
                    recyclerView.adapter = Adapter(bookList).apply {
                        setOnItemClickListener(object : Adapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val bundle = Bundle().apply {
                                    putParcelable("libro", bookList[position])
                                }
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainerView, DettagliLibroAdmin().apply {
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

