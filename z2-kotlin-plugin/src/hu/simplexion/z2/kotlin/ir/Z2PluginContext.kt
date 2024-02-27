package hu.simplexion.z2.kotlin.ir

import hu.simplexion.z2.kotlin.schematic.RUNTIME_PACKAGE
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.util.IrMessageLogger
import org.jetbrains.kotlin.ir.util.fileEntry
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import java.nio.file.Path
import kotlin.io.path.appendText

interface Z2PluginContext {

    val irContext: IrPluginContext

    val messages : MutableList<PluginMessage>

    val pluginLogFile: Path?

    val printDumps : Boolean

    val moduleFragment : IrModuleFragment

    fun String.runtimeClass(pkg: String = RUNTIME_PACKAGE) =
        checkNotNull(irContext.referenceClass(ClassId(FqName(pkg), Name.identifier(this)))) {
            "Missing runtime class: $pkg.$this. Maybe the gradle dependency on \"hu.simplexion.z2:z2-core\" is missing."
        }

    fun classSymbol(name: FqName): IrClassSymbol =
        name.shortName().toString().runtimeClass(name.parent().asString())

    fun output(title: String, content: String, declaration: IrDeclaration? = null) {

        val longTitle = "\n\n====  $title  ================================================================\n"

        pluginLogFile?.appendText("$longTitle\n\n$content")

        if (printDumps) {
            println(longTitle)
            println(content)
        } else {
            report(
                IrMessageLogger.Severity.INFO,
                title,
                IrMessageLogger.Location(
                    declaration?.fileEntry?.name ?: moduleFragment.name.asString(),
                    declaration?.fileEntry?.getLineNumber(declaration.startOffset) ?: 1,
                    declaration?.fileEntry?.getColumnNumber(declaration.startOffset) ?: 1
                )
            )
        }
    }

    class PluginMessage(
        val severity: IrMessageLogger.Severity,
        val message : String,
        val location: IrMessageLogger.Location
    ) {
        override fun toString(): String {
            return("[$severity]  $location  $message")
        }
    }

    fun report(severity: IrMessageLogger.Severity, message: String, location: IrMessageLogger.Location) {
        messages += PluginMessage(severity, message, location).also { println(it) }
    }

}