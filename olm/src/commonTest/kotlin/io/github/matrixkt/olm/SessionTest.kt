package io.github.matrixkt.olm

import kotlinx.serialization.json.Json
import kotlin.test.*


class SessionTest {
    /**
     * Basic test:
     * - alice creates an account
     * - bob creates an account
     * - alice creates an outbound session with bob (bobIdentityKey & bobOneTimeKey)
     * - alice encrypts a message with its session
     * - bob creates an inbound session based on alice's encrypted message
     * - bob decrypts the encrypted message with its session
     */
    @Test
    fun test01AliceToBob() {
        val ONE_TIME_KEYS_NUMBER = 5L

        // ALICE & BOB ACCOUNTS CREATION
        val bobAccount = Account()
        val aliceAccount = Account()

        // get bob identity key
        val bobIdentityKeys = bobAccount.identityKeys
        val bobIdentityKey = bobIdentityKeys.curve25519

        // get bob one time keys
        bobAccount.generateOneTimeKeys(ONE_TIME_KEYS_NUMBER)

        val bobOneTimeKeys = bobAccount.oneTimeKeys
        val bobOneTimeKey = bobOneTimeKeys.curve25519.values.first()

        // CREATE ALICE OUTBOUND SESSION and encrypt message to bob
        val aliceSession = Session.createOutboundSession(aliceAccount, bobIdentityKey, bobOneTimeKey)

        val clearMsg = "Hello bob, this is alice!"
        val encryptedMsgToBob = aliceSession.encrypt(clearMsg)
        println("## test01AliceToBob(): encryptedMsg=${encryptedMsgToBob.cipherText}")

        // CREATE BOB INBOUND SESSION and decrypt message from alice
        val bobSession = Session.createInboundSession(bobAccount, encryptedMsgToBob.cipherText)

        val decryptedMsg = bobSession.decrypt(encryptedMsgToBob)

        // MESSAGE COMPARISON: decrypted vs encrypted
        assertEquals(clearMsg, decryptedMsg)

        // clean objects..
        bobAccount.removeOneTimeKeys(bobSession)

        // release accounts
        bobAccount.clear()
        aliceAccount.clear()

        // release sessions
        bobSession.clear()
        aliceSession.clear()
    }

    /**
     * Same as test01AliceToBob but with bob who's encrypting messages
     * to alice and alice decrypt them.<br>
     * - alice creates an account
     * - bob creates an account
     * - alice creates an outbound session with bob (bobIdentityKey & bobOneTimeKey)
     * - alice encrypts a message with its own session
     * - bob creates an inbound session based on alice's encrypted message
     * - bob decrypts the encrypted message with its own session
     * - bob encrypts messages with its own session
     * - alice decrypts bob's messages with its own message
     * - alice encrypts a message
     * - bob decrypts the encrypted message
     */
    @Test
    fun test02AliceToBobBackAndForth() {
        // creates alice & bob accounts
        val aliceAccount = Account()
        val bobAccount = Account()

        // get bob identity key
        val bobIdentityKeys = bobAccount.identityKeys
        val bobIdentityKey = bobIdentityKeys.curve25519

        // get bob one time keys
        bobAccount.generateOneTimeKeys(ONE_TIME_KEYS_NUMBER)

        val bobOneTimeKeys = bobAccount.oneTimeKeys
        val bobOneTimeKey = bobOneTimeKeys.curve25519.values.first()

        // CREATE ALICE OUTBOUND SESSION and encrypt message to bob
        val aliceSession = Session.createOutboundSession(aliceAccount, bobIdentityKey, bobOneTimeKey)

        val helloClearMsg = "Hello I'm Alice!"

        val encryptedAliceToBobMsg1 = aliceSession.encrypt(helloClearMsg)

        // CREATE BOB INBOUND SESSION and decrypt message from alice
        val bobSession = Session.createInboundSession(bobAccount, encryptedAliceToBobMsg1.cipherText)

        // DECRYPT MESSAGE FROM ALICE
        val decryptedMsg01 = bobSession.decrypt(encryptedAliceToBobMsg1)

        // MESSAGE COMPARISON: decrypted vs encrypted
        assertEquals(helloClearMsg, decryptedMsg01)

        // BACK/FORTH MESSAGE COMPARISON
        val clearMsg1 = "Hello I'm Bob!"
        val clearMsg2 = "Isn't life grand?"
        val clearMsg3 = "Let's go to the opera."

        // bob encrypts messages
        val encryptedMsg1 = bobSession.encrypt(clearMsg1)
        val encryptedMsg2 = bobSession.encrypt(clearMsg2)
        val encryptedMsg3 = bobSession.encrypt(clearMsg3)

        // alice decrypts bob's messages
        val decryptedMsg1 = aliceSession.decrypt(encryptedMsg1)
        val decryptedMsg2 = aliceSession.decrypt(encryptedMsg2)
        val decryptedMsg3 = aliceSession.decrypt(encryptedMsg3)

        // comparison tests
        assertEquals(clearMsg1, decryptedMsg1)
        assertEquals(clearMsg2, decryptedMsg2)
        assertEquals(clearMsg3, decryptedMsg3)

        // and one more from alice to bob
        val clearMsg4 = "another message from Alice to Bob!!"
        val encryptedMsg4 = aliceSession.encrypt(clearMsg4)
        val decryptedMsg4 = bobSession.decrypt(encryptedMsg4)

        // comparison test
        assertEquals(clearMsg4, decryptedMsg4)

        // clean objects..
        bobAccount.removeOneTimeKeys(bobSession)

        bobAccount.clear()
        aliceAccount.clear()

        bobSession.clear()
        aliceSession.clear()
    }

