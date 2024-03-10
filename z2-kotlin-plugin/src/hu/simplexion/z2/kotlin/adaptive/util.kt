/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive

import org.jetbrains.kotlin.name.Name
import java.util.*

fun Name.isSynthetic() = identifier.startsWith('$') || identifier.endsWith('$')

fun String.capitalizeFirstChar() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
