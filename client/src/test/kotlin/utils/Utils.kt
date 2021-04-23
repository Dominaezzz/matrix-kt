package utils

import io.github.matrixkt.utils.MatrixJson
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.serialization.encodeToString

fun MockRequestHandleScope.respondJson(value: String, status: HttpStatusCode = HttpStatusCode.OK) = respond(
    value,
    status,
    headersOf("Content-Type", ContentType.Application.Json.toString())
)

inline fun <reified T> MockRequestHandleScope.respond(value: T, status: HttpStatusCode = HttpStatusCode.OK) = respond(
    MatrixJson.encodeToString(value),
    status,
    headersOf("Content-Type", ContentType.Application.Json.toString())
)
