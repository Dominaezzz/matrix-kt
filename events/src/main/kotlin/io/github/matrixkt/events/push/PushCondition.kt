package io.github.matrixkt.events.push

import kotlinx.serialization.Serializable

@Serializable
public data class PushCondition(
    /**
     * Required for ``room_member_count`` conditions.
     * A decimal integer optionally prefixed by one of, ==, <, >, >= or <=.
     * A prefix of < matches rooms where the member count is strictly less than the given number and so forth.
     * If no prefix is present, this parameter defaults to ==.
     */
    public val `is`: String? = null,

    /**
     * Required for ``event_match`` conditions.
     * The dot-separated field of the event to match.
     *
     * Required for ``sender_notification_permission`` conditions.
     * The field in the power level event the user needs a minimum power level for.
     * Fields must be specified under the ``notifications`` property in the power level event's ``content``.
     */
    public val key: String? = null,

    /**
     * The kind of condition to apply.
     * See `conditions <#conditions>`_ for more information on the allowed kinds and how they work.
     */
    public val kind: String,

    /**
     * Required for ``event_match`` conditions.
     * The glob-style pattern to match against.
     * Patterns with no special glob characters should be treated as having asterisks prepended and appended when testing the condition.
     */
    public val pattern: String? = null
)
