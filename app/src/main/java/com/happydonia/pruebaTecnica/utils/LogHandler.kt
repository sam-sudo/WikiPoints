package com.happydonia.pruebaTecnica.utils

import android.util.Log
import com.happydonia.pruebaTecnica.BuildConfig

object LogHandler {

    private val TAG = "TAG"


    fun w(message: String, tag: String = TAG){
        if (BuildConfig.BUILD_TYPE == "debug") {
            Log.w(tag, message)
        }


    }

    fun e(message: String, tag: String = TAG){
        if (BuildConfig.BUILD_TYPE == "debug") {
            Log.e(tag, message)
        }


    }


}