    @Test
    fun test04MatchInboundSession() {
        // ACCOUNTS CREATION
        val aliceAccount = Account()
        val bobAccount = Account()

        // get bob/luke identity key
        val bobIdentityKeys = bobAccount.identityKeys
        val aliceIdentityKeys = aliceAccount.identityKeys

        val bobIdentityKey = bobIdentityKeys.curve25519
        val aliceIdentityKey = aliceIdentityKeys.curve25519

        // get bob/luke one time keys
        bobAccount.generateOneTimeKeys(ONE_TIME_KEYS_NUMBER)
        aliceAccount.generateOneTimeKeys(ONE_TIME_KEYS_NUMBER)

        val bobOneTimeKeys = bobAccount.oneTimeKeys

        val bobOneTimeKey1 = bobOneTimeKeys.curve25519.values.first()

        // create alice inbound session for bob
        val aliceSession = Session.createOutboundSession(aliceAccount, bobIdentityKey, bobOneTimeKey1)

        val aliceClearMsg = "hello helooo to bob!"
        val encryptedAliceToBobMsg1 = aliceSession.encrypt(aliceClearMsg)

        // init bob session with alice PRE KEY
        val bobSession = Session.createInboundSession(bobAccount, encryptedAliceToBobMsg1.cipherText)

        // test matchesInboundSession() and matchesInboundSessionFrom()
        assertTrue(bobSession.matchesInboundSession(encryptedAliceToBobMsg1.cipherText))
        assertTrue(bobSession.matchesInboundSessionFrom(aliceIdentityKey, encryptedAliceToBobMsg1.cipherText))
        // following requires olm native lib new version with https://github.com/matrix-org/olm-backup/commit/7e9f3bebb8390f975a76c0188ce4cb460fe6692e
        assertFalse(bobSession.matchesInboundSessionFrom(bobIdentityKey, encryptedAliceToBobMsg1.cipherText))

        // release objects
        bobAccount.removeOneTimeKeys(bobSession)

        aliceAccount.clear()
        bobAccount.clear()

        aliceSession.clear()
        bobSession.clear()
    }

