package hu.simplexion.z2.browser.components.select

typealias SelectItemBuilderFun<T> = SelectItem<T>.(selectBase : SelectBase<T>, value : T) -> Unit