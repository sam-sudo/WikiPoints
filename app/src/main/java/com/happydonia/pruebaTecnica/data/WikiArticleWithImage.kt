package com.happydonia.pruebaTecnica.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WikiArticleWithImage(
    @SerialName("title")
    val title: String,
    @SerialName("pageid")
    val pageid: String,
    @SerialName("pageimage")
    val pageimage: String,
    val imagenUrl: String,
    @SerialName("fullurl")
    val articleUrl: String

)