package com.happydonia.pruebaTecnica.domain.adapters

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.recyclerview.widget.RecyclerView
import com.happydonia.pruebaTecnica.R
import com.happydonia.pruebaTecnica.domain.WikiArticle
import com.squareup.picasso.Picasso


class WikiArticlesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER_TYPE = 1
    private val ITEM_TYPE = 2
    var wikiArticles: MutableList<WikiArticle> = ArrayList()
    lateinit var context: Context

    private lateinit var locationManager: LocationManager

    fun WikiArticlesAdapter(wikiArticleList: MutableList<WikiArticle>, context: Context) {
        this.wikiArticles = wikiArticleList
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


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
                // Configurar el encabezado aquí
                val headerHolder = holder as HeaderViewHolder
                //headerHolder.tvActualPosition.text = "Texto que se desplaza"
                var position = getPosition()

                if(position != null){
                    var (latitud, longitud) = position


                    headerHolder.tvActualPosition.text = "Longitud: $longitud \nLatitud : $latitud"

                }else{
                    headerHolder.tvActualPosition.text = "Can't find position"
                }


            }
            ITEM_TYPE -> {
                // Configurar los elementos de la lista
                val itemHolder = holder as ViewHolder
                val item = wikiArticles[position - 1] // Restar 1 para compensar el encabezado
                itemHolder.bind(item, context)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER_TYPE
        } else {
            ITEM_TYPE
        }
    }

    override fun getItemCount(): Int {
        return wikiArticles.size + 1 // Agregar 1 para el encabezado
    }

    // ViewHolder para el encabezado con TextView desplazable
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvActualPosition: TextView = view.findViewById(R.id.tvActualPosition)
    }

    // ViewHolder para los elementos de la lista
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wikiArticleTitle = view.findViewById(R.id.tv_wikiTitle) as TextView
        val wikiArticleImage = view.findViewById(R.id.iv_wikiImage) as ImageView
        val wikiArticleDistance = view.findViewById(R.id.tv_wikiDistance) as TextView

        fun bind(wikiArticle: WikiArticle, context: Context) {
            wikiArticleTitle.text = wikiArticle.name
            wikiArticleDistance.text = wikiArticle.distance
            itemView.setOnClickListener(View.OnClickListener {
                Toast.makeText(context, wikiArticle.name, Toast.LENGTH_SHORT).show()
            })
            wikiArticleImage.loadUrl(wikiArticle.imageURL)
        }

        fun ImageView.loadUrl(url: String) {
            Picasso.with(context).load(url).into(this)
        }
    }


    private fun getPosition(): Pair<Double, Double>? {
        return try {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                // Aquí obtienes la ubicación actual
                val latitude = location.latitude
                val longitude = location.longitude
                Pair(latitude, longitude)
            } else {
                println("No se pudo obtener la ubicación.")
                null
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            null
        }
    }

}