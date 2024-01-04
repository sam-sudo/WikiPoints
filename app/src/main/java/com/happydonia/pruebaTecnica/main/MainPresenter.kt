package com.happydonia.pruebaTecnica.main

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainPresenter(private val mainView: MainContract.View, private val mainApi: MainContract.ModelApi) : MainContract.Presenter {
    override suspend fun getWikiArticles() {

        mainApi.getWikiArticlesFromApi({ wikiArticles ->

            GlobalScope.launch(Dispatchers.Main) {
                try {

                    mainApi.getWikiArticlesFromApi(
                        successListener = { articles ->
                            // Manejar resultados exitosos aquí
                            mainView.showWikiArticles(articles)
                        },
                        errorListener = { errorMessage ->
                            // Manejar errores aquí
                            mainView.showError("Error with articles!")
                        }
                    )
                } catch (e: Exception) {
                    // Manejar excepciones generales aquí
                }
            }


        },{errorMessage ->
            mainView.showError(errorMessage)
        })


    }
}