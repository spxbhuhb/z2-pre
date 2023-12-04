package hu.simplexion.z2.service

import hu.simplexion.z2.service.transport.ResponseEnvelope

class ServiceTimeoutException(
    val serviceName: String,
    val funName: String,
    val responseEnvelope: ResponseEnvelope
) : RuntimeException()