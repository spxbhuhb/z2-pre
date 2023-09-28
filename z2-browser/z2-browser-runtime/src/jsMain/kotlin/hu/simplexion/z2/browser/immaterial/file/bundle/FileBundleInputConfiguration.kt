package hu.simplexion.z2.browser.immaterial.file.bundle

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.html.Z2

class FileBundleInputConfiguration<FT,DT>(
    val folders : List<FT>,
    val types : Map<FT,List<DT>>,
    val acceptedExtensions : List<String>? = null,
    val sizeLimit : Long = Long.MAX_VALUE
) {

    var showFolderAndTypeSelect : Boolean = true

    var folderTextFun: (value : FT) -> String = { it.toString() }
    var typeRenderFun: Z2.(value: DT) -> Unit ={ it.toString() }

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
    var selectFolderFirst = browserStrings.selectFolderFirst
    var invalidExtension = browserStrings.invalidExtension
    var sizeOverLimit = browserStrings.sizeOverLimit
}