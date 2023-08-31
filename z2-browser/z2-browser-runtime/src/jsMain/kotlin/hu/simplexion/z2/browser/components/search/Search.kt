package hu.simplexion.z2.browser.components.search

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.util.localLaunch
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

class Search<T>(
    parent: Z2,
    val query: suspend (text: String) -> List<T>,
    val hint : LocalizedText? = null,
    val onSelect: (value: T) -> Unit
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    arrayOf(w400, h46, positionRelative)
) {
    var revision = 0
    var running = false
    var noItems = false
    var items = emptyList<T>()

    lateinit var input : HTMLInputElement

    lateinit var search: Z2
    lateinit var itemContainer: Z2
    lateinit var feedback: Z2

    init {
        build()
    }

    /**
     * Search uses a placeholder and an actual element. The placeholder element just reserves the place for the
     * actual element. I've chosen this way to make the rendering of the different version easier while not
     * affecting the normal document flow.
     *
     * TODO in Safari the border radius is not properly handled when there are hits
     */
    fun build() {
        div(positionAbsolute, elevationLevel3, borderRadius28, surfaceContainerLowest, p0, overflowHidden) {

            search = div(displayGrid, w400, h46, pt0, pb0, pl16, pr16) {
                gridTemplateColumns = "min-content minmax(0,1fr) min-content"

                icon(basicIcons.search).apply { addClass(alignSelfCenter, onSurfaceVariantText)}

                input(pl16, pr16, outlineNone, h46, bodyMedium, backgroundTransparent, b0) {
                    input = this.htmlElement as HTMLInputElement
                    hint?.let { input.placeholder = it.toString() }
                    onInput {
                        runQuery(input.value)
                    }
                }

                icon(basicIcons.cancel).apply {
                    addClass(alignSelfCenter, onSurfaceVariantText)
                    onClick {
                        input.value = ""
                        runQuery("")
                    }
                }
            }

            itemContainer = div(wFull, boxSizingBorderBox) {

            }

            feedback = div(displayNone, h46, alignItemsCenter, justifyContentCenter, wFull, boxSizingBorderBox, borderTopOutline) {

            }
        }
    }

    fun updateView() {
        when {
            running -> {
                feedback.clear()
                feedback.removeClass(displayNone)
                feedback.addClass(displayFlex)
                feedback.apply { text { basicStrings.searchInProgress } }
            }
            noItems -> {
                feedback.clear()
                feedback.removeClass(displayNone)
                feedback.addClass(displayFlex)
                feedback.apply { text { basicStrings.noHits }}
            }
            else -> {
                feedback.addClass(displayNone)
                feedback.removeClass(displayFlex)
            }
        }

        itemContainer.clear()

        if (items.isNotEmpty()) {
            itemContainer.apply {
                for (item in items) {
                    div("search-item") {
                        div {
                            text { item }
                        }
                        onClick {
                            input.value = item.toString()
                            items = emptyList()
                            running = false
                            noItems = false
                            updateView()
                            onSelect(item)
                        }
                    }
                }
            }
        }
    }

    fun runQuery(value: String) {
        val inputRevision = ++ revision

        if (value.isEmpty()) {
            running = false
            noItems = false
            items = emptyList()
            updateView()
            return
        }

        running = true
        updateView()

        localLaunch {
            delay(300)
            items = query(value)
            if (inputRevision == revision) {
                running = false
                noItems = items.isEmpty()
                updateView()
            }
        }
    }
}