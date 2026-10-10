package com.simoesctt.cornstore.ui

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

object ApkInstaller {
    suspend fun download(context: Context, url: String, fileName: String): File? =
        withContext(Dispatchers.IO) {
            try {
                val dir = File(context.cacheDir, "apks").apply { mkdirs() }
                val out = File(dir, fileName)
                if (out.exists() && out.length() > 0) return@withContext out

                val conn = (URL(url).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 15_000
                    readTimeout = 60_000
                    setRequestProperty("User-Agent", "CornStore-Android/1.0")
                }
                conn.connect()
                if (conn.responseCode != 200) return@withContext null
                conn.inputStream.use { input ->
                    out.outputStream().use { output -> input.copyTo(output) }
                }
                out
            } catch (e: Exception) {
                null
            }
        }

    fun install(context: Context, apk: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            apk,
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
