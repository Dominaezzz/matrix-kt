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
    fun test02PrivateKey() {
        val privateKey = byteArrayOf(
            0x77.toByte(), 0x07.toByte(), 0x6D.toByte(), 0x0A.toByte(),
            0x73.toByte(), 0x18.toByte(), 0xA5.toByte(), 0x7D.toByte(),
            0x3C.toByte(), 0x16.toByte(), 0xC1.toByte(), 0x72.toByte(),
            0x51.toByte(), 0xB2.toByte(), 0x66.toByte(), 0x45.toByte(),
            0xDF.toByte(), 0x4C.toByte(), 0x2F.toByte(), 0x87.toByte(),
            0xEB.toByte(), 0xC0.toByte(), 0x99.toByte(), 0x2A.toByte(),
            0xB1.toByte(), 0x77.toByte(), 0xFB.toByte(), 0xA5.toByte(),
            0x1D.toByte(), 0xB9.toByte(), 0x2C.toByte(), 0x2A.toByte()
        )
        assertEquals(PkDecryption.privateKeyLength, privateKey.size.toLong())

        val decryption = PkDecryption.fromPrivate(privateKey)

        val privateKeyCopy = decryption.privateKey
        assertTrue(privateKey contentEquals privateKeyCopy)

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
