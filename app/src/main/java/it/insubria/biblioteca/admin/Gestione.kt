package it.insubria.biblioteca.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import it.insubria.biblioteca.R

class Gestione : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gestione, container, false)

        // Trova il LinearLayout GestionePrestiti
        val gestionePrestitiLayout = view.findViewById<LinearLayout>(R.id.GestionePrestiti)

        // Aggiungi un OnClickListener al LinearLayout
        gestionePrestitiLayout.setOnClickListener {
            // Quando viene cliccato, apri il fragment ListaPrestiti
            val listaPrestitiFragment = ListaPrestiti()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, listaPrestitiFragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}




