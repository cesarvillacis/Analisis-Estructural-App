package com.example.analisisestructural.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object ApiClient {

    private const val API_URL = "http://3.138.111.107:8000/analizar_png"
    private val client = OkHttpClient()

    fun analizarPortico(
        pisos: Int,
        vanos: Int,
        altura: Float,
        longitud: Float
    ): ByteArray {

        val json = """
            {
                "pisos": $pisos,
                "vanos": $vanos,
                "altura": $altura,
                "longitud": $longitud
            }
        """.trimIndent()

        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(API_URL)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Error HTTP ${response.code}")
            }
            return response.body!!.bytes()
        }
    }
}

