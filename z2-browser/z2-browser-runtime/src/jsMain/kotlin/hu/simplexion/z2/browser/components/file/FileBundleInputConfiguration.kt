package hu.simplexion.z2.browser.components.file

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.html.Z2

class FileBundleInputConfiguration<FT,DT>(
    val folders : List<FT>,
    val types : List<DT>
) {

    var folderRenderFun: Z2.(value: FT) -> Unit = { + it.toString() }
    var typeRenderFun: Z2.(value: DT) -> Unit = { + it.toString() }

    var folderLabel = browserStrings.folder
    var typeLabel = browserStrings.type
    var bundleNameLabel = browserStrings.title
    var bundleNameSupport = browserStrings.bundleNameSupport
    var noFileSelected = browserStrings.noFilesSelected
    var fileAlreadyAddedLabel = browserStrings.fileAlreadyAdded
    var filesSelectedLabel = browserStrings.filesSelected
    var dropFileHereLabel = browserStrings.dropFileHere
    var dropAttachmentHereLabel = browserStrings.dropAttachmentHere
    var file = browserStrings.file
    var attachments = browserStrings.attachments
    var selectMainFirst = browserStrings.selectMainFirst
}