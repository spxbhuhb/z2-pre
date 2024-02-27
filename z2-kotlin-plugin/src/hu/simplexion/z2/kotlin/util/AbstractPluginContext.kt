package hu.simplexion.z2.kotlin.util

import hu.simplexion.z2.kotlin.services.ir.*
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

abstract class AbstractPluginContext {

    abstract val irContext: IrPluginContext

    val stringType by lazy { irContext.irBuiltIns.stringType }

    val listClass by lazy { LIST.runtimeClass(KOTLIN_COLLECTIONS) }
    val uuidClass by lazy { UUID.runtimeClass(UTIL_PACKAGE) }

    fun String.runtimeClass(pkg: String = RUNTIME_PACKAGE) =
        checkNotNull(irContext.referenceClass(ClassId(FqName(pkg), Name.identifier(this)))) {
            "Missing ${pkg}.$this class. Maybe the gradle dependency on \"hu.simplexion.z2:z2-core\" is missing."
        }

    @Suppress("UNUSED_PARAMETER")
    fun debug(label : String, message : () -> Any?) {
//        val paddedLabel = "[$label]".padEnd(30)
//        Files.write(Paths.get("/Users/tiz/Desktop/plugin.txt"), "$paddedLabel  ${message()}\n".encodeToByteArray(), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
    }
}