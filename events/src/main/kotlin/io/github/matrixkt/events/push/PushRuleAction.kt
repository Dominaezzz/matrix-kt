package io.github.matrixkt.events.push

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class PushRuleAction {
    @SerialName("notify")
    NOTIFY,
    @SerialName("dont_notify")
    DONT_NOTIFY,
    @SerialName("coalesce")
    COALESCE,
    @SerialName("set_tweak")
    SET_TWEAK;
}
