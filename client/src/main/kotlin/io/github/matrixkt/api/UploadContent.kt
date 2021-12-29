package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Upload some content to the content repository.
 */
public class UploadContent(
    public override val url: Url,
    /**
     * The content to be uploaded.
     */
    public override val body: ByteArray
) : MatrixRpc.WithAuth<RpcMethod.Post, UploadContent.Url, ByteArray, UploadContent.Response> {
    @Resource("_matrix/media/r0/upload")
    @Serializable
    public class Url(
        /**
         * The name of the file being uploaded
         */
        public val filename: String? = null
    )

    @Serializable
    public class Response(
        /**
         * The `MXC URI`_ to the uploaded content.
         */
        @SerialName("content_uri")
        public val contentUri: String
    )
}
