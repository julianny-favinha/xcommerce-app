package com.xcommerce.mc920.xcommerce.utilities

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class ClientHttpUtil {

    companion object {

        const val BASE = "https://desolate-gorge-89381.herokuapp.com"

        inline fun <reified T : Any> getRequest(path: String): T? {
            val client = OkHttpClient()
            val objectMapper = jacksonObjectMapper()

            val request = Request.Builder().url(BASE + path).build()

            Log.d("[CLIENT HTTP]", "About to do request to " + request.toString())
            val response = client.newCall(request).execute()
            Log.d("[CLIENT HTTP]", "Received: " + response.toString())

//            if (!response.isSuccessful) {
//                return null
//            }

            // TODO("change backend to conform with Product")
            val jsonExample = """{
              "highlights": [
                {
                  "id": 1,
                  "name": "GOOGLE",
                  "brand": "Google",
                  "category": "Internet",
                  "description": "HAHAHAHA",
                  "price": 1000,
                  "imageUrl": "https://www.google.com.br/images/branding/googlelogo/2x/googlelogo_color_120x44dp.png"
                }, {
                  "id": 1,
                  "name": "GOOGLE",
                  "brand": "Google",
                  "category": "Internet",
                  "description": "HAHAHAHA",
                  "price": 1000,
                  "imageUrl": "https://www.google.com.br/images/branding/googlelogo/2x/googlelogo_color_120x44dp.png"
              }]
            }"""

            return objectMapper.readValue(jsonExample)

        }

        inline fun <reified T : Any, K> postRequest(path: String, body: K): T? {
            val client = OkHttpClient()
            val objectMapper = jacksonObjectMapper()

            val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), objectMapper.writeValueAsString(body))
            val request = Request.Builder().url(BASE + path).post(requestBody).build()

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