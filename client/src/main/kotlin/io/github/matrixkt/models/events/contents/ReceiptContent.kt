package io.github.matrixkt.models.events.contents

import io.github.matrixkt.utils.InlineMapSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Informs the client of new receipts.
 */
@SerialName("m.receipt")
@Serializable(ReceiptContent.Serializer::class)
data class ReceiptContent(
    /**
     * The mapping of event ID to a collection of receipts for this event ID.
     * The event ID is the ID of the event being acknowledged and not an ID for the receipt itself.
     */
    val content: Map<String, Receipts> = emptyMap()
): Content(), Map<String, ReceiptContent.Receipts> by content {
    @Serializable
    data class Receipts(
        /**
         * A collection of users who have sent `m.read` receipts for this event.
         */
        @SerialName("m.read")
        val read: Users? = null
    )

    @Serializable(Users.Serializer::class)
    data class Users(
        /**
         * The mapping of user ID to receipt. The user ID is the entity who sent this receipt.
         */
        val content: Map<String, Receipt> = emptyMap()
    ): Map<String, Receipt> by content {
        object Serializer : InlineMapSerializer<String, Receipt, Users>(
            String.serializer(),
            Receipt.serializer(),
            ::Users)
    }

    @Serializable
    data class Receipt(
        /**
         * The timestamp the receipt was sent at.
         */
        @SerialName("ts")
        val timestamp: Long? = null
    )

    object Serializer : InlineMapSerializer<String, Receipts, ReceiptContent>(
        String.serializer(),
        Receipts.serializer(),
        ::ReceiptContent)
}
