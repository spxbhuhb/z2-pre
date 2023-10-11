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

## Schematic Data Classes

Schematic data classes store the data the application handles:

```kotlin
import hu.simplexion.z2.schematic.runtime.Schematic

class Author : Schematic<Author>() {
    var name by string() minLength 5 maxLength 5 blank false
}

class Book : Schematic<Book>() {
    var title by string() maxLength 100 blank false
    var authors by schematicList<Author>()
    var publicationDate by localDate()
}
```

If you don't like the infix notation, you can use parameters directly:

```kotlin
import hu.simplexion.z2.schematic.runtime.Schematic

class Author : Schematic<Author>() {
    var name by string(minLength = 5, maxLength = 5, blank = false)
}

class Book : Schematic<Book>() { 
    var title by string(minLength = 1, maxLength = 100, blank = false)
    var authors by schematicList<Author>()
    var publicationDate by localDate()
}
```

When you have a schematic class, you can:

* get and set the properties just as you do with any other class,
* listen to events fired when a property value changes
* collect the changes to perform partial updates
* validate the data with the `Schema` of the class

### Data Types

Support for the following data types is built in.

| Stereotype   | Kotlin Type               | Definition Function  | Natural Default                |
|--------------|---------------------------|----------------------|--------------------------------|
|              | Boolean                   | `boolean()`          | `false`                        |
|              | Duration                  | `duration()`         | `Duration.ZERO`                |
| email        | String                    | `email()`            | `""`                           |
|              | Enum<E>                   | `enum<E>()`          | `E.entries.first()`            |
|              | Instant                   | `instant()`          | `Clock.System.now()`           |
|              | Int                       | `int()`              | `0`                            |
|              | LocalDate                 | `localDate()`        | `LocalDate.fromEpochDays(0)`   |
|              | LocalDateTime             | `localDateTime()`    | `LocalDateTime(0,1,1,0,0,0,0)` |
|              | Long                      | `long()`             | `0L`                           |
| phone number | String                    | `phoneNumber()`      | `""`                           |
|              | Schematic<T>              | `schematic<T>()`     | `T()`                          |
|              | MutableList<Schematic<T>> | `schematicList<T>()` | mutableListOf<T>()`            |
| secret       | String                    | `secret()`           | `""`                           |
|              | String                    | `string()`           | `"" `                          |
|              | MutableList<String<T>>    | `stringList<T>()`    | mutableListOf<String>()`       |
|              | UUID<T>                   | `uuid<T>()`          | `UUID.nil<T>()`                |
|              | MutableList<UUID<T>>      | `uuidList<T>()`      | mutableListOf<UUID<T>>()`      |

* All fields are initialized to their "natural" default values.
* For nullable fields the natural default is `null`.
* If you want a different default value use the `default` parameter or infix function.
* Datetime types are from `kotlinx.datetime`
* UUID is from Z2 Commons
* List types are added on-demand and as of now they cannot contain null values.

To add a new field type just extend the existing ones or define your own by implementing
the `SchemaField` or `ListSchemaField` interface. For example check the existing field definitions.

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

To define a schematic class, extend `Schematic` and use the provided field definition functions.

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
