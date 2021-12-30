package io.github.matrixkt.clientserver.models.thirdparty

import kotlinx.serialization.Serializable

@Serializable
public class FieldType(
    /**
     * A regular expression for validation of a field's value.
     * This may be relatively coarse to verify the value as the application service providing this protocol may apply additional validation or filtering.
     */
    public val regexp: String,

    /**
     * An placeholder serving as a valid example of the field value.
     */
    public val placeholder: String
)
