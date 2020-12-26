package io.github.matrixkt.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class AuthenticationData {
    // /**
    //  * The login type that the client is attempting to complete.
    //  */
    // abstract val type: String

    /**
     * The value of the session key given by the homeserver.
     */
    abstract val session: String?

    /**
     * The client submits an identifier and secret password, both sent in plain-text.
     */
    @Serializable
    @SerialName("m.login.password")
    data class Password(
        /**
         * The value of the session key given by the homeserver.
         */
        override val session: String? = null,

        val identifier: UserIdentifier,
        val password: String
    ) : AuthenticationData()

    /**
     * The user completes a Google ReCaptcha 2.0 challenge
     */
    @Serializable
    @SerialName("m.login.recaptcha")
    data class GoogleRecaptcha(
        /**
         * The value of the session key given by the homeserver.
         */
        override val session: String? = null,

        val response: String
    ) : AuthenticationData()

    /**
     * The client submits a login token.
     */
    @Serializable
    @SerialName("m.login.token")
    data class Token(
        /**
         * The value of the session key given by the homeserver.
         */
        override val session: String? = null,

        val token: String,

        /**
         * client generated nonce
         */
        @SerialName("txn_id")
        val txnId: String
    ) : AuthenticationData()

    /**
     * Authentication is supported by authorising an email address with an identity server, or homeserver if supported.
     */
    @Serializable
    @SerialName("m.login.email.identity")
    data class Email(
        /**
         * The value of the session key given by the homeserver.
         */
        override val session: String? = null,

        @SerialName("threepidCreds")
        val threePidCredentials: ThreePidCredentials
    ) : AuthenticationData()

    /**
     * 	Authentication is supported by authorising a phone number with an identity server, or homeserver if supported.
     */
    @Serializable
    @SerialName("m.login.msisdn")
    data class MSISDN(
        /**
         * The value of the session key given by the homeserver.
         */
        override val session: String? = null,

        @SerialName("threepidCreds")
        val threePidCredentials: ThreePidCredentials
    ) : AuthenticationData()

    /**
     * Dummy authentication always succeeds and requires no extra parameters.
     * Its purpose is to allow servers to not require any form of User-Interactive Authentication to perform a request.
     * It can also be used to differentiate flows where otherwise one flow would be a subset of another flow.
     * eg. if a server offers flows `m.login.recaptcha` and `m.login.recaptcha`, `m.login.email.identity` and
     * the client completes the recaptcha stage first, the auth would succeed with the former flow,
     * even if the client was intending to then complete the email auth stage.
     * A server can instead send flows `m.login.recaptcha`, `m.login.dummy` and `m.login.recaptcha`, `m.login.email.identity` to fix the ambiguity.
     */
    @Serializable
    @SerialName("m.login.dummy")
    data class Dummy(
        /**
         * The value of the session key given by the homeserver.
         */
        override val session: String? = null
    ) : AuthenticationData()
}
