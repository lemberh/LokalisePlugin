package org.rnazarevych.lokalise.api.dto

import com.google.gson.annotations.SerializedName

data class UploadFileDto(
        /**
         * Base64 encoded file. Must be one of the supported file types.
         */
        val data: String,
        val filename: String,
        @SerializedName("lang_iso")
        val langIso: String
)

