package org.rnazarevych.lokalise

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.rnazarevych.lokalise.tasks.DownloadProjectData
import org.rnazarevych.lokalise.tasks.UploadStrings


const val taskGroup = "lokalise"

class LokalisePlugin : Plugin<Project> {

    companion object {
        lateinit var config: Config
    }

    override fun apply(project: Project) {

        config = project.extensions.create("lokalise", Config::class.java)

        project.afterEvaluate {

            project.tasks.apply {
                create("uploadStrings", UploadStrings::class.java) {
                    it.apiConfig = config.api
//                    it.uploadEntries = config.stringsUploadConfig.uploadEntries
                    it.uploadEntries = config.uploadEntries
                }
                create("downloadTranslations", DownloadProjectData::class.java) {
                    it.apiConfig = config.api
                    it.config = config.translationsUpdateConfig
                }
            }
        }
    }
}

open class Config {
    val api = ApiConfig()
    fun api(action: Action<in ApiConfig>) = action.execute(api)

    val translationsUpdateConfig = TranslationsUpdateConfig()
    fun translationsUpdateConfig(action: Action<in TranslationsUpdateConfig>) = action.execute(translationsUpdateConfig)

    val stringsUploadConfig = StringsUploadConfig()
    fun stringsUploadConfig(action: Action<in StringsUploadConfig>) = action.execute(stringsUploadConfig)

    val uploadEntries: MutableList<UploadEntry> = mutableListOf()
    fun uploadEntry(action: Action<in UploadEntry>) {
        val newEntry = UploadEntry()
        action.execute(newEntry)
        uploadEntries.add(newEntry)
    }
}

data class ApiConfig(
    var projectId: String = "",
    var token: String = ""
)

data class TranslationsUpdateConfig(
    var resPath: String = "",
    var lokaliseLangs: List<String> = listOf("en"),
    var androidLangs: List<String> = listOf("en"),
    var defaultLang: String = "en"
)

open class StringsUploadConfig {
    val api = ApiConfig()
    fun api(action: Action<in ApiConfig>) = action.execute(api)
    var str: MutableList<String> = mutableListOf()
    val uploadEntries: MutableList<UploadEntry> = mutableListOf()
    fun uploadEntry(action: Action<in UploadEntry>) {
        val newEntry = UploadEntry()
        action.execute(newEntry)
        uploadEntries.add(newEntry)
    }
}

open class UploadEntry(
    var path: String = "",
    var lang: String = ""
)