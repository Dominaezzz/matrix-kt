package io.github.matrixkt.models.thirdparty

import kotlinx.serialization.Serializable

@Serializable
class FieldType(
    /**
     * A regular expression for validation of a field's value.
     * This may be relatively coarse to verify the value as the application service providing this protocol may apply additional validation or filtering.
     */
    val regexp: String,

    /**
     * An placeholder serving as a valid example of the field value.
     */
    val placeholder: String
)
