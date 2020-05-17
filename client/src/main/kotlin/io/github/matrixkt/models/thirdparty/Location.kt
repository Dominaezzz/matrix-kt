package io.github.matrixkt.models.thirdparty

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
class Location(
    /**
     * An alias for a matrix room.
     */
    val alias: String,

    /**
     * The protocol ID that the third party location is a part of.
     */
    val protocol: String,

    /**
     * Information used to identify this third party location.
     */
    val fields: JsonObject
)
