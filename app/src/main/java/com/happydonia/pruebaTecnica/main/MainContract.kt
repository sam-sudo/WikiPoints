package com.happydonia.pruebaTecnica.main

import android.content.Context
import com.happydonia.pruebaTecnica.domain.WikiArticle
import com.happydonia.pruebaTecnica.domain.WikiArticleOwn

interface MainContract {

    interface View{
        fun showWikiArticles(wikiList: MutableList<WikiArticleOwn>)
        fun showError(message: String)
    }

    interface Presenter{
        fun getWikiArticles()
    }

    interface ModelApi{
        fun getWikiArticlesFromApi( successListener: (MutableList<WikiArticleOwn>) -> Unit,
                                   errorListener: (String) -> Unit )
    }

}