package io.github.matrixkt.models.events.contents

import kotlinx.serialization.Serializable

@Serializable
data class AcceptedTermsContent(
    /**
     * The list of URLs the user has previously accepted.
     * Should be appended to when the user agrees to new terms.
     */
    val accepted: List<String> = emptyList()
) : Content()
