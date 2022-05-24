package io.github.matrixkt.olm

import kotlin.random.Random
import kotlinx.serialization.json.Json

public actual open class Account(internal val ptr: JsOlm.Account) {

    public actual constructor(random: Random): this(JsOlm.Account()) {
        try {
            ptr.create()
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    public actual val identityKeys: IdentityKeys
        get() {
            val keysStr = ptr.identity_keys()
            return Json.decodeFromString(IdentityKeys.serializer(), keysStr)
        }

    /**
     * Return the largest number of "one time keys" this account can store.
     * @return the max number of "one time keys", -1 otherwise
     */
    public actual val maxNumberOfOneTimeKeys: Long
        get() {
            return ptr.max_number_of_one_time_keys()
        }

    /**
     * Generate a number of new one time keys.
     * If total number of keys stored by this account exceeds [maxNumberOfOneTimeKeys],
     * the old keys are discarded.
     * The corresponding keys are retrieved by [oneTimeKeys].
     * @param numberOfKeys number of keys to generate
     */
    public actual fun generateOneTimeKeys(numberOfKeys: Long, random: Random) {
        ptr.generate_one_time_keys(numberOfKeys)
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
            val keysStr = ptr.one_time_keys()
            return Json.decodeFromString(OneTimeKeys.serializer(), keysStr)
        }

    /**
     * Remove the "one time keys" that the session used from the account.
     * @param session session instance
     */
    public actual fun removeOneTimeKeys(session: Session) {
        ptr.remove_one_time_keys(session.ptr)
    }

    /**
     * Marks the current set of "one time keys" as being published.
     */
    public actual fun markOneTimeKeysAsPublished() {
        ptr.mark_keys_as_published()
    }

    /**
     * Generates a new fallback key. Only one previous fallback key is stored.
     */
    public actual fun generateFallbackKey(random: Random) {
        ptr.generate_fallback_key()
    }

    /**
     * Get fallback key.
     */
    public actual val fallbackKey: OneTimeKeys
        get() {
            val keysStr = ptr.fallback_key()
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
        return ptr.sign(message)
    }

    public actual fun clear () {
       ptr.free();
    }

    public actual fun pickle(key: ByteArray): String {
        return ptr.pickle(key.toString())
    }

    public actual companion object {
        /**
         * Loads an account from a pickled bytes buffer.
         *
         * @see [pickle]
         * @param[key] key used to encrypt
         * @param[pickle] bytes buffer
         */
        public actual fun unpickle(key: ByteArray, pickle: String): Account {
            val result = JsOlm.Account()
            try {
                result.unpickle(key.toString(), pickle)
            } catch (e: Exception) {
                result.free()
                throw e
            }
            return Account(result)
        }
    }
}