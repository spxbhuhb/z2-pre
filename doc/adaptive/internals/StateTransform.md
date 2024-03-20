# State Access Transform

`StateAccessTransform` transforms state variable accesses from the original code into property
access in the generated IrClass.

In general, all variable reads and writes are transformed into property getter/setter calls.

## General transform

| From                                                       | To                                              |
|------------------------------------------------------------|-------------------------------------------------|
| original function parameter reads (IrGetValue)             | state variable getter calls (declaration scope) |
| top level local variable reads (IrCall to the getter)      | state variable getter calls (declaration scope) |
| top level local variable writes (IrCall to the setter)     | state variable setter calls (declaration scope) |
| anonymous function parameter reads (IrGetValue)            | state variable getter calls (anonymous scopes)  |
| anonymous function parameter writes (IrCall to the setter) | state variable setter calls (declaration scope) |

## Local function transform

In addition to the general transform, local functions that modify state variables
are extended with dirty mask update and patch call.

- calls `adaptiveInvalidate` to invalidate the variable
- calls `adaptivePatch` to execute patch when necessary

## Closure

```kotlin
fun start(s1: Int) {       // declaration scope
    var s2: Int
    ho(s1) { p1 ->         // anonymous scope 1
        ho(s2) { p2 ->     // anonymous scope 2
            t1(p1 + p2)
        }
    }
}

fun ho(ho1: Int, @Adaptive func: (pc: Int) -> Unit) {
    var ho2: Int
}
```

External patch of `t1` needs the closure to access the values as `p1` is of `anonymous scope 1` and `p2` is 
of `anonymous scope 2`.

This is not a problem as external patch gets the fragment itself which has the closure.

Lambda functions are a bit more tricky. These are stored as state variables and passed to the component during
`adaptiveBuild` or `adaptiveExternalPatch`. 

```kotlin
fun start(s1: Int) {       // declaration scope
    var s2: Int
    ho(s1) { p1 ->         // anonymous scope 1
        ho(s2) { p2 ->     // anonymous scope 2
            ts { v ->
                s2 = s1 + p1 + p2 + v
            }
        }
    }
}
```


