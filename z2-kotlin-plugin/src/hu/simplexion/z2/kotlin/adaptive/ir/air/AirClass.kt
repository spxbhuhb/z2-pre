package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.AirClass2Ir
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.name.FqName

class AirClass(

    val armClass: ArmClass,
    val scopeClassName : FqName?,

    val irClass: IrClass,

    val adapter: IrProperty,
    val closure: IrProperty,
    val parent: IrProperty,
    val externalPatch: IrProperty,
    val fragment: IrProperty,

    val constructor: IrConstructor,
    val initializer: IrAnonymousInitializer,

    val patch: IrSimpleFunction

) : AirElement {

    override val armElement
        get() = armClass

    lateinit var stateVariableMap: Map<String, AirStateVariable>
    lateinit var stateVariableList: List<AirStateVariable>

    lateinit var dirtyMasks: List<AirDirtyMask>

    lateinit var rendering: AirBuilder

    val functions = mutableListOf<AirFunction>()

    fun toIr(context: AdaptivePluginContext): IrClass = AirClass2Ir(context, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitClass(this, data)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) {
        stateVariableList.forEach { it.accept(visitor, data) }
        dirtyMasks.forEach { it.accept(visitor, data) }
        functions.forEach { it.accept(visitor, data) }
    }

    fun findStateVariable(context : AdaptivePluginContext, name : String) : Pair<AirStateVariable, List<AirClass>>? {
        var scope : AirClass? = this
        val path = mutableListOf<AirClass>()
        while (scope != null) {
            path += scope
            scope.stateVariableMap[name]?.let {
                return (it to path)
            }
            scope = this.scopeClassName?.let { context.airClasses[it] }
        }
        return null
    }
}