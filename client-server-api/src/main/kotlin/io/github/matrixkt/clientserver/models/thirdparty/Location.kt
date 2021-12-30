package io.github.matrixkt.clientserver.models.thirdparty

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public class Location(
    /**
     * An alias for a matrix room.
     */
    public val alias: String,

    /**
     * The protocol ID that the third party location is a part of.
     */
    public val protocol: String,

    /**
     * Information used to identify this third party location.
     */
    public val fields: JsonObject
)
