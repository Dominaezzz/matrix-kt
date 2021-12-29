package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Download content from the content repository.
 */
public class GetContent(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetContent.Url, Nothing, ByteArray> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/media/r0/download/{serverName}/{mediaId}")
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
         * Indicates to the server that it should not attempt to fetch the media if it is deemed
         * remote. This is to prevent routing loops where the server contacts itself. Defaults to
         * true if not provided.
         */
        @SerialName("allow_remote")
        public val allowRemote: Boolean? = null
    )
}
