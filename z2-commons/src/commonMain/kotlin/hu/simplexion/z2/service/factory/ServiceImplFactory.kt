package hu.simplexion.z2.service.factory

import hu.simplexion.z2.service.ServiceContext
import hu.simplexion.z2.service.ServiceImpl

interface ServiceImplFactory {

    operator fun plusAssign(template: ServiceImpl<*>)

    operator fun get(serviceName: String, context: ServiceContext): ServiceImpl<*>?

}