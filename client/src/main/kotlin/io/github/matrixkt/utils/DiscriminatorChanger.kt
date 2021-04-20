package io.github.matrixkt.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

public class DiscriminatorChanger<T : Any>(
    private val tSerializer: KSerializer<T>,
    private val discriminator: String
) : KSerializer<T> {
    override val descriptor: SerialDescriptor get() = tSerializer.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        require(encoder is JsonEncoder)
        val json = Json(encoder.json) { classDiscriminator = discriminator }
        val element = json.encodeToJsonElement(tSerializer, value)
        encoder.encodeJsonElement(element)
    }

    override fun deserialize(decoder: Decoder): T {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        val json = Json(decoder.json) { classDiscriminator = discriminator }
        return json.decodeFromJsonElement(tSerializer, element)
    }
}
