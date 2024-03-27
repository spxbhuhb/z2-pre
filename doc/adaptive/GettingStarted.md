**DISCLAIMER**

Adaptive is in a **proof-of-concept** phase. The examples below should work
but changes may result in horrendous compilation error messages. Here there be monsters!

To get started with Adaptive you need a KMP project. Create a new multiplatform project with IDEA and
add the dependencies as described in the [Z2 README](/README.md).

Start experimenting with Adaptive by editing `src/jsMain/kotlin/main.kt`:

```kotlin
import hu.simplexion.z2.adaptive.dom.RuiDOMAdapter
import hu.simplexion.z2.adaptive.dom.html.Button
import hu.simplexion.z2.adaptive.dom.html.Text
import hu.simplexion.z2.adaptive.adaptive

fun main() {
    adaptive {
        var counter = 0
        filledButton { "Counter: $counter" } onClick { counter++ }
        text { "You've clicked $counter times." }
    }
}
```

## Basics: Components

The basic building blocks of an Adaptive application are components. The
following code defines the "helloWorld" component which simply displays the
text "Hello World!".

```kotlin
fun Adaptive.helloWorld() {
    text { "Hello World!" }
}
```

You can try and add this component to the `main` function.

**IMPORTANT** You have to add `HelloWorld` under `var counter = 0`, more about that below.

```kotlin
fun main() {
    adaptive {
        var counter = 0
        helloWorld()
        filledButton { "Counter: $counter" } onClick { counter++ }
        text { "You've clicked $counter times." }
    }
}
```

## Basics: State

Each component has a *state*. When this state changes the component automatically
updates the UI to reflect the change.

Defined variables are part of the component state, and they are *reactive by default*.
We call these *internal state variables*.

```kotlin
fun Adaptive.counter() {
    var counter = 0
    filledButton { "Click count: $counter" } onClick { counter++ }
}
```

The state of this component consists of the `counter` variable. Whenever you
click on the button, the `counter` is incremented. The component realizes that
the `counter` has been changed and updates the UI.

## Basics: Parameters

You can add parameters to the component. Parameters are parts of the
component state. We call these *external state variables*.

You cannot change the external state variables from the inside of
the component. However, it may happen that the parameter changes
on the outside. In that case the component updates the UI automatically.

```kotlin
fun Adaptive.counter(label: String) {
    var counter = 0
    filledButton { "$label: $counter" } onClick { counter++ }
}
```

## Basics: Boundary

Adaptive components have two main areas: *state initialization* and *rendering*.
These are separated by the *boundary*.

Above the boundary, you initialize the component state. This is a one-time
operation, executed when the component is initialized.

Below the boundary, you define how to render the component. This part
is executed whenever the state changes.

**Very important** you cannot define variables, functions etc. in the
*rendering* (except in event handlers, see later). This is a design decision we've
made to avoid confusion. The compiler will report an error if you try to do so.

Adaptive automatically finds the *boundary*: the first call to another Adaptive component
function marks the *boundary*.

```kotlin
fun Adaptive.counter() {
    var counter = 0
    // ---- boundary ----
    filledButton { "Click count: $counter" } onClick { counter++ }
}
```

## Basics: Support Functions

*Support functions* are functions called when something happens: user input,
completion of a launched co-routine etc.

Support functions may change state variables and these changes result in a UI update.

You can define support functions as local functions or as lambdas. Adaptive recognizes
when a block changes a state variable and automatically updates the UI.

The handlers in this example are equivalent. Note that whichever button you
click, labels of all show the new counter value.

```kotlin
fun Adaptive.counter() {
    var counter = 0

    fun increment() {
        counter++
    }

    filledButton { "Click count: $counter" } onClick ::increment
    filledButton { "Click count: $counter" } onClick { counter++ }
}
```

## Basics: Higher Order Components

Components may contain other components, letting you build complex UI
structures. When `counter` of the parent component changes the child
automatically updates the child component.

```kotlin
fun Adaptive.child(counter: Int) {
    text { "Click count: $counter" }
}

fun Adaptive.parent() {
    var counter = 0
    child(counter) onClick { counter++ }
}
```

## Basics: Conditions

You can use the standard Kotlin `if` and `when` to decide what to display.

```kotlin
fun Adaptive.counter() {
    var count = 0
    filledButton { "click count: $count" } onClick { count++ }
    if (count < 3) {
        text { "(click count is less than 3)" }
    }
}
```

```kotlin
fun Adaptive.counter() {
    var count = 0
    
    when (count) {
        1 -> text { "click count: 1" }
        2 -> text { "click count: 2" }
        else -> text { "click count > 2" }
    }
    
    filledButton { "click to increment" } onClick { count++ }
}
```

## Basics: For Loops

```kotlin
fun Adaptive.list() {
    for (i in 0..3) {
        text { "list item: $i" }
    }
}
```