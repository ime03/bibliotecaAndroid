package it.insubria.biblioteca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Cerca : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList: ArrayList<Libro>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cerca, container, false)

        recyclerView = view.findViewById(R.id.itemList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        itemList = arrayListOf()
        getItemData()


        return view
    }

    private fun getItemData() {
        database = FirebaseDatabase.getInstance().getReference("books")

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    itemList.clear()
                    for (userSnapshot in snapshot.children) {
                        val libro = userSnapshot.getValue(Libro::class.java)
                        itemList.add(libro!!)
                    }
                    recyclerView.adapter = Adapter(itemList).apply {
                        setOnItemClickListener(object : Adapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val bundle = Bundle().apply {
                                    putParcelable("libro", itemList[position])
                                }
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, DettagliLibro().apply {
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