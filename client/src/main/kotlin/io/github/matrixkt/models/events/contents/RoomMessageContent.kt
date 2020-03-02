package io.github.matrixkt.models.events.contents

import io.github.matrixkt.models.EncryptedFile
import io.github.matrixkt.models.events.contents.msginfo.*
import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.content


@Serializable(RoomMessageContent.Companion::class)
sealed class RoomMessageContent : Content() {
    /**
     * The textual representation of this message.
     */
    abstract val body: String

//    /**
//     * The type of message, e.g. m.image, m.text
//     */
//    @SerialName("msgtype")
//    abstract val msgType: String

    /**
     * This message is the most basic message and is used to represent text.
     */
    @Serializable
    @SerialName("m.text")
    data class Text(
        /**
         * The body of the message.
         */
        override val body: String,

        /**
         * The format used in the [formattedBody].
         * Currently only `org.matrix.custom.html` is supported.
         */
        val format: String? = null,

        /**
         * The formatted version of the [body].
         * This is required if [format] is specified.
         */
        @SerialName("formatted_body")
        val formattedBody: String? = null
    ) : RoomMessageContent()

    /**
     * This message is similar to `m.text` except that the sender is 'performing' the action contained in the [body] key,
     * similar to `/me` in IRC.
     * This message should be prefixed by the name of the sender.
     * This message could also be represented in a different colour to distinguish it from regular `m.text` messages.
     */
    @Serializable
    @SerialName("m.emote")
    data class Emote(
        /**
         * The emote action to perform.
         */
        override val body: String,

        /**
         * The format used in the [formattedBody].
         * Currently only `org.matrix.custom.html` is supported.
         */
        val format: String,

        /**
         * The formatted version of the [body].
         * This is required if [format] is specified.
         */
        @SerialName("formatted_body")
        val formattedBody: String
    ) : RoomMessageContent()

    /**
     * The `m.notice` type is primarily intended for responses from automated clients.
     * An `m.notice` message must be treated the same way as a regular `m.text` message with two exceptions.
     * Firstly, clients should present `m.notice` messages to users in a distinct manner, and secondly,
     * `m.notice` messages must never be automatically responded to.
     * This helps to prevent infinite-loop situations where two automated clients continuously exchange messages.
     */
    @Serializable
    @SerialName("m.notice")
    data class Notice(
        /**
         * The notice text to send.
         */
        override val body: String
    ) : RoomMessageContent()

    /**
     * This message represents a single image and an optional thumbnail.
     */
    @Serializable
    @SerialName("m.image")
    data class Image(
        /**
         * A textual representation of the image.
         * This could be the alt text of the image, the filename of the image,
         * or some kind of content description for accessibility e.g. 'image attachment'.
         */
        override val body: String,

        /**
         * Metadata about the image referred to in [url].
         */
        val info: ImageInfo? = null,

        /**
         * Required if the file is unencrypted.
         * The URL (typically [MXC URI](https://matrix.org/docs/spec/client_server/r0.5.0#mxc-uri)) to the image.
         */
        val url: String,

        /**
         * Required if the file is encrypted.
         * Information on the encrypted file, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#encrypted-files).
         */
        val file: EncryptedFile? = null
    ) : RoomMessageContent()

    /**
     * This message represents a generic file.
     */
    @Serializable
    @SerialName("m.file")
    data class File(
        /**
         * A human-readable description of the file.
         * This is recommended to be the filename of the original upload.
         */
        override val body: String,

        /**
         * The original filename of the uploaded file.
         */
        val filename: String? = null,

        /**
         * Information about the file referred to in [url].
         */
        val info: FileInfo? = null,

        /**
         * Required if the file is unencrypted.
         * The URL (typically [MXC URI](https://matrix.org/docs/spec/client_server/r0.5.0#mxc-uri)) to the image.
         */
        val url: String,

        /**
         * Required if the file is encrypted.
         * Information on the encrypted file, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#encrypted-files).
         */
        val file: EncryptedFile? = null
    ) : RoomMessageContent()

    /**
     * This message represents a single audio clip.
     */
    @Serializable
    @SerialName("m.audio")
    data class Audio(
        /**
         * A description of the audio e.g. 'Bee Gees - Stayin' Alive', or some
         * kind of content description for accessibility e.g. 'audio attachment'.
         */
        override val body: String,

        /**
         * Metadata for the audio clip referred to in [url].
         */
        val info: AudioInfo? = null,

        /**
         * Required if the file is unencrypted.
         * The URL (typically [MXC URI](https://matrix.org/docs/spec/client_server/r0.5.0#mxc-uri)) to the image.
         */
        val url: String,

        /**
         * Required if the file is encrypted.
         * Information on the encrypted file, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#encrypted-files).
         */
        val file: EncryptedFile? = null
    ) : RoomMessageContent()

    /**
     * This message represents a real-world location.
     */
    @Serializable
    @SerialName("m.location")
    data class Location(
        /**
         * A description of the location e.g. 'Big Ben, London, UK', or some kind of
         * content description for accessibility e.g. 'location attachment'.
         */
        override val body: String,

        /**
         * A geo URI representing this location.
         */
        @SerialName("geo_uri")
        val geoUri: String,

        val info: LocationInfo? = null
    ) : RoomMessageContent()

    /**
     * This message represents a single video clip.
     */
    @Serializable
    @SerialName("m.video")
    data class Video(
        /**
         * A description of the video e.g. 'Gangnam style', or some kind of
         * content description for accessibility e.g. 'video attachment'.
         */
        override val body: String,

        /**
         * Metadata about the video clip referred to in [url].
         */
        val info: VideoInfo? = null,

        /**
         * Required if the file is unencrypted.
         * The URL (typically [MXC URI](https://matrix.org/docs/spec/client_server/r0.5.0#mxc-uri)) to the image.
         */
        val url: String,

        /**
         * Required if the file is encrypted.
         * Information on the encrypted file, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#encrypted-files).
         */
        val file: EncryptedFile? = null
    ) : RoomMessageContent()

    object Redacted : RoomMessageContent() {
        override val body: String get() = "(Redacted)"

        override fun toString() = "Redacted"
    }

    companion object : KSerializer<RoomMessageContent> {
        override val descriptor: SerialDescriptor = object : SerialClassDescImpl("RoomMessageContent") {
            override val kind: SerialKind get() = PolymorphicKind.SEALED
        }

        override fun serialize(encoder: Encoder, obj: RoomMessageContent) {
            val serializer = encoder.context.getPolymorphic(RoomMessageContent::class, obj)
            requireNotNull(serializer) { "Could not find serializer for '${obj::class.simpleName}'" }

            val compositeEncoder = encoder.beginStructure(descriptor)
            compositeEncoder.encodeStringElement(descriptor, 0, serializer.descriptor.name)
            @Suppress("UNCHECKED_CAST")
            compositeEncoder.encodeSerializableElement(descriptor, 1, serializer as KSerializer<Any>, obj)
            compositeEncoder.endStructure(descriptor)
        }

        override fun deserialize(decoder: Decoder): RoomMessageContent {
            require(decoder is JsonInput)

            val jsonObj = decoder.decodeJson().jsonObject
            if (jsonObj.isEmpty()) {
                return Redacted
            }

            val klassName: String = jsonObj.getValue("msgtype").content
            val serializer = decoder.context.getPolymorphic(RoomMessageContent::class, klassName)
            requireNotNull(serializer) { "Could not find serializer for `msgtype` = '$klassName'" }
            return decoder.json.fromJson(serializer, jsonObj)
        }
    }
}
