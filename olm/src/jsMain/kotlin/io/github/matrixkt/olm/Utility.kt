package io.github.matrixkt.olm

/**
 * Olm SDK helper class.
 */
public actual class Utility {
    private val ptr = JsOlm.Utility()

    public actual fun clear() {
        ptr.free()
    }

    /**
     * Verify an ed25519 signature.
     *
     * An exception is thrown if the operation fails.
     * @param key the ed25519 key (fingerprint key)
     * @param message the signed message
     * @param signature the base64-encoded message signature to be checked.
     */
    public actual fun verifyEd25519Signature(key: String, message: String, signature: String) {
        ptr.ed25519_verify(key, message, signature)
    }

    /**
     * Compute the hash(SHA-256) value of the string given in parameter.
     *
     * The hash value is the returned by the method.
     * @param input message to be hashed
     * @return hash value if operation succeed, null otherwise
     */
    public actual fun sha256(input: String): String {
        return ptr.sha256(input)
    }

}
