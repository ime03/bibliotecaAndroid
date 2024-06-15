package it.insubria.biblioteca.utente

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
import it.insubria.biblioteca.adapter.AdapterRivista
import it.insubria.biblioteca.dataClass.Rivista
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class Riviste : Fragment() {
    private lateinit var riviste: ArrayList<Rivista>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_riviste, container, false)
        riviste = arrayListOf<Rivista>()
        recyclerView = view.findViewById(R.id.itemListNovitàRiviste)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        getData()
        return view
    }

    private fun getData(){
        val ref = FirebaseDatabase.getInstance().getReference("riviste")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (rivista in snapshot.children)
                    {
                        val oggi = LocalDate.now()
                        val data = LocalDate.parse(rivista.child("dataInserimento").getValue(String::class.java))
                        val giorni = ChronoUnit.DAYS.between(data, oggi)
                        if(giorni<30)
                        {
                            val r = rivista.getValue(Rivista::class.java)
                            riviste.add(r!!)

                        }


                    }
                    recyclerView.adapter = AdapterRivista(riviste).apply {
                        setOnItemClickListener(object : AdapterRivista.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val bundle = Bundle().apply {
                                    putParcelable("rivista", riviste[position])
                                }
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainerView3, DettagliRivistaUtente().apply {
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