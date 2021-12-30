package io.github.matrixkt.clientserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Device identity keys.
 */
@Serializable
public data class DeviceKeys(
    /**
     * The ID of the user the device belongs to.
     * Must match the user ID used when logging in.
     */
    @SerialName("user_id")
    public val userId: String,

    /**
     * The ID of the device these keys belong to.
     * Must match the device ID used when logging in.
     */
    @SerialName("device_id")
    public val deviceId: String,

    /**
     * The encryption algorithms supported by this device.
     *
     * Example: ["m.olm.curve25519-aes-sha256", "m.megolm.v1.aes-sha"].
     */
    public val algorithms: List<String>,

    /**
     * Public identity keys.
     * The names of the properties should be in the format ``<algorithm>:<device_id>``.
     * The keys themselves should be encoded as specified by the key algorithm.
     */
    public val keys: Map<String, String>,

    /**
     * Signatures for the device key object.
     * A map from user ID, to a map from ``<algorithm>:<device_id>`` to the signature.
     * The signature is calculated using the process described at `Signing JSON`_.
     */
    public val signatures: Map<String, Map<String, String>>,

    /**
     * Additional data added to the device key information by intermediate servers,
     * and not covered by the signatures.
     */
    public val unsigned: UnsignedDeviceInfo? = null
)
