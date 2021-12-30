@file:JvmName("ClientSerDe")

package io.github.matrixkt.utils

import io.github.matrixkt.clientserver.api.Login
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlin.jvm.JvmName

public val MatrixSerialModule: SerializersModule = SerializersModule {
    include(EventSerialModule)
    include(ClientServerSerialModule)
}

public val MatrixJson: Json = Json {
    serializersModule = MatrixSerialModule
    allowStructuredMapKeys = true

    encodeDefaults = false
    isLenient = true
    ignoreUnknownKeys = true
}
