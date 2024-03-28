package hu.simplexion.z2.kotlin.adaptive.ir.air2ir

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirClass
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.StateAccessTransform.Companion.transformStateAccess
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.functions

class AirClass2Ir(
    context: AdaptivePluginContext,
    airClass: AirClass
) : ClassBoundIrBuilder(context, airClass) {

    val renderingSymbolMap = airClass.rendering.armElement.symbolMap(this)

    fun toIr(): IrClass {
        airClass.initializer.toIr()

        buildBody()
        patchBody()
        invokeBody()

        return irClass
    }

    fun IrAnonymousInitializer.toIr() {
        body = DeclarationIrBuilder(irContext, irClass.symbol).irBlockBody {

            if (pluginContext.withTrace) {
                + irTrace("init", emptyList())
            }

            airClass.armElement.initializerStatements.forEach {
                + transformStateAccess(this.parent, it, airClass.irClass.thisReceiver !!.symbol)
            }

            val builderCall = irCallOp(
                airClass.rendering.irFunction.symbol,
                type = classBoundFragmentType,
                dispatchReceiver = irThisReceiver(),
                argument = irThisReceiver()
            )

            + airClass.fragment.irSetField(builderCall, irThisReceiver())
        }

        // The initializer has to be the last, so it will be able to access all properties
        irClass.declarations += this
    }

    fun patchBody() {
        val patch = airClass.patch

        patch.body = DeclarationIrBuilder(irContext, patch.symbol).irBlockBody {

            if (pluginContext.withTrace) {
                + irTrace(patch, "patch", emptyList())
            }

            val fragment = irTemporary(irGetFragment(patch))

            irCallExternalPatch(fragment)
            irCallPatch(fragment)

            airClass.dirtyMasks.forEach { + it.irClear(patch.dispatchReceiverParameter !!) }
        }

    }

    /**
     * Call the external patch of `adaptiveFragment`. This is somewhat complex because the function
     * is stored in a variable.
     *
     * ```kotlin
     * adaptiveFragment.adaptiveExternalPatch(adaptiveFragment)
     * ```
     */
    fun IrBlockBodyBuilder.irCallExternalPatch(fragment: IrVariable) {

        val functionType = irBuiltIns.functionN(1)
        val invoke = functionType.functions.first { it.name.identifier == "invoke" }.symbol

        + irCall(
            invoke,
            irBuiltIns.unitType,
            valueArgumentsCount = 1,
            typeArgumentsCount = 0,
            origin = IrStatementOrigin.INVOKE
        ).apply {
            dispatchReceiver = irCallOp(
                renderingSymbolMap.externalPatchGetter.symbol,
                functionType.defaultType,
                irGet(fragment)
            )
            putValueArgument(0, irGet(fragment))
        }

    }


    /**
     * Call the patch of `adaptiveFragment`.
     *
     * ```kotlin
     * adaptiveFragment.adaptivePatch()
     * ```
     */
    fun IrBlockBodyBuilder.irCallPatch(fragment: IrVariable) {
        + irCall(
            renderingSymbolMap.patch.symbol,
            irBuiltIns.unitType,
            valueArgumentsCount = 0,
            typeArgumentsCount = 0,
            origin = IrStatementOrigin.INVOKE
        ).apply {
            dispatchReceiver = irGet(fragment)
        }
    }

    fun irClearDirtyMask(receiver: IrValueParameter): IrStatement =
        irSetValue(
            airClass.dirtyMask,
            irConst(0L),
            receiver = irGet(receiver)
        )

}

