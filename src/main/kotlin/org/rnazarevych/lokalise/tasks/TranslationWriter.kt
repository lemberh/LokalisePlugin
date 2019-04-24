package org.rnazarevych.lokalise.tasks

import org.rnazarevych.lokalise.api.dto.TranslationsResponse
import org.w3c.dom.Element
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

internal class TranslationWriter {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun update(translations: TranslationsResponse, lang: String, file: File) {
        println("Opening File ${file.absolutePath}")

        val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

        xmlDoc.documentElement.normalize()

        println("Root Node:" + xmlDoc.documentElement.nodeName)

        val strings = xmlDoc.getElementsByTagName("string")
        // cut off plurals as they are not yet supported
        val translation = (translations.translations[lang] ?: listOf())
                .filter { it.pluralKey == "0" && it.translation.isNotBlank() }
                .toMutableList()

        println("Updating existing entries")
        for (i in 0 until strings.length) {
            val string = strings.item(i) as Element
            val id = string.attributes.getNamedItem("name").nodeValue


            val entry = translation.find { it.key == id }
            if (entry != null) {
                val localLmd = try {
                    LocalDateTime.parse(string.getAttribute("lmt"), formatter)
                } catch (ignored: Exception) {
                    LocalDateTime.of(1970, 1, 1, 1, 1, 1)
                }
                val remoteLmd = LocalDateTime.parse(entry.modifiedAt, formatter)

                // if there is newer text on the backend
                if (remoteLmd.isAfter(localLmd)) {
                    string.textContent = escapeXml(entry.translation)
                    string.setAttribute("lmd", entry.modifiedAt)
//                    println("$id -> ${entry.translation}")
                }

                translation.remove(entry)
            }
        }

        println("Writing ${translation.size} new entries")
        if (translation.isNotEmpty()) {
            xmlDoc.documentElement.appendChild(xmlDoc.createComment("New translations added at ${LocalDateTime.now()}"))
            translation.forEach { entry ->
                val newNode = xmlDoc.documentElement.appendChild(xmlDoc.createElement("string")) as Element
                newNode.setAttribute("name", entry.key)
                newNode.setAttribute("lmd", entry.modifiedAt)
                newNode.textContent = escapeXml(entry.translation)
            }
        }

        println("Writing to file")

        val transformer = TransformerFactory.newInstance().newTransformer().apply {
            setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
            setOutputProperty(OutputKeys.ENCODING, "UTF-8")
            setOutputProperty(OutputKeys.INDENT, "yes")
        }
        val output = StreamResult(file)
        val input = DOMSource(xmlDoc)

        transformer.transform(input, output)
    }

    private fun escapeXml(source: String) = source.replace("\'", "\\'")
}