package io.github.matrixkt.utils

import io.github.matrixkt.models.MatrixException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmName

@PublishedApi
internal suspend inline fun <reified Method : RpcMethod, reified Location : Any, reified ResponseBody> HttpClient.baseRpc(
    location: Location,
    block: HttpRequestBuilder.() -> Unit = {}
): ResponseBody {
    val response = request(location) {
        method = RpcMethod.fromType<Method>()

        block()

        // This is done after `block()` because users cannot be trusted.
        // If you want to this to be different, copy this method and do your own thing.
        expectSuccess = false
    }
    if (response.status.isSuccess()) {
        return response.body()
    } else {
        throw MatrixException(response.body())
    }
}

public suspend inline fun <reified Method : RpcMethod, reified Location : Any, reified RequestBody : Any, reified ResponseBody> HttpClient.rpc(
    rpcObject: MatrixRpc<Method, Location, RequestBody, ResponseBody>,
    block: HttpRequestBuilder.() -> Unit = {}
): ResponseBody {
    return baseRpc<Method, Location, ResponseBody>(rpcObject.url) {
        contentType(ContentType.Application.Json)
        setBody(rpcObject.body)
        block()
    }
}

@JvmName("rpcWithoutRequestBody")
public suspend inline fun <reified Method : RpcMethod, reified Location : Any, reified ResponseBody> HttpClient.rpc(
    rpcObject: MatrixRpc<Method, Location, Nothing, ResponseBody>,
    block: HttpRequestBuilder.() -> Unit = {}
): ResponseBody {
    return baseRpc<Method, Location, ResponseBody>(rpcObject.url, block)
}

public suspend inline fun <reified Method : RpcMethod, reified Location : Any, reified RequestBody : Any, reified ResponseBody> HttpClient.rpc(
    rpcObject: MatrixRpc.WithAuth<Method, Location, RequestBody, ResponseBody>,
    accessToken: String,
    block: HttpRequestBuilder.() -> Unit = {}
): ResponseBody {
    return baseRpc<Method, Location, ResponseBody>(rpcObject.url) {
        bearerAuth(accessToken)
        contentType(ContentType.Application.Json)
        setBody(rpcObject.body)
        block()
    }
}

@JvmName("rpcWithoutRequestBody")
public suspend inline fun <reified Method : RpcMethod, reified Location : Any, reified ResponseBody> HttpClient.rpc(
    rpcObject: MatrixRpc.WithAuth<Method, Location, Nothing, ResponseBody>,
    accessToken: String,
    block: HttpRequestBuilder.() -> Unit = {}
): ResponseBody {
    return baseRpc<Method, Location, ResponseBody>(rpcObject.url) {
        bearerAuth(accessToken)
        block()
    }
}

@Suppress("FunctionName")
public fun HttpClientConfig<*>.MatrixConfig(baseUrl: String, json: Json = MatrixJson) {
    defaultRequest {
        url(baseUrl.removeSuffix("/") + "/")
    }

    install(ContentNegotiation) {
        json(json)
    }

    install(Resources)
}
