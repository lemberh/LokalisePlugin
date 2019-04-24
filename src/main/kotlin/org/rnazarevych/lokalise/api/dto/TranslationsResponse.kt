package org.rnazarevych.lokalise.api.dto

import com.google.gson.annotations.SerializedName

data class TranslationsResponse(
    val translations: MutableMap<String, List<TranslationEntry>> = mutableMapOf(),
    @SerializedName("response") val response: Response = Response()
)

data class TranslationEntry(
        @SerializedName("key") val key: String = "",
        @SerializedName("key_id") val keyId: String = "",
        @SerializedName("trans_id") val transId: String = "",
        @SerializedName("translation") val translation: String = "",
        @SerializedName("description") val description: String = "",
        @SerializedName("lang_id") val langId: String = "",
        @SerializedName("plural_key") val pluralKey: String = "",
        @SerializedName("plural_name") val pluralName: String = "",
        @SerializedName("platform_mask") val platformMask: String = "",
        @SerializedName("is_hidden") val isHidden: String = "",
        @SerializedName("created_at") val createdAt: String = "",
        @SerializedName("tags") val tags: List<String> = listOf(),
        @SerializedName("modified_at") val modifiedAt: String = "",
        @SerializedName("fuzzy") val fuzzy: String = "",
        @SerializedName("is_proofread") val isProofread: String = "",
        @SerializedName("context") val context: String = "",
        @SerializedName("is_archived") val isArchived: String = "",
        @SerializedName("key_type") val keyType: String = ""
)

data class Response(
        @SerializedName("status") val status: String = "",
        @SerializedName("code") val code: String = "",
        @SerializedName("message") val message: String = ""
)
