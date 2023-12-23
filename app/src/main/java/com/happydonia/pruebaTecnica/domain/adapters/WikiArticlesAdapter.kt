package com.happydonia.pruebaTecnica.domain.adapters
import android.content.Context  // Asegúrate de importar la clase Context de Android
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.happydonia.pruebaTecnica.R
import com.happydonia.pruebaTecnica.domain.WikiArticle
import com.happydonia.pruebaTecnica.domain.WikiArticleOwn
import com.happydonia.pruebaTecnica.utils.CircleTransform
import com.happydonia.pruebaTecnica.utils.LocationHandler
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

class WikiArticlesAdapter : ListAdapter<WikiArticleOwn, RecyclerView.ViewHolder>(WikiArticleDiffCallback()) {

    private val HEADER_TYPE = 1
    private val ITEM_TYPE = 2
    lateinit var context: Context

    fun WikiArticlesAdapter( context: Context) {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            HEADER_TYPE -> HeaderViewHolder(
                layoutInflater.inflate(R.layout.header_position, parent, false)
            )
            ITEM_TYPE -> ViewHolder(
                layoutInflater.inflate(R.layout.item_wiki_article, parent, false)
            )
            else -> throw IllegalArgumentException("Tipo de vista desconocido")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            HEADER_TYPE -> {
                val headerHolder = holder as HeaderViewHolder
                // Tu lógica de enlace para el encabezado aquí
                var position = LocationHandler.getPosition(context)

                if (position != null) {
                    var (latitud, longitud) = position
                    headerHolder.tvActualPosition.text = "Longitud: $longitud \nLatitud : $latitud"
                } else {
                    headerHolder.tvActualPosition.text = "No se puede encontrar la posición"
                }
            }
            ITEM_TYPE -> {
                val itemHolder = holder as ViewHolder
                val item = getItem(position - 1)
                itemHolder.bind(item, context)
            }
        }
    }


    override fun getItemCount(): Int {
        return currentList.size +1
    }
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER_TYPE
        } else {
            ITEM_TYPE
        }
    }

    // ViewHolder para el encabezado con TextView desplazable
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvActualPosition: TextView = view.findViewById(R.id.tvActualPosition)
    }

    private lateinit var onItemClickListener: ((wikiArticle: WikiArticleOwn) -> Unit)
    fun setOnItemClickListener(onItemClickListener: (wikiArticle: WikiArticleOwn) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    // ViewHolder para los elementos de la lista
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wikiArticleTitle = view.findViewById(R.id.tv_wikiTitle) as TextView
        val wikiArticleImage = view.findViewById(R.id.iv_wikiImage) as ImageView
        val wikiArticleDistance = view.findViewById(R.id.tv_wikiDistance) as TextView



        fun bind(wikiArticle: WikiArticleOwn, context: Context) {
            wikiArticleTitle.text = wikiArticle.title
            wikiArticleDistance.text = "${wikiArticle.distance.toString()} miles"
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikiArticle.fullUrl))
                context.startActivity(intent)
            }
            wikiArticleImage.loadUrl(wikiArticle.imageUrl)
        }

        fun ImageView.loadUrl(url: String) {
            Picasso.with(context)
                .load(url)
                .fit()
                .centerCrop()
                .transform(CircleTransform())
                .into(this)
        }



    }





}




class WikiArticleDiffCallback : DiffUtil.ItemCallback<WikiArticleOwn>() {
    override fun areItemsTheSame(oldItem: WikiArticleOwn, newItem: WikiArticleOwn): Boolean {
        return oldItem.pageId == newItem.pageId
    }

    override fun areContentsTheSame(oldItem: WikiArticleOwn, newItem: WikiArticleOwn): Boolean {
        return oldItem == newItem
    }
}
