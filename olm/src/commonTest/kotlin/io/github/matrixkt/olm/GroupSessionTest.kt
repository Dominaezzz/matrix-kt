package io.github.matrixkt.olm

import kotlinx.serialization.json.Json
import kotlin.test.*


class GroupSessionTest {
    /**
     * Basic test:
     * - alice creates an outbound group session
     * - bob creates an inbound group session with alice's outbound session key
     * - alice encrypts a message with its session
     * - bob decrypts the encrypted message with its session
     * - decrypted message is identical to original alice message
     */
    @Test
    fun testDecryptMessage() = withOlmInit {
        val aliceOutSession = OutboundGroupSession()
        try {
            assertEquals(0, aliceOutSession.messageIndex)

            val sessionKey = aliceOutSession.sessionKey
            assertTrue(sessionKey.isNotBlank())

            val aliceSessionId = aliceOutSession.sessionId
            assertTrue(aliceSessionId.isNotBlank())

            val aliceToBobMessage = aliceOutSession.encrypt(CLEAR_MESSAGE1)
            assertTrue(aliceToBobMessage.isNotEmpty())

            // bob creates INBOUND GROUP SESSION with alice outbound key
            val bobInSession = InboundGroupSession(sessionKey)
            try {
                // check both session identifiers are equals
                val bobSessionId = bobInSession.sessionId
                assertFalse(bobSessionId.isEmpty())

                assertEquals(aliceSessionId, bobSessionId)

                val result = bobInSession.decrypt(aliceToBobMessage)

                // test decrypted message
                assertFalse(result.message.isEmpty())
                assertEquals(0, result.index)
                assertEquals(CLEAR_MESSAGE1, result.message)
            } finally {
                bobInSession.clear()
            }
        } finally {
            aliceOutSession.clear()
        }
    }

    @Test
    fun testSerializeOutboundSession() = withOlmInit {
        // create one OUTBOUND GROUP SESSION
        val outboundGroupSessionRef = OutboundGroupSession()

        // serialize alice session
        val sessionJson = Json.encodeToString(OutboundGroupSessionSerializer, outboundGroupSessionRef)

        // deserialize session
        val outboundGroupSessionSerial = Json.decodeFromString(OutboundGroupSessionSerializer, sessionJson)
        // get sessions keys

        // get sessions keys
        val sessionKeyRef: String = outboundGroupSessionRef.sessionKey
        val sessionKeySerial: String = outboundGroupSessionSerial.sessionKey
        assertFalse(sessionKeyRef.isEmpty())
        assertFalse(sessionKeySerial.isEmpty())

        // session keys comparison
        assertEquals(sessionKeyRef, sessionKeySerial)

        // get sessions IDs
        val sessionIdRef = outboundGroupSessionRef.sessionId
        val sessionIdSerial = outboundGroupSessionSerial.sessionId
        assertFalse(sessionIdRef.isEmpty())
        assertFalse(sessionIdSerial.isEmpty())

        // session IDs comparison
        assertEquals(sessionIdRef, sessionIdSerial)

        outboundGroupSessionRef.clear()
        outboundGroupSessionSerial.clear()
    }

    @Test
    fun test16SerializeInboundSession() = withOlmInit {
        // alice creates OUTBOUND GROUP SESSION
        val aliceOutboundGroupSession = OutboundGroupSession()

        // get the session key from the outbound group session
        val sessionKeyRef = aliceOutboundGroupSession.sessionKey

        // bob creates INBOUND GROUP SESSION
        val bobInboundGroupSessionRef = InboundGroupSession(sessionKeyRef)

        // serialize alice session
        val sessionJson = Json.encodeToString(InboundGroupSessionSerializer, bobInboundGroupSessionRef)

        // deserialize session
        val bobInboundGroupSessionSerial = Json.decodeFromString(InboundGroupSessionSerializer, sessionJson)

        // get sessions IDs
        val aliceSessionId = aliceOutboundGroupSession.sessionId
        val sessionIdRef = bobInboundGroupSessionRef.sessionId
        val sessionIdSerial = bobInboundGroupSessionSerial.sessionId

        assertFalse(aliceSessionId.isEmpty())
        assertFalse(sessionIdRef.isEmpty())
        assertFalse(sessionIdSerial.isEmpty())

        // session IDs comparison
        assertEquals(aliceSessionId, sessionIdSerial)
        assertEquals(sessionIdRef, sessionIdSerial)

        aliceOutboundGroupSession.clear()
        bobInboundGroupSessionRef.clear()
        bobInboundGroupSessionSerial.clear()
    }

