package hu.simplexion.z2.services.runtime.test.box

import hu.simplexion.z2.serialization.Message
import hu.simplexion.z2.serialization.MessageBuilder
import hu.simplexion.z2.serialization.SerializationConfig
import hu.simplexion.z2.services.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class BasicTest {

    fun box(): String {
        var response: String
        runBlocking {
            defaultServiceImplFactory += TestServiceImpl(BasicServiceContext())
            response = TestServiceConsumer.testFun(1, "hello")
        }
        return if (response.startsWith("i:1 s:hello BasicServiceContext(uuid=")) "OK" else "Fail (response=$response)"
    }

    @Test
    fun basicTest() {
        assertEquals("OK", box())
    }

}

interface TestService : Service {

    suspend fun testFun(arg1: Int, arg2: String): String

}

object TestServiceConsumer : TestService {

    override var serviceName = "TestService"

    val serializationConfig
        get() = SerializationConfig.defaultSerialization

    override suspend fun testFun(arg1: Int, arg2: String): String =
        serializationConfig.standaloneValue().decodeString(
            defaultServiceCallTransport
                .call(
                    serviceName,
                    "testFun",
                    serializationConfig.messageBuilder()
                        .int(1, "i", arg1)
                        .string(2, "s", arg2)
                        .pack()
                )
        )
}

class TestServiceImpl(override val serviceContext: ServiceContext) : TestService, ServiceImpl<TestServiceImpl> {

    override var serviceName = "TestService"

    override suspend fun dispatch(
        funName: String,
        payload: Message,
        response: MessageBuilder
    ) {
        when (funName) {
            "testFun" -> response.string(1, "value", testFun(payload.int(1, "i"), payload.string(2, "s")))
            else -> throw IllegalStateException("unknown function: $funName")
        }
    }

    override fun newInstance(serviceContext: ServiceContext): TestServiceImpl {
        return TestServiceImpl(serviceContext)
    }

    override suspend fun testFun(arg1: Int, arg2: String): String {
        return "i:$arg1 s:$arg2 $serviceContext"
    }

}