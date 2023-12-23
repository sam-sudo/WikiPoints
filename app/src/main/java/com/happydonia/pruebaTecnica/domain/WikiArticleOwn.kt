package com.happydonia.pruebaTecnica.domain

import kotlinx.serialization.SerialName


data class WikiArticleOwn(
     val pageId: String,
     val title: String,
     val distance: String,
     val imageUrl: String
)