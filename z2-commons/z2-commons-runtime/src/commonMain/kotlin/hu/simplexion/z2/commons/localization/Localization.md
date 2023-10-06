# Localization

Z2 uses localization to provide language and region specific interfaces and functions for the users.

### Quick Start

To define strings the application uses, create an object that extends `LocalizedTextProvider` and define
the strings as properties with the `static` method:

```kotlin
object labels : LocalizedTextProvider {
    val label1 = static("First Label")
    val label2 = static("Second Label")
}
```

Then use these strings in the application:

```kotlin
textButton(labels.label1) {  }
```

Following this convention lets you develop the application without worrying about the translations. All components
will use the defaults provided in the object.

You can switch the translation on later, without any modification to the code that uses these strings.

You can use one big object or scope specific smaller ones, it doesn't matter.

### Localized Text Interfaces

It is possible to define the localized strings in interfaces like below. This requires some boilerplate,
but it makes possible to add these interfaces together.

Useful for libraries. For example Z2 provides the `DateTimeStrings` and `BrowserStrings` interfaces that contain basic
strings such as "back" or "january".

```kotlin
interface ILabels : LocalizedTextProvider {
    val label1 get() = static("First Label")
    val label2 get() = static("Second Label")
}

interface IMessages : LocalizedTextProvider {
    val message1 get() = static("First Message")
    val message2 get() = static("Second Messages")
}

object strings : ILabels, IMessages {
    val string1 = static("First String")
    val string2 = static("Second String")
}
```

With the interface approach you don't have to remember where the string was declared, you can use the object everywhere:

```kotlin
textButton(strings.label1) { snackbar(strings.message1) }
```

### Keys and Namespaces

All textual information that may be localized in the system has a unique **key**.

The key is composed of these parts which are separated by the `/` character:

* type
* namespace (optional)
* name
* qualifier (optional)

For example: `direct/task/deadline` or `direct//back` are keys in the `task` and in the **default** namespace,
respectively. The default  namespace is just a namespace with an empty name.

The **type** of the key shows how the given key is declared in the code. Type examples:

* `direct` - these keys are defined directly in objects and interfaces
* `enum` - these keys generated automatically for enum classes
* `schematic` - these keys are generated automatically for Schematic classes
* `entity` - these keys belong to entities such as roles

The **qualifier** indicates that the string belongs to another some way. Examples:

* `support` - supporting explanatory text, for example `schematic/Task/deadline/support` may explain the rules of the `Task.deadline` field to the user
* `help` - help text, longer than `support`, more in-depth explanation

To separate your text resources you can declare a namespace in the localization object:

```kotlin
object scopedStrings : LocalizedTextProvider {
    override val namespace get() = "scope1"
}
```

## Turning Localization On

When you want to switch your application to multi-language:

* add the `z2-commons` compiler plugin to your project
* add the `localization` module to your backend (this is part of `boot`, so it might be already added)
* add the `localization` module to your frontend (this is part of `boot`, so it might be already added)
* add languages and translations by
  * importing from CSV on the web interface
  * adding manually on the web interface
  * inserting directly into SQL

### Client Side

Once the localization is turned on, the clients download the locale to use from the server and store
all localized texts in the global value `localizedTextStore`.

When you use a localized property like `strings.label1` the program looks up the key to look up
the localization in `localizedTextStore`. If it is there it uses the localized value, if it is missing 
it uses the default, hard-coded value.

### Server Side

On the server side, localization depends on the actual, temporary context the action is executed in. 
For example, if you send an e-mail notice about an event on the server, you may want to send it on different
languages, depending on the user preference.

Therefore, there is no `localizedTextStore` on the server side, but you have to get one when you work with
the texts.

When you use Z2 Services you can use the service context as a localized text store. The `ServiceContext.localized`
function is provided for this purpose, it uses the locale of the current user.

```kotlin
println(localized(strings.addUser))
```

You can get a locale directly with the `localeImpl.getLocale` function on the server side.

```kotlin
for (user in users) {
    val locale = localeImpl.getLocale(user.locale)
    emailImpl.send(strings.newTask)
}
```

If you have to switch locales (as in the e-mail generation example above) programatically on the servers

### Compiler Plugin

The compiler plugin:

* turns all calls such as `static("default text")` into `static(this, "property-name", "default-text")` calls
* collects all localization keys used in your code and saves them into a file
    * this gives you a complete list of texts you have to translate

## Textual Localization

There are three classes that can store localized textual information:

- `StaticText` - simple text, shown as-is
- `QuickTemplate` - text which contains `%P` in place of parameters to be added before shown
- `RichTemplate` - text that is the source code of a complex template

All three has a `value` field that stores the source text itself in the given locale.

| Use Case            | Type                         | Declaration                   | Key                               |
|---------------------|------------------------------|-------------------------------|-----------------------------------|
| button labels       | LocalizedText                | LocalizedTextStore            | store FQN + field name            |
| page, dialog titles | LocalizedText                | LocalizedTextStore            | store FQN + field name            |
| tooltips            | LocalizedText                | LocalizedTextStore            | store FQN + field name            |
| menu items          | LocalizedText                | LocalizedTextStore            | store FQN + field name            |
| messages            | LocalizedText, QuickTemplate | LocalizedTextStore            | store FQN + field name            |
| table headers       | LocalizedText                | Schematic, LocalizedTextStore | schematic, store FQN + field name |
| form field labels   | LocalizedText                | Schematic, LocalizedTextStore | schematic, store FQN + field name |
| form field support  | LocalizedText                | Schematic, LocalizedTextStore | schematic, store FQN + field name |
| form field errors   | LocalizedText, QuickTemplate | Schematic, LocalizedTextStore | schematic, store FQN + field name |
| form field help     | RichTemplate                 | Schematic, LocalizedTextStore | schematic, store FQN + field name |
| enumeration names   | LocalizedText                | automatic                     | enum FQN + item name              |
| role names          | LocalizedText                | automatic                     | role UUID                         |
| generated emails    | RichTemplate                 | runtime                       | template UUID                     |
| generated PDF files | RichTemplate                 | runtime                       | template UUID                     |
| generated DOC files | RichTemplate                 | runtime                       | template UUID                     |


### Declaring Textual Localization Resources

Check [Quick Start](#quick-start) for basic information.

To define different types of templates use the `quick` and `rich` functions.

```kotlin
object myStrings : LocalizedTextProvider {
    val myLabel = static("Send Email")
    val myMessage = quick("%N email(s) sent")
    val myEmail = rich("Dear {name}! I wish you happy nth{age} birthday! Best Regards, {sender}")
}
```

