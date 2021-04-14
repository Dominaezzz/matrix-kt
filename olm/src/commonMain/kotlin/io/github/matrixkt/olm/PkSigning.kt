package io.github.matrixkt.olm


public expect class PkSigning(seed: ByteArray) {
    public val publicKey: String

    public fun clear()

    public fun sign(message: String): String

    public companion object {
        public val seedLength: Long
    }
}
