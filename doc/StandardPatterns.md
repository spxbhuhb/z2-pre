# Standard Patterns

Standard patterns are coding / structural conventions Z2 follows. These let us
keep the code clean and well organized. 

These are not mandatory at all, it is just something I've found useful. (And pretty
to be honest.)

## Source Structure

```text
src / commonMain / kotlin / <module-package>

    api              - service API interfaces
    model            - schematics, enums
    ui               - strings, icons
    util             - utility functions
    common.kt        - module definition function, `authCommon` for example
    
src / jsMain / kotlin / <module-package>

    ui               - ui classes
    module.kt        - module definition function for browser `authJs` for example
        
src / jvmMain / kotlin / <module-package>

    impl             - service API implementations
    table            - Exposed table definitions
    runtime          - runtime support for service implementations and workers
    worker           - background workers
    util             - utility functions and classes
    module.kt        - module definition function for JVM, `authJvm` for example
```

## SQL Tables

I use `SchematicUuidTable` whenever the table stores entities. Otherwise, I use
whatever Exposed table fits the needs.

Tables are classes with a companion object that has a variable which is the default
instance of the table. This variable is used in:

* other table definitions for referencing,
* implementations and workers to access the table,
* `module.kt` to create / update the table automatically during application startup.

```kotlin
class RoleTable : SchematicUuidTable<Role>("auth_role", Role()) {

    companion object {
        val roleTable = RoleTable()
    }

    val contextName = varchar("contextName", 50).nullable()
    val programmaticName = varchar("programmaticName", 100)
    val displayName = varchar("displayName", 50)

}
```

In `module.kt`:

```kotlin
fun authJvm() {
    tables(roleTable)
}
```

### SQL Statements

All actual SQL statement builders go to the table class. The implementations and workers do not
contain any SQL and Exposed related code.

```kotlin
class RoleTable : SchematicUuidTable<Role>("auth_role", Role()) {

    companion object {
        val roleTable = RoleTable()
    }

    val contextName = varchar("contextName", 50).nullable()
    val programmaticName = varchar("programmaticName", 100)
    val displayName = varchar("displayName", 50)

    fun getByDisplayName(inDisplayName : String) : List<Role> =
        select { displayName like inDisplayName }
            .map { it.toSchematic(this, newInstance()) }
}
```