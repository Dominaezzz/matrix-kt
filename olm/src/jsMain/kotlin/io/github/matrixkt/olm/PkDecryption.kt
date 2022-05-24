package io.github.matrixkt.olm

import org.khronos.webgl.Uint8Array
import kotlin.random.Random

public actual class PkDecryption {
    private val ptr: JsOlm.PkDecryption
    public actual val publicKey: String

    private constructor(ptr: JsOlm.PkDecryption, publicKey: String) {
        this.ptr = ptr
        this.publicKey = publicKey
    }

    private constructor(ptr: JsOlm.PkDecryption) {
        this.ptr = ptr
        this.publicKey = ""
    }

    public actual constructor(random: Random) {
        ptr = JsOlm.PkDecryption()
        try {
            this.publicKey = ptr.generate_key()
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    public actual fun clear() {
        ptr.free()
    }

    public actual val privateKey: ByteArray
        get() {
            val key: Uint8Array = ptr.get_private_key()
            val t: String = key.toString()
            return t.encodeToByteArray()
        }

    public actual fun decrypt(message: PkMessage): String {
        return ptr.decrypt(message.ephemeralKey, message.mac, message.cipherText)
    }

    public actual fun pickle(key: ByteArray): String {
        return ptr.pickle(key.toString())
    }

    public actual companion object {
        public actual val publicKeyLength: Long
            get() {
                // There is no equivalent in matrix-org/olm so we need to do it this way
                val pkdec = JsOlm.PkDecryption()
                val pubKey: String
                try {
                    pubKey = pkdec.generate_key()
                    pkdec.free()
                    return pubKey.length.toLong()
                } catch (e: Exception) {
                    pkdec.free()
                    throw e
                }
            }

        public actual val privateKeyLength: Long get() = JsOlm.PRIVATE_KEY_LENGTH;

        public actual fun fromPrivate(privateKey: ByteArray): PkDecryption {
            val pkdec = JsOlm.PkDecryption()
            val uint8arrayKey = Uint8Array(privateKey.toTypedArray())
            val pubKey: String
            try {
                pubKey = pkdec.init_with_private_key(uint8arrayKey)
            } catch (e: Exception) {
                pkdec.free()
                throw e
            }

            return PkDecryption(pkdec, pubKey)
        }

        public actual fun unpickle(key: ByteArray, pickle: String): PkDecryption {
            val pkdec = JsOlm.PkDecryption()
            try {
                val privateKey: ByteArray = pkdec.unpickle(key.toString(), pickle).encodeToByteArray()
                return fromPrivate(privateKey)
            } catch (e: Exception) {
                pkdec.free()
                throw e
            }
        }
    }
}