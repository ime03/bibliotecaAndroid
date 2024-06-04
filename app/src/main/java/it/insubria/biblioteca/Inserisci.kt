package it.insubria.biblioteca

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDate


class Inserisci : Fragment() {
    val SELECT_PHOTO = 2222
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inserisci, container, false)
        val copertina = view.findViewById<Button>(R.id.buttonCopertina)
        copertina.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            startActivityForResult(photoPicker, SELECT_PHOTO)
        }
        val inserisci = view.findViewById<Button>(R.id.buttonInserisci)
        inserisci.setOnClickListener {
            controlloCampi(view)
        }
        return view
    }

    private fun controlloCampi(view: View) {
        val isbn = view.findViewById<EditText>(R.id.editTextIsbn)
        val titolo = view.findViewById<EditText>(R.id.editTextTitolo)
        val autore = view.findViewById<EditText>(R.id.editTextAutore)
        val disponibilità = view.findViewById<EditText>(R.id.editTextCopie)
        val genere = view.findViewById<EditText>(R.id.editTextGenere)
        val descrizione = view.findViewById<EditText>(R.id.editTextDescrizione)
        val isbnOk = campoValido(isbn, "L'isbn non deve essere vuoto")
        val titoloOk = campoValido(titolo, "Il titolo non deve essere vuoto")
        val autoreOk = campoValido(autore, "L'autore non deve essere vuoto")
        val disponibilitàOk = campoValido(disponibilità, "La disponibilità non deve essere vuota")
        val genereOk = campoValido(genere, "Il genere non deve essere vuoto")
        val descrizioneOk = campoValido(descrizione, "La descrizione non deve essere vuota")
        if (isbnOk && titoloOk && autoreOk && disponibilitàOk && genereOk && descrizioneOk) {
            inserimento(view)
        } else {
            Toast.makeText(context, "I campi devono essere tutti completi", Toast.LENGTH_SHORT)
                .show()
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

    private fun inserimento(view: View) {
        val ref = FirebaseDatabase.getInstance().getReference("books")
        val storageRef = FirebaseStorage.getInstance().getReference("bookImage")
        val isbn = view.findViewById<EditText>(R.id.editTextIsbn).text.toString()
        val titolo = view.findViewById<EditText>(R.id.editTextTitolo).text.toString()
        val autore = view.findViewById<EditText>(R.id.editTextAutore).text.toString()
        val descrizione = view.findViewById<EditText>(R.id.editTextDescrizione).text.toString()
        val disponibilità = view.findViewById<EditText>(R.id.editTextCopie).text.toString().toInt()
        val genere = view.findViewById<EditText>(R.id.editTextGenere).text.toString()
        val idCaricamneto = ref.push().key!!
        uri?.let { selectedUri ->
            storageRef.child(idCaricamneto).putFile(selectedUri).addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    val libro = Libro(isbn,titolo,autore,genere,disponibilità,imageUrl, LocalDate.now().toString(), descrizione)
                    ref.child(idCaricamneto).setValue(libro)
                        .addOnCompleteListener {
                            Toast.makeText(context, "Record Inserito", Toast.LENGTH_SHORT).show()
                            view.findViewById<EditText>(R.id.editTextIsbn).text.clear()
                            view.findViewById<EditText>(R.id.editTextTitolo).text.clear()
                            view.findViewById<EditText>(R.id.editTextAutore).text.clear()
                            view.findViewById<EditText>(R.id.editTextCopie).text.clear()
                            view.findViewById<EditText>(R.id.editTextGenere).text.clear()
                            view.findViewById<ImageView>(R.id.imageViewCopertina).setImageResource(R.drawable.ic_vuoto)
                            val intent = Intent(context,AdminHome::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Inserimento non andato a buon fine a causa ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Errore durante il caricamento dell'immagine: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(context, "Nessuna immagine selezionata", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data
            view?.findViewById<ImageView>(R.id.imageViewCopertina)?.setImageURI(uri)
        }
    }
}

