/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.air.visitors

import hu.simplexion.z2.kotlin.adaptive.ir.air.*
import org.jetbrains.kotlin.utils.Printer

class DumpAirTreeVisitor(
    out: Appendable
) : AirElementVisitorVoid<Unit> {

    private val printer = Printer(out, "  ")

    override fun visitElement(element: AirElement) {
        element.acceptChildren(this, null)
    }

    override fun visitEntryPoint(airEntryPoint: AirEntryPoint) {
        indented {
            println { "ENTRY_POINT class:${airEntryPoint.airClass.irClass.name}" }
            super.visitEntryPoint(airEntryPoint)
        }
    }

    override fun visitClass(airClass: AirClass) {
        indented {
            with(airClass) {
                println { "CLASS name:${irClass.name}" }
            }
            super.visitClass(airClass)
        }
    }

    override fun visitStateVariable(stateVariable: AirStateVariable) {
        indented {
            with(stateVariable) {
                println { "STATE_VARIABLE indexInFragment: $indexInFragment indexInClosure: $indexInClosure name:$name" }
            }
            super.visitStateVariable(stateVariable)
        }
    }

    override fun visitBuildBranch(branch: AirBuildBranch) {
        indented {
            with(branch) {
                println { "BUILD_BRANCH index:$index" }
            }
            super.visitBuildBranch(branch)
        }
    }

    override fun visitPatchBranch(branch: AirPatchDescendantBranch) {
        indented {
            with(branch) {
                println { "PATCH_BRANCH index:$index" }
            }
            super.visitPatchBranch(branch)
        }
    }

    override fun visitInvokeBranch(branch: AirInvokeBranch) {
        indented {
            with(branch) {
                println { "INVOKE_BRANCH index:$index" }
            }
            super.visitInvokeBranch(branch)
        }
    }

    private inline fun println(body: () -> String) {
        printer.println(body())
    }

    private inline fun indented(body: () -> Unit) {
        printer.pushIndent()
        body()
        printer.popIndent()
    }
    
}