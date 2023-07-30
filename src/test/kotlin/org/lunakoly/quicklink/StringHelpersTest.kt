package org.lunakoly.quicklink

import org.lunakoly.quicklink.utils.removeProtocol
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class StringHelpersTest : BasePlatformTestCase() {
    fun testRemoveProtocol() {
        assertEquals("", "".removeProtocol())
    }

    override fun getTestDataPath() = "src/test/testData"
}
