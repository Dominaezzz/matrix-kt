package io.github.matrixkt.models.events

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonObject

public class RoomEventSerializer<Content : Any, UnsignedData : Any>(
    private val contentSerializer: KSerializer<Content>,
    private val unsignedDataSerializer: KSerializer<UnsignedData>
) : KSerializer<RoomEvent<Content, UnsignedData>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RoomEvent") {
        element<String>("type")
        element<JsonObject>("content")
        element<String>("event_id")
        element<String>("sender")
        element<Long>("origin_server_ts")
        element<JsonObject>("unsigned", isOptional = true)
        element<String>("state_key", isOptional = true)
        element<JsonObject>("prev_content", isOptional = true)
        element<String>("room_id")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): RoomEvent<Content, UnsignedData> {
        var type: String? = null
        var content: Content? = null
        var eventId: String? = null
        var sender: String? = null
        var originServerTimestamp: Long? = null
        var unsigned: UnsignedData? = null
        var stateKey: String? = null
        var prevContent: Content? = null
        var roomId: String? = null

        val cd = decoder.beginStructure(descriptor)
        while (true) {
            when (val index = cd.decodeElementIndex(descriptor)) {
                0 -> type = cd.decodeStringElement(descriptor, index)
                1 -> content = cd.decodeSerializableElement(descriptor, index, contentSerializer, content)
                2 -> eventId = cd.decodeStringElement(descriptor, index)
                3 -> sender = cd.decodeStringElement(descriptor, index)
                4 -> originServerTimestamp = cd.decodeLongElement(descriptor, index)
                5 -> unsigned = cd.decodeSerializableElement(descriptor, index, unsignedDataSerializer.nullable, unsigned)
                6 -> stateKey = cd.decodeSerializableElement(descriptor, index, String.serializer().nullable, stateKey)
                7 -> prevContent = cd.decodeSerializableElement(descriptor, index, contentSerializer.nullable, prevContent)
                8 -> roomId = cd.decodeStringElement(descriptor, index)
                CompositeDecoder.DECODE_DONE -> break
                CompositeDecoder.UNKNOWN_NAME -> continue
                else -> throw SerializationException("Decoded unknown index $index")
            }
        }
        cd.endStructure(descriptor)

        fun missingField(fieldName: String): Nothing {
            throw SerializationException("Field '$fieldName' is required, but it was missing")
        }

        if (roomId == null) missingField("room_id")
        if (type == null) missingField("type")
        if (content == null) missingField("content")
        if (eventId == null) missingField("event_id")
        if (sender == null) missingField("sender")
        if (originServerTimestamp == null) missingField("origin_server_ts")

        return RoomEvent(type, content, eventId, sender, originServerTimestamp, unsigned, roomId, stateKey, prevContent)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: RoomEvent<Content, UnsignedData>) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeSerializableElement(descriptor, 1, contentSerializer, value.content)
            encodeStringElement(descriptor, 2, value.eventId)
            encodeStringElement(descriptor, 3, value.sender)
            encodeLongElement(descriptor, 4, value.originServerTimestamp)
            encodeNullableSerializableElement(descriptor, 5, unsignedDataSerializer, value.unsigned)
            encodeNullableSerializableElement(descriptor, 6, String.serializer(), value.stateKey)
            encodeNullableSerializableElement(descriptor, 7, contentSerializer, value.prevContent)
            encodeStringElement(descriptor, 8, value.roomId)
        }
    }
}
