package org.rnazarevych.lokalise.tasks

import org.rnazarevych.lokalise.api.dto.TranslationsResponse
import org.rnazarevych.lokalise.removeEmptyNodes
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

    fun update(translationKeys: TranslationsResponse, lang: String, file: File) {
        println("Opening File ${file.absolutePath}")

        val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

        xmlDoc.documentElement.normalize()

        println("Root Node:" + xmlDoc.documentElement.nodeName)
//        println("PARSED TRANSLATIONS\n $translationKeys")

        val strings = xmlDoc.getElementsByTagName("string")
        // filter only keys that contain translation for the given language
        // and also exclude plurals, as we dont support them for now
        val languageKeys = translationKeys.keys
            .filter { key ->
                val containsTranslationForTheLanguage = key.translations.any { t ->
                    t.languageIso == lang && t.translation.isNotBlank()
                }
                !key.isPlural && containsTranslationForTheLanguage
            }
            .toMutableList()

        println("Updating existing entries")
        var updatedCount = 0
        for (i in 0 until strings.length) {
            val string = strings.item(i) as Element
            val stringId = string.attributes.getNamedItem("name").nodeValue


            val translationKey = languageKeys.find { it.keyName.android == stringId }
            val translation = translationKey?.translations?.find { it.languageIso == lang }
            if (translation != null) {
                val localLmd = try {
                    LocalDateTime.parse(string.getAttribute("lmt"), formatter)
                } catch (ignored: Exception) {
                    LocalDateTime.of(1970, 1, 1, 1, 1, 1)
                }
                val remoteLmd = LocalDateTime.parse(trimTimeZone(translation.modifiedAt), formatter)

                // if there is newer text on the backend
                if (remoteLmd.isAfter(localLmd)) {
                    string.textContent = escapeXml(translation.translation)
                    string.setAttribute("lmd", remoteLmd.format(formatter))
                    updatedCount++
//                    println("$id -> ${entry.translation}")
                }

                languageKeys.remove(translationKey)
            }
        }

        println("Updated $updatedCount entries")
        println("Writing ${languageKeys.size} new entries")
        if (languageKeys.isNotEmpty()) {
            xmlDoc.documentElement.appendChild(xmlDoc.createComment("New translations added at ${LocalDateTime.now()}"))
            languageKeys.forEach { key ->
                key.translations.find { it.languageIso == lang }?.let { translation ->
                    val newNode = xmlDoc.documentElement.appendChild(xmlDoc.createElement("string")) as Element
                    newNode.setAttribute("name", key.keyName.android)
                    newNode.setAttribute("lmd", trimTimeZone(translation.modifiedAt))
                    newNode.textContent = escapeXml(translation.translation)
                }
            }
        }

        xmlDoc.removeEmptyNodes()

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

    private fun trimTimeZone(date: String) = date.substring(0, 19)
}
