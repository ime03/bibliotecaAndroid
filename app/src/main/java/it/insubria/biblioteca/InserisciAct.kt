package it.insubria.biblioteca

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class InserisciAct : AppCompatActivity() {

    val SELECT_PHOTO = 2222
    private var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserisci)
        val copertina = findViewById<Button>(R.id.buttonCopertina)
        copertina.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            startActivityForResult(photoPicker, SELECT_PHOTO)
        }
        val inserisci = findViewById<Button>(R.id.buttonInserisci)
        inserisci.setOnClickListener {
            controlloCampi()
        }

    }

    private fun controlloCampi()
    {
        val isbn = findViewById<EditText>(R.id.editTextIsbn)
        val titolo = findViewById<EditText>(R.id.editTextTitolo)
        val autore = findViewById<EditText>(R.id.editTextAutore)
        val disponibilità = findViewById<EditText>(R.id.editTextCopie)
        val genere = findViewById<EditText>(R.id.editTextGenere)
        val isbnOk = campoValido(isbn, "L'isbn non deve essere vuoto")
        val titoloOk = campoValido(titolo, "Il titolo non deve essere vuoto")
        val autoreOk = campoValido(autore, "L'autore non deve essere vuoto")
        val disponibilitàOk = campoValido(disponibilità, "La disponibilità non deve essere vuota")
        val genereOk = campoValido(genere, "Il genere non deve essere vuoto")
        if(isbnOk && titoloOk && autoreOk && disponibilitàOk && genereOk)
        {
            inserimento()
        }
        else
        {
            Toast.makeText(this,"I campi devono essere tutti completi", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data
            val img = findViewById<ImageView>(R.id.imageViewCopertina)
            img.setImageURI(uri)
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

    private fun inserimento(){
        val ref = FirebaseDatabase.getInstance().getReference("books")
        val storageRef = FirebaseStorage.getInstance().getReference("bookImage")
        val isbn = findViewById<EditText>(R.id.editTextIsbn).text.toString()
        val titolo = findViewById<EditText>(R.id.editTextTitolo).text.toString()
        val autore = findViewById<EditText>(R.id.editTextAutore).text.toString()
        val disponibilità = findViewById<EditText>(R.id.editTextCopie).text.toString().toInt()
        val genere = findViewById<EditText>(R.id.editTextGenere).text.toString()
        val idCaricamneto = ref.push().key!!
        uri?.let { selectedUri ->
            storageRef.child(idCaricamneto).putFile(selectedUri).addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    val book = Book(isbn,titolo,autore,genere,disponibilità,imageUrl)
                    ref.child(idCaricamneto).setValue(book)
                        .addOnCompleteListener {
                            Toast.makeText(this, "Record Inserito", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,AdminHome::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Inserimento non andato a buon fine a causa ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Errore durante il caricamento dell'immagine: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "Nessuna immagine selezionata", Toast.LENGTH_SHORT).show()
        }
    }


}

