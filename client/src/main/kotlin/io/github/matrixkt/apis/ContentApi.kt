package io.github.matrixkt.apis

import io.github.matrixkt.models.ConfigResponse
import io.github.matrixkt.models.ThumbnailMethod
import io.github.matrixkt.models.UploadResponse
import io.github.matrixkt.models.UrlPreviewResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpStatement
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.ByteArrayContent
import kotlin.reflect.KProperty0

class ContentApi internal constructor(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) {
    private inline val accessToken: String get() = accessTokenProp.get()

    /**
     * Upload some content to the content repository.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[contentType] The content type of the file being uploaded
     * @param[filename] The name of the file being uploaded
     * @return The [MXC URI](https://matrix.org/docs/spec/client_server/r0.5.0#mxc-uri) to the uploaded content.
     */
    suspend fun uploadContent(contentType: String?, filename: String?, content: ByteArray): String {
        val response = client.post<UploadResponse>("/_matrix/media/r0/upload") {
            parameter("filename", filename)
            header("Authorization", "Bearer $accessToken")

            body = ByteArrayContent(content, contentType?.let(ContentType.Companion::parse))
        }
        return response.contentUri
    }

    /**
     * Download content from the content repository.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: No.
     *
     * @param[serverName] The server name from the `mxc://` URI (the authoritory component)
     * @param[mediaId] The media ID from the `mxc://` URI (the path component)
     * @param[allowRemote] Indicates to the server that it should not attempt to fetch the media if it is deemed remote.
     * This is to prevent routing loops where the server contacts itself. Defaults to true if not provided.
     * @return The bytes for the uploaded file.
     */
    suspend fun getContent(serverName: String, mediaId: String, allowRemote: Boolean? = null): ByteArray {
        val statement = client.get<HttpStatement> {
            url {
                path("_matrix", "media", "r0", "download", serverName, mediaId)
            }

            parameter("allow_remote", allowRemote)
        }

        return statement.execute {
            // The content type of the file that was previously uploaded.
            it.headers[HttpHeaders.ContentType]
            // The name of the file that was previously uploaded, if set.
            it.headers[HttpHeaders.ContentDisposition]

            it.receive<ByteArray>()
        }
    }

    /**
     * Download content from the content repository.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: No.
     *
     * @param[serverName] The server name from the `mxc://` URI (the authoritory component)
     * @param[mediaId] The media ID from the `mxc://` URI (the path component)
     * @param[fileName] A filename to give in the `Content-Disposition` header.
     * @param[allowRemote] Indicates to the server that it should not attempt to fetch the media if it is deemed remote.
     * This is to prevent routing loops where the server contacts itself. Defaults to true if not provided.
     * @return The bytes for the uploaded file.
     */
    suspend fun getContentOverrideName(serverName: String, mediaId: String, fileName: String, allowRemote: Boolean? = null): ByteArray {
        val statement = client.get<HttpStatement> {
            url {
                path("_matrix", "media", "r0", "download", serverName, mediaId, fileName)
            }

            parameter("allow_remote", allowRemote)
        }
        return statement.execute {
            // The content type of the file that was previously uploaded.
            it.headers[HttpHeaders.ContentType]
            // The name of the file that was previously uploaded, if set.
            it.headers[HttpHeaders.ContentDisposition]

            it.receive<ByteArray>()
        }
    }

    /**
     * Download a thumbnail of content from the content repository.
     * See the [thumbnailing](https://matrix.org/docs/spec/client_server/r0.5.0#thumbnails) section for more information.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: No.
     *
     * @param[serverName] The server name from the `mxc://` URI (the authoritory component)
     * @param[mediaId] The media ID from the `mxc://` URI (the path component)
     * @param[width] The *desired* width of the thumbnail. The actual thumbnail may be larger than the size specified.
     * @param[height] The *desired* height of the thumbnail. The actual thumbnail may be larger than the size specified.
     * @param[method] The desired resizing method.
     * See the [thumbnailing](https://matrix.org/docs/spec/client_server/r0.5.0#thumbnails) section for more information. One of: ["crop", "scale"]
     * @param[allowRemote] Indicates to the server that it should not attempt to fetch the media if it is deemed remote.
     * This is to prevent routing loops where the server contacts itself. Defaults to true if not provided.
     * @return The bytes for the thumbnail.
     */
    suspend fun getContentThumbnail(serverName: String, mediaId: String, width: Int, height: Int, method: ThumbnailMethod? = null, allowRemote: Boolean? = null): ByteArray {
        val statement = client.get<HttpStatement> {
            url {
                path("_matrix", "media", "r0", "thumbnail", serverName, mediaId)
            }

            parameter("width", width)
            parameter("height", height)
            parameter("method", method)
            parameter("allow_remote", allowRemote)
        }
        return statement.execute {
            // The content type of the file that was previously uploaded.
            it.headers[HttpHeaders.ContentType]
            // The name of the file that was previously uploaded, if set.
            it.headers[HttpHeaders.ContentDisposition]

            it.receive<ByteArray>()
        }
    }

    /**
     * Get information about a URL for a client.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: No.
     *
     * @param[url] The URL to get a preview of.
     * @param[ts] The preferred point in time to return a preview for.
     * The server may return a newer version if it does not have the requested version available.
     */
    suspend fun getUrlPreview(url: String, ts: Long? = null): UrlPreviewResponse {
        return client.get(path = "_matrix/media/r0/preview_url") {
            parameter("url", url)
            parameter("ts", ts)
        }
    }

    /**
     * This endpoint allows clients to retrieve the configuration of the content repository, such as upload limitations.
     * Clients SHOULD use this as a guide when using content repository endpoints.
     * All values are intentionally left optional.
     * Clients SHOULD follow the advice given in the field description when the field is not available.
     *
     * NOTE: Both clients and server administrators should be aware that proxies between the client and
     * the server may affect the apparent behaviour of content repository APIs, for example,
     * proxies may enforce a lower upload size limit than is advertised by the server on this endpoint.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     */
    suspend fun getConfig(): ConfigResponse {
        return client.get(path = "_matrix/media/r0/config") {
            header("Authorization", "Bearer $accessToken")
        }
    }
}
