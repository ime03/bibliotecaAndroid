package it.insubria.biblioteca

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class Profilo : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var nome: TextView
    private lateinit var cognome: TextView
    private lateinit var codF: TextView
    private lateinit var dataNascita: TextView
    private lateinit var email: TextView
    private lateinit var imgProfilo: ImageView

    private val selezionaImgLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                caricaImg(uri)
            }
        }
    }

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
        imgProfilo = view.findViewById(R.id.imageView3)
        imgProfilo.setOnClickListener {
            selezionaImg()
        }
        val reset = view.findViewById<Button>(R.id.resetButton)
            reset.setOnClickListener{
                val intent = Intent(context,PassReset::class.java)
                startActivity(intent)
            }
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

    private fun selezionaImg() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        selezionaImgLauncher.launch(Intent.createChooser(intent, getString(R.string.sel_img_text)))
    }

    private fun caricaImg(filePath: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val profileImagesRef = storageRef.child("profile_images/${auth.currentUser?.uid}")
        val uploadTask = profileImagesRef.putFile(filePath)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            profileImagesRef.downloadUrl.addOnSuccessListener { uri ->
                val imageURL = uri.toString()
                aggiornaImg(imageURL)
                loadUserProfile()
            }
        }.addOnFailureListener {
            Toast.makeText(context, getString(R.string.err_car_img_text), Toast.LENGTH_SHORT).show()
        }
    }

    private fun aggiornaImg(imageURL: String) {
        val currentUser = auth.currentUser
        currentUser?.let {
            val userId = it.uid
            val userRef = database.child(userId)
            userRef.child("imgProfilo").setValue(imageURL)
                .addOnSuccessListener {
                    Toast.makeText(context, getString(R.string.img_corr_agg_text), Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, getString(R.string.er_agg_img_text), Toast.LENGTH_SHORT).show()
                }
        }
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
                    val imgProfiloURL = snapshot.child("imgProfilo").value.toString()
                    Glide.with(requireContext())
                        .load(imgProfiloURL)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(imgProfilo)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }
}