package hu.simplexion.z2.browser.components.file

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.Z2

class FileBundleBuilderConfiguration<T>(
    val types : List<T>
) {

    var typeRenderFun: Z2.(value: T) -> Unit = { it.toString() }

    var nameValidationFun: ((field : ValueField<String>) -> Boolean)? = null

    var typeLabel = browserStrings.type
    var bundleNameLabel = browserStrings.title
    var bundleNameSupport = browserStrings.bundleNameSupport
    var noFileSelected = browserStrings.noFilesSelected
    var fileAlreadyAddedLabel = browserStrings.fileAlreadyAdded
    var filesSelectedLabel = browserStrings.filesSelected
    var dropFilesHereLabel = browserStrings.dropFilesHere

}