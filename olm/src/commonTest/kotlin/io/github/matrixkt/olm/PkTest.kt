package io.github.matrixkt.olm

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class PkTest {
    @Test
    fun test01EncryptAndDecrypt() {
        val decryption = PkDecryption()
        val encryption = PkEncryption(decryption.publicKey)

        println("Ephemeral Key: ${decryption.publicKey}")

        val clearMessage = "Public key test"
        val message = encryption.encrypt(clearMessage)

        println("message: ${message.cipherText} ${message.mac} ${message.ephemeralKey}")

        val decryptedMessage = decryption.decrypt(message)
        assertEquals(clearMessage, decryptedMessage)

        encryption.clear()
        decryption.clear()
    }

    @Test
    fun test03Signing() {
        val seed = Random.nextBytes(PkSigning.seedLength.toInt()) // Check this PkSigning.generateSeed()
        val signing = PkSigning(seed)

        val message =
            "We hold these truths to be self-evident, that all men are created equal, that they are endowed by their Creator with certain unalienable Rights, that among these are Life, Liberty and the pursuit of Happiness."
        val signature = signing.sign(message)

        val utility = Utility()
        utility.verifyEd25519Signature(signing.publicKey, message, signature)

        signing.clear()
        utility.clear()
    }
}
