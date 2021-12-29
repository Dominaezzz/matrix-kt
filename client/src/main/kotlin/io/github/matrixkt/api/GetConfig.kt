package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This endpoint allows clients to retrieve the configuration of the content
 * repository, such as upload limitations.
 * Clients SHOULD use this as a guide when using content repository endpoints.
 * All values are intentionally left optional. Clients SHOULD follow
 * the advice given in the field description when the field is not available.
 *
 * **NOTE:** Both clients and server administrators should be aware that proxies
 * between the client and the server may affect the apparent behaviour of content
 * repository APIs, for example, proxies may enforce a lower upload size limit
 * than is advertised by the server on this endpoint.
 */
public class GetConfig(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetConfig.Url, Nothing, GetConfig.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/media/r0/config")
    @Serializable
    public class Url

    @Serializable
    public class Response(
        /**
         * The maximum size an upload can be in bytes.
         * Clients SHOULD use this as a guide when uploading content.
         * If not listed or null, the size limit should be treated as unknown.
         */
        @SerialName("m.upload.size")
        public val uploadSize: Long? = null
    )
}
