package io.github.matrixkt.impls

import io.github.matrixkt.apis.AuthApi
import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlin.reflect.KProperty0

internal class AuthApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : AuthApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun getLoginFlows(): LoginFlowsResponse {
        return client.get(path = "_matrix/client/r0/login")
    }

    override suspend fun login(params: LoginRequest): LoginResponse {
        return client.post(path = "_matrix/client/r0/login") {
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun logout() {
        return client.post(path = "_matrix/client/r0/logout") {
            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun logoutAll() {
        return client.post(path = "_matrix/client/r0/logout/all") {
            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun register(kind: RegistrationKind?, params: RegisterRequest): RegisterResponse {
        return client.post(path = "_matrix/client/r0/register") {
            if (kind != null) parameter("kind", kind)
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun requestTokenToRegisterEmail(params: EmailValidationRequest): TokenValidationResponse {
        return client.post(path = "_matrix/client/r0/register/email/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun requestTokenToRegisterMSISDN(params: MSISDNValidationRequest): TokenValidationResponse {
        return client.post(path = "_matrix/client/r0/register/msisdn/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun checkUsernameAvailability(username: String): Boolean {
        val response = client.get<JsonObject>(path = "_matrix/client/r0/register/available") {
            parameter("username", username)
        }
        return response["available"]!!.boolean
    }
}
