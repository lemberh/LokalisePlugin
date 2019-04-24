package org.rnazarevych.lokalise.api.dto

import com.google.gson.annotations.SerializedName

data class ResourcesImportDto(
        @SerializedName("api_token")
        val apiToken: String,
        val id: String,
        @SerializedName("file")
        val filePath: String,
        val fileLang: String,
        val replace: Int,
        val icuPlurals: Int,
        val fillEmpty: Int,
        val distinguish: Int,
        val hidden: Int,
        val tags: List<String>
)

