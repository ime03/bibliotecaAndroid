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


class PercorsiLettura : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var percorsi: ArrayList<PercorsoLettura>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perclett, container, false)
        percorsi = arrayListOf<PercorsoLettura>()
        recyclerView = view.findViewById(R.id.itemListNovitàPerclett)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        getData()
        return view
    }

    private fun getData(){
        val ref = FirebaseDatabase.getInstance().getReference("percorsilettura")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (percorso in snapshot.children)
                    {
                        val oggi = LocalDate.now()
                        val data = LocalDate.parse(percorso.child("dataInserimento").getValue(String::class.java))
                        val giorni = ChronoUnit.DAYS.between(data, oggi)
                        if(giorni<30)
                        {
                            val p = percorso.getValue(PercorsoLettura::class.java)
                            percorsi.add(p!!)

                        }


                    }
                    recyclerView.adapter = AdapterPercorsi(percorsi).apply {
                        setOnItemClickListener(object : AdapterPercorsi.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val bundle = Bundle().apply {
                                    putParcelable("percorso", percorsi[position])
                                }
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainerView3, DettagliPercorsoUtente().apply {
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