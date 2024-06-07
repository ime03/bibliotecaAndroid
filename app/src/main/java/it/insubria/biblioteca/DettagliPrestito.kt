package it.insubria.biblioteca

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate


class DettagliPrestito : Fragment() {
    private var prestito: PrestitoLibro? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        prestito = arguments?.getParcelable("prestitolibro") ?: PrestitoLibro()
        val view = inflater.inflate(R.layout.fragment_dettagli_prestito, container, false)
        view.findViewById<TextView>(R.id.DataInizioDettagli).text = prestito!!.dataInizio.toString()
        view.findViewById<TextView>(R.id.DataScadenzaDettagli).text = prestito!!.dataScadenza.toString()
        val btnRestituisci = view.findViewById<Button>(R.id.btn_res_prestito)
        if(!prestito!!.dataRestituzione.equals(""))
        {
            view.findViewById<TextView>(R.id.textViewRestituito).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.RestituzioneDettagli).text = prestito!!.dataRestituzione
            btnRestituisci.isEnabled = false
            btnRestituisci.setText("LIBRO GIA' RESTITUITO")
        }
        btnRestituisci.setOnClickListener {
            aggiornaDisponibilità(prestito!!.ID!!)
            impostaRestituzione(prestito!!.IdPrestito!!)
            Toast.makeText(context,"Restituzione avvenuta correttamente",Toast.LENGTH_SHORT).show()
            val intent = Intent(context,UserHome::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return view
    }

    private fun impostaRestituzione(idPrestito: String) {
        val dataOdierna = LocalDate.now()
        val ref = FirebaseDatabase.getInstance().getReference("prestiti").child(idPrestito)
        ref.child("dataRestituzione").setValue(dataOdierna.toString())
    }

    private fun aggiornaDisponibilità(idLibro: String) {
        var disp = 0
        val ref = FirebaseDatabase.getInstance().getReference("books").child(idLibro)
        ref.child("disponibilità").get().addOnSuccessListener {
            disp = it.getValue(Int::class.java)!!
        }
        ref.child("disponibilità").setValue(disp+1)
    }


}