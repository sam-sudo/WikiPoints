package com.happydonia.pruebaTecnica.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.happydonia.pruebaTecnica.R
import com.happydonia.pruebaTecnica.domain.WikiArticleOwn
import com.happydonia.pruebaTecnica.domain.adapters.WikiArticlesAdapter
import com.happydonia.pruebaTecnica.utils.LogHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity  : AppCompatActivity(), MainContract.View{

    private val LOCATION_PERMISSION_REQUEST_CODE = 1


    lateinit var mRecyclerView : RecyclerView
    val mAdapter : WikiArticlesAdapter = WikiArticlesAdapter()
    lateinit var mainPresenter: MainPresenter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvNoPermission: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogHandler.w("onCreate mainActivity")

        mRecyclerView = findViewById<RecyclerView>(R.id.rvWikiArticles)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter.WikiArticlesAdapter(this)
        mRecyclerView.adapter = mAdapter

        mainPresenter = MainPresenter(this,MainApi(this))


        progressBar = findViewById(R.id.progressBar)
        tvNoPermission = findViewById(R.id.tv_no_permissions)


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

        hideProgressBar()
        showRecyclerList()
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

        hideRecyclerList()
        showProgressBar()
        GlobalScope.launch {
            try {

                mainPresenter.getWikiArticles()
            }catch (e: Exception){
                LogHandler.w("Error en el primer try catch")
            }
        }
        LogHandler.w("permission accepted done", "Permissions")

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
            showError("No se han concedido los permisos de ubicación.")
            dialog.dismiss()
            showNoPermissionsText()
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

    // Función para mostrar el Snackbar
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showNoPermissionsText() {
        tvNoPermission.visibility = View.VISIBLE
    }

    private fun hideNoPermissionsText() {
        tvNoPermission.visibility = View.GONE
    }



    private fun hideRecyclerList() {
        mRecyclerView.visibility = View.GONE
    }
    private fun showRecyclerList() {
        hideNoPermissionsText()
        mRecyclerView.visibility = View.VISIBLE
    }

}