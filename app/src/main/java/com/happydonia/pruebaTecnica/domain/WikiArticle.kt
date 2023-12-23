package com.happydonia.pruebaTecnica.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WikiArticle(
    @SerialName("pageid") val pageId: Int,
    @SerialName("ns") val namespace: Int,
    @SerialName("title") val title: String,
    @SerialName("lat") val latitude: Double,
    @SerialName("lon") val longitude: Double,
    @SerialName("dist") val distance: Double,
    @SerialName("primary") val primary: String
)