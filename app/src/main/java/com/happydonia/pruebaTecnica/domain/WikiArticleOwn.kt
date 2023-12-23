package com.happydonia.pruebaTecnica.domain

import kotlinx.serialization.SerialName


data class WikiArticleOwn(
     var pageId: String,
     var title: String,
     var imageUrl: String,
     var fullUrl: String,
     var distance: String?
)