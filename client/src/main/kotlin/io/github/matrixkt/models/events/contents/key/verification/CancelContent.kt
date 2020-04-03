package io.github.matrixkt.models.events.contents.key.verification

import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Cancels a key verification process/request.
 * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 */
@SerialName("m.key.verification.cancel")
@Serializable
data class CancelContent(
    /**
     * The opaque identifier for the verification process/request.
     */
    @SerialName("transaction_id")
    val transactionId: String,

    /**
     * A human readable description of the code. The client should only rely on this string if it does not understand the code.
     */
    val reason: String,

    /**
     * The error code for why the process/request was cancelled by the user.
     * Error codes should use the Java package naming convention if not in the following list:
     * 
     * "m.user": The user cancelled the verification.
     * 
     * "m.timeout": The verification process timed out. Verification processes can define their own timeout parameters.
     * 
     * "m.unknown_transaction": The device does not know about the given transaction ID.
     * 
     * "m.unknown_method": The device does not know how to handle the requested method. This should be sent for m.key.verification.start messages and messages defined by individual verification processes.
     * 
     * "m.unexpected_message": The device received an unexpected message. Typically raised when one of the parties is handling the verification out of order.
     * 
     * "m.key_mismatch": The key was not verified.
     * 
     * "m.user_mismatch": The expected user did not match the user verified.
     * 
     * "m.invalid_message": The message received was invalid.
     * 
     * "m.accepted": A m.key.verification.request was accepted by a different device. The device receiving this error can ignore the verification request.
     * 
     * Clients should be careful to avoid error loops.
     * For example, if a device sends an incorrect message and the client returns "m.invalid_message" to which it gets an unexpected response with "m.unexpected_message",
     * the client should not respond again with "m.unexpected_message" to avoid the other device potentially sending another error response.
     */
    val code: String
) : Content()
