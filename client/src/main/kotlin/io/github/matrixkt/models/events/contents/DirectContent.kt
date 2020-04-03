package io.github.matrixkt.models.events.contents

import io.github.matrixkt.utils.InlineMapSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

@Serializable(DirectContent.Serializer::class)
data class DirectContent(
    val content: Map<String, List<String>>
) : Content(), Map<String, List<String>> by content {
    object Serializer : KSerializer<DirectContent> by InlineMapSerializer(String.serializer(), ListSerializer(String.serializer()), ::DirectContent)
}
