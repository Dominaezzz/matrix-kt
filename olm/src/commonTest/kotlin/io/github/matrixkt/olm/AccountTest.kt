package io.github.matrixkt.olm

import kotlinx.serialization.json.Json
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
        assertFalse(identityKeys.ed25519.isBlank())

        // is JSON_KEY_IDENTITY_KEY present?
        assertFalse(identityKeys.curve25519.isBlank())
    }

    @Test
    fun testGenerateFallbackKeys() {
        withAccount {
            // Test generated one time keys.
            it.generateFallbackKey()

            val oneTimeKeys = it.fallbackKey
            val map = oneTimeKeys.curve25519

            assertTrue(map.isNotEmpty())
            println(oneTimeKeys)
        }
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
            val map = oneTimeKeys.curve25519
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
        val idKeysReference: IdentityKeys
        val oneTimeKeysReference: OneTimeKeys

        withAccount { accountRef ->
            accountRef.generateOneTimeKeys(GENERATION_ONE_TIME_KEYS_NUMBER)

            idKeysReference = accountRef.identityKeys
            oneTimeKeysReference = accountRef.oneTimeKeys

            // serialize account
            jsonStr = Json.encodeToString(AccountSerializer, accountRef)
        }

        val account = Json.decodeFromString(AccountSerializer, jsonStr)
        val idKeysDeserialized: IdentityKeys
        val oneTimeKeysDeserialized: OneTimeKeys
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
            identityKey1.curve25519,
            identityKey2.curve25519,
            identityKey3.curve25519,
            identityKey4.curve25519,
            identityKey5.curve25519,
            identityKey6.curve25519,
            identityKey7.curve25519,
            identityKey8.curve25519,
            identityKey9.curve25519,
            identityKey10.curve25519
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
