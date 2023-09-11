package hu.simplexion.z2.browser.components.search

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.stateLayer
import hu.simplexion.z2.commons.util.localLaunch
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

class Search<T>(
    parent: Z2,
    val configuration: SearchConfiguration<T>
) : Z2(
    parent,
    document.createElement("div") as HTMLElement,
    arrayOf(wFull, h46, positionRelative)
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
        div(wFull, positionAbsolute, elevationLevel3, borderRadius28, surfaceContainerLowest, p0, overflowHidden) {

            search = div(displayGrid, h46, pt0, pb0, pl16, pr16) {
                gridTemplateColumns = "min-content minmax(0,1fr) min-content"

                icon(browserIcons.search).apply { addClass(alignSelfCenter, onSurfaceVariantText)}

                input(pl16, pr16, outlineNone, h46, bodyMedium, backgroundTransparent, borderNone) {
                    input = this.htmlElement as HTMLInputElement
                    configuration.hint?.let { input.placeholder = it.toString() }
                    onInput {
                        runQuery(input.value)
                    }

                    onFocus {
                        this@Search.zIndex = 100
                    }

                    onBlur {
                        this@Search.zIndex = 0
                        running = false
                        noItems = false
                        items = emptyList()
                        updateView()
                    }
                }

                icon(browserIcons.cancel).apply {
                    addClass(alignSelfCenter, onSurfaceVariantText)
                    onClick {
                        input.value = ""
                        runQuery("")
                    }
                }
            }

            itemContainer = div(wFull, boxSizingBorderBox) {
                onMouseDown {
                    it.preventDefault()
                }
            }

            feedback = div(displayNone, h46, alignItemsCenter, justifyContentCenter, wFull, boxSizingBorderBox, borderTopOutlineVariant) {

            }

        }
    }

    fun updateView() {
        when {
            running -> {
                feedback.clear()
                feedback.removeClass(displayNone)
                feedback.addClass(displayFlex)
                feedback.apply { text { browserStrings.searchInProgress } }
            }
            noItems -> {
                feedback.clear()
                feedback.removeClass(displayNone)
                feedback.addClass(displayFlex)
                feedback.apply { text { browserStrings.noHits }}
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
                    div(displayFlex, h46, boxSizingBorderBox, borderTopOutlineVariant, alignItemsCenter, positionRelative) {
                        stateLayer()
                        div(pl24, pr16, boxSizingBorderBox) {
                            configuration.itemRenderFun(this, item)
                        }
                        onClick {
                            println(configuration.itemTextFun(item))
                            input.value = configuration.itemTextFun(item)
                            items = emptyList()
                            running = false
                            noItems = false
                            updateView()
                            configuration.selectFun(item)
                        }
                    }
                }
            }
        }
    }

    fun runQuery(value: String) {
        configuration.filterChangeFun(value)

        val inputRevision = ++ revision

        if (value.isEmpty() || value.length < configuration.minimumFilterLength) {
            running = false
            noItems = false
            items = emptyList()
            updateView()
            return
        }

        running = true
        updateView()

        localLaunch {
            items = configuration.queryFun(value)
            if (inputRevision == revision) {
                running = false
                noItems = items.isEmpty()
                updateView()
            }
        }
    }
}