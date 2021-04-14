package io.github.matrixkt.olm

/**
 * Result in [InboundGroupSession.decrypt].
 */
public data class GroupMessage(
    val message: String,

    val index: Long
)
