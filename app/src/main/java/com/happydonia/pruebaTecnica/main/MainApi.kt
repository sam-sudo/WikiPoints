package com.happydonia.pruebaTecnica.main

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.happydonia.pruebaTecnica.data.WikiArticle
import org.json.JSONException
import com.android.volley.Request;
import com.happydonia.pruebaTecnica.data.WikiArticleOwn
import com.happydonia.pruebaTecnica.data.WikiArticleWithImage
import com.happydonia.pruebaTecnica.utils.LocationHandler
import com.happydonia.pruebaTecnica.utils.LogHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class MainApi(var context: Context): MainContract.ModelApi{

    private val BASE_URL = "https://en.wikipedia.org/w/api.php"
    private val ACTION_QUERY = "query"
    private val FORMAT_JSON = "json"

    // Método para hacer la solicitud a la API de MediaWiki
    private suspend fun  searchNearsArticlesAsync(latitud: Double, longitud: Double): MutableList<WikiArticle> {
        return suspendCancellableCoroutine { continuation ->

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

                        continuation.resume(articles)

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


    }


    suspend fun searchArticlesInfo( articlesIds: List<Int>):MutableList<WikiArticleWithImage> {
        return suspendCancellableCoroutine { continuation ->

            // Obtén la instancia de RequestQueue
            val queue: RequestQueue = Volley.newRequestQueue(context)
            val pageIdsString = articlesIds.joinToString("|")
            // Parámetros de la solicitud
            val parameters = String.format(
                "?action=%s&prop=coordinates|pageimages|info&pageids=%s&inprop=url&format=%s",
                ACTION_QUERY,
                pageIdsString,
                FORMAT_JSON
            )

            // Crea la URL completa para la solicitud
            val url = BASE_URL + parameters

            Log.w("TAG", "url 2: $url", )


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

                        val pagesObject = queryObject.getJSONObject("pages")
                        LogHandler.w("pagesObject : ${pagesObject.toString()}")

                        // Obtener las claves de "pages"
                        val pageIds = pagesObject.keys()

                        // Iterar sobre las claves y obtener la información de cada página
                        val pagesList = mutableListOf<WikiArticleWithImage>()

                        for (pageId in pageIds) {
                            val pageObject = pagesObject.getJSONObject(pageId)

                            val title = pageObject.getString("title")
                            val fullUrl = pageObject.getString("fullurl")
                            val thumbnailObject = pageObject.getJSONObject("thumbnail")
                            val thumbnailSource = thumbnailObject.getString("source")
                            val pageImage = pageObject.getString("pageimage")

                            val wikiPage = WikiArticleWithImage(title,pageId,pageImage,thumbnailSource,fullUrl)

                            pagesList.add(wikiPage)
                        }

                        continuation.resume(pagesList)

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
    }

    private fun createOwnArticleObject(wikiArticle: MutableList<WikiArticle>, wikiArticleWithImage: MutableList<WikiArticleWithImage>): MutableList<WikiArticleOwn> {

        var articlesOwnList: MutableList<WikiArticleOwn> = ArrayList()

        for ((index,articleWithImage) in wikiArticleWithImage.withIndex() ){

            var articleown = WikiArticleOwn(articleWithImage.pageid,articleWithImage.title,articleWithImage.imagenUrl,articleWithImage.articleUrl,"")
            articlesOwnList.add(articleown )

        }

        for ((index,articleWithImage) in wikiArticle.withIndex() ){

            articlesOwnList[index].distance = articleWithImage.distance.toString()

        }



        return articlesOwnList
    }




    override suspend fun getWikiArticlesFromApi(
        successListener: (MutableList<WikiArticleOwn>) -> Unit,
        errorListener:(String) -> Unit){


        var position = LocationHandler.getPosition(context = context)

        LogHandler.w("position: $position")
        if(position != null){

            var (latitude, longitude) = position
            LogHandler.w("position: $latitude")


            try {
                // Realiza la primera llamada a la API
                val articlesWiki = withContext(Dispatchers.IO) {

                    searchNearsArticlesAsync(latitude, longitude)
                }

                var wikiIds: List<Int> = articlesWiki.map {
                    it.pageId
                }
                LogHandler.w("pagesIDS: $wikiIds")

                // Realiza la segunda llamada a la API con datos de la primera respuesta
                val articlesWithImages = withContext(Dispatchers.IO) {
                    searchArticlesInfo(wikiIds)
                }

                LogHandler.w("pre articlesOwnList: ")

                var articlesOwnList: MutableList<WikiArticleOwn> = createOwnArticleObject(articlesWiki,articlesWithImages)

                LogHandler.w("articlesOwnList: $articlesOwnList")
                // Llama al successListener con los resultados finales
                successListener(articlesOwnList)

                LogHandler.w("fin corrutina")
            } catch (e: Exception) {
                // Manejar errores
                errorListener(e.message ?: "Error desconocido")
            }
        } else {
            // Manejar el caso en que la posición sea nula
        }


    }

}