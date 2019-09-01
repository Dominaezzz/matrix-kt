package io.github.matrixkt.impls

import io.github.matrixkt.apis.AuthApi
import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.io.core.use
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlin.reflect.KProperty0

internal class AuthApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : AuthApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun getLoginFlows(): LoginFlowsResponse {
        val response = client.get<HttpResponse>(path = "_matrix/client/r0/login")

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun login(params: LoginRequest): LoginResponse {
        val response = client.post<HttpResponse>(path = "_matrix/client/r0/login") {
            contentType(ContentType.Application.Json)
            body = params
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun logout() {
        val response = client.post<HttpResponse>(path = "_matrix/client/r0/logout") {
            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun logoutAll() {
        val response = client.post<HttpResponse>(path = "_matrix/client/r0/logout/all") {
            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun register(kind: RegistrationKind?, params: RegisterRequest): RegisterResponse {
        val response = client.post<HttpResponse>(path = "_matrix/client/r0/register") {
            if (kind != null) parameter("kind", kind)
            contentType(ContentType.Application.Json)
            body = params
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun requestTokenToRegisterEmail(params: EmailValidationRequest): TokenValidationResponse {
        val response = client.post<HttpResponse>(path = "_matrix/client/r0/register/email/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun requestTokenToRegisterMSISDN(params: MSISDNValidationRequest): TokenValidationResponse {
        val response = client.post<HttpResponse>(path = "_matrix/client/r0/register/msisdn/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun checkUsernameAvailability(username: String): Boolean {
        val response = client.get<HttpResponse>(path = "_matrix/client/r0/register/available") {
            parameter("username", username)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<JsonObject>()["available"]!!.boolean
                else -> throw it.receive<MatrixError>()
            }
        }
    }
}
