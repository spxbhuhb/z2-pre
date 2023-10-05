[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.z2/z2-boot)](https://mvnrepository.com/artifact/hu.simplexion.z2/z2-boot)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Libraries for Kotlin Multiplatform (mostly browser + JVM backend) development.

Status: **initial development**

**====  Important ====**

**Z2 is under its initial development. Links may be broken, parts may be missing.**

## Modules

| Library                   | Purpose                                                                 |
|---------------------------|-------------------------------------------------------------------------|
| [Commons](z2-commons)     | Common functions and data structures. Localization.                     |
| [Service](z2-service)     | Client-Server communication with simple function calls.                 |
| [Schematic](z2-schematic) | Data schemas for UI building, validation, communication, user feedback. |
| [Browser](z2-browser)     | Material 3 and other components for web browsers.                       |
| [Module](z2-module)       | Fully functional application modules (UI + Backend, SQL with Exposed)   |

## Gradle Dependency

You can use many of the modules independently, check the module documentation for details.

That said, the easiest way is to depend on `z2-boot` which contains everything.

`settings.gradle.kts`

```kotlin
pluginManagement {
    includeBuild("../z2-schematic/z2-schematic-gradle-plugin")
    includeBuild("../z2-service/z2-service-gradle-plugin")
}
```

`build.gradle.kts`

```kotlin
plugins {
    kotlin("multiplatform") version "1.9.10"
    id("hu.simplexion.z2.schematic") version "<schematic-plugin-version>"
    id("hu.simplexion.z2.service") version "<service-plugin-version>"
}
```

```kotlin
sourceSets["commonMain"].dependencies {
    implementation("hu.simplexion.z2:z2-boot:$z2_boot_version")
}
```

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
