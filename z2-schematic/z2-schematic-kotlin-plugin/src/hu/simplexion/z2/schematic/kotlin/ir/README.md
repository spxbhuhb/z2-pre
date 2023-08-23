# Transforms

The plugin looks for any class declarations that are descendant of `Schematic` and:

* transforms each schematic property
* if there is no companion object, adds one
* adds a `schematicSchema` property to the companion object which is initialized to the `Schema` of the class
* adds a `schematicSchema` property to the class that returns with the `Companion.schematicSchema`

Example:

Original:

```kotlin
class Adhoc : Schematic<Adhoc> {
    val intField by int(min = 5)
}
```

Transformed: 

```kotlin
class Adhoc : Schematic<Adhoc> {
    
    val intField : Int
        get() = schematicValues["intField"]!! as Int
        set(value) {
            schematicChangeInt("intField", value)
        }
    
    override val schematicSchema 
        get() = Companion.schematicSchema
    
    companion object {
        val schematicSchema = Schema(
            IntSchemaField("intField", 5, null)
        )
    }
}
```

## Property Transform

### Original Code

```kotlin
val intField by int(min = 5)
```

```text
PROPERTY name:intField visibility:public modality:FINAL [delegated,var]
  FIELD PROPERTY_DELEGATE name:intField$delegate type:kotlin.properties.ReadWriteProperty<foo.bar.Adhoc, kotlin.Int> visibility:private [final]
    EXPRESSION_BODY
      CALL 'public final fun provideDelegate (thisRef: T of hu.simplexion.z2.schematic.runtime.Schematic.PlaceholderDelegateProvider, prop: kotlin.reflect.KProperty<*>): kotlin.properties.ReadWriteProperty<T of hu.simplexion.z2.schematic.runtime.Schematic.PlaceholderDelegateProvider, V of hu.simplexion.z2.schematic.runtime.Schematic.PlaceholderDelegateProvider> [operator] declared in hu.simplexion.z2.schematic.runtime.Schematic.PlaceholderDelegateProvider' type=kotlin.properties.ReadWriteProperty<foo.bar.Adhoc, kotlin.Int> origin=null
        $this: CALL 'public final fun int (default: kotlin.Int, min: kotlin.Int?, max: kotlin.Int?): hu.simplexion.z2.schematic.runtime.Schematic.PlaceholderDelegateProvider<foo.bar.Adhoc, kotlin.Int> [fake_override] declared in foo.bar.Adhoc' type=hu.simplexion.z2.schematic.runtime.Schematic.PlaceholderDelegateProvider<foo.bar.Adhoc, kotlin.Int> origin=null
          $this: GET_VAR '<this>: foo.bar.Adhoc declared in foo.bar.Adhoc' type=foo.bar.Adhoc origin=null
          min: CONST Int type=kotlin.Int value=5
        thisRef: GET_VAR '<this>: foo.bar.Adhoc declared in foo.bar.Adhoc' type=foo.bar.Adhoc origin=null
        prop: PROPERTY_REFERENCE 'public final intField: kotlin.Int [delegated,var]' field=null getter='public final fun <get-intField> (): kotlin.Int declared in foo.bar.Adhoc' setter='public final fun <set-intField> (<set-?>: kotlin.Int): kotlin.Unit declared in foo.bar.Adhoc' type=kotlin.reflect.KMutableProperty1<foo.bar.Adhoc, kotlin.Int> origin=PROPERTY_REFERENCE_FOR_DELEGATE
  FUN DELEGATED_PROPERTY_ACCESSOR name:<get-intField> visibility:public modality:FINAL <> ($this:foo.bar.Adhoc) returnType:kotlin.Int
    correspondingProperty: PROPERTY name:intField visibility:public modality:FINAL [delegated,var]
    $this: VALUE_PARAMETER name:<this> type:foo.bar.Adhoc
    BLOCK_BODY
      RETURN type=kotlin.Nothing from='public final fun <get-intField> (): kotlin.Int declared in foo.bar.Adhoc'
        CALL 'public abstract fun getValue (thisRef: T of kotlin.properties.ReadWriteProperty, property: kotlin.reflect.KProperty<*>): V of kotlin.properties.ReadWriteProperty [operator] declared in kotlin.properties.ReadWriteProperty' type=kotlin.Int origin=null
          $this: GET_FIELD 'FIELD PROPERTY_DELEGATE name:intField$delegate type:kotlin.properties.ReadWriteProperty<foo.bar.Adhoc, kotlin.Int> visibility:private [final]' type=kotlin.properties.ReadWriteProperty<foo.bar.Adhoc, kotlin.Int> origin=null
            receiver: GET_VAR '<this>: foo.bar.Adhoc declared in foo.bar.Adhoc.<get-intField>' type=foo.bar.Adhoc origin=null
          thisRef: GET_VAR '<this>: foo.bar.Adhoc declared in foo.bar.Adhoc.<get-intField>' type=foo.bar.Adhoc origin=null
          property: PROPERTY_REFERENCE 'public final intField: kotlin.Int [delegated,var]' field=null getter='public final fun <get-intField> (): kotlin.Int declared in foo.bar.Adhoc' setter='public final fun <set-intField> (<set-?>: kotlin.Int): kotlin.Unit declared in foo.bar.Adhoc' type=kotlin.reflect.KMutableProperty1<foo.bar.Adhoc, kotlin.Int> origin=PROPERTY_REFERENCE_FOR_DELEGATE
  FUN DELEGATED_PROPERTY_ACCESSOR name:<set-intField> visibility:public modality:FINAL <> ($this:foo.bar.Adhoc, <set-?>:kotlin.Int) returnType:kotlin.Unit
    correspondingProperty: PROPERTY name:intField visibility:public modality:FINAL [delegated,var]
    $this: VALUE_PARAMETER name:<this> type:foo.bar.Adhoc
    VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Int
    BLOCK_BODY
      CALL 'public abstract fun setValue (thisRef: T of kotlin.properties.ReadWriteProperty, property: kotlin.reflect.KProperty<*>, value: V of kotlin.properties.ReadWriteProperty): kotlin.Unit [operator] declared in kotlin.properties.ReadWriteProperty' type=kotlin.Unit origin=null
        $this: GET_FIELD 'FIELD PROPERTY_DELEGATE name:intField$delegate type:kotlin.properties.ReadWriteProperty<foo.bar.Adhoc, kotlin.Int> visibility:private [final]' type=kotlin.properties.ReadWriteProperty<foo.bar.Adhoc, kotlin.Int> origin=null
          receiver: GET_VAR '<this>: foo.bar.Adhoc declared in foo.bar.Adhoc.<set-intField>' type=foo.bar.Adhoc origin=null
        thisRef: GET_VAR '<this>: foo.bar.Adhoc declared in foo.bar.Adhoc.<set-intField>' type=foo.bar.Adhoc origin=null
        property: PROPERTY_REFERENCE 'public final intField: kotlin.Int [delegated,var]' field=null getter='public final fun <get-intField> (): kotlin.Int declared in foo.bar.Adhoc' setter='public final fun <set-intField> (<set-?>: kotlin.Int): kotlin.Unit declared in foo.bar.Adhoc' type=kotlin.reflect.KMutableProperty1<foo.bar.Adhoc, kotlin.Int> origin=PROPERTY_REFERENCE_FOR_DELEGATE
        value: GET_VAR '<set-?>: kotlin.Int declared in foo.bar.Adhoc.<set-intField>' type=kotlin.Int origin=null
```

