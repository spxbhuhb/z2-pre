# Internals

## Setter and Getter Transform

```kotlin
var a by int()
```

turns into:

```kotlin
var a : Int 
    get() = schematicValues["a"]!! as Int
    set(value) {
        schematicChange("a", 0, value)
    }
```

`schematicChange(String, Int, Any?)`:

* calls `asChange` of the field from the schema and 
* passes it to `schematicChange(String, SchematicChange)`

## SAF Transform

```kotlin
testFun { test.intField }
```

turns into:

```kotlin
testFun(SchematicContext(test.schematicSchema["intField"])) { test.intField }
```