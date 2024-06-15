package it.insubria.biblioteca.utente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import it.insubria.biblioteca.dataClass.PercorsoLettura
import it.insubria.biblioteca.R
import it.insubria.biblioteca.dataClass.Prestito

class DettagliPercorsoUtente : Fragment() {

    private var percorso: PercorsoLettura? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var listaPrestiti: ArrayList<Prestito>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        percorso = arguments?.getParcelable("percorso") ?: PercorsoLettura()
        listaPrestiti = arrayListOf<Prestito>()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dettagli_percorso_u, container, false)
        percorso?.let {
            view.findViewById<TextView>(R.id.titolo_dettagli).text = it.titolo
            view.findViewById<TextView>(R.id.etàmin_dettagli).text = it.etàMinima.toString()
            view.findViewById<TextView>(R.id.etàmax_dettagli).text = it.etàMassima.toString()
            view.findViewById<TextView>(R.id.descrizione_dettagli).text = it.descrizione


            val copertinaImageView = view.findViewById<ImageView>(R.id.copertina_dettagli)
            Glide.with(requireContext())
                .load(it.copertina)
                .into(copertinaImageView)


        }
        return view
    }
}





