package io.github.matrixkt.api

import io.github.matrixkt.models.MSISDNValidationRequest
import io.github.matrixkt.models.TokenValidationResponse
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * The homeserver must check that the given phone number is **not**
 * already associated with an account on this homeserver. This API should
 * be used to request validation tokens when adding a phone number to an
 * account. This API's parameters and response are identical to that of
 * the |/register/msisdn/requestToken|_ endpoint. The homeserver should validate
 * the phone number itself, either by sending a validation message itself or by using
 * a service it has control over.
 */
public class RequestTokenTo3PIDMSISDN(
    public override val url: Url,
    public override val body: MSISDNValidationRequest
) : MatrixRpc<RpcMethod.Post, RequestTokenTo3PIDMSISDN.Url, MSISDNValidationRequest, TokenValidationResponse> {
    @Resource("_matrix/client/r0/account/3pid/msisdn/requestToken")
    @Serializable
    public class Url
}
