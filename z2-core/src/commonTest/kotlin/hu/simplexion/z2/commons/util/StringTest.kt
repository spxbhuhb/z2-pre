package hu.simplexion.z2.commons.util

import hu.simplexion.z2.util.afterVersion
import hu.simplexion.z2.util.beforeVersion
import hu.simplexion.z2.util.equalsVersion
import kotlin.test.Test
import kotlin.test.assertTrue

class StringTest {

    @Test
    fun versionCompare() {
        assertTrue("2".afterVersion("1"))
        assertTrue("2".afterVersion("01"))
        assertTrue("02".afterVersion("1"))
        assertTrue("02".afterVersion("01"))

        assertTrue("1.1".afterVersion("1.0"))
        assertTrue("1.1".equalsVersion("1.01"))

        assertTrue("1.1.1".afterVersion("1.0.0"))

        assertTrue("2023.8.3".afterVersion("2023.08.02"))
        assertTrue("2023.8.1".afterVersion("2023.07.30"))

        assertTrue("2023.8.3".afterVersion("2023.08.02"))
        assertTrue("2023.8.3".afterVersion("2023.08.02-SNAPSHOT"))
        assertTrue("2023.8.3".afterVersion("2023.08.03-SNAPSHOT"))
        assertTrue("2023.8.3".equalsVersion("2023.08.03"))

        assertTrue("2023.8.3".beforeVersion("2023.08.4-SNAPSHOT"))
        assertTrue("2023.8.3".beforeVersion("2023.08.4"))
    }
}