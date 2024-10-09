package org.rnazarevych.lokalise.api.dto

import com.google.gson.annotations.SerializedName

data class TranslationsResponse(
    @SerializedName("project_id") val projectId: String,
    val keys: List<TranslationKey> = emptyList()
)

data class TranslationKey(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_at_timestamp")
    val createdAtTimestamp: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("key_id")
    val keyId: Long,
    @SerializedName("key_name")
    val keyName: KeyName,
    @SerializedName("platforms")
    val platforms: List<String>,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("is_plural")
    val isPlural: Boolean,
    @SerializedName("translations")
    val translations: List<Translation>
)

data class KeyName(
    @SerializedName("android")
    val android: String,
    @SerializedName("ios")
    val ios: String,
    @SerializedName("other")
    val other: String,
    @SerializedName("web")
    val web: String
)

data class Translation(
    @SerializedName("is_reviewed")
    val isReviewed: Boolean,
    @SerializedName("key_id")
    val keyId: Long,
    @SerializedName("language_iso")
    val languageIso: String,
    @SerializedName("modified_at")
    val modifiedAt: String,
    @SerializedName("modified_at_timestamp")
    val modifiedAtTimestamp: Int,
    @SerializedName("modified_by")
    val modifiedBy: Int,
    @SerializedName("modified_by_email")
    val modifiedByEmail: String,
    @SerializedName("reviewed_by")
    val reviewedBy: Int,
    @SerializedName("translation")
    val translation: String,
    @SerializedName("translation_id")
    val translationId: Long,
    @SerializedName("words")
    val words: Int
)
