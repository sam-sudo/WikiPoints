package com.happydonia.pruebaTecnica.utils

import android.content.Context
import android.location.LocationManager

object LocationHandler {

    private lateinit var locationManager: LocationManager



     fun getPosition(context: Context): Pair<Double, Double>? {

        return try {

            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


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