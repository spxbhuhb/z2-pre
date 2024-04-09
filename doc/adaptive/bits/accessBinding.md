# Access Binding

`AdaptiveAccessBinding` parameters paired with selector functions are transformed by the compiler
plugin to provide metadata about the accessed state variable.

The accessor function may decide what to do based on the metadata. This makes it possible to
use the proper view for a state variable automatically.

```kotlin
fun Adaptive.example() {
    val a = 12
    accessor { a }
}

fun <T> Adaptive.accessor(
    binding: AdaptiveAccessBinding<T>? = null,
    selector: () -> T
) {
    checkNotNull(binding)
    if (binding.metadata.type == "kotlin.Int") {
        T1(binding.value as Int)
    }
}
```