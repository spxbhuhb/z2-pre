package hu.simplexion.z2.commons.i18n

import kotlin.test.Test
import kotlin.test.assertEquals

class  LocalizedTextStoreTest {

    @Test
    fun test() {
        val store = object : LocalizedTextStore() {
            val a by "A"
            val b by "B"
        }
        assertEquals("A", store.a.toString())
        assertEquals("B", store.b.toString())
    }

    @Test
    fun testSupport() {
        val store = object : LocalizedTextStore() {
            val a by "A"
            val b by a.support("B")
        }
        assertEquals("A", store.a.toString())
        assertEquals("B", store.b.toString())
        assertEquals("B", store.a.support.toString())
    }

}