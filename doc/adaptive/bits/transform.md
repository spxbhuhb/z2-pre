# Transform

Transform functions change the state of a fragment after it is created but before it is mounted.

They simply make code easier to read.

```kotlin
fun Adaptive.example() {
    val a = 12
    subject() readOnly true 
}

interface SubjectState : AdaptiveState {
    infix fun readonly(value : Boolean) {
        setStateVariable(0, value)
        patch()
    }
}

fun Adaptive.subject() : SubjectState {
    return thisState()
}
```