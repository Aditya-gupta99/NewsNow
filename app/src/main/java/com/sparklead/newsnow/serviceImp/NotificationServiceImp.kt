package com.sparklead.newsnow.serviceImp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.sparklead.newsnow.model.PushNotification
import com.sparklead.newsnow.remote.HttpRoutes
import com.sparklead.newsnow.service.NotificationService
import com.sparklead.newsnow.utils.Constants.SERVER_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class NotificationServiceImp : NotificationService {

    // send notification post request
    override suspend fun pushNotification(notification: PushNotification) =
        withContext(Dispatchers.IO) {

            val connection = URL(HttpRoutes.FCM_BASE_URL).openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "key=$SERVER_KEY")

            // Enable input/output streams
            connection.doOutput = true

            val objectMapper = ObjectMapper().registerModule(KotlinModule())
            val requestBody = objectMapper.writeValueAsString(notification)

            // Write the request body to the output stream
            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.writeBytes(requestBody)
            outputStream.flush()
            outputStream.close()

            // Get the response
            val responseCode = connection.responseCode
            println("Response Code: $responseCode")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                println("Response: $response")
            } else {
                // Handle the error
                println("Error: ${connection.responseMessage}")
            }
        }
}