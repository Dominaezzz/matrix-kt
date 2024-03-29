package io.github.matrixkt.clientserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MSISDNValidationRequest(
    /**
     * A unique string generated by the client, and used to identify the validation attempt.
     * It must be a string consisting of the characters `[0-9a-zA-Z.=_-]`.
     * Its length must not exceed 255 characters and it must not be empty.
     */
    @SerialName("client_secret")
    public val clientSecret: String,

    /**
     * The two-letter uppercase ISO-3166-1 alpha-2 country code that the
     * number in [phoneNumber] should be parsed as if it were dialled from.
     */
    public val country: String,

    /**
     * The phone number to validate.
     */
    @SerialName("phone_number")
    public val phoneNumber: String,

    /**
     * The server will only send an SMS if the ``send_attempt`` is a
     * number greater than the most recent one which it has seen,
     * scoped to that [country] + [phoneNumber] + [clientSecret]
     * triple. This is to avoid repeatedly sending the same SMS in
     * the case of request retries between the POSTing user and the
     * identity server. The client should increment this value if
     * they desire a new SMS (e.g. a reminder) to be sent.
     */
    @SerialName("send_attempt")
    public val sendAttempt: Long,

    /**
     * Optional. When the validation is completed, the identity server will
     * redirect the user to this URL. This option is ignored when submitting
     * 3PID validation information through a POST request.
     */
    @SerialName("next_link")
    public val nextLink: String? = null
)
