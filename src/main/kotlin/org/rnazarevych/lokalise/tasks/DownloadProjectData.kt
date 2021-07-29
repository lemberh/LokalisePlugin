package org.rnazarevych.lokalise.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.rnazarevych.lokalise.ApiConfig
import org.rnazarevych.lokalise.TranslationsUpdateConfig
import org.rnazarevych.lokalise.api.Api
import org.rnazarevych.lokalise.taskGroup
import java.io.File
import java.lang.RuntimeException

open class DownloadProjectData : DefaultTask() {

    @Input
    var apiConfig: ApiConfig = ApiConfig("", "")
    @Input
    var config: TranslationsUpdateConfig = TranslationsUpdateConfig()

    init {
        group = taskGroup
    }

    private val translationWriter = TranslationWriter()

    @TaskAction
    fun download() {
        println("Downloading ${config.lokaliseLangs} ...")

        val response = Api.api.fetchTranslations(apiConfig.projectId).execute()

        if (!response.isSuccessful){
            throw RuntimeException("${response.message()} - ${response.code()}")
        }

        println("Downloaded ")

        config.lokaliseLangs.forEachIndexed { index, lang ->
            val androidLang = config.androidLangs[index]
            val suffix = if (androidLang == config.defaultLang) "" else "-$androidLang"

            val localTranslationFile = File("${config.resPath}/values$suffix/strings.xml")
            translationWriter.update(response.body()!!, lang, localTranslationFile)
        }
    }

    //todo  configure download and upload tasks
    //todo set which languages to download
    //todo fetch project languages
}