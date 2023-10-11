package hu.simplexion.z2.browser.material.switch

class SwitchConfig {
    var selectedIcon: Boolean = true
    var unselectedIcon: Boolean = false
    var onChange: ((field : SwitchField) -> Unit)? = null
}