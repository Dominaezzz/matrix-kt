package io.github.matrixkt.models.events

import kotlinx.serialization.json.JsonObject

public typealias MatrixEvent = RoomEvent<JsonObject, JsonObject>

public fun MatrixEvent(
    type: String,
    content: JsonObject,
    eventId: String,
    sender: String,
    originServerTimestamp: Long,
    unsigned: JsonObject? = null,
    roomId: String,
    stateKey: String? = null,
    prevContent: JsonObject? = null
): MatrixEvent {
    return RoomEvent(type, content, eventId, sender, originServerTimestamp, unsigned, roomId, stateKey, prevContent)
}
