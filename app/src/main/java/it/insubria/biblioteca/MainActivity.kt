package it.insubria.biblioteca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reg = findViewById<TextView>(R.id.TextViewRegistrati)

        reg.setOnClickListener{
            val intent = Intent(
                this,Registrazione::class.java
            )
            startActivity(intent)
        }
    }
}