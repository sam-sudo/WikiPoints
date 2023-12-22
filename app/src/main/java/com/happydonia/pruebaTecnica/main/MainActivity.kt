package com.happydonia.pruebaTecnica.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.happydonia.pruebaTecnica.R
import com.happydonia.pruebaTecnica.domain.WikiArticle
import com.happydonia.pruebaTecnica.domain.adapters.WikiArticlesAdapter
import com.happydonia.pruebaTecnica.utils.LogHandler

class MainActivity  : AppCompatActivity(), MainContract.View{

    private val LOCATION_PERMISSION_REQUEST_CODE = 1


    lateinit var mRecyclerView : RecyclerView
    val mAdapter : WikiArticlesAdapter = WikiArticlesAdapter()

    lateinit var tvActualPosition : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogHandler.w("onCreate mainActivity")
        setContentView(R.layout.activity_main)



        if (checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            LogHandler.w("permiso otorgado ya", "Permissions")
            permissionsAccepted()
        } else {
            // El permiso no está otorgado, mostrar el diálogo de solicitud
            LogHandler.w("permiso sin otorgar aún", "Permissions")
            showLocationPermissionDialog()
        }






    }

    fun getWikiArticles(): MutableList<WikiArticle>{
        var wikiArticleList:MutableList<WikiArticle> = ArrayList()
        wikiArticleList.apply {
            add(WikiArticle("Artículo 1", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "10 km"))
            add(WikiArticle("Artículo 2", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "15 km"))
            add(WikiArticle("Artículo 3", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "20 km"))
            add(WikiArticle("Artículo 4", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "5 km"))
            add(WikiArticle("Artículo 5", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "12 km"))
            add(WikiArticle("Artículo 6", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "8 km"))
            add(WikiArticle("Artículo 7", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "18 km"))
            add(WikiArticle("Artículo 8", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "25 km"))
            add(WikiArticle("Artículo 9", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "3 km"))
            add(WikiArticle("Artículo 10", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "14 km"))
            add(WikiArticle("Artículo 11", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "7 km"))
            add(WikiArticle("Artículo 12", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "22 km"))
            add(WikiArticle("Artículo 13", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "11 km"))
            add(WikiArticle("Artículo 14", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "17 km"))
            add(WikiArticle("Artículo 15", "https://upload.wikimedia.org/wikipedia/commons/0/07/Madonna_You%27ll_See.jpg", "9 km"))
        }
        return wikiArticleList
    }


    override fun showWikiArticles(wikiList: MutableList<WikiArticle>) {
        mRecyclerView = findViewById<RecyclerView>(R.id.rvWikiArticles)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.WikiArticlesAdapter(getWikiArticles(), this)
        mRecyclerView.adapter = mAdapter
    }

    override fun showError(message: String) {
        //todo mostrar pantalla con mensaje de error o un snackbar 
    }


    fun permissionsAccepted(){
        LogHandler.w("permission accepted", "Permissions")
        showWikiArticles(getWikiArticles())
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionsAccepted()
                } else {
                    LogHandler.w("permission denied", "Permissions")
                    showError("No location permission granted!")
                }
            }
        }
    }

    private fun showLocationPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permisos de Ubicación")
        builder.setMessage("Esta aplicación necesita permisos de ubicación para funcionar correctamente.")
        builder.setPositiveButton("Conceder") { dialog, which ->
            requestLocationPermission()
            dialog.dismiss()
        }
        builder.setNegativeButton("Denegar") { dialog, which ->
            // Puedes mostrar un mensaje o realizar acciones si el usuario niega los permisos
            dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }



}