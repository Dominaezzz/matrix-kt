package io.github.matrixkt.olm

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse

class UtilityTest {
    /**
     * Test the signing API
     */
    @Test
    fun test01VerifyEd25519Signing() {
        // language=json
        val message = """{"algorithms":["m.megolm.v1.aes-sha2","m.olm.v1.curve25519-aes-sha2"],"device_id":"YMBYCWTWCG","keys":{"curve25519:YMBYCWTWCG":"KZFa5YUXV2EOdhK8dcGMMHWB67stdgAP4+xwiS69mCU","ed25519:YMBYCWTWCG":"0cEgQJJqjgtXUGp4ZXQQmh36RAxwxr8HJw2E9v1gvA0"},"user_id":"@mxBob14774891254276b253f42-f267-43ec-bad9-767142bfea30:localhost:8480"}"""

        // create account
        val account = Account()

        // sign message
        val messageSignature = account.sign(message)

        // get identities key (finger print key)
        val identityKeys = account.identityKeys

        val fingerPrintKey = identityKeys.getValue(JsonKeys.FINGER_PRINT_KEY)

        // instantiate utility object
        val utility = Utility()

        // verify signature
        utility.verifyEd25519Signature(fingerPrintKey, message, messageSignature)

        // check a bad signature is detected => errorMsg = BAD_MESSAGE_MAC
        val badSignature = "Bad signature Bad signature Bad signature.."

        assertFails {
            utility.verifyEd25519Signature(fingerPrintKey, message, badSignature)
        }

        // check bad fingerprint size => errorMsg = INVALID_BASE64
        val badSizeFingerPrintKey = fingerPrintKey.substring(fingerPrintKey.length / 2)

        assertFails {
            utility.verifyEd25519Signature(badSizeFingerPrintKey, message, messageSignature)
        }

        utility.clear()
        account.clear()
    }

    @Test
    fun test02sha256() {
        val utility = Utility()

        val msgToHash = "The quick brown fox jumps over the lazy dog"

        val hashResult = utility.sha256(msgToHash)
        assertFalse(hashResult.isEmpty())

        utility.clear()
    }
}
