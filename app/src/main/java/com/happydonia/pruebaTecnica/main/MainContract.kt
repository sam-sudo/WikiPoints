package com.happydonia.pruebaTecnica.main

import com.happydonia.pruebaTecnica.data.WikiArticleOwn

interface MainContract {

    interface View{
        fun showWikiArticles(wikiList: MutableList<WikiArticleOwn>)
        fun showError(message: String)
    }

    interface Presenter{
        suspend fun getWikiArticles()
    }

    interface ModelApi{
        suspend fun getWikiArticlesFromApi(successListener: (MutableList<WikiArticleOwn>) -> Unit,
                                           errorListener: (String) -> Unit )
    }

}