### Transformed Code

```kotlin
val intField : Int
    get() = schematicValues["intField"]!! as Int
    set(value) {
        schematicChangeInt("intField", value)
    }
```

```text
PROPERTY name:intField visibility:public modality:FINAL [var]
  FUN name:<get-intField> visibility:public modality:FINAL <> ($this:foo.bar.Adhoc) returnType:kotlin.Int
    correspondingProperty: PROPERTY name:intField visibility:public modality:FINAL [var]
    $this: VALUE_PARAMETER name:<this> type:foo.bar.Adhoc
    BLOCK_BODY
      RETURN type=kotlin.Nothing from='public final fun <get-intField> (): kotlin.Int declared in foo.bar.Adhoc'
        TYPE_OP type=kotlin.Int origin=CAST typeOperand=kotlin.Int
          CALL 'public final fun CHECK_NOT_NULL <T0> (arg0: T0 of kotlin.internal.ir.CHECK_NOT_NULL?): {T0 of kotlin.internal.ir.CHECK_NOT_NULL & Any} declared in kotlin.internal.ir' type=kotlin.Any origin=EXCLEXCL
            <T0>: kotlin.Any
            arg0: CALL 'public abstract fun get (key: K of kotlin.collections.MutableMap): V of kotlin.collections.MutableMap? [fake_override,operator] declared in kotlin.collections.MutableMap' type=kotlin.Any? origin=null
              $this: CALL 'public final fun <get-schematicValues> (): kotlin.collections.MutableMap<kotlin.String, kotlin.Any?> [fake_override] declared in foo.bar.Adhoc' type=kotlin.collections.MutableMap<kotlin.String, kotlin.Any?> origin=GET_PROPERTY
                $this: GET_VAR '<this>: foo.bar.Adhoc declared in foo.bar.Adhoc.<get-intField>' type=foo.bar.Adhoc origin=null
              key: CONST String type=kotlin.String value="intField"
  FUN name:<set-intField> visibility:public modality:FINAL <> ($this:foo.bar.Adhoc, value:kotlin.Int) returnType:kotlin.Unit
    correspondingProperty: PROPERTY name:intField visibility:public modality:FINAL [var]
    $this: VALUE_PARAMETER name:<this> type:foo.bar.Adhoc
    VALUE_PARAMETER name:value index:0 type:kotlin.Int
    BLOCK_BODY
      CALL 'public final fun schematicChangeInt (field: kotlin.String, value: kotlin.Int): kotlin.Unit [fake_override] declared in foo.bar.Adhoc' type=kotlin.Unit origin=null
        $this: GET_VAR '<this>: foo.bar.Adhoc declared in foo.bar.Adhoc.<set-intField>' type=foo.bar.Adhoc origin=null
        field: CONST String type=kotlin.String value="intField"
        value: GET_VAR 'value: kotlin.Int declared in foo.bar.Adhoc.<set-intField>' type=kotlin.Int origin=null
```
## schematicSchema

