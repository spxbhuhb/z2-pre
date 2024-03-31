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
                println { "CLASS name:$name boundary:(${boundary.startOffset},${boundary.statementIndex})" }
            }
            super.visitClass(armClass)
        }
    }

    override fun visitExternalStateVariable(stateVariable: ArmExternalStateVariable) {
        indented {
            with(stateVariable) {
                println { "EXTERNAL_STATE_VARIABLE indexInState:$indexInState indexInClosure:$indexInClosure name:$name" }
            }
            super.visitStateVariable(stateVariable)
        }
    }

    override fun visitInternalStateVariable(stateVariable: ArmInternalStateVariable) {
        indented {
            with(stateVariable) {
                println { "INTERNAL_STATE_VARIABLE indexInState:$indexInState indexInClosure:$indexInClosure name:$name" }
            }
            super.visitStateVariable(stateVariable)
        }
    }

    override fun visitSequence(statement: ArmSequence) {
        indented {
            with(statement) {
                println { "SEQUENCE index:$index startOffset:${statement.startOffset} indices:${statement.statements.map { it.index }}" }
            }
            super.visitSequence(statement)
        }
    }

    override fun visitCall(statement: ArmCall) {
        indented {
            with(statement) {
                println { "CALL index:$index startOffset:${statement.startOffset} type:<$target>" }
            }
            super.visitCall(statement)
        }
    }

    override fun visitWhen(statement: ArmSelect) {
        indented {
            with(statement) {
                println { "WHEN index:$index startOffset:${statement.startOffset}" }
            }
            super.visitWhen(statement)
        }
    }

    override fun visitLoop(statement: ArmLoop) {
        indented {
            with(statement) {
                println { "LOOP index:$index startOffset:${statement.startOffset}" }
            }
            super.visitLoop(statement)
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

    override fun visitFragmentFactoryArgument(fragmentFactoryArgument: ArmFragmentFactoryArgument) {
        indented {
            with(fragmentFactoryArgument) {
                println { "$origin index:$index type:${armClass.name} ${dependencies.withLabel("dependencies")}" }
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