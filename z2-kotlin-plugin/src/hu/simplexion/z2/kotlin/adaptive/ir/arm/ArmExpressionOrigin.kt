/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

enum class ArmExpressionOrigin {
    VALUE_ARGUMENT,
    SUPPORT_FUNCTION_ARGUMENT,
    FRAGMENT_FACTORY_ARGUMENT,
    BRANCH_CONDITION,
    BRANCH_RESULT,
    FOR_LOOP_CONDITION,
    FOR_LOOP_BODY,
}
