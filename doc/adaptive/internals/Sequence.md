# Sequence

A sequence of fragments like:

## Examples and Considerations

### Basic

* The sequence is at `si:0`.
* All children are made by calling `build` of the sequence's parent (`test`).
* Closure is the same for all fragments.

```kotlin
fun Adaptive.test() { // si:0
    T1(1) // si: 1
    T1(2) // si: 2
}
```

### Lower order call

* the `test.lowerFun` external state variable will be an `AdaptiveFragmentFactory`
* `test.build()` uses `lowerFun.build()` to create the fragment at `si: 2`
* closure of the fragment at `si: 2` is set to `AdaptiveFragmentFactory.closure`

```kotlin
fun Adaptive.test(lowerFun : Adaptive.() -> Unit) { // si:0
    T1(1) // si: 1
    lowerFun() // si: 2
    T1(2) // si: 3
}
```

### Higher order call

* `higherFun` is a higher order component with a state variable that is an `AdaptiveFragmentFactory`
* `higherFun` uses the fragment factory to create components when the lower order function is called
* `test.build`:
  * creates an instance of `AdaptiveHigherFun`
  * calls `create` of the created instance, `create` 
      * calls `externalPatch` - sets the fragment factory state variable
      * calls `internalPatch`
      * calls `create` of the contained fragment

```kotlin
fun Adaptive.test() {
    T1(1)
    higherFun {
        T1(2)
        T1(3)
    }
    T1(4)
}
```


