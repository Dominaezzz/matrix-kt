package io.github.matrixkt.api

import io.github.matrixkt.models.MSISDNValidationRequest
import io.github.matrixkt.models.TokenValidationResponse
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * The homeserver must check that the given phone number is **not**
 * already associated with an account on this homeserver. The homeserver
 * should validate the phone number itself, either by sending a validation
 * message itself or by using a service it has control over.
 */
public class RequestTokenToRegisterMSISDN(
    public override val url: Url,
    public override val body: MSISDNValidationRequest
) : MatrixRpc<RpcMethod.Post, RequestTokenToRegisterMSISDN.Url, MSISDNValidationRequest, TokenValidationResponse> {
    @Resource("_matrix/client/r0/register/msisdn/requestToken")
    @Serializable
    public class Url
}
