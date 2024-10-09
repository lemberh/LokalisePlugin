package org.rnazarevych.lokalise.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.rnazarevych.lokalise.LokalisePlugin
import org.rnazarevych.lokalise.api.dto.TranslationsResponse
import org.rnazarevych.lokalise.api.dto.UploadFileDto
import org.rnazarevych.lokalise.api.interceptors.AuthInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface LokaliseApi {

    @POST("projects/{projectId}/files/upload")
    fun uploadFile(
        @Path("projectId") projectId: String,
        @Body uploadFileDto: UploadFileDto
    ): Call<ResponseBody>

    @Deprecated("this is old API which is not supported, to use it must be updated to new api v2")
    @FormUrlEncoded
    @POST("project/export")
    fun exportProjectData(
        @Field("api_token") apiToken: String,
        @Field("id") id: String,
        @Field("type") type: String = "xml",
        @Field("langs") langs: List<String>? = null
    ): Call<ResponseBody>

    @GET("projects/{projectId}/keys")
    fun fetchTranslations(
        @Path("projectId") projectId: String,
        @Query("include_translations") includeTranslations: Int = 1,
        @Query("filter_platforms") platform: String = "android",
        @Query("limit") limit: Int = 5000
    ): Call<TranslationsResponse>

}

object Api {

    private val gson = GsonBuilder()
        .create()

    private val loggingInterceptor: Interceptor = HttpLoggingInterceptor(
        HttpLoggingInterceptor.Logger { println(it) }
    ).setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(LokalisePlugin.config.api.token))
//            .addInterceptor(loggingInterceptor)
            .connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()
    }

    val api: LokaliseApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.lokalise.com/api2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(LokaliseApi::class.java)
    }
}
