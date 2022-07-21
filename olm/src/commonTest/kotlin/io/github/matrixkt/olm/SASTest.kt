package io.github.matrixkt.olm

import kotlin.test.*

class SASTest: BaseTest {
    @Test
    fun testSASCode() = runTest {
        val aliceSas = SAS()
        val bobSas = SAS()
        try {
            val alicePKey = aliceSas.publicKey
            val bobPKey = bobSas.publicKey

            assertFalse(aliceSas.isTheirKeySet)
            assertFalse(bobSas.isTheirKeySet)

            println("#### Alice pub Key is $alicePKey")
            println("#### Bob pub Key is $bobPKey")
            aliceSas.setTheirPublicKey(bobPKey)
            bobSas.setTheirPublicKey(alicePKey)

            assertTrue(aliceSas.isTheirKeySet)
            assertTrue(bobSas.isTheirKeySet)

            val codeLength = 6
            val alice_sas = aliceSas.generateShortCode("SAS", codeLength)
            val bob_sas = bobSas.generateShortCode("SAS", codeLength)
            println("#### Alice SAS is ${alice_sas.decodeToString()}")
            println("#### Bob SAS is ${bob_sas.decodeToString()}")

            assertEquals(codeLength, alice_sas.size)
            assertEquals(codeLength, bob_sas.size)
            assertTrue(alice_sas.contentEquals(bob_sas))

            val aliceMac = aliceSas.calculateMac("Hello world!", "SAS")
            val bobMac = bobSas.calculateMac("Hello world!", "SAS")

            assertEquals(aliceMac, bobMac)
            println("#### Alice Mac is $aliceMac")
            println("#### Bob Mac is $bobMac")

            val aliceLongKdfMac = aliceSas.calculateMacLongKdf("Hello world!", "SAS")
            val bobLongKdfMac = bobSas.calculateMacLongKdf("Hello world!", "SAS")
            assertEquals(aliceLongKdfMac, bobLongKdfMac, "Mac should be the same")
            println("#### Alice lkdf Mac is $aliceLongKdfMac")
            println("#### Bob lkdf Mac is $bobLongKdfMac")
        } finally {
            aliceSas.clear()
            bobSas.clear()
        }
    }
}
