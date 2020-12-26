package io.github.matrixkt.olm

import kotlinx.serialization.Serializable

@Serializable
data class IdentityKeys(
    /**
     * Curve25519 identity key is a public-key cryptographic system which can be used to establish a shared secret.
     * In Matrix, each device has a long-lived Curve25519 identity key which is used to establish
     * Olm sessions with that device. The private key should never leave the device, but the
     * public part is signed with the Ed25519 fingerprint key ([ed25519]) and published to the network.
     */
    val curve25519: String,

    /**
     * Ed25519 finger print is a public-key cryptographic system for signing messages.
     *
     * In Matrix, each device has an Ed25519 key pair which serves to identify that device.
     * The private the key should never leave the device,
     * but the public part is published to the Matrix network.
     */
    val ed25519: String
)
