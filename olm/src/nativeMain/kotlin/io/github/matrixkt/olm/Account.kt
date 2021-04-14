package io.github.matrixkt.olm

import colm.internal.*
import kotlinx.cinterop.*
import kotlinx.serialization.json.Json
import platform.posix.size_t
import kotlin.random.Random

/**
 * Account class used to create Olm sessions in conjunction with [Session] class.
 *
 * [Account] provides APIs to retrieve the Olm keys.
 *
 * Detailed implementation guide is available at [Implementing End-to-End Encryption in Matrix clients](http://matrix.org/docs/guides/e2e_implementation.html).
 */
public actual class Account private constructor(internal val ptr: CPointer<OlmAccount>) {
    public actual constructor(random: Random): this(genericInit(::olm_account, ::olm_account_size)) {
        try {
            val randomSize = olm_create_account_random_length(ptr)
            val result = withRandomBuffer(randomSize, random) { randomBuff ->
                olm_create_account(ptr, randomBuff, randomSize)
            }
            checkError(result)
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    public actual fun clear() {
        olm_clear_account(ptr)
        nativeHeap.free(ptr)
    }

    /**
     * Return the identity keys (identity and fingerprint keys) in a dictionary.
     * @sample
     * {
     *  "curve25519":"Vam++zZPMqDQM6ANKpO/uAl5ViJSHxV9hd+b0/fwRAg",
     *  "ed25519":"+v8SOlOASFTMrX3MCKBM4iVnYoZ+JIjpNt1fi8Z9O2I"
     * }
     * @return identity keys dictionary if operation succeeds, null otherwise
     */
    public actual val identityKeys: IdentityKeys
        get() {
            // identity keys allocation
            val identityKeysLength = olm_account_identity_keys_length(ptr)
            val identityKeysBytes = ByteArray(identityKeysLength.convert())

            // retrieve key pairs in identityKeysBytesPtr
            val keysResult = olm_account_identity_keys(ptr, identityKeysBytes.refTo(0), identityKeysLength)
            checkError(keysResult)

            val identityKeysStr = identityKeysBytes.decodeToString()
            return Json.decodeFromString(IdentityKeys.serializer(), identityKeysStr)
        }

    /**
     * Return the largest number of "one time keys" this account can store.
     * @return the max number of "one time keys", -1 otherwise
     */
    public actual val maxNumberOfOneTimeKeys: Long
        get() {
            return olm_account_max_number_of_one_time_keys(ptr).convert()
        }

    /**
     * Generate a number of new one time keys.
     * If total number of keys stored by this account exceeds [maxNumberOfOneTimeKeys],
     * the old keys are discarded.
     * The corresponding keys are retrieved by [oneTimeKeys].
     * @param numberOfKeys number of keys to generate
     */
    public actual fun generateOneTimeKeys(numberOfKeys: Long, random: Random) {
        // keys memory allocation
        val randomLength = olm_account_generate_one_time_keys_random_length(ptr, numberOfKeys.convert())
        val result = withRandomBuffer(randomLength, random) { randomBuffer ->
            olm_account_generate_one_time_keys(ptr, numberOfKeys.convert(), randomBuffer, randomLength)
        }
        checkError(result)
    }

    /**
     * Return the "one time keys" in a dictionary.
     * The number of "one time keys", is specified by [generateOneTimeKeys]
     * @sample
     * { "curve25519":
     *  {
     *      "AAAABQ":"qefVZd8qvjOpsFzoKSAdfUnJVkIreyxWFlipCHjSQQg",
     *      "AAAABA":"/X8szMU+p+lsTnr56wKjaLgjTMQQkCk8EIWEAilZtQ8",
     *      "AAAAAw":"qxNxxFHzevFntaaPdT0fhhO7tc7pco4+xB/5VRG81hA",
     *  }
     * }
     * Note: these keys are to be published on the server.
     * @return one time keys in string dictionary.
     */
    public actual val oneTimeKeys: OneTimeKeys
        get() {
            val keysLength = olm_account_one_time_keys_length(ptr)
            val keysBytes = ByteArray(keysLength.convert())

            val keysResult = olm_account_one_time_keys(ptr, keysBytes.refTo(0), keysLength)
            checkError(keysResult)

            val keysStr = keysBytes.decodeToString()
            return Json.decodeFromString(OneTimeKeys.serializer(), keysStr)
        }

    /**
     * Remove the "one time keys" that the session used from the account.
     * @param session session instance
     */
    public actual fun removeOneTimeKeys(session: Session) {
        val result = olm_remove_one_time_keys(ptr, session.ptr)
        checkError(result)
    }

    /**
     * Marks the current set of "one time keys" as being published.
     */
    public actual fun markOneTimeKeysAsPublished() {
        val result = olm_account_mark_keys_as_published(ptr)
        checkError(result)
    }

    /**
     * Generates a new fallback key. Only one previous fallback key is stored.
     */
    public actual fun generateFallbackKey(random: Random) {
        val randomLength = olm_account_generate_fallback_key_random_length(ptr)
        val result = withRandomBuffer(randomLength, random) { randomBuffer ->
            olm_account_generate_fallback_key(ptr, randomBuffer, randomLength)
        }
        checkError(result)
    }

    /**
     * Get fallback key.
     */
    public actual val fallbackKey: OneTimeKeys
        get() {
            val keysLength = olm_account_fallback_key_length(ptr)
            val keysBytes = ByteArray(keysLength.convert())

            val keysResult = olm_account_fallback_key(ptr, keysBytes.refTo(0), keysLength)
            checkError(keysResult)

            val keysStr = keysBytes.decodeToString()
            return Json.decodeFromString(OneTimeKeys.serializer(), keysStr)
        }

    /**
     * Sign a message with the ed25519 fingerprint key for this account.
     *
     * The signed message is returned by the method.
     * @param message message to sign
     * @return the signed message
     */
    public actual fun sign(message: String): String {
        val messageToSign = message.encodeToByteArray()
        val messageLength = messageToSign.size

        // signature memory allocation
        val signatureLength = olm_account_signature_length(ptr)
        val signedMessage = ByteArray(signatureLength.convert())

        val result = olm_account_sign(
            ptr,
            if (messageToSign.isNotEmpty()) messageToSign.refTo(0) else null, messageLength.convert(),
            signedMessage.refTo(0), signatureLength)
        checkError(result)

        val resultStr = signedMessage.decodeToString()
        messageToSign.fill(0)
        return resultStr
    }

    /**
     * Return an account as a bytes buffer.
     *
     * The account is serialized and encrypted with [key].
     *
     * @param[key] encryption key
     * @return the account as bytes buffer
     */
    public actual fun pickle(key: ByteArray): String {
        return genericPickle(ptr, key, ::olm_pickle_account_length, ::olm_pickle_account, ::checkError)
    }

    private fun checkError(result: size_t) {
        genericCheckError(ptr, result, ::olm_account_last_error)
    }

    public actual companion object {
        /**
         * Loads an account from a pickled bytes buffer.
         *
         * @see [pickle]
         * @param[data] bytes buffer
         * @param[key] key used to encrypt
         * @exception Exception the exception
         */
        public actual fun unpickle(key: ByteArray, pickle: String): Account {
            val obj = Account(genericInit(::olm_account, ::olm_account_size))
            try {
                obj.run {
                    genericUnpickle(ptr, key, pickle, ::olm_unpickle_account, ::checkError)
                }
            } catch (e: Exception) {
                obj.clear() // Prevent leak
                throw e
            }
            return obj
        }
    }
}
