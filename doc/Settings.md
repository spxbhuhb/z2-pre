# Setting

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