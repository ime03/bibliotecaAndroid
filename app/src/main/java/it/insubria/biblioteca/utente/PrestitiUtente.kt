package it.insubria.biblioteca.utente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import it.insubria.biblioteca.R
import it.insubria.biblioteca.dataClass.Libro


class PrestitiUtente : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_prestiti, container, false)
        replaceFragment(PrestitiLibri())
        val btn_libri = view.findViewById<Button>(R.id.libriButton)
        val btnRiv = view.findViewById<Button>(R.id.rivButton)
        val btnFilm = view.findViewById<Button>(R.id.filmButton)
        val btnMusica = view.findViewById<Button>(R.id.musicaButton)
        btn_libri.setOnClickListener {
            replaceFragment(PrestitiLibri())
        }
        btnRiv.setOnClickListener {
            replaceFragment(PrestitiRiviste())
        }
        btnFilm.setOnClickListener {
            replaceFragment(PrestitiFilms())
        }
        btnMusica.setOnClickListener {
            replaceFragment(PrestitiAlbums())
        }


        return view
    }

    private fun replaceFragment(fragment: Fragment){
        //supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainerView4, fragment).addToBackStack(null).commit()
    }

}

