package io.github.matrixkt.impls

import io.github.matrixkt.apis.ContentApi
import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.readBytes
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.ByteArrayContent
import kotlin.reflect.KProperty0

internal class ContentApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : ContentApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun uploadContent(contentType: String?, filename: String?, content: ByteArray): String {
        val response = client.post<UploadResponse>("/_matrix/media/r0/upload") {
            if (filename != null) parameter("filename", filename)
            header("Authorization", "Bearer $accessToken")

            body = ByteArrayContent(content, contentType?.let(ContentType.Companion::parse))
        }
        return response.contentUri
    }

    override suspend fun getContent(serverName: String, mediaId: String, allowRemote: Boolean?): ByteArray {
        val statement = client.get<HttpStatement> {
            url {
                path("_matrix", "media", "r0", "download", serverName, mediaId)
            }

            if (allowRemote != null) parameter("allow_remote", allowRemote)
        }

        return statement.execute {
            // The content type of the file that was previously uploaded.
            it.headers[HttpHeaders.ContentType]
            // The name of the file that was previously uploaded, if set.
            it.headers[HttpHeaders.ContentDisposition]

            it.receive<ByteArray>()
        }
    }

    override suspend fun getContentOverrideName(serverName: String, mediaId: String, fileName: String, allowRemote: Boolean?): ByteArray {
        val statement = client.get<HttpStatement> {
            url {
                path("_matrix", "media", "r0", "download", serverName, mediaId, fileName)
            }

            if (allowRemote != null) parameter("allow_remote", allowRemote)
        }
        return statement.execute {
            // The content type of the file that was previously uploaded.
            it.headers[HttpHeaders.ContentType]
            // The name of the file that was previously uploaded, if set.
            it.headers[HttpHeaders.ContentDisposition]

            it.receive<ByteArray>()
        }
    }

    override suspend fun getContentThumbnail(
        serverName: String,
        mediaId: String,
        width: Int,
        height: Int,
        method: ThumbnailMethod?,
        allowRemote: Boolean?
    ): ByteArray {
        val statement = client.get<HttpStatement> {
            url {
                path("_matrix", "media", "r0", "thumbnail", serverName, mediaId)
            }

            parameter("width", width)
            parameter("height", height)
            if (method != null) parameter("method", method)
            if (allowRemote != null) parameter("allow_remote", allowRemote)
        }
        return statement.execute {
            // The content type of the file that was previously uploaded.
            it.headers[HttpHeaders.ContentType]
            // The name of the file that was previously uploaded, if set.
            it.headers[HttpHeaders.ContentDisposition]

            it.receive<ByteArray>()
        }
    }

    override suspend fun getUrlPreview(url: String, ts: Long?): UrlPreviewResponse {
        return client.get(path = "_matrix/media/r0/preview_url") {
            parameter("url", url)
            if (ts != null) parameter("ts", ts)
        }
    }

    override suspend fun getConfig(): ConfigResponse {
        return client.get(path = "_matrix/media/r0/config") {
            header("Authorization", "Bearer $accessToken")
        }
    }
}
