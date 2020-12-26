package io.github.matrixkt.olm

import colm.internal.*
import kotlinx.cinterop.*
import platform.posix.size_t

actual class PkSigning actual constructor(seed: ByteArray) {
    private val ptr = genericInit(::olm_pk_signing, ::olm_pk_signing_size)

    actual val publicKey: String

    init {
        val publicKeyLength = olm_pk_signing_public_key_length()
        val publicKey = ByteArray(publicKeyLength.convert())

        val result = olm_pk_signing_key_from_seed(ptr,
            publicKey.refTo(0), publicKeyLength,
            seed.refTo(0), seed.size.convert())
        checkError(result)

        this.publicKey = publicKey.decodeToString()
    }

    actual fun clear() {
        olm_clear_pk_signing(ptr)
        nativeHeap.free(ptr)
    }

    actual fun sign(message: String): String {
        val signatureLength = olm_pk_signature_length()
        val signature = UByteArray(signatureLength.convert())

        message.withNativeRead { messagePtr, messageLen ->
            val result = olm_pk_sign(ptr,
                messagePtr?.reinterpret(), messageLen,
                signature.refTo(0), signatureLength)
            checkError(result)
        }
        return signature.asByteArray().decodeToString()
    }

    private fun checkError(result: size_t) {
        genericCheckError(ptr, result, ::olm_pk_signing_last_error)
    }

    actual companion object {
        actual val seedLength: Long get() = olm_pk_signing_seed_length().convert()
    }
}
