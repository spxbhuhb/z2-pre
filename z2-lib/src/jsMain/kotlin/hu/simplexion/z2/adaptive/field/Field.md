# Field

The general approach of UI fields is to have:

- value
- states
- configurations
- renderers

Value is the actual value of the field.

The states change frequently as the user modifies the content of the field.

The configurations are typically set during the declaration of the field, rarely change afterward.

The generic `FieldValue`, `FieldState` and `FieldConfig` classes are used to store data common for all input fields.
Additional `*State` and `*Config` classes are available to store data specific for the field type:

```kotlin
class TextField(
    parent: Z2,
    val fieldValue: FieldValue<String>,
    val fieldState: FieldState,
    val fieldConfig: FieldConfig,
    val textConfig: TextConfig    // this is specific to TextField
) 
```

Renderers are responsible for building and maintaining the actual UI representation of the field. Typically,
the field specific config (`TextConfig`) for example stores the actual renderer the field uses:

```kotlin
class TextConfig : Schematic<TextConfig>() {

    var renderer by generic<TextRenderer>()

}
```

All state and config classes are schematic classes and all fields update themselves when a value changes.