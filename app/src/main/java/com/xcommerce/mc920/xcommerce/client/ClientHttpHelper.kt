package com.xcommerce.mc920.xcommerce.client

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request

class ClientHttpHelper {

    companion object {

        const val BASE = "https://desolate-gorge-89381.herokuapp.com"

        inline fun <reified T: Any> getRequest(path: String): T? {
            val client = OkHttpClient()
            val objectMapper = jacksonObjectMapper()

            val request = Request.Builder().url(BASE + path).build()

            Log.d("[CLIENT HTTP]", "About to do request to " + request.toString())
            val response = client.newCall(request).execute()
            Log.d("[CLIENT HTTP]", "Received: " + response.toString())

            if (!response.isSuccessful) {
                return null
            }

            return objectMapper.readValue(response.body()!!.byteStream())

        }

    }
}