package io.github.matrixkt.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A list of terms URLs the user has previously accepted.
 * Clients SHOULD use this to avoid presenting the user with terms they have already agreed to.
 */
@SerialName("m.accepted_terms")
@Serializable
public data class AcceptedTermsContent(
    /**
     * The list of URLs the user has previously accepted.
     * Should be appended to when the user agrees to new terms.
     */
    val accepted: List<String> = emptyList()
)
