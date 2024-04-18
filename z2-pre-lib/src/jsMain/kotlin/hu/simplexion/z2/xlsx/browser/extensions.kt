/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.xlsx.browser

import hu.simplexion.z2.browser.immaterial.table.Table
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.xlsx.conf.XlsxConfiguration
import hu.simplexion.z2.xlsx.internal.ContentWriter
import hu.simplexion.z2.xlsx.internal.buildFileContent
import hu.simplexion.z2.xlsx.model.XlsxDocument
import hu.simplexion.z2.xlsx.save
import kotlinx.datetime.Clock
import org.khronos.webgl.Uint8Array
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

/**
 * xlsx content type for downloading
 */
private const val XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

/**
 * export and download Table data, with default Configuration
 */
fun <T> Table<T>.onExportXlsx() {
    val doc = toXlsxDocument("Data export", XlsxConfiguration())
    doc.save(exportXlsxFileName)
}

/**
 * Copy Table data to XlsxDocument, according to exportFiltered and exportHeaders Table properties
 *
 * Typed cell value is acquired by Column.exportFun method.
 *
 * Starts at the left-top corner (A1)
 */
fun <T> Table<T>.toXlsxDocument(title: String, config: XlsxConfiguration): XlsxDocument {

    val doc = XlsxDocument(config)
    val sheet = doc.newSheet(title)

    val decimalFormat = config.formats.newCustomNumberFormat("#,##0")

    var rowIndex = 1
    var columnIndex = 1

    for (column in columns) {
        if (!column.exportable) continue
        val value = column.exportHeader ?: ""
        val cell = sheet[columnIndex, rowIndex]
        cell.value = value
        columnIndex++
    }

    rowIndex++

    for (row in fullData) {
        columnIndex = 1
        for (column in columns) {
            if (!column.exportable) continue
            val value = column.exportFun?.let { it(row.data) }
            val cell = sheet[columnIndex, rowIndex]
            cell.value = value
            if (column.schemaField?.type == SchemaFieldType.Decimal) {
                cell.numberFormat = decimalFormat
            }
            columnIndex++
        }
        rowIndex++
    }

    return doc
}

/**
 * Filename, derived from exportFileName
 */
val <T> Table<T>.exportXlsxFileName: String
    get() = exportFileName.replace(".csv", ".xlsx")

/**
 * Browser helper method to download an XlsxDocument via javascript Blob object
 */
fun downloadXlsX(fileName: String, doc: XlsxDocument) {

    val blobParts = mutableListOf<Uint8Array>()

    doc.buildFileContent(ContentWriter(blobParts::add) {
        val blob = Blob(blobParts.toTypedArray(), BlobPropertyBag(XLSX_CONTENT_TYPE))
        downloadBlob(fileName, blob)
        console.log("${Clock.System.now()} $fileName download completed, size: ${blob.size}")
    })

}
