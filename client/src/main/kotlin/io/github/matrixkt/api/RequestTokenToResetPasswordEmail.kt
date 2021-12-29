package io.github.matrixkt.api

import io.github.matrixkt.models.EmailValidationRequest
import io.github.matrixkt.models.TokenValidationResponse
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * The homeserver must check that the given email address **is
 * associated** with an account on this homeserver. This API should be
 * used to request validation tokens when authenticating for the
 * ``/account/password`` endpoint.
 *
 * This API's parameters and response are identical to that of the
 * |/register/email/requestToken|_ endpoint, except that
 * ``M_THREEPID_NOT_FOUND`` may be returned if no account matching the
 * given email address could be found. The server may instead send an
 * email to the given address prompting the user to create an account.
 * ``M_THREEPID_IN_USE`` may not be returned.
 *
 * The homeserver should validate the email itself, either by sending a
 * validation email itself or by using a service it has control over.
 *
 *
 * .. |/register/email/requestToken| replace:: ``/register/email/requestToken``
 *
 * .. _/register/email/requestToken: #post-matrix-client-r0-register-email-requesttoken
 */
public class RequestTokenToResetPasswordEmail(
    public override val url: Url,
    public override val body: EmailValidationRequest
) : MatrixRpc<RpcMethod.Post, RequestTokenToResetPasswordEmail.Url, EmailValidationRequest, TokenValidationResponse> {
    @Resource("_matrix/client/r0/account/password/email/requestToken")
    @Serializable
    public class Url
}
