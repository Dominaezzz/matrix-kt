package io.github.matrixkt.olm

import kotlin.test.*

class AccountTest {
    @Test
    fun testCreateAndReleaseAccount() {
        val account = Account()
        account.clear()
    }

    @Test
    fun testAccountIdentityKeys() {
        val identityKeys = withAccount { it.identityKeys }

        println("Identity keys: $identityKeys")

        // is JSON_KEY_FINGER_PRINT_KEY present?
        assertFalse(identityKeys[JsonKeys.FINGER_PRINT_KEY].isNullOrBlank())

        // is JSON_KEY_IDENTITY_KEY present?
        assertFalse(identityKeys[JsonKeys.IDENTITY_KEY].isNullOrBlank())
    }

    //****************************************************
    //***************** ONE TIME KEYS TESTS **************
    //****************************************************

    @Test
    fun testMaxOneTimeKeys() {
        val maxOneTimeKeys = withAccount { it.maxNumberOfOneTimeKeys }

        assertTrue(maxOneTimeKeys > 0)
    }

    @Test
    fun testGenerateOneTimeKeys() {
        withAccount {
            // Test generated one time keys.
            it.generateOneTimeKeys(GENERATION_ONE_TIME_KEYS_NUMBER)

            // Test the generated amount of one time keys = GENERATION_ONE_TIME_KEYS_NUMBER.
            val oneTimeKeys = it.oneTimeKeys
            val map = oneTimeKeys[JsonKeys.ONE_TIME_KEY]
            assertNotNull(map)

            assertEquals(GENERATION_ONE_TIME_KEYS_NUMBER, map.size.toLong())
        }
    }

    @Test
    fun testMarkOneTimeKeysAsPublished() {
        withAccount {
            it.markOneTimeKeysAsPublished()
        }
    }

    @Test
    fun testSignMessage() {
        val clearMsg = "Super secret! Maybe spoilers..."

        val signedMsg = withAccount {
            it.sign(clearMsg)
        }
        println(signedMsg)

        // additional tests are performed in test01VerifyEd25519Signing()
    }

    // ********************************************************
    // ************* SERIALIZATION TEST ***********************
    // ********************************************************

    @Test
    fun testSerialization() {
        val jsonStr: String
        val idKeysReference: Map<String, String>
        val oneTimeKeysReference: Map<String, Map<String, String>>

        withAccount { accountRef ->
            accountRef.generateOneTimeKeys(GENERATION_ONE_TIME_KEYS_NUMBER)

            idKeysReference = accountRef.identityKeys
            oneTimeKeysReference = accountRef.oneTimeKeys

            // serialize account
            jsonStr = OlmJson.stringify(AccountSerializer, accountRef)
        }

        val account = OlmJson.parse(AccountSerializer, jsonStr)
        val idKeysDeserialized: Map<String, String>
        val oneTimeKeysDeserialized: Map<String, Map<String, String>>
        try {
            // get de-serialized keys
            idKeysDeserialized = account.identityKeys
            oneTimeKeysDeserialized = account.oneTimeKeys
        } finally {
            account.clear()
        }

        // compare identity keys
        assertEquals(idKeysReference, idKeysDeserialized)

        // compare onetime keys
        assertEquals(oneTimeKeysReference, oneTimeKeysDeserialized)
    }

    // ****************************************************
    // *************** SANITY CHECK TESTS *****************
    // ****************************************************

    @Test
    fun testGenerateOneTimeKeysError() {
        // keys number = 0 => no error

        withAccount {
            // Should not throw
            it.generateOneTimeKeys(0)

            // keys number = negative value
            assertFails {
                it.generateOneTimeKeys(-5)
            }
        }
    }

    /**
     * Create multiple accounts and check that identity keys are still different.
     * This test validates random series are provide enough random values.
     */
    @Test
    fun testMultipleAccountCreation() {
        val identityKey1 = withAccount { it.identityKeys }
        val identityKey2 = withAccount { it.identityKeys }
        val identityKey3 = withAccount { it.identityKeys }
        val identityKey4 = withAccount { it.identityKeys }
        val identityKey5 = withAccount { it.identityKeys }
        val identityKey6 = withAccount { it.identityKeys }
        val identityKey7 = withAccount { it.identityKeys }
        val identityKey8 = withAccount { it.identityKeys }
        val identityKey9 = withAccount { it.identityKeys }
        val identityKey10 = withAccount { it.identityKeys }

        val allKeys = listOf(
            identityKey1.getValue(JsonKeys.IDENTITY_KEY),
            identityKey2.getValue(JsonKeys.IDENTITY_KEY),
            identityKey3.getValue(JsonKeys.IDENTITY_KEY),
            identityKey4.getValue(JsonKeys.IDENTITY_KEY),
            identityKey5.getValue(JsonKeys.IDENTITY_KEY),
            identityKey6.getValue(JsonKeys.IDENTITY_KEY),
            identityKey7.getValue(JsonKeys.IDENTITY_KEY),
            identityKey8.getValue(JsonKeys.IDENTITY_KEY),
            identityKey9.getValue(JsonKeys.IDENTITY_KEY),
            identityKey10.getValue(JsonKeys.IDENTITY_KEY)
        )

        val keyCounts = allKeys.groupingBy { it }.eachCount()
        for ((key, count) in keyCounts) {
            // Check that all the keys are unique
            assertEquals(1, count, "$key was repeated $count times. Keys should be unique.")
        }
    }

    companion object {
        private const val GENERATION_ONE_TIME_KEYS_NUMBER: Long = 50
    }
}
