package it.insubria.biblioteca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import it.insubria.biblioteca.admin.AdminHome
import it.insubria.biblioteca.utente.UserHome

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth= FirebaseAuth.getInstance()
        val reg = findViewById<TextView>(R.id.TextViewRegistrati)
        val reset = findViewById<TextView>(R.id.TextViewPassReset)

        reset.setOnClickListener {
            val intent = Intent(
                this,PassReset::class.java
            )
            startActivity(intent)
        }

        reg.setOnClickListener{
            val intent = Intent(
                this,Registrazione::class.java
            )
            startActivity(intent)
        }

        val lb = findViewById<Button>(R.id.ButtonAccedi)
        lb.setOnClickListener {
            controlloCampi()
        }
    }

    private fun controlloCampi() {
        val mail = findViewById<EditText>(R.id.EditTextEmail)
        val pass = findViewById<EditText>(R.id.EditTextPassword)
        val mailOk = campoValido(mail, getString(R.string.mail_vuota))
        val passOk = campoValido(pass, getString(R.string.pass_vuota))
        if(mailOk && passOk)
        {
            autenticazione(mail,pass)
        }
        else
        {
            Toast.makeText(this,getString(R.string.campi_vuoti), Toast.LENGTH_SHORT).show()
        }

    }

    private fun campoValido(field: EditText, errorMessage: String): Boolean {
        return if (field.text.toString().isBlank()) {
            field.error = errorMessage
            false
        } else {
            true
        }
    }

    private fun autenticazione(mail: EditText, pass: EditText)
    {

        val mail1 = mail.text.toString()
        val pass1 = pass.text.toString()
        auth.signInWithEmailAndPassword(mail1,pass1).addOnSuccessListener {
            if(auth.currentUser?.isEmailVerified==true)
            {
                controllaRuolo()
            }
            else
            {
                Toast.makeText(this@MainActivity,"Verifica la mail prima di accedere",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            e->
            Toast.makeText(this,"Errore di autenticazione a causa di: ${e.message}",Toast.LENGTH_SHORT).show()
        }
    }

    private fun controllaRuolo(){
        val utente = auth.currentUser!!
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(utente.uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val tipoUtente = snapshot.child("ruolo").value.toString()
                if(tipoUtente.equals("amministratore"))
                {
                    Toast.makeText(this@MainActivity,"Benvenuto amministratore",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, AdminHome::class.java)
                    startActivity(intent)
                }
                else if(tipoUtente.equals("utente"))
                {
                    val intent = Intent(this@MainActivity, UserHome::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}