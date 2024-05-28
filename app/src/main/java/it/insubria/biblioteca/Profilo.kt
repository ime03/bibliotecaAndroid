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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Profilo : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var u: Utente
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        u = Utente()
        val view = inflater.inflate(R.layout.fragment_profilo, container, false)

        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (userSnapshot in snapshot.children)
                    {
                        if(userSnapshot.key == auth.uid)
                        {
                            val nome = userSnapshot.child("nome").getValue(String::class.java)
                            val cognome = userSnapshot.child("cognome").getValue(String::class.java)
                            val dataN = userSnapshot.child("dataNascita").getValue(String::class.java)
                            val cf = userSnapshot.child("codF").getValue(String::class.java)
                            val tvNome = view.findViewById<TextView>(R.id.nomeTextView)
                            val tvData = view.findViewById<TextView>(R.id.dnTextView)
                            val tvCf = view.findViewById<TextView>(R.id.cfTextView)
                            val tvCognome = view.findViewById<TextView>(R.id.cognomeTextView)
                            tvNome.text = tvNome.text.toString() + nome
                            tvCognome.text = tvCognome.text.toString()+cognome
                            tvCf.text = tvCf.text.toString()+cf
                            tvData.text = tvData.text.toString()+dataN
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val logoutButton = view.findViewById<Button>(R.id.btnLog).setOnClickListener {
            auth.signOut()
            Toast.makeText(context,"Logout effettuato",Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
        return view
    }

}