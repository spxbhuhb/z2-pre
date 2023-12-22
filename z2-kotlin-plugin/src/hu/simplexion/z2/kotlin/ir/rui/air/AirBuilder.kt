package hu.simplexion.z2.kotlin.ir.rui.air

interface AirBuilder : AirFunction {

    val externalPatch: AirFunction
    val subBuilders: List<AirBuilder>

}