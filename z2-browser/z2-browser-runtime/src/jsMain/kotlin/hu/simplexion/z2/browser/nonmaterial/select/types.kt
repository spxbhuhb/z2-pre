package hu.simplexion.z2.browser.nonmaterial.select

typealias SelectItemBuilderFun<T> = SelectItem<T>.(selectBase : SelectBase<T>, value : T) -> Unit