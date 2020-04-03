package io.github.matrixkt.models.events.contents

import io.github.matrixkt.utils.InlineMapSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

/**
 * A map of which rooms are considered 'direct' rooms for specific users is kept in
 * ``account_data`` in an event of type ``m.direct``.
 * The content of this event is an object where the keys are the user IDs and
 * values are lists of room ID strings of the 'direct' rooms for that user ID.
 */
@SerialName("m.direct")
@Serializable(DirectContent.Serializer::class)
data class DirectContent(
    val content: Map<String, List<String>>
) : Content(), Map<String, List<String>> by content {
    object Serializer : KSerializer<DirectContent> by InlineMapSerializer(String.serializer(), ListSerializer(String.serializer()), ::DirectContent)
}
