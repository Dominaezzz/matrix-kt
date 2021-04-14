package io.github.matrixkt.olm

import kotlinx.serialization.Serializable

@Serializable
public data class OneTimeKeys(
    /**
     * As well as the identity key, each device creates a number of Curve25519 key pairs which are
     * also used to establish Olm sessions, but can only be used once. Once again, the private part
     * remains on the device but the public part is published to the Matrix network
     */
    val curve25519: Map<String, String>
)
