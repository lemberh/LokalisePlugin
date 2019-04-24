package org.rnazarevych.lokalise.api.converters

import org.rnazarevych.lokalise.api.dto.TranslationEntry
import org.rnazarevych.lokalise.api.dto.TranslationsResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.RuntimeException
import java.lang.reflect.Type

class TranslationsResponseConverter : JsonDeserializer<TranslationsResponse> {

    private val gson = GsonBuilder().create()

    override fun deserialize(json: JsonElement?,
                             typeOfT: Type?,
                             context: JsonDeserializationContext?
    ): TranslationsResponse {
        if (json == null) {
            throw RuntimeException("Json object is null")
        }

        val translationsResponse = gson.fromJson(json, TranslationsResponse::class.java)

        val translations = json.asJsonObject?.get("strings")?.asJsonObject
        val listType = object : TypeToken<List<TranslationEntry>>() {}.type
        translations?.entrySet()?.forEach {
            translationsResponse.translations[it.key] = gson.fromJson(it.value, listType)
        }

        return translationsResponse
    }

}