package com.simoesctt.cornstore.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppEntry(
    @SerialName("package") val packageName: String = "",
    val versionCode: String = "",
    val versionName: String = "",
    val minSdk: String = "",
    val targetSdk: String = "",
    val label: String = "",
    val repo: String = "",
    val description: String = "",
    val license: String = "",
    val stars: Int = 0,
    val homepage: String = "",
    val topics: List<String> = emptyList(),
    val tag: String = "",
    @SerialName("release_url") val releaseUrl: String = "",
    @SerialName("apk_url") val apkUrl: String = "",
    @SerialName("apk_name") val apkName: String = "",
    @SerialName("apk_size") val apkSize: Long = 0,
    @SerialName("published_at") val publishedAt: String = "",
    @SerialName("signature_sha256") val signature: String = ""
)

@Serializable
data class Catalog(
    val version: Int = 1,
    val generated_at: String = "",
    val count: Int = 0,
    val apps: List<AppEntry> = emptyList()
)
