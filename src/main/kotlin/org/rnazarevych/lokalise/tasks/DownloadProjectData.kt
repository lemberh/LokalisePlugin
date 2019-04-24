package org.rnazarevych.lokalise.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.rnazarevych.lokalise.ApiConfig
import org.rnazarevych.lokalise.TranslationsUpdateConfig
import org.rnazarevych.lokalise.api.Api
import org.rnazarevych.lokalise.taskGroup
import java.io.File

open class DownloadProjectData : DefaultTask() {

    var apiConfig: ApiConfig = ApiConfig("", "")

    var config: TranslationsUpdateConfig = TranslationsUpdateConfig()

    init {
        group = taskGroup
    }

    private val translationWriter = TranslationWriter()

    @TaskAction
    fun download() {
        println("Downloading ${config.langs} ...")

        val languages = config.langs.joinToString("','", "['", "']")
        val response = Api.api.fetchTranslations(apiConfig.token, apiConfig.projectId, languages).execute()

        println("Downloaded ")

        config.langs.map { lang ->
            val suffix = if (lang == config.defaultLang) "" else "-$lang"

            translationWriter.update(response.body()!!, lang, File("${config.resPath}/values$suffix/strings.xml"))
        }
    }

    //todo  configure download and upload tasks
    //todo set which languages to download
    //todo fetch project languages
}