package com.happydonia.pruebaTecnica.main

import android.content.Context


class MainPresenter(private val mainView: MainContract.View, private val mainApi: MainContract.ModelApi) : MainContract.Presenter {
    override fun getWikiArticles() {

        mainApi.getWikiArticlesFromApi({ wikiArticles ->
            mainView.showWikiArticles(wikiArticles)
        },{errorMessage ->
            mainView.showError("Error trying download Articles")
        })


    }
}