package io.github.matrixkt.models.thirdparty

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
class User(
    /**
     * A Matrix User ID represting a third party user.
     */
    @SerialName("userid")
    val userId: String,

    /**
     * The protocol ID that the third party location is a part of.
     */
    val protocol: String,

    /**
     * Information used to identify this third party location.
     */
    val fields: JsonObject
)
