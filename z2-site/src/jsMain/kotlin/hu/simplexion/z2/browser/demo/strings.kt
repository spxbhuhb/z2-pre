package hu.simplexion.z2.browser.demo

import hu.simplexion.z2.auth.ui.IAuthStrings
import hu.simplexion.z2.commons.localization.text.LocalizedTextProvider
import hu.simplexion.z2.localization.ui.ILocalizationStrings

object strings : LocalizedTextProvider, IAuthStrings, ILocalizationStrings {
    val select = static("Select")
    val components = static("Components")
    val pages = static("Pages")
    val button = static("Button")
    val filledButton = static("Filled Button")
    val calendar = static("Calendar")
    val textButton = static("Text Button")
    val smallDenseTextButton = static("Small Dense Text Button")
    val settings = static("Settings")
    val segment1 = static("Segment 1")
    val segment2 = static("Segment 2")
    val segment3 = static("Segment 3")
    val textField = static("Text Field")
    val label = static("Label")
    val supportingText = static("Supporting Text")
    val modal = static("Modal")
    val confirmDialog = static("Confirm Dialog")
    val confirmMessage = static("Nothing happens, whichever button you click.")
    val card = static("Card")
    val loremShort = static("Lorem ipsum dolor sit amet, consectetur adipiscing elit")
    val lorem =
        static("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
    val headline = static("Headline")
    val popup = static("Popup")
    val radioButton = static("Radio Button")
    val menu = static("Menu")
    val menuItem1 = static("Menu Item 1")
    val menuItem2 = static("Menu Item 2")
    val snackbar = static("Snackbar")
    val click = static("Click")
    val switch = static("Switch")
    val table = static("Table")
    val container = static("Container")
    val datepicker = static("Date Picker")
    val navigationDrawer = static("Navigation Drawer")
    val inbox = static("Inbox")
    val outbox = static("Outbox")
    val favourites = static("Favourites")
    val trash = static("Trash")
    val applicationTitle = static("Z2 Browser Demo")
    val main = static("Main Page")
    val pageNotFound = static("Page not found.")
    val tableTitle = static("Table Title")
    val headerA = static("Header A")
    val headerB = static("Header B")
    val other = static("Other")
    val form = static("Form")
    val dump = static("Dump")
    val setProgrammatically = static("Set Programmatically")
    val item1 = static("item 1")
    val item2 = static("item 2")
    val item3 = static("item 3")
    val search = static("Search")
    val file = static("File")
    val selectedFiles = static("Selected Files")
    val fileSelectDialog = static("File Select Dialog")
    val upload = static("Upload")
    val touch = static("Touch")
    val timepicker = static("Time Picker")
    val administration = static("Administration")

    val interfaces = static("Interfaces", "Manage connections to other systems, such as e-mail or database servers.")
    val connection = static("Connections", "Manage connections to other systems, such as e-mail or database servers.")
    val impressum = static("Impressum", "Privacy, terms, etc.")
    val template = static("Templates", "Templates for outgoing e-mails, generated documents.")
    val history = static("Histories", "Security, technical and business level event histories.")

    val routing = static("Routing")
    val content = static("Content")
    val subRoute = static("Sub-Route")
    val parameter = static("Parameter")
    val parameterSubRoute = static("Parameter Sub-Route")

    val loginSupport = static("Z2 Browser Components Demo")
    val password = static("Password")
    val forgottenPassword = static("Forgot password?")
    val help = static("Help")
    val privacy = static("Privacy")
    val term = static("Terms")
    val english = static("English")
    val registration = static("Create account")
}