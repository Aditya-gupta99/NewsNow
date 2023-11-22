package com.sparklead.newsnow.serviceImp

import com.sparklead.newsnow.model.Article
import com.sparklead.newsnow.model.Source
import com.sparklead.newsnow.service.NewsService
import com.sparklead.newsnow.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NewsServiceImp : NewsService {
    override suspend fun getAllNewsArticle(): List<Article> = withContext(Dispatchers.IO) {
        val connection = URL(Constants.BASE_URL).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "application/json")
        connection.connectTimeout = 10000
        connection.readTimeout = 10000

        val reader = InputStreamReader(connection.inputStream)

        reader.use { input ->
            val response = StringBuilder()
            val bufferReader = BufferedReader(input)

            bufferReader.forEachLine {
                response.append(it.trim())
            }

            // Parsing the JSON response and convert it to a list of Article objects
            return@withContext parseJsonResponse(response.toString())
        }
    }

    private fun parseJsonResponse(jsonString: String): List<Article> {
        val articlesList = mutableListOf<Article>()

        try {
            val jsonObject = JSONObject(jsonString)
            val articlesArray = jsonObject.getJSONArray("articles")

            for (i in 0 until articlesArray.length()) {
                val articleObject = articlesArray.getJSONObject(i)

                val sourceObject = articleObject.getJSONObject("source")
                val source = Source(
                    id = sourceObject.getString("id"),
                    name = sourceObject.getString("name")
                )

                val article = Article(
                    author = articleObject.getString("author"),
                    content = articleObject.getString("content"),
                    description = articleObject.getString("description"),
                    publishedAt = articleObject.getString("publishedAt"),
                    source = source,
                    title = articleObject.getString("title"),
                    url = articleObject.getString("url"),
                    urlToImage = articleObject.getString("urlToImage")
                )

                articlesList.add(article)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return articlesList
    }

}