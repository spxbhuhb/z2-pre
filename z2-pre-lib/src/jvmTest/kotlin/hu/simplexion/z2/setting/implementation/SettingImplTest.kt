package hu.simplexion.z2.setting.implementation

class SettingImplTest {
//
//    @Test
//    fun basic() {
//        integrated { context ->
//            settingImpl(context).put(context.principal, "a", "1")
//
//            settingImpl(context).get(context.principal, "a").apply {
//                assertEquals(1, size)
//                assertItem(0, "a", "1")
//            }
//        }
//    }
//
//    @Test
//    fun subsequent() {
//        integrated { context ->
//            settingImpl(context).put(context.principal, "a", "1")
//            settingImpl(context).put(context.principal, "a/b", "1.2")
//
//            settingImpl(context).get(context.principal, "a", false).apply {
//                assertEquals(1, size)
//                assertItem(0, "a", "1")
//            }
//
//            settingImpl(context).get(context.principal, "a", true)
//                .sortedBy { it.path }
//                .apply {
//                    assertEquals(2, size)
//                    assertItem(0, "a", "1")
//                    assertItem(1, "a/b", "1.2")
//                }
//        }
//    }
//
//    @Test
//    fun valueSeparation() {
//        integrated { context ->
//            settingImpl(context).put(context.principal, "a", "1")
//            settingImpl(context).put(context.principal, "a/b", "1.2")
//            settingImpl(context).put(context.principal, "ab", "1")
//            settingImpl(context).put(context.principal, "b", "2")
//            settingImpl(context).put(context.principal, "b/a", "2.1")
//
//
//            settingImpl(context).get(context.principal, "a", true).apply {
//                assertEquals(2, size)
//                assertItem(0, "a", "1")
//                assertItem(1, "a/b", "1.2")
//            }
//        }
//    }
//
//    @Test
//    fun userSeparationFails() {
//        integratedWithSo { test, so ->
//            val testImpl = settingImpl(test)
//
//            assertFailsWith<AccessDenied> {
//                testImpl.put(so.principal, "a", "2")
//            }
//
//            assertFailsWith<AccessDenied> {
//                testImpl.get(so.principal, "a")
//            }
//        }
//    }
//
//    @Test
//    fun userSeparationSo() {
//        integratedWithSo { test, so ->
//            val soImpl = settingImpl(so)
//            val testImpl = settingImpl(test)
//
//            testImpl.put(test.principal, "a", "1")
//            soImpl.put(so.principal, "a", "2")
//
//            testImpl.get(test.principal, "a").apply {
//                assertEquals(1, size)
//                assertItem(0, "a", "1")
//            }
//
//            soImpl.get(test.principal, "a").apply {
//                assertEquals(1, size)
//                assertItem(0, "a", "1")
//            }
//
//            soImpl.get(so.principal, "a").apply {
//                assertEquals(1, size)
//                assertItem(0, "a", "2")
//            }
//        }
//    }
//
//    @Test
//    fun schematic() {
//        integrated { test ->
//            val s = TestSettingSchematic().apply {
//                i1 = 1
//                i2 = 1
//                s1 = "a"
//                s2 = "a"
//            }
//
//            val testImpl = settingImpl(test)
//
//            testImpl.put(test.principal, "bp", s)
//
//            testImpl.get(test.principal, "bp/i1").assertItem(0, "bp/i1", "1")
//            testImpl.get(test.principal, "bp/s1").assertItem(0, "bp/s1", "a")
//            // the other two should not be here as those are the defaults
//
//            val s2 = testImpl.get(test.principal, "bp", TestSettingSchematic())
//
//            assertEquals(s.i1, s2.i1)
//            assertEquals(s.i2, s2.i2)
//            assertEquals(s.s1, s2.s1)
//            assertEquals(s.s2, s2.s2)
//        }
//    }
//
//    fun List<Setting>.assertItem(index: Int, path: String, value: String) {
//        assertEquals(path, this[index].path)
//        assertEquals(value, this[index].value)
//    }
}