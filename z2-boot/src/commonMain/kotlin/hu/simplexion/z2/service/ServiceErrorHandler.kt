package hu.simplexion.z2.service

import hu.simplexion.z2.service.transport.ResponseEnvelope

/**
 * Handle service error responses.
 */
open class ServiceErrorHandler {

    open fun connectionError(ex: Exception) {

    }

    open fun callError(serviceName: String, funName: String, responseEnvelope: ResponseEnvelope) {

    }

}