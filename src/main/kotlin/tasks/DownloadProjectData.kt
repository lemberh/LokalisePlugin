package tasks

import UpdateStrings
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import taskGroup
import java.io.File

open class DownloadProjectData : DefaultTask() {

    var resPath: String = ""
    var langs: List<String> = listOf("en")
    var defaultLang: String = "en"

    init {
        group = taskGroup
    }

    private val reader = UpdateStrings()

    @TaskAction
    fun download() {
        println("Downloading $langs ...")

        val languages = langs.joinToString("','", "['", "']")
        val response = api.Api.api.fetchTranslations(LokalisePlugin.config.token, LokalisePlugin.config.projectId, languages).execute()

        println("Downloaded ")

        langs.map { lang ->
            val suffix = if (lang == defaultLang) "" else "-$lang"

            reader.update(response.body()!!, lang, File("$resPath/values$suffix/strings.xml"))
        }
    }

    //todo  configure download and upload tasks
    //todo set which languages to download
    //todo fetch project languages
}