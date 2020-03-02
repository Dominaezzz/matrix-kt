package io.github.matrixkt.impls

import io.github.matrixkt.apis.AccountApi
import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.reflect.KProperty0

internal class AccountApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : AccountApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun getAccount3PIDs(): List<ThirdPartyIdentifier> {
        val response = client.get<Get3PidsResponse>("/_matrix/client/r0/account/3pid") {
            header("Authorization", "Bearer $accessToken")
        }
        return response.threepids
    }

    override suspend fun add3PID(params: Add3PidRequest) {
        return client.post("/_matrix/client/r0/account/3pid") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun delete3pidFromAccount(params: Remove3PidRequest): Remove3PidResponse {
        return client.post("/_matrix/client/r0/account/3pid/delete") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun getTokenOwner(): String {
        val response = client.get<WhoAmIResponse>("/_matrix/client/r0/account/whoami") {
            header("Authorization", "Bearer $accessToken")
        }
        return response.userId
    }

    override suspend fun changePassword(params: ChangePasswordRequest) {
        return client.post(path = "_matrix/client/r0/account/password") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun deactivateAccount(params: DeactivateRequest): DeactivateResponse {
        return client.post(path = "_matrix/client/r0/account/deactivate") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun requestTokenToResetPasswordEmail(params: EmailValidationRequest): TokenValidationResponse {
        return client.post(path = "_matrix/client/r0/account/password/email/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun requestTokenToResetPasswordMSISDN(params: MSISDNValidationRequest): TokenValidationResponse {
        return client.post(path = "_matrix/client/r0/account/password/msisdn/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun requestTokenTo3PIDEmail(params: EmailValidationRequest): TokenValidationResponse {
        return client.post("/_matrix/client/r0/account/3pid/email/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun requestTokenTo3PIDMSISDN(params: MSISDNValidationRequest): TokenValidationResponse {
        return client.post("/_matrix/client/r0/account/3pid/msisdn/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }
    }
}
