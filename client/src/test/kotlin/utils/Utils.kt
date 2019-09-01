package utils

import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

fun respondJson(value: String, status: HttpStatusCode = HttpStatusCode.OK) = respond(
    value,
    status,
    headersOf("Content-Type", ContentType.Application.Json.toString())
)
