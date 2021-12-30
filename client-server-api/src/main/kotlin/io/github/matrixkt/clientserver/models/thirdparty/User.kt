package io.github.matrixkt.clientserver.models.thirdparty

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public class User(
    /**
     * A Matrix User ID represting a third party user.
     */
    @SerialName("userid")
    public val userId: String,

    /**
     * The protocol ID that the third party location is a part of.
     */
    public val protocol: String,

    /**
     * Information used to identify this third party location.
     */
    public val fields: JsonObject
)
