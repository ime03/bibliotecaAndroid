package it.insubria.biblioteca

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit


class Home : Fragment() {
    private lateinit var bookList: ArrayList<Book>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bookList= arrayListOf<Book>()
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val ref = FirebaseDatabase.getInstance().getReference("books")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (booksSnapshot in snapshot.children)
                    {
                        val oggi = LocalDate.now()
                        val data = LocalDate.parse(booksSnapshot.child("data").getValue(String::class.java))
                        val giorni = ChronoUnit.DAYS.between(data, oggi)
                        Log.v("DATA", data.toString())
                        Log.v("PERIODO", giorni.toString())
                        if(giorni<30)
                        {
                            val book = booksSnapshot.getValue(Book::class.java)
                            bookList.add(book!!)

                        }


                    }
                }
                stampaLista(bookList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }




        })
        return view
    }

    private fun stampaLista(bookList: ArrayList<Book>) {
        for (book in bookList)
        {
            Log.v("ISBN", book.isbn.toString())
            Log.v("Titolo",book.titolo.toString())
            Log.v("Autore",book.autore.toString())
            Log.v("Genere",book.genere.toString())
            Log.v("Disponibilità",book.disponibilità.toString())
            Log.v("Data",book.data.toString())
            Log.v("URL Copertina",book.copertina.toString())
        }
    }


}