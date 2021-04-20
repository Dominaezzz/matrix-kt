package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Download content from the content repository. This is the same as
 * the download endpoint above, except permitting a desired file name.
 */
public class GetContentOverrideName(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetContentOverrideName.Url, Any?, ByteArray> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/media/r0/download/{serverName}/{mediaId}/{fileName}")
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
         * A filename to give in the ``Content-Disposition`` header.
         */
        public val fileName: String,
        /**
         * Indicates to the server that it should not attempt to fetch the media if it is deemed
         * remote. This is to prevent routing loops where the server contacts itself. Defaults to
         * true if not provided.
         */
        @SerialName("allow_remote")
        public val allowRemote: Boolean? = null
    )
}
