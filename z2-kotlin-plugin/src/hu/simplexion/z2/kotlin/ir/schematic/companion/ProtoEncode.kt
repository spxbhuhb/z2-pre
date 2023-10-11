package hu.simplexion.z2.kotlin.ir.schematic.companion

import hu.simplexion.z2.kotlin.ir.schematic.ENCODE_PROTO
import hu.simplexion.z2.kotlin.ir.schematic.ENCODE_PROTO_VALUE_NAME
import hu.simplexion.z2.kotlin.ir.schematic.SchematicPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.defaultType

class ProtoEncode(
    pluginContext: SchematicPluginContext,
    val companionTransform: CompanionTransform,
) : AbstractCompanionFun(
    pluginContext,
    companionTransform,
    ENCODE_PROTO,
    pluginContext.schematicCompanionEncodeProto
) {

    override val returnType: IrType
        get() = irBuiltIns.byteArray.defaultType

    override fun IrSimpleFunction.addParameters() {
        addValueParameter(ENCODE_PROTO_VALUE_NAME, transformedClass.defaultType)
    }

    override fun IrSimpleFunction.buildBody() {

        body = DeclarationIrBuilder(irContext, this.symbol).irBlockBody {

            val schema = irCall(
                companionTransform.companionSchematicSchemaGetter,
                dispatchReceiver = irGetObject(companionClass.symbol)
            )

            + irReturn(
                irCall(
                    pluginContext.schemaEncodeProto,
                    dispatchReceiver = schema,
                    args = arrayOf(irGet(valueParameters[0]))
                )
            )
        }
    }
}