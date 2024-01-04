package hu.simplexion.z2.service.runtime.test.box.context

import hu.simplexion.z2.service.Service
import hu.simplexion.z2.service.getService
import hu.simplexion.z2.service.ServiceContext
import hu.simplexion.z2.service.ServiceImpl
import kotlinx.coroutines.runBlocking
import hu.simplexion.z2.service.defaultServiceImplFactory
import hu.simplexion.z2.commons.util.UUID

interface TestService : Service {

    suspend fun testFun(arg1: Int, arg2: String): String

    suspend fun testFun(): UUID<TestService>

}

fun <T> ServiceContext.ensure(vararg roles: String, block: () -> T): T {
    return block()
}

val testServiceConsumer = getService<TestService>()

class TestServiceImpl : TestService, ServiceImpl<TestServiceImpl> {

    override suspend fun testFun(arg1: Int, arg2: String) =
        "i:$arg1 s:$arg2 $serviceContext"

    override suspend fun testFun() =
        serviceContext.ensure { UUID<TestService>() }
}

fun box(): String {
    defaultServiceImplFactory += TestServiceImpl()

    var response = runBlocking { testServiceConsumer.testFun(1, "hello") }

    if (! response.startsWith("i:1 s:hello")) return "Fail (response=$response)"

    val uuidResponse = runBlocking { testServiceConsumer.testFun() }

    return "OK"
}