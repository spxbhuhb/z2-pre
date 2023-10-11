# Z2 Commons

[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.z2/z2-commons)](https://mvnrepository.com/artifact/hu.simplexion.z2/z2-commons)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Common utility functions for Kotlin Multiplatform programming.

**NOTE** The library is under active development, the non-snapshot release on Maven Central is probably lagging behind. 
This documentation covers the latest SNAPSHOT release.

Status: **experimental**

## Dependency

```kotlin
implementation("hu.simplexion.z2:z2-commons:2023.7.28")
```

## Packages

| Name                  | Function                                  |
|-----------------------|-------------------------------------------|
| [i18n](#i18n)         | Internationalization primitives.          |
| [protobuf](#protobuf) | Build and parse Protocol Buffer messages. |
| [util](#utility)      | Miscellenaous utility functions.          |

## Protobuf

Functions for [Protocol Buffer](https://protobuf.dev/) messages.

* focus on Kotlin data types
* only proto version 3
* not a full implementation

| Type  | Name                                                                                                    | Function           | Platform |
|-------|---------------------------------------------------------------------------------------------------------|--------------------|----------|
| class | [`ProtoMessage`](src/commonMain/kotlin/hu/simplexion/z2/commons/protobuf/ProtoMessage.kt)               | Parse wire format. | Common   |
| class | [`ProtoMessageBuilder`](src/commonMain/kotlin/hu/simplexion/z2/commons/protobuf/ProtoMessageBuilder.kt) | Build wire format. | Common   |

## Utility

| Type | Name                                                                                     | Function                                                              | Platform |
|------|------------------------------------------------------------------------------------------|-----------------------------------------------------------------------|----------|
| fun  | [`ByteArray.toDotString`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/string.kt) | Convert bytes to a "dot-string".                                      | Common   |
| fun  | [`ByteArray.toUuid`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/uuid.kt)        | Convert bytes to UUID.                                                | Common   |
| fun  | [`ByteArray.toLong`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/number.kt)      | Convert bytes to Long.                                                | Common   |
| fun  | [`Int.toByteArray`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/number.kt)       | Get a ByteArray that contains the Int.                                | Common   |
| fun  | [`Int.encodeInto`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/number.kt)        | Encode an Int into a ByteArray at a given offset.                     | Common   |
| fun  | [`fourRandomInt`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/random.kt)         | Get 4 random Int values.                                              | Js, JVM  |
| fun  | [`hereAndNow`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/coroutines.kt)        | `Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())` | Common   |
| fun   | [`localLaunch`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/coroutines.kt)       | Create a scope with `Dispatchers.Default` and launch the block in it. | Common   |
| fun   | [`Long.toByteArray`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/number.kt)      | Get a ByteArray that contains the long.                               | Common   |
| fun   | [`Long.encodeInto`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/number.kt)       | Encode a Long into a ByteArray at a given offset.                     | Common   |
| class | [`UUID`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/uuid.kt)                    | Type bound UUID 4 implementation                                      | Js, JVM  |
| fun   | [`vmNowMicro`](src/commonMain/kotlin/hu/simplexion/z2/commons/util/clock.kt)             | Virtual machine time in microseconds.                                 | Js, JVM  |

## I18N

* [Commons](https://github.com/spxbhuhb/z2-commons) is enough for single-language applications
* [I18N](https://github.com/spxbhuhb/z2-i18n) is intended for multi-language applications

For text localization define objects that extend `LocalizedTextStore`:

```kotlin
object loginStrings : LocalizedTextStore(UUID("7f56e8dd-8bf0-49e7-a567-eb81adc501ed")) {
    val account by "Account"
}

fun Z2.loginForm() =
    div {
        textField(label = loginStrings.account)
    }
```

| Class/Interface      | Description                                                          |
|----------------------|----------------------------------------------------------------------|
| `LocalizedText`      | Textual information that should be translated to the current locale. |
| `LocalizedIcon`      | Name of an icon that should be converted to the current locale.      |
| `LocalizedTextStore` | A text store that contains `LocalizedText` instances.                |
| `LocalizedIconStore` | An icon store that contains `LocalizedIcon` instances.               |

## License

> Copyright (c) 2020-2023 Simplexion Kft, Hungary and contributors
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this work except in compliance with the License.
> You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.