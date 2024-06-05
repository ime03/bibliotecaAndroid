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
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profilo : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var nome: TextView
    private lateinit var cognome: TextView
    private lateinit var codF: TextView
    private lateinit var dataNascita: TextView
    private lateinit var email: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profilo, container, false)
        nome = view.findViewById(R.id.nome_profilo)
        cognome = view.findViewById(R.id.cognome_profilo)
        codF = view.findViewById(R.id.cf_profilo)
        dataNascita = view.findViewById(R.id.datanascita_profilo)
        email = view.findViewById(R.id.email_profilo)
        val btnLogout = view.findViewById<Button>(R.id.logoutButton)
            btnLogout.setOnClickListener {
                auth.signOut()
                val intent = Intent(context,MainActivity::class.java)
                Toast.makeText(context,"Logout effettuato",Toast.LENGTH_SHORT).show()
                startActivity(intent)
                requireActivity().finish()
        }
        loadUserProfile()
        return view
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        currentUser?.let {
            email.text = it.email
            val userId = it.uid
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nomeValue = snapshot.child("nome").value.toString()
                    val cognomeValue = snapshot.child("cognome").value.toString()
                    val codFValue = snapshot.child("cf").value.toString()
                    val dataNascitaValue = snapshot.child("dataNascita").value.toString()

                    nome.text = nomeValue
                    cognome.text = cognomeValue
                    codF.text = codFValue
                    dataNascita.text = dataNascitaValue
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }
}