### Added Code

```kotlin
override val schematicSchema 
    get() = Companion.schematicSchema
```

```text
PROPERTY name:schematicSchema visibility:public modality:OPEN [val]
  overridden:
    public abstract schematicSchema: hu.simplexion.z2.schematic.runtime.schema.Schema [val]
  FUN name:<get-schematicSchema> visibility:public modality:OPEN <> ($this:foo.bar.Adhoc) returnType:hu.simplexion.z2.schematic.runtime.schema.Schema
    correspondingProperty: PROPERTY name:schematicSchema visibility:public modality:OPEN [val]
    overridden:
      public abstract fun <get-schematicSchema> (): hu.simplexion.z2.schematic.runtime.schema.Schema declared in hu.simplexion.z2.schematic.runtime.Schematic
    $this: VALUE_PARAMETER name:<this> type:foo.bar.Adhoc
    BLOCK_BODY
      RETURN type=kotlin.Nothing from='public open fun <get-schematicSchema> (): hu.simplexion.z2.schematic.runtime.schema.Schema declared in foo.bar.Adhoc'
        CALL 'public final fun <get-schematicSchema> (): hu.simplexion.z2.schematic.runtime.schema.Schema declared in foo.bar.Adhoc.Companion' type=hu.simplexion.z2.schematic.runtime.schema.Schema origin=GET_PROPERTY
          $this: GET_OBJECT 'CLASS OBJECT name:Companion modality:FINAL visibility:public [companion] superTypes:[kotlin.Any]' type=foo.bar.Adhoc.Companion
```

## Companion Object

### Added Code

When there is no existing companion, it is created:

```text
CLASS OBJECT name:Companion modality:FINAL visibility:public [companion] superTypes:[kotlin.Any]
  $this: VALUE_PARAMETER INSTANCE_RECEIVER name:<this> type:foo.bar.Adhoc.Companion
  CONSTRUCTOR visibility:private <> () returnType:foo.bar.Adhoc.Companion [primary]
    BLOCK_BODY
      DELEGATING_CONSTRUCTOR_CALL 'public constructor <init> () [primary] declared in kotlin.Any'
      INSTANCE_INITIALIZER_CALL classDescriptor='CLASS OBJECT name:Companion modality:FINAL visibility:public [companion] superTypes:[kotlin.Any]'
  FUN FAKE_OVERRIDE name:equals visibility:public modality:OPEN <> ($this:kotlin.Any, other:kotlin.Any?) returnType:kotlin.Boolean [fake_override,operator]
    overridden:
      public open fun equals (other: kotlin.Any?): kotlin.Boolean [operator] declared in kotlin.Any
    $this: VALUE_PARAMETER name:<this> type:kotlin.Any
    VALUE_PARAMETER name:other index:0 type:kotlin.Any?
  FUN FAKE_OVERRIDE name:hashCode visibility:public modality:OPEN <> ($this:kotlin.Any) returnType:kotlin.Int [fake_override]
    overridden:
      public open fun hashCode (): kotlin.Int declared in kotlin.Any
    $this: VALUE_PARAMETER name:<this> type:kotlin.Any
  FUN FAKE_OVERRIDE name:toString visibility:public modality:OPEN <> ($this:kotlin.Any) returnType:kotlin.String [fake_override]
    overridden:
      public open fun toString (): kotlin.String declared in kotlin.Any
    $this: VALUE_PARAMETER name:<this> type:kotlin.Any
```

