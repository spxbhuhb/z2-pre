package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.RuiPluginContext
import hu.simplexion.z2.kotlin.ir.rui.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.ir.rui.air2ir.AirClass2Ir
import hu.simplexion.z2.kotlin.ir.rui.rum.RumClass
import org.jetbrains.kotlin.ir.declarations.*

class AirClass(

    val rumClass: RumClass,
    val airScope : AirClass?,

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

    val isAnonymous
        get() = (airScope != null)

    val startScope : AirClass
        get() {
            var scope : AirClass = this
            while (scope.airScope != null) {
                scope = scope.airScope!!
            }
            return scope
        }

    fun toIr(context: RuiPluginContext): IrClass = AirClass2Ir(context, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitClass(this, data)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) {
        stateVariableList.forEach { it.accept(visitor, data) }
        dirtyMasks.forEach { it.accept(visitor, data) }
        functions.forEach { it.accept(visitor, data) }
    }

    fun findStateVariable(name : String) : Pair<AirStateVariable, List<AirClass>>? {
        var scope : AirClass? = this
        val path = mutableListOf<AirClass>()
        while (scope != null) {
            path += scope
            scope.stateVariableMap[name]?.let {
                return (it to path)
            }
            scope = this.airScope
        }
        return null
    }
}