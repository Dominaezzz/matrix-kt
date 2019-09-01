package io.github.matrixkt.models

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(PolymorphicSerializer::class)
sealed class UserIdentifier {
//    /**
//     * The type of identification.
//     * See [Identifier types](https://matrix.org/docs/spec/client_server/r0.5.0#identifier-types)
//     * for supported values and additional property descriptions.
//     */
//    abstract val type: String

    /**
     * The user is identified by their Matrix ID.
     */
    @Serializable
    @SerialName("m.id.user")
    data class Matrix(
        /**
         * A client can identify a user using their Matrix ID.
         * This can either be the fully qualified Matrix user ID, or just the localpart of the user ID.
         */
        val user: String
    ) : UserIdentifier()

    /**
     * The user is identified by a third-party identifier in canonicalised form.
     *
     * A client can identify a user using a 3PID associated with the user's account on the homeserver,
     * where the 3PID was previously associated using the `/account/3pid` API.
     * See the [3PID Types](https://matrix.org/docs/spec/appendices.html#pid-types) Appendix for a list of Third-party ID media.
     */
    @Serializable
    @SerialName("m.id.thirdparty")
    data class ThirdParty(
        /**
         * The medium of the third party identifier.
         */
        val medium: String,

        /**
         * The canonicalised third party address of the user.
         */
        val address: String
    ) : UserIdentifier()

    /**
     * The user is identified by a phone number.
     *
     * A client can identify a user using a phone number associated with the user's account,
     * where the phone number was previously associated using the `/account/3pid` API.
     * The phone number can be passed in as entered by the user;
     * the homeserver will be responsible for canonicalising it.
     * If the client wishes to canonicalise the phone number,
     * then it can use the `m.id.thirdparty` identifier type with a medium of msisdn instead.
     */
    @Serializable
    @SerialName("m.id.phone")
    data class PhoneNumber(
        /**
         * The country that the phone number is from.
         */
        val country: String,

        /**
         * The phone number
         */
        val phone: String
    ) : UserIdentifier()
}
