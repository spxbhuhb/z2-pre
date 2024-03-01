# Schematic

Schematic data models lets you describe and use application data. This is a fundamental
part of Z2, almost all other functions are built on schematics.

There are three important interfaces to start with:

- `Schematic` - the interface to be implemented by all schematic data models
- `SchematicEntity` - which extends `Schematic` and models identified entities
- `SchematicEntityStore` - which is an interface for stores of schematic entities

In addition, there is one class which is the cornerstone of this module:

- `Schema` - the actual data model that belongs to a given schematic

When defining schematic classes, you simply implement the `Schematic` or the `SchematicEntity` interface:

```kotlin
class A : Schematic<A> {
    var i by int()
}

class EntityA : SchematicEntity<A> {
    override uuid by self<A>()
    override name by string()
}
```

There are a few important points in the code above:

- When declaring schematics you use the delegation syntax: `by`
- The interfaces provide you pre-defined builder functions such as `int`, `string` etc.
- When defining an entity you must override the `uuid` and the `name` field.

## Entities

Schematic entities are schematic classes with a few additional features:

- mandatory `uuid` field
- mandatory `name` field
- `SchematicEntityCompanion` companion interface
- `SchematicEntityCompanion.schematicEntityStore` field
- `SchematicEntityStore` API
- `reference` schematic field type

### Entity Stores

`SchematicEntityStore` defines basic operations for storing and retrieving entities. The class
is designed to follow the syntax of `Map`.

```kotlin
interface SchematicEntityStore<T : SchematicEntity<T>> {
    
    val schematicEntityCompanion : SchematicEntityCompanion<T> 
        get() = placeholder() // replaced by the compiler plugin
  
    fun new(): T = schematicEntityCompanion.newInstance()
  
    suspend fun put(entity: T): UUID<T>
  
    suspend fun get(uuid: UUID<T>): T?
  
    suspend fun remove(uuid: UUID<T>)
  
    suspend fun values(): List<T>

}
```

To define an entity store add it to a service API. You can use it other ways but the compiler plugin
fills the companion field ONLY when the entity store is part of a service.

```kotlin
interface BookApi : Service, SchematicEntityStore<Book> {
    // additional service functions as needed
}
```

### Companion

The compiler plugin adds the `SchematicEntityCompanion` interface to the companion object of all entities:

```kotlin
interface SchematicEntityCompanion<T : SchematicEntity<T>> : SchematicCompanion<T> {
    var schematicEntityStore: SchematicEntityStore<T>
}
```

When you use the `getService<T>()` function to get an instance of an entity store the store registers 
itself in the companion object of the entity

```kotlin
val bookService = getService<BookApi>()

val newBook = Book.schematicEntityStore.new() // same as bookService.new()
```

### References

When you write a schematic class you can use the `reference` function to declare a 
reference to a schematic entity:

```kotlin
class Book : SchematicEntity<Book> {
    override var uuid by self<Book>()
    override var name by string()
  
    var publisher by reference<Publisher>()
    var authors by referenceList<Author>()
}
```

Reference fields have an interesting feature: they store the companion object 
of the referenced entity:

```kotlin
val bookCompanion = Book.getReferenceCompanion("publisher")
```

If you combine this with the `store` property of the entity companion you can get the 
referenced objects without knowing their respective services:

```kotlin
val book = bookService.values().first()
val publisher = Book.getReferenceCompanion("publisher").schematicEntityStore.get(book.publisher)
```

This might look as much ado about nothing, but it let us write this:

```kotlin
field { publisher }
```

or 

```kotlin
column { publisher }
```

And this is where the all the above exists:

- the schematic access function (`field` or `column`) knows that `publisher` is a reference
- it can get the companion of the referenced entity
- from the companion it can get the store of the entity
- from the store it can load the entity and display the selected value
- also from the store it can query the values and display the possible choices for the user

# Old Stuff


Schematic lets you provide metadata and validation for your classes in a clean, concise way.
There are different libraries that provide this functionality, however Z2 takes this a step
further and gives you many integrated tools to use this metadata automatically:

* schematic aware UI components use the metadata to:
    * automatically provide two-way data binding
    * automatically select the appropriate editor and table column for the given data type
    * automatically validate user input based on the schema
    * automatically provide validation feedback to the users (with full localization support)
    * automatically add labels to the fields and headers to tables
    * automatically provide data type and locale aware formatting, search, sorting for tables
