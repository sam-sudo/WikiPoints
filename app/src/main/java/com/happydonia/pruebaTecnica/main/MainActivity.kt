package com.happydonia.pruebaTecnica.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.happydonia.pruebaTecnica.R
import com.happydonia.pruebaTecnica.domain.WikiArticle
import com.happydonia.pruebaTecnica.domain.WikiArticleOwn
import com.happydonia.pruebaTecnica.domain.adapters.WikiArticlesAdapter
import com.happydonia.pruebaTecnica.utils.LogHandler

class MainActivity  : AppCompatActivity(), MainContract.View{

    private val LOCATION_PERMISSION_REQUEST_CODE = 1


    lateinit var mRecyclerView : RecyclerView
    val mAdapter : WikiArticlesAdapter = WikiArticlesAdapter()
    lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogHandler.w("onCreate mainActivity")

        mRecyclerView = findViewById<RecyclerView>(R.id.rvWikiArticles)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.WikiArticlesAdapter(this)
        mRecyclerView.adapter = mAdapter

        mainPresenter = MainPresenter(this,MainApi(this))




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





        LogHandler.w("END onCreate mainActivity")

    }




    override fun showWikiArticles(wikiList: MutableList<WikiArticleOwn>) {
        LogHandler.w("showWiki")

        mAdapter.submitList(wikiList)

        /*mAdapter.wikiArticles = wikiList*/


    }

    override fun showError(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }




    //Permissions
    private fun permissionsAccepted(){
        LogHandler.w("permission accepted", "Permissions")
        //showWikiArticles(getWikiArticles())
        mainPresenter.getWikiArticles()


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