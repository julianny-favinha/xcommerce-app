package com.xcommerce.mc920.xcommerce.utilities

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.xcommerce.mc920.xcommerce.user.UserHelper
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class ClientHttpUtil {

    companion object {

        const val BASE = "https://hidden-gorge-33055.herokuapp.com"

        inline fun <reified T : Any> getRequest(path: String, needLogin: Boolean = false): T? {
            val client = OkHttpClient()
            val objectMapper = jacksonObjectMapper()


            val request = if (needLogin) {
                Request.Builder().url(BASE + path).addHeader("x-auth-token", UserHelper.token).build()
            }
            else {
                Request.Builder().url(BASE + path).build()
            }

            Log.d("[CLIENT HTTP]", "About to do request to " + request.toString())
            val response = client.newCall(request).execute()
            Log.d("[CLIENT HTTP]", "Received: " + response.toString())

            if (!response.isSuccessful) {
                return null
            }

            return objectMapper.readValue<T>(response.body()!!.byteStream())

        }

        inline fun <reified T : Any, K> postRequest(path: String, body: K, needLogin: Boolean = false): T? {
            val client = OkHttpClient()
            val objectMapper = jacksonObjectMapper()

            Log.d("[CLIENT HTTP]", "Request body: " + objectMapper.writeValueAsString(body))

            val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), objectMapper.writeValueAsString(body))
            val request = if (needLogin) {
                Request.Builder().url(BASE + path).post(requestBody).addHeader("x-auth-token", UserHelper.token).build()
            } else {
                Request.Builder().url(BASE + path).post(requestBody).build()
            }

            Log.d("[CLIENT HTTP]", "About to do request to " + request.toString())
            val response = client.newCall(request).execute()
            Log.d("[CLIENT HTTP]", "Received: " + response.toString())

            if (!response.isSuccessful) {
                return null
            }

            return objectMapper.readValue(response.body()!!.byteStream())
        }

        inline fun <reified T : Any, K> putRequest(path: String, body: K, needLogin: Boolean = false): T? {
            val client = OkHttpClient()
            val objectMapper = jacksonObjectMapper()

            val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), objectMapper.writeValueAsString(body))

            val request = if (needLogin) {
                Request.Builder().url(BASE + path).put(requestBody).addHeader("x-auth-token", UserHelper.token).build()
            } else {
                Request.Builder().url(BASE + path).put(requestBody).build()
            }

            Log.d("[CLIENT HTTP]", "About to do request to " + request.toString())
            val response = client.newCall(request).execute()
            Log.d("[CLIENT HTTP]", "Received: " + response.toString())

            if (!response.isSuccessful) {

                return null
            }
            val body = objectMapper.readValue<T>(response.body()!!.byteStream())
            Log.d("[CLIENT HTTP]", "Received: $body")

            return body
        }

    }
}