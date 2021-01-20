package io.github.matrixkt.apis

import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.reflect.KProperty0

class AccountApi internal constructor(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) {
    private inline val accessToken: String get() = accessTokenProp.get()

    /**
     * Gets a list of the third party identifiers that the homeserver has associated with the user's account.
     *
     * This is not the same as the list of third party identifiers bound to the user's Matrix ID in identity servers.
     *
     * Identifiers in this list may be used by the homeserver as, for example, identifiers that it will accept to reset the user's account password.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     */
    suspend fun getAccount3PIDs(): List<ThirdPartyIdentifier> {
        val response = client.get<Get3PidsResponse>("_matrix/client/r0/account/3pid") {
            header("Authorization", "Bearer $accessToken")
        }
        return response.threepids
    }

    /**
     * Adds contact information to the user's account.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     */
    suspend fun add3PID(params: Add3PidRequest) {
        return client.post("_matrix/client/r0/account/3pid") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    /**
     * Removes a third party identifier from the user's account.
     * This might not cause an unbind of the identifier from the identity server.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     */
    suspend fun delete3pidFromAccount(params: Remove3PidRequest): Remove3PidResponse {
        return client.post("_matrix/client/r0/account/3pid/delete") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    /**
     * Gets information about the owner of a given access token.
     *
     * Note that, as with the rest of the Client-Server API,
     * Application Services may masquerade as users within their namespace by giving a `user_id` query parameter.
     * In this situation, the server should verify that the given `user_id` is registered by the appservice,
     * and return it in the response body.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @return The user id that owns the access token.
     */
    suspend fun getTokenOwner(): String {
        val response = client.get<WhoAmIResponse>("_matrix/client/r0/account/whoami") {
            header("Authorization", "Bearer $accessToken")
        }
        return response.userId
    }

    /**
     * Changes the password for an account on this homeserver.
     *
     * This API endpoint uses the
     * [User-Interactive Authentication API](https://matrix.org/docs/spec/client_server/r0.5.0#user-interactive-authentication-api)
     * to ensure the user changing the password is actually the owner of the account.
     *
     * An access token should be submitted to this endpoint if the client has an active session.
     *
     * The homeserver may change the flows available depending on whether a valid access token is provided.
     * The homeserver SHOULD NOT revoke the access token provided in the request,
     * however all other access tokens for the user should be revoked if the request succeeds.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     */
    suspend fun changePassword(params: ChangePasswordRequest) {
        return client.post(path = "_matrix/client/r0/account/password") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    /**
     * Deactivate the user's account, removing all ability for the user to login again.
     *
     * This API endpoint uses the User-Interactive Authentication API.
     *
     * An access token should be submitted to this endpoint if the client has an active session.
     *
     * The homeserver may change the flows available depending on whether a valid access token is provided.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     */
    suspend fun deactivateAccount(params: DeactivateRequest): DeactivateResponse {
        return client.post(path = "_matrix/client/r0/account/deactivate") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    /**
     * The homeserver must check that the given email address *is associated* with an account on this homeserver.
     * This API should be used to request validation tokens when authenticating for the `/account/password` endpoint.
     *
     * This API's parameters and response are identical to that of the `/register/email/requestToken` endpoint,
     * except that `M_THREEPID_NOT_FOUND` may be returned if no account matching the given email address could be found.
     * The server may instead send an email to the given address prompting the user to create an account.
     * `M_THREEPID_IN_USE` may not be returned.
     *
     * The homeserver has the choice of validating the email address itself,
     * or proxying the request to the `/validate/email/requestToken` Identity Service API.
     * The request should be proxied to the domain that is sent by the client in the `id_server`.
     * It is imperative that the homeserver keep a list of trusted Identity Servers and only proxies to those that it trusts.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     */
    suspend fun requestTokenToResetPasswordEmail(params: EmailValidationRequest): TokenValidationResponse {
        return client.post(path = "_matrix/client/r0/account/password/email/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    /**
     * The homeserver must check that the given phone number is associated with an account on this homeserver.
     * This API should be used to request validation tokens when authenticating for the `/account/password` endpoint.
     *
     * This API's parameters and response are identical to that of the `/register/msisdn/requestToken` endpoint,
     * except that `M_THREEPID_NOT_FOUND` may be returned if no account matching the given phone number could be found.
     * The server may instead send the SMS to the given phone number prompting the user to create an account.
     * `M_THREEPID_IN_USE` may not be returned.
     *
     * The homeserver has the choice of validating the phone number itself,
     * or proxying the request to the `/validate/msisdn/requestToken` Identity Service API.
     * The request should be proxied to the domain that is sent by the client in the `id_server`.
     * It is imperative that the homeserver keep a list of trusted Identity Servers and only proxies to those that it trusts.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     */
    suspend fun requestTokenToResetPasswordMSISDN(params: MSISDNValidationRequest): TokenValidationResponse {
        return client.post(path = "_matrix/client/r0/account/password/msisdn/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    /**
     * The homeserver must check that the given email address is **not** already associated with an account on this homeserver.
     * This API should be used to request validation tokens when adding an email address to an account.
     * This API's parameters and response are identical to that of the `/register/email/requestToken` endpoint.
     * The homeserver has the choice of validating the email address itself, or proxying the request to the
     * `/validate/email/requestToken` Identity Service API as identified by `id_server`.
     * It is imperative that the homeserver keep a list of trusted Identity Servers and only proxies to those that it trusts.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     */
    suspend fun requestTokenTo3PIDEmail(params: EmailValidationRequest): TokenValidationResponse {
        return client.post("_matrix/client/r0/account/3pid/email/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }
    }

    /**
     * The homeserver must check that the given phone number is **not** already associated with an account on this homeserver.
     * This API should be used to request validation tokens when adding a phone number to an account.
     * This API's parameters and response are identical to that of the `/register/msisdn/requestToken` endpoint.
     * The homeserver has the choice of validating the phone number itself, or proxying the request to the
     * `/validate/msisdn/requestToken` Identity Service API as identified by `id_server`.
     * It is imperative that the homeserver keep a list of trusted Identity Servers and only proxies to those that it trusts.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     */
    suspend fun requestTokenTo3PIDMSISDN(params: MSISDNValidationRequest): TokenValidationResponse {
        return client.post("_matrix/client/r0/account/3pid/msisdn/requestToken") {
            contentType(ContentType.Application.Json)
            body = params
        }
    }
}
