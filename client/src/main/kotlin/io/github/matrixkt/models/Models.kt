package io.github.matrixkt.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
public data class ThirdPartySigned(
    /**
     * The Matrix ID of the user who issued the invite.
     */
    val sender: String,

    /**
     * The Matrix ID of the invitee.
     */
    val mixid: String,

    /**
     * The state key of the `m.third_party_invite` event.
     */
    val token: String,

    /**
     * A signatures object containing a signature of the entire signed object.
     */
    val signatures: Map<String, Map<String, String>>
)

@Serializable
public class PublicRoomsChunk(
    /**
     * Aliases of the room. May be empty.
     */
    public val aliases: List<String> = emptyList(),

    /**
     * The URL for the room's avatar, if one is set.
     */
    @SerialName("avatar_url")
    public val avatarUrl: String? = null,

    /**
     * The canonical alias of the room, if any.
     */
    @SerialName("canonical_alias")
    public val canonicalAlias: String? = null,

    /**
     * Whether guest users may join the room and participate in it.
     * If they can, they will be subject to ordinary power level
     * rules like any other user.
     */
    @SerialName("guest_can_join")
    public val guestCanJoin: Boolean,

    /**
     * The name of the room, if any.
     */
    public val name: String? = null,

    /**
     * The number of members joined to the room.
     */
    @SerialName("num_joined_members")
    public val numJoinedMembers: Long,
    /**
     * The ID of the room.
     */
    @SerialName("room_id")
    public val roomId: String,

    /**
     * The topic of the room, if any.
     */
    public val topic: String? = null,

    /**
     * Whether the room may be viewed by guest users without joining.
     */
    @SerialName("world_readable")
    public val worldReadable: Boolean
)

@Serializable
public enum class Direction {
    @SerialName("f")
    FORWARD,
    @SerialName("b")
    BACKWARD;
}

@Serializable
public enum class ThumbnailMethod {
    @SerialName("crop")
    CROP,
    @SerialName("scale")
    SCALE;
}

@Serializable
public data class UrlPreviewResponse(
    /**
     * The byte-size of the image. Omitted if there is no image attached.
     */
    @SerialName("matrix:image:size")
    val imageSize: Long? = null,

    /**
     * An [MXC URI](https://matrix.org/docs/spec/client_server/r0.5.0#mxc-uri) to the image. Omitted if there is no image.
     */
    @SerialName("og:image")
    val image: String? = null,

    @SerialName("og:title")
    val title: String? = null,

    @SerialName("og:description")
    val description: String? = null,

    @SerialName("og:image:type")
    val imageType: String? = null,

    @SerialName("og:image:height")
    val imageHeight: Int? = null,

    @SerialName("og:image:width")
    val imageWidth: Int? = null
)

@Serializable
public data class ThreePidCredentials(
    /**
     * The client secret used in the session with the identity server.
     */
    @SerialName("client_secret")
    val clientSecret: String,

    /**
     * The identity server to use.
     */
    @SerialName("id_server")
    val idServer: String,

    /**
     * The session identifier given by the identity server.
     */
    val sid: String
)

@Serializable
public data class Capabilities(
    /**
     * Capability to indicate if the user can change their password.
     */
    @SerialName("m.change_password")
    val changePassword: ChangePasswordCapability? = null,

    /**
     * The room versions the server supports.
     */
    @SerialName("m.room_versions")
    val roomVersions: RoomVersionsCapability? = null
)

@Serializable
public data class ChangePasswordCapability(
    /**
     * True if the user can change their password, false otherwise.
     */
    val enabled: Boolean
)

@Serializable
public data class RoomVersionsCapability(
    /**
     * The default room version the server is using for new rooms.
     */
    val default: String,

    /**
     * A detailed description of the room versions the server supports.
     */
    val available: Map<String, RoomVersionStability>
)

/**
 * The stability of the room version.
 */
@Serializable
public enum class RoomVersionStability {
    @SerialName("stable")
    STABLE,
    @SerialName("unstable")
    UNSTABLE;
}

@Serializable
public data class KeyObject(
    /**
     * The key, encoded using unpadded base64.
     */
    val key: String,

    /**
     * Signature for the device. Mapped from user ID to signature object.
     */
    val signatures: Map<String, Map<String, String>>
)

public object OneTimeKeySerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("OneTimeKey")

    override fun deserialize(decoder: Decoder): Any {
        if (decoder !is JsonDecoder) throw SerializationException("This class can be loaded only by Json")

        val tree = decoder.decodeJsonElement()
        return if (tree is JsonObject) {
            decoder.json.decodeFromJsonElement(KeyObject.serializer(), tree)
        } else if (tree is JsonPrimitive && tree.isString) {
            tree.content
        } else {
            throw SerializationException("Expected JsonObject or JsonLiteral")
        }
    }

    override fun serialize(encoder: Encoder, value: Any) {
        return when (value) {
            is KeyObject -> encoder.encodeSerializableValue(KeyObject.serializer(), value)
            is String -> encoder.encodeString(value)
            else -> TODO("Only KeyObject and String supported")
        }
    }
}

@Serializable
public data class UnsignedDeviceInfo(
    /**
     * The display name which the user set on the device.
     */
    @SerialName("device_display_name")
    val deviceDisplayName: String? = null
)
