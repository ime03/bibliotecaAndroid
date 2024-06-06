package it.insubria.biblioteca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class PassReset : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var buttonReset: Button
    private lateinit var  registrati: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pass_reset)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email = findViewById(R.id.EditTextEmailReset)
        buttonReset = findViewById(R.id.ButtonContinuaReset)
        registrati = findViewById(R.id.TextViewRegistrati)

        auth = FirebaseAuth.getInstance()

        buttonReset.setOnClickListener{
            val resetEmail = email.text.toString()
            auth.sendPasswordResetEmail(resetEmail)
                .addOnSuccessListener {
                    Toast.makeText(this,"Ti abbiamo inviato un email per il cambio password!",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@PassReset,MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@PassReset,MainActivity::class.java)
                    startActivity(intent)
                }
        }

        registrati.setOnClickListener{
            val intent = Intent(
                this,Registrazione::class.java
            )
            startActivity(intent)
        }
    }
}