package hu.simplexion.z2.browser.components.file

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.commons.i18n.LocalizedText

class FileBundleBuilderConfiguration<T>(
    val types : List<T>,
    val typeLabel : LocalizedText = browserStrings.type,
    val bundleNameLabel: LocalizedText = browserStrings.title,
    val bundleNameSupport: LocalizedText = browserStrings.bundleNameSupport,
    val noFileSelected : LocalizedText = browserStrings.noFilesSelected,
    val fileAlreadyAddedLabel : LocalizedText = browserStrings.fileAlreadyAdded,
    val filesSelectedLabel : LocalizedText = browserStrings.filesSelected,
    val dropFilesHereLabel : LocalizedText = browserStrings.dropFilesHere,
)