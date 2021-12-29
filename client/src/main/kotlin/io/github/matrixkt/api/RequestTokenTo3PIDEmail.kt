package io.github.matrixkt.api

import io.github.matrixkt.models.EmailValidationRequest
import io.github.matrixkt.models.TokenValidationResponse
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * The homeserver must check that the given email address is **not**
 * already associated with an account on this homeserver. This API should
 * be used to request validation tokens when adding an email address to an
 * account. This API's parameters and response are identical to that of
 * the |/register/email/requestToken|_ endpoint. The homeserver should validate
 * the email itself, either by sending a validation email itself or by using
 * a service it has control over.
 */
public class RequestTokenTo3PIDEmail(
    public override val url: Url,
    public override val body: EmailValidationRequest
) : MatrixRpc<RpcMethod.Post, RequestTokenTo3PIDEmail.Url, EmailValidationRequest,
        TokenValidationResponse> {
    @Resource("_matrix/client/r0/account/3pid/email/requestToken")
    @Serializable
    public class Url
}