    /**
     * Create multiple outbound group sessions and check that session Keys are different.
     * This test validates random series are provide enough random values.
     */
    @Test
    fun test17MultipleOutboundSession() = withOlmInit {
        val outboundGroupSession1 = OutboundGroupSession()
        val outboundGroupSession2 = OutboundGroupSession()
        val outboundGroupSession3 = OutboundGroupSession()
        val outboundGroupSession4 = OutboundGroupSession()
        val outboundGroupSession5 = OutboundGroupSession()
        val outboundGroupSession6 = OutboundGroupSession()
        val outboundGroupSession7 = OutboundGroupSession()
        val outboundGroupSession8 = OutboundGroupSession()

        // get the session key from the outbound group sessions
        val sessionKeys = listOf(
            outboundGroupSession1.sessionKey,
            outboundGroupSession2.sessionKey,
            outboundGroupSession3.sessionKey,
            outboundGroupSession4.sessionKey,
            outboundGroupSession5.sessionKey,
            outboundGroupSession6.sessionKey,
            outboundGroupSession7.sessionKey,
            outboundGroupSession8.sessionKey
        )

        assertEquals(sessionKeys.size, sessionKeys.distinct().size)

        // get the session IDs from the outbound group sessions
        val sessionId = listOf(
            outboundGroupSession1.sessionId,
            outboundGroupSession2.sessionId,
            outboundGroupSession3.sessionId,
            outboundGroupSession4.sessionId,
            outboundGroupSession5.sessionId,
            outboundGroupSession6.sessionId,
            outboundGroupSession7.sessionId,
            outboundGroupSession8.sessionId
        )

        assertEquals(sessionId.size, sessionId.distinct().size)

        outboundGroupSession1.clear()
        outboundGroupSession2.clear()
        outboundGroupSession3.clear()
        outboundGroupSession4.clear()
        outboundGroupSession5.clear()
        outboundGroupSession6.clear()
        outboundGroupSession7.clear()
        outboundGroupSession8.clear()
    }

    /**
     * Specific test for the following run time error:
     * "JNI DETECTED ERROR IN APPLICATION: input is not valid Modified UTF-8: illegal start byte 0xf0 in call to NewStringUTF".<br>
     * When the msg to decrypt contain emojis, depending on the android platform, the NewStringUTF() behaves differently and
     * can even crash.
     * This issue is described in details here: https://github.com/eclipsesource/J2V8/issues/142
     */
    @Test
    fun test18TestBadCharacterCrashInDecrypt() = withOlmInit {
        // values taken from a "real life" crash case
        val sessionKeyRef = "AgAAAAycZE6AekIctJWYxd2AWLOY15YmxZODm/WkgbpWkyycp6ytSp/R+wo84jRrzBNWmv6ySLTZ9R0EDOk9VI2eZyQ6Efdwyo1mAvrWvTkZl9yALPdkOIVHywyG65f1SNiLrnsln3hgsT1vUrISGyKtsljoUgQpr3JDPEhD0ilAi63QBjhnGCW252b+7nF+43rb6O6lwm93LaVwe2341Gdp6EkhTUvetALezEqDOtKN00wVqAbq0RQAnUJIowxHbMswg+FyoR1K1oCjnVEoF23O9xlAn5g1XtuBZP3moJlR2lwsBA"
        val msgToDecryptWithEmoji = "AwgNEpABpjs+tYF+0y8bWtzAgYAC3N55p5cPJEEiGPU1kxIHSY7f2aG5Fj4wmcsXUkhDv0UePj922kgf+Q4dFsPHKq2aVA93n8DJAQ/FRfcM98B9E6sKCZ/PsCF78uBvF12Aaq9D3pUHBopdd7llUfVq29d5y6ZwX5VDoqV2utsATkKjXYV9CbfZuvvBMQ30ZLjEtyUUBJDY9K4FxEFcULytA/IkVnATTG9ERuLF/yB6ukSFR+iUWRYAmtuOuU0k9BvaqezbGqNoK5Grlkes+dYX6/0yUObumcw9/iAI"

        // bob creates INBOUND GROUP SESSION
        val bobInboundGroupSession = InboundGroupSession(sessionKeyRef)

        val result = bobInboundGroupSession.decrypt(msgToDecryptWithEmoji)

        assertEquals(13L, result.index)
    }

