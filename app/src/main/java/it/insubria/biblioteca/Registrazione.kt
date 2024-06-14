package it.insubria.biblioteca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Registrazione : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrazione)
        val regButton = findViewById<Button>(R.id.ButtonRegistrati)
        regButton.setOnClickListener {
            controlloCampi()
        }
    }

    private fun controlloCampi() {
        val nome = findViewById<EditText>(R.id.EditTextNome)
        val cognome = findViewById<EditText>(R.id.EditTextCognome)
        val cf = findViewById<EditText>(R.id.EditTextCF)
        val dnascita = findViewById<EditText>(R.id.EditTextNascita)
        val mail = findViewById<EditText>(R.id.EditTextEmailR)
        val pass = findViewById<EditText>(R.id.EditTextPasswordR)
        val nomeOk = campoValido(nome, getString(R.string.nome_vuoto))
        val cognomeOk = campoValido(cognome, getString(R.string.cognome_vuoto))
        val cfOk = campoValido(cf, getString(R.string.codf_vuoto))
        val dnascitaOk = campoValido(dnascita, getString(R.string.datan_vuota))
        val mailOk = campoValido(mail, getString(R.string.mail_vuota))
        val passOk = campoValido(pass, getString(R.string.pass_vuota))
        if(nomeOk && cognomeOk && cfOk && dnascitaOk && mailOk && passOk)
        {
            registrazione()
        }
        else
        {
            Toast.makeText(this,getString(R.string.campi_vuoti),Toast.LENGTH_SHORT).show()
        }

    }

    private fun registrazione() {
        val ref = FirebaseDatabase.getInstance("https://biblioteca-a8291-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
        val nome = findViewById<EditText>(R.id.EditTextNome).text.toString()
        val cognome = findViewById<EditText>(R.id.EditTextCognome).text.toString()
        val cf = findViewById<EditText>(R.id.EditTextCF).text.toString()
        val dnascita = findViewById<EditText>(R.id.EditTextNascita).text.toString()
        val mail = findViewById<EditText>(R.id.EditTextEmailR).text.toString()
        val pass = findViewById<EditText>(R.id.EditTextPasswordR).text.toString()
        auth.createUserWithEmailAndPassword(mail,pass).addOnSuccessListener {
            Toast.makeText(this,getString(R.string.vermail_text),Toast.LENGTH_SHORT).show()
            auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                val uid = auth.uid
                val user = Utente(uid.toString(),nome,cognome,cf,dnascita,"utente")
                ref.child(uid!!).setValue(user).addOnSuccessListener {
                    Toast.makeText(this,getString(R.string.regeff_text),Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }?.addOnFailureListener{ e->
                Toast.makeText(this,"${getString(R.string.reginv_text)} ${e.message}",Toast.LENGTH_SHORT).show()

            }
            }.addOnFailureListener { e ->
            Toast.makeText(
                this,
                "${getString(R.string.reginv_text)} ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
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



}
