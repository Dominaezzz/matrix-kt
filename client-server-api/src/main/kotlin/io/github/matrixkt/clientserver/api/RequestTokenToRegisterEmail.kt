package io.github.matrixkt.clientserver.api

import io.github.matrixkt.clientserver.models.EmailValidationRequest
import io.github.matrixkt.clientserver.models.TokenValidationResponse
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * The homeserver must check that the given email address is **not**
 * already associated with an account on this homeserver. The homeserver
 * should validate the email itself, either by sending a validation email
 * itself or by using a service it has control over.
 */
public class RequestTokenToRegisterEmail(
    public override val url: Url,
    public override val body: EmailValidationRequest
) : MatrixRpc<RpcMethod.Post, RequestTokenToRegisterEmail.Url, EmailValidationRequest, TokenValidationResponse> {
    @Resource("_matrix/client/r0/register/email/requestToken")
    @Serializable
    public class Url
}
