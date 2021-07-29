package org.rnazarevych.lokalise.tasks

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.rnazarevych.lokalise.ApiConfig
import org.rnazarevych.lokalise.UploadEntry
import org.rnazarevych.lokalise.api.Api
import org.rnazarevych.lokalise.api.dto.UploadFileDto
import org.rnazarevych.lokalise.taskGroup
import java.io.File
import java.nio.charset.StandardCharsets

open class UploadStrings : DefaultTask() {

    @Input
    var apiConfig: ApiConfig = ApiConfig("", "")
    @Input
    var uploadEntries: List<UploadEntry> = emptyList()

    init {
        group = taskGroup
    }

    //todo configure what to upload

    @TaskAction
    fun upload() {
        uploadEntries.forEach { entry ->
            println("Uploading...")
            println(entry)

            val file = File(entry.path)

            val encoded =
                Base64.encodeBase64(FileUtils.readFileToByteArray(file))
            val data = String(encoded, StandardCharsets.US_ASCII)

            val dto = UploadFileDto(
                data,
                file.name,
                entry.lang
            )

            val response = Api.api.uploadFile(apiConfig.projectId,dto).execute()

            if (!response.isSuccessful) {
                throw RuntimeException("${response.message()} - ${response.code()}")
            }else{
                println(response.body()?.string())
            }
        }
    }
}