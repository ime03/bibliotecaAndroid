package it.insubria.biblioteca

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
        val nomeOk = campoValido(nome, "Il nome non deve essere vuoto")
        val cognomeOk = campoValido(cognome, "Il cognome non deve essere vuoto")
        val cfOk = campoValido(cf, "Il codice fiscale non deve essere vuoto")
        val dnascitaOk = campoValido(dnascita, "La data di nascita non deve essere vuota")
        val mailOk = campoValido(mail, "La mail non deve essere vuota")
        val passOk = campoValido(pass, "La password non deve essere vuota")
        if(nomeOk && cognomeOk && cfOk && dnascitaOk && mailOk && passOk)
        {
            registrazione()
        }
        else
        {
            Toast.makeText(this,"I campi devono essere tutti completi",Toast.LENGTH_SHORT).show()
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
            var utente: HashMap<String,Any> = HashMap()
            val uid = auth.uid
            utente["userID"]= uid.toString()
            utente["nome"]= nome
            utente["ruolo"]="utente"
            utente["cognome"]=cognome
            utente["codF"]=cf
            utente["dataNascita"] = dnascita
            ref.child(uid!!).setValue(utente).addOnSuccessListener {
                Toast.makeText(this,"Registrazione effettuata con successo",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            e->
            Toast.makeText(this,"Registrazione non andata a buon fine a causa di ${e.message}",Toast.LENGTH_SHORT).show()

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
