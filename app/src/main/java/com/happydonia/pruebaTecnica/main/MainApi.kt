package com.happydonia.pruebaTecnica.main

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.happydonia.pruebaTecnica.domain.WikiArticle
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.Request;
import com.android.volley.Response
import com.android.volley.VolleyError;
import com.happydonia.pruebaTecnica.GeosearchResult
import com.happydonia.pruebaTecnica.domain.WikiArticleOwn
import com.happydonia.pruebaTecnica.utils.LocationHandler
import com.happydonia.pruebaTecnica.utils.LogHandler
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MainApi(var context: Context): MainContract.ModelApi{

    private val BASE_URL = "https://en.wikipedia.org/w/api.php"
    private val ACTION_QUERY = "query"
    private val FORMAT_JSON = "json"

    // Método para hacer la solicitud a la API de MediaWiki
    fun searchNearsArticles( latitud: Double, longitud: Double,
                             successListener: (MutableList<WikiArticleOwn>) -> Unit,
                             errorListener:(String) -> Unit) {
        // Obtén la instancia de RequestQueue
        val queue: RequestQueue = Volley.newRequestQueue(context)

        LogHandler.w("latitud: $latitud")
        // Parámetros de la solicitud
        val parameters = String.format(
            "?action=%s&list=geosearch&gscoord=%s|%s&gsradius=10000&format=%s",
            ACTION_QUERY,
            latitud,
            longitud,
            FORMAT_JSON
        )

        // Crea la URL completa para la solicitud
        val url = BASE_URL + parameters

        Log.w("TAG", "url: $url", )

        // Crea la solicitud GET
        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                // Manejar la respuesta exitosa
                LogHandler.w("response:")
                try {
                    // Obtener la lista de artículos cercanos
                    val queryObject = response.getJSONObject("query")
                    LogHandler.w("queryObject : $queryObject")
                    val pagesObject = queryObject.getJSONArray("geosearch")

                    LogHandler.w("pagesObject : ${pagesObject.toString()}")

                    // Deserializar el JSON a un objeto QueryResult
                    val articles = mutableListOf<WikiArticle>()

                    for (i in 0 until pagesObject.length()) {
                        val articleObject = pagesObject.getJSONObject(i)

                        val pageId = articleObject.getInt("pageid")
                        val namespace = articleObject.getInt("ns")
                        val title = articleObject.getString("title")
                        val latitude = articleObject.getDouble("lat")
                        val longitude = articleObject.getDouble("lon")
                        val distance = articleObject.getDouble("dist")
                        val primary = articleObject.getString("primary")

                        val article = WikiArticle(pageId, namespace, title, latitude, longitude, distance, primary)
                        articles.add(article)
                    }
                    LogHandler.w("articles : ${articles.toString()}")

                    Log.w("dd", "getWikiArticlesFromApi: ", )

                    var wikiArticleList:MutableList<WikiArticleOwn> = ArrayList()
                    wikiArticleList.apply {
                        add(WikiArticleOwn("1111","prueba imagen url","10 km","https://commons.wikimedia.org/wiki/File:Aiga_bus_trans.svg"))
                        add(WikiArticleOwn("111133","prueba imagen url","12 km","https://commons.wikimedia.org/wiki/File:Aiga_bus_trans.svg"))
                        add(WikiArticleOwn("111133","prueba imagen url","13 km","https://commons.wikimedia.org/wiki/File:Aiga_bus_trans.svg"))

                    }

                    successListener(wikiArticleList)

                    //searchArticlesInfo()

                    /*for (article in articles) {
                        println("Title: ${article.title}")
                        println("Latitude: ${article.latitude}")
                        println("Longitude: ${article.longitude}")
                        println()
                    }*/

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                // Manejar el error de la solicitud
                // Aquí puedes manejar errores de conexión, tiempo de espera, etc.
            }
        )


        // Añade la solicitud a la cola
        queue.add(request)
    }
   /* fun searchArticlesInfo( articlesIds: ArrayList<Int>) {
        // Obtén la instancia de RequestQueue
        val queue: RequestQueue = Volley.newRequestQueue(context)

        // Parámetros de la solicitud
        val parameters = String.format(
            "?action=%s&list=geosearch&gscoord=%s|%s&gsradius=10000&format=%s",
            ACTION_QUERY,
            latitud,
            longitud,
            FORMAT_JSON
        )

        // Crea la URL completa para la solicitud
        val url = BASE_URL + parameters

        Log.w("TAG", "url: $url", )

        // Crea la solicitud GET
        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                // Manejar la respuesta exitosa
                LogHandler.w("response:")
                try {
                    // Obtener la lista de artículos cercanos
                    val queryObject = response.getJSONObject("query")
                    LogHandler.w("queryObject : $queryObject")
                    val pagesObject = queryObject.getJSONArray("geosearch")

                    LogHandler.w("pagesObject : ${pagesObject.toString()}")

                    // Deserializar el JSON a un objeto QueryResult
                    val articles = mutableListOf<WikiArticle>()

                    for (i in 0 until pagesObject.length()) {
                        val articleObject = pagesObject.getJSONObject(i)

                        val pageId = articleObject.getInt("pageid")
                        val namespace = articleObject.getInt("ns")
                        val title = articleObject.getString("title")
                        val latitude = articleObject.getDouble("lat")
                        val longitude = articleObject.getDouble("lon")
                        val distance = articleObject.getDouble("dist")
                        val primary = articleObject.getString("primary")

                        val article = WikiArticle(pageId, namespace, title, latitude, longitude, distance, primary)
                        articles.add(article)
                    }

                    searchArticlesInfo()

                    *//*for (article in articles) {
                        println("Title: ${article.title}")
                        println("Latitude: ${article.latitude}")
                        println("Longitude: ${article.longitude}")
                        println()
                    }*//*

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                // Manejar el error de la solicitud
                // Aquí puedes manejar errores de conexión, tiempo de espera, etc.
            }
        )


        // Añade la solicitud a la cola
        queue.add(request)
    }*/




    override fun getWikiArticlesFromApi(
        successListener: (MutableList<WikiArticleOwn>) -> Unit,
        errorListener:(String) -> Unit){


        var position = LocationHandler.getPosition(context = context)

        LogHandler.w("position: $position")
        if(position != null){
            var (latitude, longitude) = position
            LogHandler.w("position: $latitude")

            searchNearsArticles(latitude,longitude, successListener, errorListener)


        }else{

        }


    }

}