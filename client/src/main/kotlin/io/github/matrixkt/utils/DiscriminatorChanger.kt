package io.github.matrixkt.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer

class DiscriminatorChanger<T : Any>(
    delegateSerializer: KSerializer<T>,
    private val classDiscriminator: String
) : JsonTransformingSerializer<T>(delegateSerializer) {
    private val actualDiscriminator = "type"

    @ExperimentalStdlibApi
    override fun transformDeserialize(element: JsonElement): JsonElement {
        require(element is JsonObject)

        return JsonObject(buildMap<String, JsonElement> {
            putAll(element)
            val discriminatorValue = remove(classDiscriminator)
            checkNotNull(discriminatorValue)
            put(actualDiscriminator, discriminatorValue)
        })
    }

    @ExperimentalStdlibApi
    override fun transformSerialize(element: JsonElement): JsonElement {
        require(element is JsonObject)

        return JsonObject(buildMap<String, JsonElement> {
            putAll(element)
            val discriminatorValue = remove(actualDiscriminator)
            checkNotNull(discriminatorValue)
            put(classDiscriminator, discriminatorValue)
        })
    }
}
