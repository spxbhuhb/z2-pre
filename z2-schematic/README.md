# Z2 Schematic

[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.z2/z2-schematic-runtime)](https://mvnrepository.com/artifact/hu.simplexion.z2/z2-schematic-runtime)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Schematic classes for easy UI building, data management and communication.

Status: **proof-of-concept**

Schematic classes provide functions to:

* give metadata (data type, label, icon etc.) to UI components automatically
* provide validation and user feedback for the UI components
* create patches that contain only the changes
* apply patches to previous versions

You can think of schematic classes as view models with benefits.

The library has a runtime part and a Kotlin compiler plugin that transforms the code.

## Caveats

* inner classes are not supported
* field declaration in constructor is not supported

## Overview

When using schematic we work with:

* schematic data classes
* schemas
* schematic access functions

The first two is quite straightforward, while the third is a bit tricky, but very powerful.

### Schematic Data Classes

Schematic data classes store the data the application handles:

```kotlin
class Book : Schematic<Book>() { 
    var title by string(maxLength = 100, blank = false)
    var authors by list<Author>(minSize = 1, maxSize = 10)
    var publicationDate by localDate(after = LocalDate(1970,1,1))
}
```

When you have a schematic class, you can:

* get and set the properties just as you do with any other class,
* get the changes with the `schematicCollect` function
* apply the changes to another instance of the class with the `schematicPatch` function
* add event listeners which are called whenever a field changes
* validate the data with the `Schema` of the class

#### Default Values

* All fields are initialized to their "natural" default values.
* If you want a different value use the `default` parameter.

| Type              | Natural Default  |
|-------------------|------------------|
| any nullable type | `null`           |
| Boolean           | `false`          |
| Int               | `0`              |
| String            | `"" `            |
| UUID<T>           | `UUID.nil<T>()`  |

### Schemas

The `Schema` is generated for the schematic class automatically by the compiler plugin.
You can't see it in the editor, but it looks like this:

```kotlin
class Book : Schematic<Book>() {
    //. .. definition of fields as above
    companion object {
        val schematicSchema = Schema(
            StringSchemaField("title", maxLength = 100, blank = false),
            ListSchemaField("authors", minSize = 1, maxSize = 2, Author.schematicSchema),
            LocalDateSchemaField("publicationDate", after = LocalDate(1970,1,1))
        )
    }
    
}
```

You can:

* access the schema of a schematic class instance in the `schematicSchema` property
* validate a schematic data class instance with the `validate` and `suspendValidate` functions
* get the schema field for any schematic property automatically with schematic access functions

### Schematic Access Functions

The schematic access functions (SAF) are a bit tricky and one of the main reasons this library has been written.
Easiest way to explain how they work is by an example:

```kotlin
div { // let's assume this is part of a web page
    editor { book.title }
}
```

`editor` is a SAF. Let's see its implementation:

```kotlin
@SchematicAccessFunction
fun editor(context : SchematicAccessContext? = null, accessor : () -> Any?) {
    checkNotNull(context)
   
    when (context.field.type) {
        SchematicFieldType.String -> stringEditor(context)
        SchematicFieldType.LocalDate -> localDateEditor(context)
        else -> defaultEditor(context)
    }
}
```

There are a few rules SAF functions have to follow:

* must be annotated with `SchematicAccessFunction`
* the last parameter must be a lambda with one statement that must get a schematic property
* the parameter before the last one must be of the `SchematicAccessContext` type

The `SchematicAccessContext` class contains:

* the instance that contains the accessed property
* the `SchemaField` that belongs to the accessed property
* the value of the accessed property

```kotlin
class SchematicAccessContext(
    val schematic : Schematic<*>, // this is the instance of book used in the curly brackets.
    val field : SchemaField<*>, // this is the metadata that belongs to `title`
    val value : Any? // this is the value of `title`
)
```

This has many uses. For example, you can write an `editor` that can validate the field based on the schema
data.

## Details

### Schematic Classes

To define a schematic class, extend `Schematic` and use the provided field definition functions. You may find the [list
of available functions](#field-definition-functions) below or [define your own](#writing-field-definition-functions).

```kotlin
class Test : Schematic<Test>() {
    val intField by int(min = 5)
}
```

Is turned into:

```kotlin
class Test : Schematic<Test> {

    val intField : Int
        get() = schematicValues["intField"]!! as Int
        set(value) {
            schematicChangeInt("intField", value)
        }

    override val schematicSchema
        get() = Companion.schematicSchema

    companion object {
        val schematicSchema = Schema(
            IntSchemaField("intField", min = 5)
        )
    }
}
```

The schema is independent of the data instances, but any given instance can access its own schema through the
`schematicSchema` property.

### Field Definition Functions

```kotlin
fun boolean(
    default: Boolean = false
)
```

```kotlin
fun int(
    default : Int = 0,
    min : Int? = null,
    max : Int? = null
) : Int
```

```kotlin
fun string(
    default : String = "",
    minLength : Int? = null,
    maxLength : Int? = null,
    blank : Boolean = true,
    pattern : Regex? = null
)
```

```kotlin
 fun <V> uuid(
     default: UUID<V>? = null,
     nil : Boolean? = null,
 )
```

### Writing Field Definition Functions

To define an FDF you need to:

1. create the function itself
2. create a schema field class that extends `SchemaField`
   1. All parameters of the FDF must be present as parameters of the field class constructor with the same name and same order after the overridden fields.

```kotlin
@Suppress("UNUSED_PARAMETER")
@FieldDefinitionFunction(IntSchemaField::class)
fun int(
    default: Int = 0,
    min: Int? = null,
    max: Int? = null
) = PlaceholderDelegateProvider<T,Int>()
```

```kotlin
class IntSchemaField(
    override val name: String,
    val default: Int = 0,
    val min: Int? = null,
    val max: Int? = null,
) : SchemaField {

    override val type: SchemaFieldType
        get() = SchemaFieldType.Int

   override fun toTypedValue(anyValue: Any?, fails: MutableList<ValidationFailInfo>): Int? {
      if (anyValue == null) return null

      return when (anyValue) {
         is Int -> anyValue
         is Number -> anyValue.toInt()
         is String -> anyValue.toIntOrNull()
         else -> {
            fails += fail(validationStrings.integerFail)
            null
         }
      }
   }

   override fun validateNotNullable(value: Int, fails: MutableList<ValidationFailInfo>) {
      if (min != null && value < min) fails += fail(validationStrings.minValueFail, min)
      if (max != null && value > max) fails += fail(validationStrings.maxValueFail, max)
   }
}
```

### Definition Transform Functions

As of now there is one definition transform function (DTF): `nullable`.
This transforms the field into a nullable one.