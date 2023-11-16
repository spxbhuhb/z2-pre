# CSS

Z2 provides the following ways to add styles to your components:

* style attribute of an element
* classic styles
* shorthands

## Style Attribute

The most specific way to add styles to one specific element. Sets the `style` attribute of the
HTML element directly.

```kotlin
fun Z2.test() {
    style.width = 400.px
}
```

## Classic Styles

Add, remove and toggle styles defined in CSS files like below. Adds, removes or toggles the `class` 
attribute of the HTML element directly. You have to write the CSS file manually and add it to
your distribution.

```kotlin
fun Z2.test() {
    addClass("red-background", "red-border")
    onClick {
        toggleClass("red-background")
        removeClass("red-border")
    }
}
```

## Shorthands

Shorthands let you add detailed styling to your component without defining CSS classes.
Each shorthand is a one-line CSS class which sets one CSS parameter to one specific value.

For example `pt24` is the shorthand for `.pt24 { padding-top: 24px }`.

To use shorthands simply pass them to the builder functions in your code like this:

```kotlin
div(pt24, borderOutline, borderRadius8) {
    
}
```

Shorthands cannot be removed. If you want to add, remove or toggle styles dynamically use classic
styling.

The reason for this is that the compiler optimizes these shorthands. In the final code an optimized
version of the `div` function is called where all these shorthand styles are passed as one static
string. This may sound like unnecessary magic, but the actual code size reduction is considerable.