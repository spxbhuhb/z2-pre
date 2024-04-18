/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.z2.xlsx

import hu.simplexion.z2.xlsx.internal.ContentWriter
import hu.simplexion.z2.xlsx.internal.buildFileContent
import hu.simplexion.z2.xlsx.model.XlsxDocument
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * Save xlsx file.
 */
actual fun XlsxDocument.save(fileName: String) {
    FileOutputStream(fileName).use(::writeTo)
}

/**
 * Generate XlsxFile and Write it to the OutputStream
 */
fun XlsxDocument.writeTo(os: OutputStream) {
    buildFileContent(ContentWriter<OutputStream>(os))
}