    // ********************************************************
    // ************* SERIALIZATION TEST ***********************
    // ********************************************************
    /**
     * Same as [test02AliceToBobBackAndForth], but alice's session
     * is serialized and de-serialized before performing the final
     * comparison (encrypt vs )
     */
    @Test
    fun test05SessionSerialization() {
        val ONE_TIME_KEYS_NUMBER = 1L

        // creates alice & bob accounts
        val aliceAccount = Account()
        val bobAccount = Account()

        // get bob identity key
        val bobIdentityKeys = bobAccount.identityKeys
        val bobIdentityKey = bobIdentityKeys.curve25519

        // get bob one time keys
        bobAccount.generateOneTimeKeys(ONE_TIME_KEYS_NUMBER)

        val bobOneTimeKeys = bobAccount.oneTimeKeys
        val bobOneTimeKey = bobOneTimeKeys.curve25519.values.first()

        // CREATE ALICE OUTBOUND SESSION and encrypt message to bob
        val aliceSession = Session.createOutboundSession(aliceAccount, bobIdentityKey, bobOneTimeKey)

        val helloClearMsg = "Hello I'm Alice!"

        val encryptedAliceToBobMsg1 = aliceSession.encrypt(helloClearMsg)

        // CREATE BOB INBOUND SESSION and decrypt message from alice
        // init bob session with alice PRE KEY
        val bobSession = Session.createInboundSession(bobAccount, encryptedAliceToBobMsg1.cipherText)

        // DECRYPT MESSAGE FROM ALICE
        val decryptedMsg01 = bobSession.decrypt(encryptedAliceToBobMsg1)

        // MESSAGE COMPARISON: decrypted vs encrypted
        assertEquals(helloClearMsg, decryptedMsg01)

        // BACK/FORTH MESSAGE COMPARISON
        val clearMsg1 = "Hello I'm Bob!"
        val clearMsg2 = "Isn't life grand?"
        val clearMsg3 = "Let's go to the opera."

        // bob encrypts messages
        val encryptedMsg1 = bobSession.encrypt(clearMsg1)
        val encryptedMsg2 = bobSession.encrypt(clearMsg2)
        val encryptedMsg3 = bobSession.encrypt(clearMsg3)

        // serialize alice session
        val aliceSessionJson = Json.encodeToString(SessionSerializer, aliceSession)

        // deserialize alice session
        val aliceSessionDeserial = Json.decodeFromString(SessionSerializer, aliceSessionJson)

        // de-serialized alice session decrypts bob's messages
        val decryptedMsg1 = aliceSessionDeserial.decrypt(encryptedMsg1)
        val decryptedMsg2 = aliceSessionDeserial.decrypt(encryptedMsg2)
        val decryptedMsg3 = aliceSessionDeserial.decrypt(encryptedMsg3)

        // comparison tests
        assertEquals(clearMsg1, decryptedMsg1)
        assertEquals(clearMsg2, decryptedMsg2)
        assertEquals(clearMsg3, decryptedMsg3)

        // clean objects..
        bobAccount.removeOneTimeKeys(bobSession)

        bobAccount.clear()
        aliceAccount.clear()

        bobSession.clear()
        aliceSession.clear()
        aliceSessionDeserial.clear()
    }

    // ****************************************************
    // *************** SANITY CHECK TESTS *****************
    // ****************************************************

    @Test
    fun test06SanityCheckErrors() {
        val ONE_TIME_KEYS_NUMBER = 5L

        // ALICE & BOB ACCOUNTS CREATION
        val bobAccount = Account()
        val aliceAccount = Account()

        // get bob identity key
        val bobIdentityKeys = bobAccount.identityKeys
        val bobIdentityKey = bobIdentityKeys.curve25519

        // get bob one time keys
        bobAccount.generateOneTimeKeys(ONE_TIME_KEYS_NUMBER)

        val bobOneTimeKeys = bobAccount.oneTimeKeys
        val bobOneTimeKey = bobOneTimeKeys.curve25519.values.first()

        // CREATE ALICE SESSION
        val aliceSession = Session.createOutboundSession(aliceAccount, bobIdentityKey, bobOneTimeKey)

        // encrypt properly
        val encryptedMsgToBob = aliceSession.encrypt("A message for bob")

        // SANITY CHECK TESTS FOR: createInboundSessionWithAccount()
        assertFails {
            Session.createInboundSession(bobAccount, INVALID_PRE_KEY)
        }

        // init properly
        val bobSession = Session.createInboundSession(bobAccount, encryptedMsgToBob.cipherText)

        // release objects
        bobAccount.removeOneTimeKeys(bobSession)

        aliceAccount.clear()
        bobAccount.clear()

        aliceSession.clear()
        bobSession.clear()
    }

    companion object {
        private const val INVALID_PRE_KEY = "invalid PRE KEY hu hu!"
        private const val ONE_TIME_KEYS_NUMBER: Long = 4
    }
}
