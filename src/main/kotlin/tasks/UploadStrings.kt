package tasks

import api.Api
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import taskGroup
import java.io.File

open class UploadStrings : DefaultTask() {

    var files: List<UploadEntry> = emptyList()

    init {
        group = taskGroup
    }

    //todo configure what to upload

    @TaskAction
    fun upload() {
        files.forEach { entry ->
            println("Uploading...")
            println(entry)

            val file = File(entry.path)
            val requestFile = RequestBody.create(MediaType.parse("application/xml"), file)
            val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val apiToken = RequestBody.create(MediaType.parse("text/plain"), LokalisePlugin.config.token)
            val id = RequestBody.create(MediaType.parse("text/plain"), LokalisePlugin.config.projectId)
            val language = RequestBody.create(MediaType.parse("text/plain"), entry.lang)


            val response = Api.api.importFile(apiToken, id, filePart, language).execute()

            println(response.body()?.string())
        }
    }
}

data class UploadEntry(
        val path: String,
        val lang: String
)