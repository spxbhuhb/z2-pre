package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.RuiPluginContext
import hu.simplexion.z2.kotlin.ir.rui.air2ir.AirClass2Ir
import hu.simplexion.z2.kotlin.ir.rui.rum.RumClass
import org.jetbrains.kotlin.ir.declarations.*

class AirClass(

    val rumClass: RumClass,

    val irClass: IrClass,

    val adapter: IrProperty,
    val scope: IrProperty,
    val externalPatch: IrProperty,
    val fragment: IrProperty,

    val constructor: IrConstructor,
    val initializer: IrAnonymousInitializer,

    val patch: IrSimpleFunction
) : AirElement {

    override val rumElement
        get() = rumClass

    lateinit var stateVariableMap: Map<String, AirStateVariable>
    lateinit var stateVariableList: List<AirStateVariable>

    lateinit var dirtyMasks: List<AirDirtyMask>

    lateinit var rendering: AirBuilder

    val functions = mutableListOf<AirFunction>()

    fun toIr(context: RuiPluginContext): IrClass = AirClass2Ir(context, this).toIr()

}