# Localization

Z2 uses localization to provide language and region specific interfaces and functions for the users.

### Quick Start

To define strings the application uses, create an interface that extends `LocalizedTextProvider` and define
the strings as properties with a getter:

```kotlin
interface Days : LocalizedTextProvider {
    val monday get() = "hétfő"
    val tuesday get() = "kedd"
    val wednesday get() = "szerda"
    val thursday get() = "csütörtök"
    val friday get() = "péntek"
    val saturday get() = "szombat"
    val sunday get() = "vasárnap"
}
```

Then define an object that implements this interface (I like to keep these object names lowercase, doesn't really
matter, it's just my convention):

```kotlin
object days : Days {
    override val namespace get() = "days"
}
```

With that you can start to use these strings in the application:

```kotlin
textButton(days.monday) {  }
```

Following this convention lets you develop the application without worrying about the translations. All components
will use the defaults provided in the interface.

## Turning Localization On

When you want to make your application multi-language:

* add the compiler plugin that converts the `LocalizedTextProvider` interfaces into multi-language
* add the `i18n` module to your backend (this is part of `boot`, so it might be already added)
* add languages
  * on the web interface
  * directly to SQL
* and translations
  * export the key/default list, translate, load the whole list back
  * use the web interface to add the translations manually, one-by-one 

## Textual Localization

All textual information that may be localized in the system has a unique **key**.

The key is composed of two parts: a **namespace** and a **name**.

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
interface MyStrings : LocalizedTextProvider {
    val myLabel get() = "Send Email"
    val myMessage get() = quick("%N email(s) sent")
    val myEmail get() = rich("Dear {name}! I wish you happy nth{age} birthday! Best Regards, {sender}")
}
```

