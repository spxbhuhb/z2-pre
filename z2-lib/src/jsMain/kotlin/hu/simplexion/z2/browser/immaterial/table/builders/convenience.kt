package hu.simplexion.z2.browser.immaterial.table.builders

infix fun ColumnBuilder<*>.size(value : String) {
    this.initialSize = value
}