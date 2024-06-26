## Adapters

Each entry point has an adapter instance which implements the
[AdaptiveAdapter](/z2-core/src/commonMain/kotlin/hu/simplexion/z2/adaptive/AdaptiveAdapter.kt)
interface. These instances act as the starting point for the binding between the components and the underlying UI.

Adapters define the type [bridge](#bridge) components use. For browsers, it can be a DOM Node, for JVM
it might be Android View.

## Bridge

The bridge connects Adaptive fragments with their representation in the underlying
UI. Low-level fragments (those that directly interact with the actual UI)
typically implement the `AdaptiveBridge` interface and transform their internal
state into an actual UI state.

The `BT` type parameter of the bridge is a type in the underlying UI, typically
ancestor of all UI elements, such as `Node` in HTML or `View` in Android.

`mount` and `unmount` functions get the bridge of the parent fragment
and use `add` and `remove` methods to add and remove themselves. Some bridges
also implement the `replace` method which makes it possible to replace a
fragment with another in place. This is used by `if` and `when`.

### Bridge Dependent And bridge Independent Fragments

A bridge independent fragment is one that does not depend on the actual type
its bridge uses. On the other hand, a bridge dependent fragment is one
that uses the bridge in some very specific manner.

For example `AdaptiveSequence` is a bridge independent fragment. It does not care
about what goes on, it just has a few children, and they will handle the
bridging themselves.

`Text` fragments are typically bridge dependent because each platform has its
own way to add constant text to the UI. In browsers for example you use
`document.createTextNode`.

Bridge independent fragments use type parameter for the bridge receiver type:

```kotlin
open class AdaptiveSequence<BT>(
    override val adapter: AdaptiveAdapter<BT>,
    /* ... */
) : AdaptiveFragment<BT> {
    /* ... */
}
```

Fragments generated by the plugin are bridge independent, thus usable with any
kind of adapter/bridge.

## Placeholders

A placeholder is an anchor the fragment uses to add/remove its children. We cannot
just add and then replace fragments because it is possible that the selected
fragment is a block or a loop. Those add an unknown number of children, thus simple
replace is impossible. For browsers the placeholder may be a simple `Node`
(Svelte uses a `Text`), for Android an actual Placeholder view exists.

Placeholders are created by the `AdaptiveAdapter.createPlaceholder` function.