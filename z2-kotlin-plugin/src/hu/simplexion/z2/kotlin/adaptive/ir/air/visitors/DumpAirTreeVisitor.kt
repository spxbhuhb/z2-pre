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
                println { "STATE_VARIABLE name:${irProperty.name}" }
            }
            super.visitStateVariable(stateVariable)
        }
    }

    override fun visitDirtyMask(dirtyMask: AirDirtyMask) {
        indented {
            with(dirtyMask) {
                println { "DIRTY_MASK name:${irProperty.name}" }
            }
            super.visitDirtyMask(dirtyMask)
        }
    }

    override fun visitBuilderSequence(builder: AirBuilderSequence) {
        indented {
            with(builder) {
                println { "BUILDER type:SEQUENCE name:${irFunction.name} externalPatch:${externalPatch.irFunction.name}" }
            }
            super.visitBuilderSequence(builder)
        }
    }

    override fun visitBuilderCall(builder: AirBuilderCall) {
        indented {
            with(builder) {
                println { "BUILDER type:CALL name:${irFunction.name} externalPatch:${externalPatch.irFunction.name} target:${armElement.target}" }
            }
            super.visitBuilderCall(builder)
        }
    }

    override fun visitBuilderForLoop(builder: AirBuilderForLoop) {
        indented {
            with(builder) {
                println { "BUILDER type:FOR_LOOP name:${irFunction.name} externalPatch:${externalPatch.irFunction.name}" }
            }
            super.visitBuilderForLoop(builder)
        }
    }

    override fun visitBuilderWhen(builder: AirBuilderWhen) {
        indented {
            with(builder) {
                println { "BUILDER type:WHEN name:${irFunction.name} externalPatch:${externalPatch.irFunction.name}" }
            }
            super.visitBuilderWhen(builder)
        }
    }

    override fun visitExternalPatchSequence(externalPatch: AirExternalPatchSequence) {
        indented {
            with(externalPatch) {
                println { "EXTERNAL_PATCH type:SEQUENCE name:${irFunction.name}" }
            }
            super.visitExternalPatchSequence(externalPatch)
        }
    }

    override fun visitExternalPatchCall(externalPatch: AirExternalPatchCall) {
        indented {
            with(externalPatch) {
                println { "EXTERNAL_PATCH type:CALL name:${irFunction.name} target:${externalPatch.armElement.target}" }
            }
            super.visitExternalPatchCall(externalPatch)
        }
    }

    override fun visitExternalPatchForLoop(externalPatch: AirExternalPatchForLoop) {
        indented {
            with(externalPatch) {
                println { "EXTERNAL_PATCH type:FOR_LOOP name:${irFunction.name}" }
            }
            super.visitExternalPatchForLoop(externalPatch)
        }
    }

    override fun visitExternalPatchWhen(externalPatch: AirExternalPatchWhen) {
        indented {
            with(externalPatch) {
                println { "EXTERNAL_PATCH type:WHEN name:${irFunction.name}" }
            }
            super.visitExternalPatchWhen(externalPatch)
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