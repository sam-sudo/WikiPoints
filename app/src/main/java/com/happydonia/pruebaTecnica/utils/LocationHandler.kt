package com.happydonia.pruebaTecnica.utils

import android.content.Context
import android.location.LocationManager

object LocationHandler {



     fun getPosition(context: Context): Pair<Double, Double>? {
            LogHandler.w("position","get position")
        return try {

            var locationManager: LocationManager  = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


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