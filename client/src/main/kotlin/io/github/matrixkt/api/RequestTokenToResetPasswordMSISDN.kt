package io.github.matrixkt.api

import io.github.matrixkt.models.MSISDNValidationRequest
import io.github.matrixkt.models.TokenValidationResponse
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * The homeserver must check that the given phone number **is
 * associated** with an account on this homeserver. This API should be
 * used to request validation tokens when authenticating for the
 * ``/account/password`` endpoint.
 *
 * This API's parameters and response are identical to that of the
 * |/register/msisdn/requestToken|_ endpoint, except that
 * ``M_THREEPID_NOT_FOUND`` may be returned if no account matching the
 * given phone number could be found. The server may instead send the SMS
 * to the given phone number prompting the user to create an account.
 * ``M_THREEPID_IN_USE`` may not be returned.
 *
 * The homeserver should validate the phone number itself, either by sending a
 * validation message itself or by using a service it has control over.
 *
 * .. |/register/msisdn/requestToken| replace:: ``/register/msisdn/requestToken``
 *
 * .. _/register/msisdn/requestToken: #post-matrix-client-r0-register-email-requesttoken
 */
public class RequestTokenToResetPasswordMSISDN(
    public override val url: Url,
    public override val body: MSISDNValidationRequest
) : MatrixRpc<RpcMethod.Post, RequestTokenToResetPasswordMSISDN.Url, MSISDNValidationRequest, TokenValidationResponse> {
    @Resource("_matrix/client/r0/account/password/msisdn/requestToken")
    @Serializable
    public class Url
}
