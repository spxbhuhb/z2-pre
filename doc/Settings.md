# Settings

## Add Setting Sources

Backend - at the very beginning of application startup (preferably in `main`):

```kotlin
// get settings from environment variables that start with Z2_
settingsFromEnvironment { "Z2_" } 

// get settings from the ../app/etc/z2.properties file
settingsFromPropertyFile { "../app/etc/z2.properties" }

// connect to the database, JDBC connection settings are loaded from:
// - the property file, or if the property file does not contain them
// - the environment variables
JdbcSettings().connect() 

// get settings from the "z2_settings" SQL table
settingsFromSqlTable { settingTable }
```

Frontend - at the very beginning of application startup (preferably in `main`):

```kotlin
// get the settings from the server, this works for browsers in itself but for
// desktop and mobile you need to load the server connection parameters from 
// another setting source
settingsFromServer {  }
```

## Use Settings

```kotlin
// once you added sources are ready use the `setting` function
val jdbcUrl by setting<String> { "DB_JDBC_URL" }
```

Settings are read **on-demand**. This means that you may define the settings at top level without worrying about class load
order. However, you should read them only when the appropriate setting source is ready.

Supported types **JVM**:

- Boolean
- Integer
- Path
- String

Supported types **Browser**:

- none at the moment (I should finish it)

## Setting Sources

### Environment Variables

```shell
export Z2_EMAIL_USER=a@b.com
```

```kotlin
// get settings from environment variables that start with Z2_
settingsFromEnvironment { "Z2_" }

val emailUser by settings<String> { "EMAIL_USER" }
```

### Property Files

```properties
EMAIL_USER=a@b.com
```

```kotlin
// get settings from the ../app/etc/z2.properties file
settingsFromPropertyFile { "../app/etc/z2.properties" }

val emailUser by settings<String> { "EMAIL_USER" }
```

### SQL Table

```sql
insert into z2_settings('065ca04c-cd6d-7775-8000-5fa6142b4b7b', 'EMAIL_USER', 'a@b.com')
```

## Setting Providers

Settings of the application may come from many sources: program parameters, environment variables, databases etc.

To support this variety the settings module provides different providers:

- `EnvironmentSettingProvider` - settings from environment variables
- `PropertiesSettingProvider` - settings from Java property files
- `SqlSettingProvider` - settings from SQL databases
- `ServerSettingProvider` - settings from a server
- `DelegatingSettingProvider` - settings from other setting implementations

# Old Stuff

Store (owner, path, value) triples in the `setting` SQL table.

API: [SettingApi](../src/commonMain/kotlin/hu/simplexion/z2/setting/api/SettingApi.kt)

* The owner of the setting is an principal.
* Each owner may get and change his/her own settings only, except security officers.
* Security officers may get and change any settings.
* If a non security officer owner tries to get or set a setting of another owner, the service throws an `AccessDenied` exception.
* For system settings use `UUID.nil`.

## Path

The setting path is like a URL or a file system path: `a/b/c`.

The `children` parameter of `SettingApi.get` instructs the service to fetch the children of
the given path as well.

| Path    | Value   |
|---------|---------|
| `a`     | `1`     |
| `a\b`   | `1.1`   |
| `a\b\c` | `1.1.1` |
| `ab`    | `2`     |
| `b`     | `3`     |
| `b\c`   | `3.1`   |

`services.get(owner, "a", true)` returns with:

```text
Setting("a", "1")
Setting("a\b", "1.1")
Setting("a\b\c", "1.1.1")
```

`services.get(owner, "a", false)` returns with:

```text
Setting("a", "1")
```

## Schematics

Schematic classes may be saved directly. 