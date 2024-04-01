package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.Names
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptivePluginContext
import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.air2ir.AirClass2Ir
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClass
import hu.simplexion.z2.kotlin.util.property
import org.jetbrains.kotlin.ir.declarations.*

class AirClass(

    val armClass: ArmClass,

    val irClass: IrClass,

    val constructor: IrConstructor,

    val initializer: IrAnonymousInitializer,

    val build: IrSimpleFunction,
    val patchDescendant: IrSimpleFunction,
    val invoke: IrSimpleFunction,
    val patchInternal: IrSimpleFunction

) : AirElement {

    lateinit var stateVariableMap: Map<String, AirStateVariable>
    lateinit var stateVariableList: List<AirStateVariable>

    val buildBranches = mutableListOf<AirBuildBranch>()
    val patchDescendantBranches = mutableListOf<AirPatchDescendantBranch>()
    val invokeBranches = mutableListOf<AirInvokeBranch>()

    val adapter: IrProperty = irClass.property(Names.ADAPTER)
    val parent: IrProperty = irClass.property(Names.PARENT)
    val index: IrProperty = irClass.property(Names.INDEX)
    val dirtyMask: IrProperty = irClass.property(Names.DIRTY_MASK)

    fun toIr(context: AdaptivePluginContext): IrClass = AirClass2Ir(context, this).toIr()

    override fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R =
        visitor.visitClass(this, data)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) {
        stateVariableList.forEach { it.accept(visitor, data) }
    }

//    fun findStateVariable(context: AdaptivePluginContext, name: String): Pair<AirStateVariable, List<AirClass>>? {
//        var scope: AirClass? = this
//        val path = mutableListOf<AirClass>()
//        while (scope != null) {
//            path += scope
//            scope.stateVariableMap[name]?.let {
//                return (it to path)
//            }
//            scope = this.scopeClassName?.let { context.airClasses[it] }
//        }
//        return null
//    }
}