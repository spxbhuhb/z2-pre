/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors

import hu.simplexion.z2.kotlin.adaptive.ir.arm.*
import org.jetbrains.kotlin.utils.Printer

class DumpArmTreeVisitor(
    out: Appendable
) : ArmElementVisitorVoid<Unit> {

    private val printer = Printer(out, "  ")

    override fun visitElement(element: ArmElement) {
        element.acceptChildren(this, null)
    }

    override fun visitEntryPoint(armEntryPoint: ArmEntryPoint) {
        indented {
            println { "ENTRY_POINT class:${armEntryPoint.armClass.name}" }
            super.visitEntryPoint(armEntryPoint)
        }
    }

    override fun visitClass(armClass: ArmClass) {
        indented {
            with(armClass) {
                println { "CLASS name:$name boundary:(${boundary.startOffset},${boundary.statementIndex}) parentScope:${parentScope?.fqName}" }
            }
            super.visitClass(armClass)
        }
    }

    override fun visitExternalStateVariable(stateVariable: ArmExternalStateVariable) {
        indented {
            with(stateVariable) {
                println { "EXTERNAL_STATE_VARIABLE index:$index name:$name" }
            }
            super.visitStateVariable(stateVariable)
        }
    }

    override fun visitInternalStateVariable(stateVariable: ArmInternalStateVariable) {
        indented {
            with(stateVariable) {
                println { "INTERNAL_STATE_VARIABLE index:$index name:$name" }
            }
            super.visitStateVariable(stateVariable)
        }
    }

    override fun visitDirtyMask(dirtyMask: ArmDirtyMask) {
        indented {
            with(dirtyMask) {
                println { "DIRTY_MASK index:$index" }
            }
            super.visitDirtyMask(dirtyMask)
        }
    }

    override fun visitSequence(statement: ArmSequence) {
        indented {
            with(statement) {
                println { "RENDERING type:SEQUENCE index:$index startOffset: ${statement.irBlock.startOffset}" }
            }
            super.visitSequence(statement)
        }
    }

    override fun visitCall(statement: ArmCall) {
        indented {
            with(statement) {
                println { "RENDERING type:CALL index:$index startOffset: ${statement.irCall.startOffset} type:<$target>" }
            }
            super.visitCall(statement)
        }
    }

    override fun visitCallbackFunctionCall(statement: ArmSupportFunctionCall) {
        indented {
            with(statement) {
                println { "RENDERING type:CALLBACK_CALL index:$index startOffset: ${statement.irCall.startOffset} type:<$target>" }
            }
            super.visitCallbackFunctionCall(statement)
        }
    }

    override fun visitHigherOrderCall(statement: ArmHigherOrderCall) {
        indented {
            with(statement) {
                println { "RENDERING type:HIGHER_ORDER_CALL index:$index type:<$target>" }
            }
            super.visitHigherOrderCall(statement)
        }
    }


    override fun visitParameterFunctionCall(statement: ArmParameterFunctionCall) {
        indented {
            with(statement) {
                println { "RENDERING type:PARAMETER_FUNCTION_CALL index:$index" }
            }
            super.visitParameterFunctionCall(statement)
        }
    }

    override fun visitWhen(statement: ArmWhen) {
        indented {
            with(statement) {
                println { "RENDERING type:WHEN index:$index" }
            }
            super.visitWhen(statement)
        }
    }

    override fun visitForLoop(statement: ArmForLoop) {
        indented {
            with(statement) {
                println { "RENDERING type:FOR_LOOP index:$index" }
            }
            super.visitForLoop(statement)
        }
    }

    override fun visitBranch(branch: ArmBranch) {
        indented {
            with(branch) {
                println { "RENDERING type:BRANCH index:$index" }
            }
            super.visitBranch(branch)
        }
    }

    override fun visitExpression(expression: ArmExpression) {
        indented {
            with(expression) {
                println { "$origin ${dependencies.withLabel("dependencies")}" }
            }
            super.visitExpression(expression)
        }
    }

    override fun visitValueArgument(valueArgument: ArmValueArgument) {
        indented {
            with(valueArgument) {
                println { "$origin $index ${dependencies.withLabel("dependencies")}" }
            }
        }
    }

    override fun visitSupportFunctionArgument(supportFunctionArgument: ArmSupportFunctionArgument) {
        indented {
            with(supportFunctionArgument) {
                println { "$origin $index ${dependencies.withLabel("dependencies")}" }
            }
        }
    }

    override fun visitHigherOrderArgument(higherOrderArgument: ArmHigherOrderArgument) {
        indented {
            with(higherOrderArgument) {
                println { "$origin $index type:${armClass.name} ${dependencies.withLabel("dependencies")}" }
            }
        }
    }

    override fun visitDeclaration(declaration: ArmDeclaration) {
        indented {
            with(declaration) {
                println { "$origin ${dependencies.withLabel("dependencies")}" }
            }
            super.visitDeclaration(declaration)
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

    fun ArmDependencies.withLabel(label: String) =
        "$label:[${this.joinToString(", ") { it.toString() }}]"

}