package io.github.matrixkt.olm

import colm.internal.NativeSize
import colm.internal.OlmLibrary.olm_clear_pk_signing
import colm.internal.OlmLibrary.olm_pk_sign
import colm.internal.OlmLibrary.olm_pk_signature_length
import colm.internal.OlmLibrary.olm_pk_signing
import colm.internal.OlmLibrary.olm_pk_signing_key_from_seed
import colm.internal.OlmLibrary.olm_pk_signing_last_error
import colm.internal.OlmLibrary.olm_pk_signing_public_key_length
import colm.internal.OlmLibrary.olm_pk_signing_seed_length
import colm.internal.OlmLibrary.olm_pk_signing_size
import com.sun.jna.Native
import com.sun.jna.Pointer

actual class PkSigning {
    private val ptr = genericInit(::olm_pk_signing, ::olm_pk_signing_size)

    actual fun clear() {
        olm_clear_pk_signing(ptr)
        Native.free(Pointer.nativeValue(ptr.pointer))
    }

    actual fun fromSeed(seed: ByteArray): String {
        return seed.withNativeRead { seedPtr ->
            val publicKeyLength = olm_pk_signing_public_key_length()
            withAllocation(publicKeyLength.toLong()) { publicKey ->
                val result = olm_pk_signing_key_from_seed(ptr,
                    publicKey, publicKeyLength,
                    seedPtr, NativeSize(seed.size))
                checkError(result)
                publicKey.toKString(publicKeyLength.toInt())
            }
        }
    }

    actual fun sign(message: String): String {
        val signatureLength = olm_pk_signature_length()
        return message.withNativeRead { messagePtr, messageLen ->
            withAllocation(signatureLength.toLong()) {
                val result = olm_pk_sign(ptr, messagePtr, messageLen,
                    it, signatureLength)
                checkError(result)
                it.toKString(signatureLength.toInt())
            }
        }
    }

    private fun checkError(result: NativeSize) {
        genericCheckError(ptr, result, ::olm_pk_signing_last_error)
    }

    actual companion object {
        actual val seedLength: Long get() = olm_pk_signing_seed_length().toLong()
    }
}
