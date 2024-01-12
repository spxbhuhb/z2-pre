package hu.simplexion.z2.rui

typealias RuiStateVariableMask = Int

const val CLEAR_STATE_MASK : RuiStateVariableMask = 0

fun RuiStateVariableMask.isClearOf(variables : RuiStateVariableMask) : Boolean = ((this and variables) == 0)
fun RuiStateVariableMask.isDirtyOf(variables : RuiStateVariableMask) : Boolean = ((this and variables) == 0)
fun RuiStateVariableMask.extend(localDirtyMask : RuiStateVariableMask, localDirtyMaskSize: Int) : RuiStateVariableMask = this or (localDirtyMask shl localDirtyMaskSize)