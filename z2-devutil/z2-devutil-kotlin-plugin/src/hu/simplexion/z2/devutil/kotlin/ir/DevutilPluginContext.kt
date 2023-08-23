/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.schematic.kotlin.ir

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class DevutilPluginContext(
    val irContext: IrPluginContext,
) {

    fun debug(label : String, message : () -> Any?) {
        val paddedLabel = "[$label]".padEnd(20)
        Files.write(Paths.get("/Users/tiz/Desktop/plugin.txt"), "$paddedLabel  ${message()}\n".encodeToByteArray(), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
    }
}