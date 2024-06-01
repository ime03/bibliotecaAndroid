package it.insubria.biblioteca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter(private val itemList : ArrayList<Libro>) : RecyclerView.Adapter<Adapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentitem = itemList[position]
        holder.titolo.text = currentitem.titolo
        holder.autore.text = currentitem.autore

        Glide.with(holder.itemView.context)
            .load(currentitem.copertina) // Carica l'URL dell'immagine di copertina
            .into(holder.copertina)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val titolo : TextView = itemView.findViewById(R.id.titolo_item)
        val autore : TextView = itemView.findViewById(R.id.autore_item)
        val copertina: ImageView = itemView.findViewById(R.id.copertina_item)

    }
}