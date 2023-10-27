package hu.simplexion.z2.service


interface Service {

    /**
     * Name of the service. You may set this manually or let the plugin set it.
     * The plugin uses the fully qualified class name of the service interface.
     */
    @Suppress("UNUSED_PARAMETER")
    var serviceName : String
        get() = placeholder()
        set(value) = placeholder()

}