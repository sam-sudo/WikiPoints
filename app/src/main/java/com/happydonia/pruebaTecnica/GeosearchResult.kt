package com.happydonia.pruebaTecnica

import com.happydonia.pruebaTecnica.domain.WikiArticle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeosearchResult(
    @SerialName("geosearch") val geoSearchResults: List<WikiArticle>
)
