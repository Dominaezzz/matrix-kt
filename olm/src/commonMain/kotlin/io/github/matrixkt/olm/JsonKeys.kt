package io.github.matrixkt.olm

object JsonKeys {
    /**
     * As well as the identity key, each device creates a number of Curve25519 key pairs which are
     * also used to establish Olm sessions, but can only be used once. Once again, the private part
     * remains on the device. but the public part is published to the Matrix network
     */
    const val ONE_TIME_KEY = "curve25519"

    /**
     * Curve25519 identity key is a public-key cryptographic system which can be used to establish a shared secret.
     * In Matrix, each device has a long-lived Curve25519 identity key which is used to establish
     * Olm sessions with that device. The private key should never leave the device, but the
     * public part is signed with the Ed25519 fingerprint key ([FINGER_PRINT_KEY]) and published to the network.
     */
    const val IDENTITY_KEY = "curve25519"

    /**
     * Ed25519 finger print is a public-key cryptographic system for signing messages.
     *
     * In Matrix, each device has an Ed25519 key pair which serves to identify that device.
     * The private the key should never leave the device,
     * but the public part is published to the Matrix network.
     */
    const val FINGER_PRINT_KEY = "ed25519"
}
