package io.github.matrixkt.utils

import io.github.matrixkt.models.MatrixException
import io.github.matrixkt.utils.resource.href
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

public suspend inline fun <reified Method : RpcMethod, reified Location, RequestBody : Any?, reified ResponseBody> HttpClient.rpc(
    rpcObject: MatrixRpc<Method, Location, RequestBody, ResponseBody>,
    block: HttpRequestBuilder.() -> Unit = {}
): ResponseBody {
    try {
        return request {
            method = RpcMethod.fromType<Method>()
            href(rpcObject.url, url)

            val rpcBody = rpcObject.body
            if (rpcBody != null) {
                contentType(ContentType.Application.Json)
                body = rpcBody
            }

            block()

            // This is done after `block()` because users cannot be trusted.
            // It needs to be true for the `try`/`catch` to work as expected.
            // If you want to this to be false, copy this method and do your own thing.
            expectSuccess = true
        }
    } catch (e: ResponseException) {
        throw MatrixException(e.response.receive())
    }
}

public suspend inline fun <reified Method : RpcMethod, reified Location, RequestBody : Any?, reified ResponseBody> HttpClient.rpc(
    rpcObject: MatrixRpc.WithAuth<Method, Location, RequestBody, ResponseBody>,
    accessToken: String,
    block: HttpRequestBuilder.() -> Unit = {}
): ResponseBody {
    try {
        return request {
            method = RpcMethod.fromType<Method>()
            href(rpcObject.url, url)

            header(HttpHeaders.Authorization, "Bearer $accessToken")

            val rpcBody = rpcObject.body
            if (rpcBody != null) {
                contentType(ContentType.Application.Json)
                body = rpcBody
            }

            block()

            // This is done after `block()` because users cannot be trusted.
            // It needs to be true for the `try`/`catch` to work as expected.
            // If you want to this to be false, copy this method and do your own thing.
            expectSuccess = true
        }
    } catch (e: ResponseException) {
        throw MatrixException(e.response.receive())
    }
}

@Suppress("FunctionName")
public fun HttpClientConfig<*>.MatrixConfig(baseUrl: Url, json: Json = MatrixJson) {
    defaultRequest {
        val builder = URLBuilder(baseUrl)
        if (url.encodedPath.startsWith('/')) {
            builder.encodedPath = builder.encodedPath.removeSuffix("/")
        }
        builder.encodedPath += url.encodedPath
        url.takeFrom(builder)
    }

    Json {
        serializer = KotlinxSerializer(json)
    }
}
