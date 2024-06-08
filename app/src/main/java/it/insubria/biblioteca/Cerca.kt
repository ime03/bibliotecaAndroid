package it.insubria.biblioteca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class Cerca : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList: ArrayList<Libro>
    private lateinit var sv:SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cerca, container, false)
        recyclerView = view.findViewById(R.id.itemList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        sv = view.findViewById(R.id.mySearchView)
        sv.clearFocus()
        sv.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                getItemData(newText)
                return true
            }

        })
        itemList = arrayListOf()
        getItemData("")
        return view
    }

    private fun getItemData(text:String) {
        database = FirebaseDatabase.getInstance().getReference("books")

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    itemList.clear()
                    for (bookSnapshot in snapshot.children) {
                        if(bookSnapshot.child("titolo").value.toString().toLowerCase().contains(text.toLowerCase()))
                        {
                            val libro = bookSnapshot.getValue(Libro::class.java)
                            itemList.add(libro!!)
                        }

                    }
                    recyclerView.adapter = Adapter(itemList).apply {
                        setOnItemClickListener(object : Adapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val bundle = Bundle().apply {
                                    putParcelable("libro", itemList[position])
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
                // Gestisci onCancelled
            }
        })
    }
}