    /**
     * Specific test to check an error message is returned by decryptMessage() API.
     *
     * A corrupted encrypted message is passed, and a INVALID_BASE64 is
     * expected.
     */
    @Test
    fun test19TestErrorMessageReturnedInDecrypt() = withOlmInit {
        val EXPECTED_ERROR_MESSAGE = "INVALID_BASE64"

        val sessionKeyRef         = "AgAAAAycZE6AekIctJWYxd2AWLOY15YmxZODm/WkgbpWkyycp6ytSp/R+wo84jRrzBNWmv6ySLTZ9R0EDOk9VI2eZyQ6Efdwyo1mAvrWvTkZl9yALPdkOIVHywyG65f1SNiLrnsln3hgsT1vUrISGyKtsljoUgQpr3JDPEhD0ilAi63QBjhnGCW252b+7nF+43rb6O6lwm93LaVwe2341Gdp6EkhTUvetALezEqDOtKN00wVqAbq0RQAnUJIowxHbMswg+FyoR1K1oCjnVEoF23O9xlAn5g1XtuBZP3moJlR2lwsBA"
        val corruptedEncryptedMsg = "AwgANYTHINGf87ge45ge7gr*/rg5ganything4gr41rrgr4re55tanythingmcsXUkhDv0UePj922kgf+"

        // valid INBOUND GROUP SESSION
        val bobInboundGroupSession = InboundGroupSession(sessionKeyRef)

        val exception = assertFails {
            bobInboundGroupSession.decrypt(corruptedEncryptedMsg)
        }

        assertNotEquals(0, EXPECTED_ERROR_MESSAGE.length)
        assertTrue(exception.message!!.contains(EXPECTED_ERROR_MESSAGE))

        bobInboundGroupSession.clear()
    }

    /**
     * Test the import/export functions.
     */
    @Test
    fun test20TestInboundGroupSessionImportExport() = withOlmInit {
        val sessionKey = "AgAAAAAwMTIzNDU2Nzg5QUJERUYwMTIzNDU2Nzg5QUJDREVGMDE" +
                                "yMzQ1Njc4OUFCREVGMDEyMzQ1Njc4OUFCQ0RFRjAxMjM0NTY3OD" +
                                "lBQkRFRjAxMjM0NTY3ODlBQkNERUYwMTIzNDU2Nzg5QUJERUYwM" +
                                "TIzNDU2Nzg5QUJDREVGMDEyMw0bdg1BDq4Px/slBow06q8n/B9W" +
                                "BfwWYyNOB8DlUmXGGwrFmaSb9bR/eY8xgERrxmP07hFmD9uqA2p" +
                                "8PMHdnV5ysmgufE6oLZ5+8/mWQOW3VVTnDIlnwd8oHUYRuk8TCQ"

        val message = "AwgAEhAcbh6UpbByoyZxufQ+h2B+8XHMjhR69G8F4+qjMaFlnIXusJZX3r8LnR" +
                             "ORG9T3DXFdbVuvIWrLyRfm4i8QRbe8VPwGRFG57B1CtmxanuP8bHtnnYqlwPsD"

        val inboundGroupSession = InboundGroupSession(sessionKey)
        assertTrue(inboundGroupSession.isVerified)

        with(inboundGroupSession.decrypt(message)) {
            assertEquals("Message", this.message)
            assertEquals(0L, this.index)
        }

        val export = inboundGroupSession.export(0)
        assertTrue(export.isNotEmpty())

        val index = inboundGroupSession.firstKnownIndex
        assertTrue(index >= 0)

        inboundGroupSession.clear()

        val inboundGroupSession2 = InboundGroupSession.import(export)
        assertFalse(inboundGroupSession2.isVerified)

        with(inboundGroupSession2.decrypt(message)) {
            assertEquals("Message", this.message)
            assertEquals(0L, this.index)
        }

        assertTrue(inboundGroupSession2.isVerified)
        inboundGroupSession2.clear()
    }

    companion object {
        private const val CLEAR_MESSAGE1 = "Hello!"
    }
}
