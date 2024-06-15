package it.insubria.biblioteca.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import it.insubria.biblioteca.dataClass.PercorsoLettura
import it.insubria.biblioteca.R

class AdapterPercorsi(private val itemList : ArrayList<PercorsoLettura>) : RecyclerView.Adapter<AdapterPercorsi.ViewHolder>(){

    private lateinit var mlistener : onItemClickListener

    // Interfaccia per gestire i click sugli elementi dell'adapter
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    // Metodo per impostare il listener per i click sugli elementi
    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistener = listener
    }

    // Creazione di nuove righe della lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.perclettura,parent,false)
        return ViewHolder(itemView,mlistener)
    }

    // Ritorna il numero totale di elementi nella lista
    override fun getItemCount(): Int {
        return itemList.size
    }

    // Associa i dati agli elementi della lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentitem = itemList[position]
        holder.bind(currentitem)
    }

    // ViewHolder per contenere gli elementi della riga della lista
    class ViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        private val titolo : TextView = itemView.findViewById(R.id.titolo_item)
        private val emin : TextView = itemView.findViewById(R.id.minima_item)
        private val emax : TextView = itemView.findViewById(R.id.massima_item)
        private val copertina: ImageView = itemView.findViewById(R.id.copertina_item)

        init {
            // Imposta il listener per gestire i click sugli elementi della lista
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        // Metodo per bindare i dati con la viewholder
        fun bind(percorso: PercorsoLettura) {
            titolo.text = percorso.titolo
            emin.text = percorso.etàMinima.toString()
            emax.text = percorso.etàMassima.toString()

            // Carica l'immagine del libro utilizzando Glide
            Glide.with(itemView.context)
                .load(percorso.copertina)
                .apply(RequestOptions().fitCenter())
                .into(copertina)
        }
    }
}