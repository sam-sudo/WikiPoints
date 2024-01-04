package com.happydonia.pruebaTecnica.main

import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.happydonia.pruebaTecnica.data.WikiArticle
import com.happydonia.pruebaTecnica.data.WikiArticleOwn
import com.happydonia.pruebaTecnica.data.WikiArticleWithImage
import com.happydonia.pruebaTecnica.utils.LocationHandler
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verifyNoInteractions
class MainApiTest{

    private lateinit var context: Context
    private lateinit var mainApi: MainApi
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        context = mock()
        mainApi = MainApi(context)
    }

    @Test
    fun `test createOwnArticleObject`() {
        // Configura datos simulados
        val wikiArticle = mutableListOf(
            WikiArticle(1, 0, "Title1", 37.7749, -122.4194, 10.0, ""),
            WikiArticle(2, 0, "Title2", 37.775, -122.419, 20.0, "")
        )

        val wikiArticleWithImage = mutableListOf(
            WikiArticleWithImage("Title1", "1", "ImageUrl1", "urlimagen1","ArticleUrl1"),
            WikiArticleWithImage("Title2", "2", "ImageUrl2", "urlimagen2","ArticleUrl2")
        )

        // Llama a la función bajo prueba
        val result = mainApi.createOwnArticleObject(wikiArticle, wikiArticleWithImage)

        // Verifica el resultado esperado
        assertEquals(2, result.size)

        // Verifica que los objetos WikiArticleOwn se hayan creado correctamente
        assertEquals("1", result[0].pageId)
        assertEquals("Title1", result[0].title)
        assertEquals("urlimagen1", result[0].imageUrl)
        assertEquals("ArticleUrl1", result[0].fullUrl)
        assertEquals("10.0", result[0].distance)

        assertEquals("2", result[1].pageId)
        assertEquals("Title2", result[1].title)
        assertEquals("urlimagen2", result[1].imageUrl)
        assertEquals("ArticleUrl2", result[1].fullUrl)
        assertEquals("20.0", result[1].distance)
    }


}