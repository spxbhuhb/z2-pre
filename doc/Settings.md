# Settings

Settings are data items used to customize application mechanics. Examples:

| Owner     | Path                | Value                                |
|-----------|---------------------|--------------------------------------|
|           | `JDBC_DATABASE_URL` | `jdbc:postgresql://127.0.0.1/z2site` |
|           | `JDBC_PASSWORD`     | `SGVsbG8gV29ybGQ=`                   |
| account-1 | `ENTRIES_PER_PAGE`  | `50`                                 |
| account-2 | `ENTRIES_PER_PAGE`  | `20`                                 |

Owner of the setting is the UUID of the user account the settings belongs to.

To use settings in your application declare a variable with the `setting` function:

```kotlin
val emailUser by setting()
```

There are many ways to pass the value of this setting to the application.

Through environment variable:

```shell
export Z2_EMAIL_USER=jdbc:postgresql://127.0.0.1/z2site
```

Through a Java property file:

```properties
email.user=jdbc:postgresql://127.0.0.1/z2site
```

In a database:

```sql
insert into z2_settings(null, 'EMAIL_USER', 'a@b.com', false)
```

## Encoded Settings

You may want to encode security-related settings, so they are not stored in plain text:

```kotlin
val emailPassword by setting { secret = "Something-Something" }
```

Then you specify a secret, the given setting is encoded with AES-256 encoding with the secret being the key. You have of
course protect the secret, and it is not good practice to hard-code it.

You can for example pass it in an environment variable. The code below gets `settingsSecret` from environment variables
and `emailPassword` from any setting source available.

```kotlin
val settingSecret by setting { sources = anyOf(ENVIRONMENT) }
val emailPassword by setting { secret = settingSecret }
```

## Setting Implementations

Settings of the application may come from many sources: program parameters, environment variables, databases etc.

To support this variety the settings module provides different implementations of `SettingApi`:

- `EnvironmentSettingImpl` - settings from environment variables
- `PropertiesSettingImpl` - settings from Java property files
- `SqlSettingImpl` - settings from SQL databases
- `ServerSettingImpl` - settings from a server
- `EmbeededSettingImpl` - settings hard-coded in the program
- `DelegatingSettingImpl` - settings from other setting implementations

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