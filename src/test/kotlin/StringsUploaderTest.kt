import org.junit.Test
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Roman Nazarevych on 7/2/18.
 * Copyright OiTchau 2018
 */
class StringsUploaderTest {

    @Test
    fun apply() {
//        val project = ProjectBuilder.builder().build()
//        val task = project.task("greeting", tasks.UploadStrings())
//        assertTrue(task is tasks.UploadStrings)
    }

    private val form = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    @Test
    fun test(){
        println(LocalDateTime.parse("2020-08-12 16:55:47 (Etc/UTC)".substring(0,19),form))
    }
}