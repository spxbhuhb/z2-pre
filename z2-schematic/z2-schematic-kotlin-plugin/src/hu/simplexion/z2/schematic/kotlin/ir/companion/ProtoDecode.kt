package hu.simplexion.z2.schematic.kotlin.ir.companion

import hu.simplexion.z2.schematic.kotlin.ir.DECODE_PROTO
import hu.simplexion.z2.schematic.kotlin.ir.DECODE_PROTO_MESSAGE_NAME
import hu.simplexion.z2.schematic.kotlin.ir.SchematicPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.defaultType

class ProtoDecode(
    pluginContext: SchematicPluginContext,
    val companionTransform: CompanionTransform,
) : AbstractCompanionFun(
    pluginContext,
    companionTransform,
    DECODE_PROTO,
    pluginContext.schematicCompanionDecodeProto
) {

    override fun IrSimpleFunction.addParameters() {
        addValueParameter(DECODE_PROTO_MESSAGE_NAME, pluginContext.protoMessageType.makeNullable())
    }

    override fun IrSimpleFunction.buildBody() {

        body = DeclarationIrBuilder(irContext, this.symbol).irBlockBody {

            val instance = IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                transformedClass.defaultType,
                transformedClass.constructors.first { it.isPrimary }.symbol,
                0, 0, 0
            )

            val schema = irCall(
                companionTransform.companionSchematicSchemaGetter,
                dispatchReceiver = irGetObject(companionClass.symbol)
            )

            + irReturn(
                irCall(
                    pluginContext.schemaDecodeProto,
                    dispatchReceiver = schema,
                    args = arrayOf(instance, irGet(valueParameters[0]))
                )
            )
        }
    }
}