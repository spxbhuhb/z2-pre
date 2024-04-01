/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.xlsx.internal.model

internal interface Part {
    val partName: String
    val contentType: String
    val relType: String
}