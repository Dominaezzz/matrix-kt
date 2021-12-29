package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Get information about a URL for the client. Typically this is called when a
 * client sees a URL in a message and wants to render a preview for the user.
 *
 * .. Note::
 *   Clients should consider avoiding this endpoint for URLs posted in encrypted
 *   rooms. Encrypted rooms often contain more sensitive information the users
 *   do not want to share with the homeserver, and this can mean that the URLs
 *   being shared should also not be shared with the homeserver.
 */
public class GetUrlPreview(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetUrlPreview.Url, Nothing, GetUrlPreview.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/media/r0/preview_url")
    @Serializable
    public class Url(
        /**
         * The URL to get a preview of.
         */
        public val url: String,
        /**
         * The preferred point in time to return a preview for. The server may
         * return a newer version if it does not have the requested version
         * available.
         */
        public val ts: Long? = null
    )

    @Serializable
    public class Response(
        /**
         * The byte-size of the image. Omitted if there is no image attached.
         */
        @SerialName("matrix:image:size")
        public val matrixImageSize: Long? = null,
        /**
         * An `MXC URI`_ to the image. Omitted if there is no image.
         */
        @SerialName("og:image")
        public val ogImage: String? = null
    )
}
