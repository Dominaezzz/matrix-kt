package io.github.matrixkt.utils

import kotlinx.serialization.*

open class InlineMapSerializer<Key, Value, T : Map<Key, Value>>(
    keySerializer: KSerializer<Key>,
    valueSerializer: KSerializer<Value>,
    private val ctor: (Map<Key, Value>) -> T
) : KSerializer<T> {
    private val delegate = (keySerializer to valueSerializer).map

    override val descriptor: SerialDescriptor get() = delegate.descriptor

    override fun deserialize(decoder: Decoder): T {
        return ctor(decoder.decode(delegate))
    }

    override fun serialize(encoder: Encoder, obj: T) {
        encoder.encode(delegate, obj)
    }
}