Add the `schematicSchema` field to the companion:

```kotlin
val schematicSchema = Schema(
    IntSchemaField("intField", 5, null)
)
```

```text
PROPERTY name:schematicSchema visibility:public modality:FINAL [val]
  FIELD PROPERTY_BACKING_FIELD name:schematicSchema type:hu.simplexion.z2.schematic.runtime.schema.Schema visibility:private [final]
    EXPRESSION_BODY
      CONSTRUCTOR_CALL 'public constructor <init> (vararg fields: hu.simplexion.z2.schematic.runtime.schema.SchemaField) [primary] declared in hu.simplexion.z2.schematic.runtime.schema.Schema' type=hu.simplexion.z2.schematic.runtime.schema.Schema origin=null
        fields: VARARG type=kotlin.Array<out hu.simplexion.z2.schematic.runtime.schema.SchemaField> varargElementType=hu.simplexion.z2.schematic.runtime.schema.SchemaField
          CONSTRUCTOR_CALL 'public constructor <init> (name: kotlin.String, default: kotlin.Int, min: kotlin.Int?, max: kotlin.Int?) [primary] declared in hu.simplexion.z2.schematic.runtime.schema.field.IntSchemaField' type=hu.simplexion.z2.schematic.runtime.schema.field.IntSchemaField origin=null
            name: CONST String type=kotlin.String value="intField"
            default: CONST Int type=kotlin.Int value=5
            min: CONST Null type=kotlin.Nothing? value=null
  FUN DEFAULT_PROPERTY_ACCESSOR name:<get-schematicSchema> visibility:public modality:FINAL <> ($this:foo.bar.Adhoc.Companion) returnType:hu.simplexion.z2.schematic.runtime.schema.Schema
    correspondingProperty: PROPERTY name:schematicSchema visibility:public modality:FINAL [val]
    $this: VALUE_PARAMETER name:<this> type:foo.bar.Adhoc.Companion
    BLOCK_BODY
      RETURN type=kotlin.Nothing from='public final fun <get-schematicSchema> (): hu.simplexion.z2.schematic.runtime.schema.Schema declared in foo.bar.Adhoc.Companion'
        GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:schematicSchema type:hu.simplexion.z2.schematic.runtime.schema.Schema visibility:private [final]' type=hu.simplexion.z2.schematic.runtime.schema.Schema origin=null
          receiver: GET_VAR '<this>: foo.bar.Adhoc.Companion declared in foo.bar.Adhoc.Companion.<get-schematicSchema>' type=foo.bar.Adhoc.Companion origin=null
```


## Notes

This is the function called to create the delegate for a property.
The annotation shows which field class should be created for this property.

```text
FUN FAKE_OVERRIDE name:int visibility:public modality:FINAL <> ($this:hu.simplexion.z2.schematic.runtime.Schematic<T of hu.simplexion.z2.schematic.runtime.Schematic>, default:kotlin.Int, min:kotlin.Int?, max:kotlin.Int?) returnType:hu.simplexion.z2.schematic.runtime.Schematic.PlaceholderDelegateProvider<foo.bar.Adhoc, kotlin.Int> [fake_override]
  annotations:
    SchematicDelegate(schemaFieldClass = CLASS_REFERENCE 'CLASS IR_EXTERNAL_DECLARATION_STUB CLASS name:IntSchemaField modality:FINAL visibility:public superTypes:[hu.simplexion.z2.schematic.runtime.schema.SchemaField]' type=kotlin.reflect.KClass<hu.simplexion.z2.schematic.runtime.schema.field.IntSchemaField>)
  overridden:
    public final fun int (default: kotlin.Int, min: kotlin.Int?, max: kotlin.Int?): hu.simplexion.z2.schematic.runtime.Schematic.PlaceholderDelegateProvider<T of hu.simplexion.z2.schematic.runtime.Schematic, kotlin.Int> declared in hu.simplexion.z2.schematic.runtime.Schematic
  $this: VALUE_PARAMETER name:<this> type:hu.simplexion.z2.schematic.runtime.Schematic<T of hu.simplexion.z2.schematic.runtime.Schematic>
  VALUE_PARAMETER name:default index:0 type:kotlin.Int
  VALUE_PARAMETER name:min index:1 type:kotlin.Int?
  VALUE_PARAMETER name:max index:2 type:kotlin.Int?
```