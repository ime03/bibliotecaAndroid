package it.insubria.biblioteca

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class DettagliLibro : Fragment() {

    private var libro: Libro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        libro = arguments?.getParcelable("libro") ?: Libro()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dettagli_libro, container, false)
        libro?.let {
            view.findViewById<TextView>(R.id.titolo_dettagli).text = it.titolo
            view.findViewById<TextView>(R.id.autore_dettagli).text = it.autore
            view.findViewById<TextView>(R.id.genere_dettagli).text = it.genere
            view.findViewById<TextView>(R.id.disponibilita_dettagli).text = it.disponibilita.toString()
        }
        return view
    }
}