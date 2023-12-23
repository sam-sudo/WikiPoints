package com.happydonia.pruebaTecnica.domain


import kotlinx.serialization.SerialName

data class Image(
    @SerialName("ns")
    val ns: Int,
    @SerialName("title")
    val title: String
)