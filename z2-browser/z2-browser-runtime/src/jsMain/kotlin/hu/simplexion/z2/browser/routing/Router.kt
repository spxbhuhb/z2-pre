package hu.simplexion.z2.browser.routing

import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.util.UUID
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class Router<R>(
    override val label: LocalizedText? = null,
    override val icon: LocalizedIcon? = null
) : RoutingTarget<R> {

    override var parent: Router<R>? = null

    override var relativePath = ""

    val parameters = mutableListOf<RouterParameter<*>>()

    val targets = mutableListOf<RoutingTarget<R>>()

    override fun open(receiver: R, path: List<String>) {
        trace { "[routing]  open  router=${this::class.simpleName}  relativePath=$relativePath  absolutePath=$absolutePath  path.size=${path.size} path=${path.joinToString("/")}" }

        val subPath = path.drop(1 + parameters.size)

        for (index in parameters.indices) {
            if (path.size > 1 + index) parameters[index].set(path[1 + index])
        }

        if (subPath.isEmpty()) {
            default(receiver, subPath)
            return
        }

        val target = targets.firstOrNull { it.accepts(subPath) }

        if (target == null) {
            parent?.notFound(receiver, subPath) ?: notFound(receiver, subPath)
            return
        }

        return target.open(receiver, subPath)
    }

    fun up() {
        parent?.let {
            root.open(it)
        }
    }

    open fun default(receiver: R, path: List<String>) {
        notFound(receiver, path)
    }

    open fun notFound(receiver: R, path: List<String>) {
        trace { "[routing]  notfound  router=${this::class.simpleName}  absolutePath=$absolutePath  remaining=$path" }
        throw IllegalStateException("routing path not found: fullPath=${absolutePath.joinToString { ", " }}  path=${path.joinToString { "/" }}")
    }

    open fun int(): RouterParameter<Int> {
        return IntRouterParameter()
    }

    open fun string(): RouterParameter<String> {
        return StringRouterParameter()
    }

    open fun <T> uuid(): RouterParameter<UUID<T>> {
        return UuidRouterParameter()
    }

    open fun action(label: LocalizedText? = null, icon: LocalizedIcon? = null, actionFun: () -> Unit): RoutedAction<R> {
        return RoutedAction(label, icon, actionFun)
    }

    open fun render(label: LocalizedText? = null, icon: LocalizedIcon? = null, renderFun: R.() -> Unit): RoutedRenderer<R> {
        return RoutedRenderer(label, icon, renderFun)
    }

    class ParameterDelegate<R,T>(
        var parameter: RouterParameter<T>
    ) : ReadOnlyProperty<Router<R>, T> {
        override fun getValue(thisRef: Router<R>, property: KProperty<*>): T = parameter.value
    }

    class ActionDelegate<R>(
        val action: RoutedAction<R>
    ) : ReadOnlyProperty<Router<R>, RoutedAction<R>> {
        override fun getValue(thisRef: Router<R>, property: KProperty<*>): RoutedAction<R> = action
    }

    class RendererDelegate<R>(
        val renderer: RoutedRenderer<R>
    ) : ReadOnlyProperty<Router<R>, RoutedRenderer<R>> {
        override fun getValue(thisRef: Router<R>, property: KProperty<*>): RoutedRenderer<R> = renderer
    }

    class RouterDelegate<R>(
        val router: Router<R>
    ) : ReadOnlyProperty<Router<R>, Router<R>> {
        override fun getValue(thisRef: Router<R>, property: KProperty<*>): Router<R> = router
    }

    operator fun <T> RouterParameter<T>.provideDelegate(thisRef: Router<R>, prop: KProperty<*>): ReadOnlyProperty<Router<R>, T> {
        thisRef.parameters += this
        return ParameterDelegate(this)
    }

    operator fun RoutedAction<R>.provideDelegate(thisRef: Router<R>, prop: KProperty<*>): ReadOnlyProperty<Router<R>, RoutedAction<R>> {
        this.parent = thisRef
        this.relativePath = prop.name
        thisRef.targets += this
        return ActionDelegate(this)
    }

    operator fun RoutedRenderer<R>.provideDelegate(thisRef: Router<R>, prop: KProperty<*>): ReadOnlyProperty<Router<R>, RoutedRenderer<R>> {
        this.parent = thisRef
        this.relativePath = prop.name
        thisRef.targets += this
        return RendererDelegate(this)
    }

    operator fun Router<R>.provideDelegate(thisRef: Router<R>, prop: KProperty<*>): ReadOnlyProperty<Router<R>, Router<R>> {
        this.parent = thisRef
        this.relativePath = prop.name
        thisRef.targets += this
        return RouterDelegate(this)
    }

    fun trace(message: () -> Any?) {
        if (traceRouting) console.log(message())
    }
}
