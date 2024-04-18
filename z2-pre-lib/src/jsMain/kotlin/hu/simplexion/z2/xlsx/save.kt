/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.z2.xlsx

import hu.simplexion.z2.xlsx.browser.downloadXlsX
import hu.simplexion.z2.xlsx.model.XlsxDocument

/**
 * Download xlsx file.
 */
actual fun XlsxDocument.save(fileName: String) {
    downloadXlsX(fileName, this)
}
