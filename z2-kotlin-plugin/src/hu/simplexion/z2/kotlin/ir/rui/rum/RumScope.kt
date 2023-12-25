package hu.simplexion.z2.kotlin.ir.rui.rum

interface RumScope {
    val parentScope : RumScope?
    val stateVariables : MutableList<RumStateVariable>
}