# Security System

# Authorization

Authorization should be performed at the beginning of each service API call.

The general approach is based on roles in the service context and one of the`ensure` functions or the `publicAccess` function.

`ensure` functions throw an exception whenever the condition is not valid.

| function                      | description                                                                                                                                                                                       |
|-------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `publicAccess`                | No authorization.                                                                                                                                                                                 |
| `ensureLoggedIn`              | Ensures that there **IS** a principal in the service context.                                                                                                                                     |
| `ensureInternal`              | Ensures that there **IS NOT** a principal in the service context.                                                                                                                                 |
| `ensureSecurityOfficer`       | Ensures that the built-in `security-officer` role is present in the service context.                                                                                                              |
| `ensureTechnicalAdmin`        | Ensures that **AT LEAST ONE** of the [technical administrator roles](#role-configuration) are present in the service context.                                                                     |
| `ensureAll`                   | Ensures that **ALL** the roles passed as parameters are present in the service context.                                                                                                           |
| `ensureAny`                   | Ensures that **AT LEAST ONE** of the roles passed as parameters are present in the service context.                                                                                               |
| `ensure`                      | Ensures that passed check result is `Allowed`.                                                                                                                                                    |
| `ensuredBy`                   | Ensures that the passed function returns with `true`.                                                                                                                                             |
| `ensuredByLogic`              | Notes that the authorization is ensured by the code logic. You **SHALL** explain in the parameter how.                                                                                            |
| `ensureSelf`                  | Ensures that the passed principal is the principal of the service context.                                                                                                                        |
| `ensureSelfOrSecurityOfficer` | Ensures that the passed principal is the principal of the service context **OR** the built-in `security-officer` role is present in the context.                                                  |
| `ensureSelfOrTechnicalAdmin`  | Ensures that the passed principal is the principal of the service context **OR** **AT LEAST ONE** of the [technical administrator roles](#role-configuration) are present in the service context. |

The example below shows a complete setup for non-trivial authorization. Notes:

* `globalManagementRole` is initialized during startup from the database
* `isGlobalManager` and `isClientManager` are helper functions so the actual security checks in the business code are very clean
* `updateClient` is an API function which uses `ensuredBy` to make sure the call is allowed

```kotlin
lateinit var globalManagerRole: Role

val ServiceImpl<*>.isGlobalManager
    get() = serviceContext.has(globalManagerRole).isAllowed

fun ServiceImpl<*>.isClientManager(client: Client) : Boolean =
    clientImpl.isClientManager(serviceContext.account, client)

override suspend fun updateClient(client : Client) {
    ensuredBy { isGlobalManager or isClientManager(client) }
}
```

### Role Configuration

Some implementations and methods allows the actual roles used by the `ensure` methods to be changed during system
startup. To do so, change the appropriate variable, for example:

```kotlin
import hu.simplexion.z2.auth.context.technicalAdminRoles

technicalAdminRoles = arrayOf(securityOfficerRole,technicalRole)
```

After the setup above the following authorization check accepts any of the roles above:

```kotlin
override suspend fun test() {
    ensureTechnicalAdmin() // securityOfficerRole OR technicalRole
}
```