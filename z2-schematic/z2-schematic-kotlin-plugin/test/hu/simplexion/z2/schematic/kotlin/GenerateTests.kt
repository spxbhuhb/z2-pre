package hu.simplexion.z2.schematic.kotlin

import hu.simplexion.z2.schematic.kotlin.runners.AbstractAdhocTest
import hu.simplexion.z2.schematic.kotlin.runners.AbstractBoxTest
import hu.simplexion.z2.schematic.kotlin.runners.AbstractDiagnosticTest
import org.jetbrains.kotlin.generators.generateTestGroupSuiteWithJUnit5

fun main() {
    generateTestGroupSuiteWithJUnit5 {
        testGroup(testDataRoot = "testData", testsRoot = "test-gen") {

            testClass<AbstractDiagnosticTest> {
                model("diagnostics")
            }

            testClass<AbstractBoxTest> {
                model("box")
            }

            testClass<AbstractAdhocTest> {
                model("adhoc")
            }

        }
    }
}