package it.insubria.biblioteca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.util.Log
import androidx.core.content.ContextCompat

class AdapterGestionePrestiti(private val itemList: ArrayList<PrestitoLibro>) : RecyclerView.Adapter<AdapterGestionePrestiti.ViewHolder>() {

    private lateinit var mlistener: onItemClickListener
    private val itemsToShow: ArrayList<PrestitoLibro> = ArrayList(itemList)

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mlistener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemprestito, parent, false)
        return ViewHolder(itemView, mlistener)
    }

    override fun getItemCount(): Int {
        itemsToShow.clear()
        itemsToShow.addAll(itemList.filter { it.dataRestituzione.isNullOrEmpty() })
        return itemsToShow.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemsToShow[position]
        holder.bind(currentItem)
    }


    class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        private val titolo: TextView = itemView.findViewById(R.id.titolo_item)
        private val autore: TextView = itemView.findViewById(R.id.autore_item)
        private val utente: TextView = itemView.findViewById(R.id.utente_item)
        private val stato: TextView = itemView.findViewById(R.id.stato_item)
        private val copertina: ImageView = itemView.findViewById(R.id.copertina_item)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        fun bind(prestitolibro: PrestitoLibro) {
            titolo.text = prestitolibro.titolo
            autore.text = prestitolibro.autore
            utente.text = prestitolibro.IdUtente

            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val dataScadenza = LocalDate.parse(prestitolibro.dataScadenza, formatter)
                val oggi = LocalDate.now()

                Log.d("AdapterGestionePrestiti", "Data scadenza: $dataScadenza, Oggi: $oggi")

                stato.text = when {
                    dataScadenza.isBefore(oggi) -> {
                        Log.d("AdapterGestionePrestiti", "Libro scaduto")
                        stato.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                        "Scaduto"
                    }
                    else -> {
                        Log.d("AdapterGestionePrestiti", "Libro in corso")
                        stato.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                        "In corso"
                    }
                }
            } catch (e: Exception) {
                Log.e("AdapterGestionePrestiti", "Errore nel parsing delle date", e)
                stato.text = "Errore"
            }

            Glide.with(itemView.context)
                .load(prestitolibro.copertina)
                .apply(RequestOptions().fitCenter())
                .into(copertina)
        }
    }
}