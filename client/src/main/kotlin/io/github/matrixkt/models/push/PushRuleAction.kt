package io.github.matrixkt.models.push

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PushRuleAction {
    @SerialName("notify")
    NOTIFY,
    @SerialName("dont_notify")
    DONT_NOTIFY,
    @SerialName("coalesce")
    COALESCE,
    @SerialName("set_tweak")
    SET_TWEAK;
}
