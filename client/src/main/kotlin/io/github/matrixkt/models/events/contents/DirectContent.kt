package io.github.matrixkt.models.events.contents

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer

/**
 * A map of which rooms are considered 'direct' rooms for specific users is kept in
 * ``account_data`` in an event of type ``m.direct``.
 * The content of this event is an object where the keys are the user IDs and
 * values are lists of room ID strings of the 'direct' rooms for that user ID.
 */
@SerialName("m.direct")
@Serializable(DirectContent.TheSerializer::class)
data class DirectContent(
    val content: Map<String, List<String>>
) : Content(), Map<String, List<String>> by content {
    @Serializer(forClass = DirectContent::class)
    object TheSerializer : KSerializer<DirectContent> {
        private val delegate = MapSerializer(String.serializer(), ListSerializer(String.serializer()))

        override fun serialize(encoder: Encoder, value: DirectContent) {
            encoder.encode(delegate, value)
        }

        override fun deserialize(decoder: Decoder): DirectContent {
            return DirectContent(decoder.decode(delegate))
        }
    }
}
