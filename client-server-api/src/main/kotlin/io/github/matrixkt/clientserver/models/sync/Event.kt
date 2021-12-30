package io.github.matrixkt.clientserver.models.sync

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class Event(
    /**
     * The fields in this object will vary depending on the type of event.
     * When interacting with the REST API, this is the HTTP body.
     */
    val content: JsonObject,

    /**
     * The type of event.
     * This SHOULD be namespaced similar to Java package naming conventions e.g. 'com.example.subdomain.event.type'
     */
    val type: String,

    /**
     * The Matrix user ID of the user who sent this event.
     */
    val sender: String? = null
)
