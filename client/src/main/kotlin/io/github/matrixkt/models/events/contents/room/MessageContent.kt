package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.EncryptedFile
import io.github.matrixkt.models.events.contents.msginfo.*
import kotlinx.serialization.*
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This event is used when sending messages in a room.
 * Messages are not limited to be text.
 * The `msgtype` key outlines the type of message, e.g. text, audio, image, video, etc.
 * The `body` key is text and MUST be used with every kind of `msgtype` as a fallback mechanism for when a client cannot render a message.
 * This allows clients to display something even if it is just plain text.
 * For more information on `msgtypes`,
 * see [`m.room.message` msgtypes](https://matrix.org/docs/spec/client_server/r0.5.0#m-room-message-msgtypes).
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialName("m.room.message")
@JsonClassDiscriminator("msgtype")
@Serializable
public abstract class MessageContent {
    /**
     * The textual representation of this message.
     */
    public abstract val body: String

    /**
     * This message is the most basic message and is used to represent text.
     */
    @Serializable
    @SerialName("m.text")
    public data class Text(
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
    ) : MessageContent()

    /**
     * This message is similar to `m.text` except that the sender is 'performing' the action contained in the [body] key,
     * similar to `/me` in IRC.
     * This message should be prefixed by the name of the sender.
     * This message could also be represented in a different colour to distinguish it from regular `m.text` messages.
     */
    @Serializable
    @SerialName("m.emote")
    public data class Emote(
        /**
         * The emote action to perform.
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
    ) : MessageContent()

    /**
     * The `m.notice` type is primarily intended for responses from automated clients.
     * An `m.notice` message must be treated the same way as a regular `m.text` message with two exceptions.
     * Firstly, clients should present `m.notice` messages to users in a distinct manner, and secondly,
     * `m.notice` messages must never be automatically responded to.
     * This helps to prevent infinite-loop situations where two automated clients continuously exchange messages.
     */
    @Serializable
    @SerialName("m.notice")
    public data class Notice(
        /**
         * The notice text to send.
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
    ) : MessageContent()

    /**
     * Represents a server notice for a user.
     */
    @Serializable
    @SerialName("m.server_notice")
    public data class ServerNotice(
        /**
         * A human-readable description of the notice.
         */
        override val body: String,

        /**
         * The type of notice being represented.
         */
        @SerialName("server_notice_type")
        val serverNoticeType: String,

        /**
         * A URI giving a contact method for the server administrator.
         * Required if the notice type is "m.server_notice.usage_limit_reached".
         */
        @SerialName("admin_contact")
        val adminContact: String? = null,

        /**
         * The kind of usage limit the server has exceeded.
         * Required if the notice type is "m.server_notice.usage_limit_reached".
         */
        @SerialName("limit_type")
        val limitType: String? = null
    ) : MessageContent()

    /**
     * This message represents a single image and an optional thumbnail.
     */
    @Serializable
    @SerialName("m.image")
    public data class Image(
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
        val url: String? = null,

        /**
         * Required if the file is encrypted.
         * Information on the encrypted file, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#encrypted-files).
         */
        val file: EncryptedFile? = null
    ) : MessageContent()

    /**
     * This message represents a generic file.
     */
    @Serializable
    @SerialName("m.file")
    public data class File(
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
        val url: String? = null,

        /**
         * Required if the file is encrypted.
         * Information on the encrypted file, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#encrypted-files).
         */
        val file: EncryptedFile? = null
    ) : MessageContent()

    /**
     * This message represents a single audio clip.
     */
    @Serializable
    @SerialName("m.audio")
    public data class Audio(
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
        val url: String? = null,

        /**
         * Required if the file is encrypted.
         * Information on the encrypted file, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#encrypted-files).
         */
        val file: EncryptedFile? = null
    ) : MessageContent()

    /**
     * This message represents a real-world location.
     */
    @Serializable
    @SerialName("m.location")
    public data class Location(
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
    ) : MessageContent()

    /**
     * This message represents a single video clip.
     */
    @Serializable
    @SerialName("m.video")
    public data class Video(
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
        val url: String? = null,

        /**
         * Required if the file is encrypted.
         * Information on the encrypted file, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#encrypted-files).
         */
        val file: EncryptedFile? = null
    ) : MessageContent()
}
