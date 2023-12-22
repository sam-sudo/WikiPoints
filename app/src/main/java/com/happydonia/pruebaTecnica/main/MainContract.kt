package com.happydonia.pruebaTecnica.main

import com.happydonia.pruebaTecnica.domain.WikiArticle

interface MainContract {

    interface View{
        fun showWikiArticles(wikiList: MutableList<WikiArticle>)
        fun showError(message: String)
    }

    interface Presenter{
        fun getWikiArticles()
    }

    interface Model{
        fun getWikiArticlesFromApi()
    }

}