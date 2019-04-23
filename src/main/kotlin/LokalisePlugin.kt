import org.gradle.api.Plugin
import org.gradle.api.Project
import tasks.DownloadProjectData
import tasks.UploadStrings


const val taskGroup = "lokalise"

class LokalisePlugin : Plugin<Project> {

    companion object {
        lateinit var config: Config
    }

    override fun apply(project: Project) {

        config = project.extensions.create("lokalise", Config::class.java)

// todo add some default tasks
//        with(project.tasks) {
//            create("uploadTask", UploadStrings::class.java)
//            create("downloadTask", DownloadProjectData::class.java)
//        }
    }
}

open class Config {
    lateinit var projectId: String
    lateinit var token: String
}