package io.github.matrixkt.models.thirdparty

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Protocol(
    /**
     * Fields which may be used to identify a third party user.
     * These should be ordered to suggest the way that entities may be grouped, where higher groupings are ordered first.
     * For example, the name of a network should be searched before the nickname of a user.
     */
    @SerialName("user_fields")
    val userFields: List<String>,

    /**
     * Fields which may be used to identify a third party location.
     * These should be ordered to suggest the way that entities may be grouped, where higher groupings are ordered first.
     * For example, the name of a network should be searched before the name of a channel.
     */
    @SerialName("location_fields")
    val locationFields: List<String>,

    /**
     * A content URI representing an icon for the third party protocol.
     */
    val icon: String? = null,

    /**
     * The type definitions for the fields defined in the [userFields] and [locationFields].
     * Each entry in those arrays MUST have an entry here.
     * The string key for this object is field name itself.
     *
     * May be an empty object if no fields are defined.
     */
    @SerialName("field_types")
    val fieldTypes: Map<String, FieldType>,

    /**
     * A list of objects representing independent instances of configuration.
     * For example, multiple networks on IRC if multiple are provided by the same application service.
     */
    val instances: List<ProtocolInstance>
)
