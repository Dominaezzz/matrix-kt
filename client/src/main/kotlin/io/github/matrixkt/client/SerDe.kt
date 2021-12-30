package io.github.matrixkt.client

import io.github.matrixkt.clientserver.ClientServerSerialModule
import io.github.matrixkt.clientserver.api.Login
import io.github.matrixkt.events.EventSerialModule
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

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
