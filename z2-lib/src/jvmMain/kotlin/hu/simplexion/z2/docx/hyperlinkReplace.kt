package hu.simplexion.z2.docx

import jakarta.xml.bind.JAXBElement
import org.docx4j.jaxb.Context
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.wml.ContentAccessor
import org.docx4j.wml.ObjectFactory
import org.docx4j.wml.P.Hyperlink
import org.docx4j.wml.R
import org.docx4j.wml.RPr
import java.io.File
import java.io.InputStream
import java.io.OutputStream

fun hyperlinkReplace(inFile: File, outStream: OutputStream, mapping: Map<String, Any?>) {
    inFile.inputStream().use {
        HyperlinkReplace(mapping).run(it, outStream)
    }
}

fun hyperlinkReplace(inFile: File, outFile: File, mapping: Map<String, Any?>) {
    inFile.inputStream().use { inStream ->
        outFile.outputStream().use { outStream ->
            HyperlinkReplace(mapping).run(inStream, outStream)
        }
    }
}

class HyperlinkReplace(
    val mapping: Map<String, Any?>
) {
    val factory: ObjectFactory = Context.getWmlObjectFactory()

    fun run(input: InputStream, output: OutputStream) {
        val template = WordprocessingMLPackage.load(input)

        template.mainDocumentPart.getJAXBNodesViaXPath("//*[w:hyperlink]", false).forEach {
            if (it is ContentAccessor) {
                for (index in it.content.indices) {
                    val child = it.content[index]
                    if (child !is JAXBElement<*>) continue

                    val hyperlink = child.value
                    if (hyperlink !is Hyperlink) continue

                    val relationship = template.mainDocumentPart.relationshipsPart.getRelationshipByID(hyperlink.id)
                    val key = relationship.target
                        .removePrefix("http://")
                        .removePrefix("https://")
                        .removeSuffix("/")

                    val replacement = mapping[key] ?: continue

                    val t = factory.createText().apply { value = replacement.toString() }
                    val rPr = factory.createRPr().copyFrom(hyperlink)

                    val r = factory.createR()
                    r.content.add(rPr)
                    r.content.add(t)

                    it.content[index] = r
                }
            }
            // println(XmlUtils.marshaltoString(it))
        }

        template.save(output)
    }

    fun ContentAccessor.firstOrNull(filter: (content: Any) -> Boolean): Any? {
        for (child in this.content) {
            if (filter(child)) return child
            if (child is ContentAccessor) {
                child.firstOrNull(filter)?.let { return it }
            }
        }
        return null
    }

    fun RPr.copyFrom(hyperlink: Hyperlink): RPr {
        val originalR = hyperlink.firstOrNull { it is R } ?: return this
        if (originalR !is R) return this

        val original = originalR.rPr

        this.rFonts = original.rFonts
        this.b = original.b
        this.i = original.i

        return this
    }
}