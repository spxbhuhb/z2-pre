# Auth

## Module Initialization

### JVM

Performed by [authJvm](./jvm.kt).

Creates or updates all SQL tables.

When all auth tables are empty:

- make the security officer principal
- create the security officer role
- grant the security officer role to the security officer principal
- create the technical admin role
- grant the technical admin role to the security officer principal

When at least one of the auth tables is not empty:

- validate the security officer principal (exists and not locked)
- validate the security officer role (exists and granted to the security officer)
- validate the technical admin role (exists)

The creation and validation uses the settings
from [ApplicationSettings](/z2-lib/src/commonMain/kotlin/hu/simplexion/z2/application/ApplicationSettings.kt),
see [Settings](/doc/Settings.md) for more information.