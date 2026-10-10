package com.simoesctt.cornstore.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

object CatalogRepository {
    private const val CATALOG_URL = "https://simoesctt.github.io/corn-store-index/index.json"

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetch(): Result<Catalog> = withContext(Dispatchers.IO) {
        try {
            val conn = (URL(CATALOG_URL).openConnection() as HttpURLConnection).apply {
                connectTimeout = 15_000
                readTimeout = 30_000
                requestMethod = "GET"
                setRequestProperty("User-Agent", "CornStore-Android/1.0")
            }
            conn.connect()
            if (conn.responseCode != 200) {
                return@withContext Result.failure(Exception("HTTP ${conn.responseCode}"))
            }
            val body = conn.inputStream.bufferedReader().use { it.readText() }
            val catalog = json.decodeFromString<Catalog>(body)
            Result.success(catalog)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
