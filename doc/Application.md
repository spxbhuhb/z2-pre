# Application

## Role and Principal Initialization

To initialize roles and principals follow the patterns from:

* [ApplicationSettings](/z2-lib/src/commonMain/kotlin/hu/simplexion/z2/application/ApplicationSettings.kt)
* [jvm.kt](/z2-lib/src/jvmMain/kotlin/hu/simplexion/z2/auth/jvm.kt)

```kotlin
object ApplicationRoles {
    
    const val myBuiltinPrincipalName = "Built-In Printicpal"
    const val myBuiltinPrincipalUuid = UUID<Principal>("insert a unique uuid here")

    val myApplicationRole = Role().also {
        it.uuid = UUID("insert a unique UUID here")
        it.programaticName = "insert the role name here"
        it.displayName = "insert the role name here"
    }
}

fun initJvm() {
    transaction {
        with(ApplicationRoles) {
            if (firstTimeInit) {
                makePrincipal(myBuiltinPrincipalName, myBuiltinPrincipalUuid)
                makeRole(myApplicationRole)
                makeRoleGrant(myApplicationRole.uuid, myBuiltinPrincipalUuid)
            } else {
                validatePrincipal(myBuiltinPrincipalUuid)
                validateRole(myApplicationRole)
                validateRoleGrant(myApplicationRole.uuid, myBuiltinPrincipalUuid)
            }
        }
    }
}
```