package hu.simplexion.z2.kotlin.ir.localization

import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.name.Name

fun transformNamespace(property: IrProperty, pluginContext: LocalizationPluginContext): Boolean {
    return when (property.name.identifier) {

        LocalizationPluginContext.SCHEMATIC_FQ_NAME,
        LocalizationPluginContext.LOCALIZATION_NAMESPACE -> {
            property.accept(LocalizationNamespacePropertyTransform(pluginContext), null)
            return true
        }

        else -> false
    }
}

fun LocalizationPluginContext.register(declaration: IrDeclaration, name: Name, call: IrCall) {
    getCallArg(call, LocalizationPluginContext.STATIC_VALUE_ARG_INDEX).let {
        resources += "${declaration.parentAsClass.fqNameWhenAvailable}/${name.identifier}\t${it ?: ""}"
    }

    getCallArg(call, LocalizationPluginContext.STATIC_SUPPORT_ARG_INDEX)?.let {
        resources += "${declaration.parentAsClass.fqNameWhenAvailable}/${name.identifier}/support\t${it}"
    }
}

fun getCallArg(call: IrCall, argIndex: Int): String? {
    val arg = call.getValueArgument(argIndex)
    return if (arg != null && arg is IrConst<*>) {
        arg.value.toString()
    } else {
        null
    }
}

fun setLocalizedNamespace() {

}