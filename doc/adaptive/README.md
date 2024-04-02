Adaptive is a UI framework for Kotlin, inspired by [Svelte](https://svelte.dev).

The concept is fundamentally different from Compose and React:

* there is no remember and there are no hooks
* state update is automatic for most use cases
* most of the code runs only once
* the components are stored in a tree (similar to DOM)
* updates are applied only where it is strictly necessary
* the state of the components is 100% visible from user code

```kotlin
fun Z3.hello() =
    div(paddigLeft10) {
        text { "Hello World" }
    }
```

Adaptive is platform antagonistic. As of know we work mostly on the browser implementation, but there is no reason
why there couldn't be one for whatever platform. Actually, the test implementation is pure Kotlin, it can be run on
any platforms.

## Basics

* [Getting Started](./GettingStarted.md)

## Integration With The Underlying UI

Adapters and bridges

## How It Works

Well, the concept is really simple but the details are really devious. :)

* When you write a function that extends `Adaptive` the compiler plugin creates a class for that function.
* That class contains the code necessary to build the UI and update it whenever something changes.

There are a number of [test cases](/z2-core/src/commonTest/kotlin/hu/simplexion/z2/adaptive/test/manual) which
have classes very similar to the one the plugin creates.

For more details you can check the [internals](/doc/adaptive/internals).

## Plugin Development

* [Internals](./README.md)
* [Troubleshooting](./Troubleshooting.md)