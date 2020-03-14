package io.github.matrixkt.utils

import kotlinx.serialization.*
import kotlinx.serialization.builtins.MapSerializer

open class InlineMapSerializer<Key, Value, T : Map<Key, Value>>(
    keySerializer: KSerializer<Key>,
    valueSerializer: KSerializer<Value>,
    private val ctor: (Map<Key, Value>) -> T
) : KSerializer<T> {
    private val delegate = MapSerializer(keySerializer, valueSerializer)

    override val descriptor get() = delegate.descriptor

    override fun deserialize(decoder: Decoder): T {
        return ctor(decoder.decode(delegate))
    }

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encode(delegate, value)
    }
}
