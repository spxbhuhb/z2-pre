package hu.simplexion.z2.browser.immaterial.select

typealias SelectItemBuilderFun<T> = SelectItem<T>.(selectBase : SelectBase<T>, value : T) -> Unit