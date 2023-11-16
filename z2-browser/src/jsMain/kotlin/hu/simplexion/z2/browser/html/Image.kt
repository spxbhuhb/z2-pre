package hu.simplexion.z2.browser.html

import hu.simplexion.z2.browser.css.CssClass
import kotlinx.browser.document
import org.w3c.dom.HTMLImageElement

class Image(
   parent : Z2? = null,
   classes : Array<out CssClass>,
   builder: Image.() -> Unit
) : Z2(
   parent,
   document.createElement("img") as HTMLImageElement,
   classes
) {
   val imageElement = htmlElement as HTMLImageElement

   var src
      get() = imageElement.src
      set(value) { imageElement.src = value }

   init {
       builder()
   }
}