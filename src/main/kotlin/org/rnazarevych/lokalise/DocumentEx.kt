package org.rnazarevych.lokalise

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

const val EMPTY_NODE_EXPRESSION = "//text()[normalize-space(.)='']"

fun Document.removeEmptyNodes() {
    val xp: XPath = XPathFactory.newInstance().newXPath()
    val nodes: NodeList = xp.evaluate(EMPTY_NODE_EXPRESSION, this, XPathConstants.NODESET) as NodeList
    for (i in 0 until nodes.length) {
        val node: Node = nodes.item(i)
        if (node.textContent.trim().isEmpty()) {
            node.parentNode.removeChild(node)
        }
    }
}