* services support schematic classes as parameters and return values automatically
* schematic classes fire data change events on the event bus events
* automatic mapping between schematic classes and Exposed columns
* provide functions to get and set values based on field name
* provide sensible [defaults](#defaults) for all data types

## Overview

When using schematic we work with:

* schematic data classes
* schemas
* schematic access functions

The first two is quite straightforward, while the third is a bit tricky, but very powerful.

## Schematic Data Classes

Schematic data classes store the data the application handles. They **MUST** extend the `Schematic` interface.

```kotlin
import hu.simplexion.z2.schematic.Schematic

class Author : Schematic<Author> {
    val uuid: UUID<Author>
    val name by string {
        constraint minLength 5 maxlength 5 blank false
        attribute label "Author's Name"
    }
}

class Book : Schematic<Book> {
    var title by string { constraint maxLength 100 blank false }
    var authors by list<Author> { constraint minLength 1 }
    var publicationDate: LocalDate
}
```

You can define fields with the definition functions provided by the `Schematic` interface or as a normal
Kotlin field if you do not want to provide additional metadata.

Each schematic class has:

* two automatically added [constructors](#constructors-and-defaults)
* a [companion object](#companion-object) which implements the `SchematicCompanion` interface
* a [schema](#schemas) which contains the [field definitions](#field-definitions) and provides various functions

### Constructors and Defaults

All schematic classes have at least two constructors generated automatically:

* empty constructor
* all-field constructor

Both constructors set all the fields to their default value but the all-field constructor lets you change
any of the fields when you create a new instance.

The default value of a field depends on the [field definition](#field-definitions), nullability and [data type](#data-types).

1. if there is an explicit `defaultVal` or `defaultFun` specified in the schema, the value or function return value is set, respectively
2. otherwise if the field is nullable the `null` value is set
3. otherwise the default value of for the field [data type](#data-types) is set

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

* Datetime types are from `kotlinx.datetime`
* UUID is from Z2 Commons
* List types cannot contain null values.

### Field Definitions

All fields of schematic classes have a definition in the schema, even if you use the minimal definition without additional metadata.

The following example contains three field definitions showing how to specify additional constraints and attributes.

```kotlin
import hu.simplexion.z2.schematic.Schematic

class Author : Schematic<Author> {
    
    val uuid: UUID<Author>
    
    val name by string {
        constraint minLength 5 maxlength 5 blank false
        attribute label "Author's Name"
        attribute doc "This is the name of the author. If there are more than one authors, please add them separately."
    }
  
    val active by boolean { 
        attribute default true
        attribute doc "This field is true if the author is still an active author, false otherwise."
    }
}
```

### Companion object

The compiler plugin automatically adds a companion object to all classes that implements the `Schematic` interface.
This companion cannot be seen in the editor, but it looks like this:

```kotlin
class Author : Schematic<Author>() {
    //. .. definition of fields as above

    companion object : SchematicCompanion<Author> {
        
        val schematicSchema = Schema(
            UuidSchemaField("title") { },
            StringSchemaField("name") { 
                constraint minLength 5 maxlength 5 blank false
                attribute label "Author's Name"
                attribute doc "This is the name of the author. If there are more than one authors, please add them separately."
            },
            BooleanSchemaField("active") {
                attibute default true
              attribute doc "This field is true if the author is still an active author, false otherwise."
            }
        )
    }
}
```

As you see, the field definitions are **MOVED** from the class into the companion object.

### Schemas

You can access the schema with the `schematicSchema` property of the companion or an instance.

```kotlin
Author.schematicSchema.dumpln()
Author().schematicSchema.dumpln()
```

You can get a definition of a specific field through the `fields` property of the schema:

```kotlin
Author.schematicSchema.fields["uuid"].dumpln()
```

`fields` is an array, so you can iterate over it to access all the fields defined in the schema:

```kotlin
Author.schematicSchema.fields.forEach { 
    field.dumpln()
}
```

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
fun editor(context: SchematicAccessContext? = null, accessor: () -> Any?) {
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
    val schematic: Schematic<*>, // this is the instance of book used in the curly brackets.
    val field: SchemaField<*>, // this is the metadata that belongs to `title`
    val value: Any? // this is the value of `title`
)
```

This has many uses. For example, you can write an `editor` that can validate the field based on the schema
data.

## Details

### Schematic Classes

To define a schematic class, extend `Schematic` and use the provided field definition functions.

```kotlin
class Test : Schematic<Test> {
    val intField by int(min = 5)
}
```

Is turned into:

```kotlin
class Test : Schematic<Test> {

    val intField: Int
        get() = schematicValues["intField"] !! as Int
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
