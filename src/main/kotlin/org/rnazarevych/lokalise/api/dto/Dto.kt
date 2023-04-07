package org.rnazarevych.lokalise.api.dto

import com.google.gson.annotations.SerializedName

/**
 * https://developers.lokalise.com/reference/upload-a-file
 */
data class UploadFileDto(
        /**
         * Base64 encoded file. Must be one of the supported file types.
         */
        val data: String,
        val filename: String,
        @SerializedName("lang_iso")
        val langIso: String,
        /**
         * Enable to automatically convert placeholders to the Lokalise universal placeholders.
         */
        @SerializedName("convert_placeholders")
        val convertPlaceholders: Boolean
)

