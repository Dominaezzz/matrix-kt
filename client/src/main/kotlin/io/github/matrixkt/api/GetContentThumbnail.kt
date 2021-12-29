package io.github.matrixkt.api

import io.github.matrixkt.models.ThumbnailMethod
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Download a thumbnail of content from the content repository. See the `thumbnailing
 * <#thumbnails>`_
 * section for more information.
 */
public class GetContentThumbnail(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetContentThumbnail.Url, Nothing, ByteArray> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/media/r0/thumbnail/{serverName}/{mediaId}")
    @Serializable
    public class Url(
        /**
         * The server name from the ``mxc://`` URI (the authoritory component)
         */
        public val serverName: String,
        /**
         * The media ID from the ``mxc://`` URI (the path component)
         */
        public val mediaId: String,
        /**
         * The *desired* width of the thumbnail. The actual thumbnail may be
         * larger than the size specified.
         */
        public val width: Long,
        /**
         * The *desired* height of the thumbnail. The actual thumbnail may be
         * larger than the size specified.
         */
        public val height: Long,
        /**
         * The desired resizing method. See the `thumbnailing <#thumbnails>`_
         * section for more information.
         */
        public val method: ThumbnailMethod? = null,
        /**
         * Indicates to the server that it should not attempt to fetch the media if it is deemed
         * remote. This is to prevent routing loops where the server contacts itself. Defaults to
         * true if not provided.
         */
        @SerialName("allow_remote")
        public val allowRemote: Boolean? = null
    )
}
