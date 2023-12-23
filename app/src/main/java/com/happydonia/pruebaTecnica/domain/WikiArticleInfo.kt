package com.happydonia.pruebaTecnica.domain


import kotlinx.serialization.SerialName

data class WikiArticleInfo(
    @SerialName("canonicalurl")
    val canonicalurl: String,
    @SerialName("contentmodel")
    val contentmodel: String,
    @SerialName("editurl")
    val editurl: String,
    @SerialName("extract")
    val extract: String,
    @SerialName("fullurl")
    val fullurl: String,
    @SerialName("images")
    val images: List<Image>,
    @SerialName("lastrevid")
    val lastrevid: Int,
    @SerialName("length")
    val length: Int,
    @SerialName("ns")
    val ns: Int,
    @SerialName("pagelanguage")
    val pagelanguage: String,
    @SerialName("pagelanguagedir")
    val pagelanguagedir: String,
    @SerialName("pagelanguagehtmlcode")
    val pagelanguagehtmlcode: String,
    @SerialName("title")
    val title: String,
    @SerialName("touched")
    val touched: String
)