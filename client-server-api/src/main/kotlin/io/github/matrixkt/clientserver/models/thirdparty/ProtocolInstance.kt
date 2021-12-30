package io.github.matrixkt.clientserver.models.thirdparty

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public class ProtocolInstance(
    /**
     * A human-readable description for the protocol, such as the name.
     */
    public val desc: String,

    /**
     * An optional content URI representing the protocol.
     * Overrides the one provided at the higher level [Protocol] object.
     */
    public val icon: String? = null,

    /**
     * Preset values for [fields] the client may use to search by.
     */
    public val fields: JsonObject,

    /**
     * A unique identifier across all instances.
     */
    @SerialName("network_id")
    public val networkId: String
)
