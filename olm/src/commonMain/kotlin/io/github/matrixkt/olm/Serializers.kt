package io.github.matrixkt.olm

import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.experimental.and
import kotlin.random.Random

private const val RANDOM_KEY_SIZE = 32

private fun generateRandomKey(): ByteArray {
    val buffer = ByteArray(RANDOM_KEY_SIZE)
    Random.nextBytes(buffer) // FIXME Use secure random.
    // the key is saved as string
    // so avoid the UTF8 marker bytes
    for (i in buffer.indices) {
        buffer[i] = buffer[i] and 0x7F
    }
    return buffer
}

@Serializable
internal class Pickle(val key: ByteArray, val data: String)

private class PickleSerializer<T>(val pickle: T.(ByteArray) -> String, val unpickle: (ByteArray, String) -> T) : KSerializer<T> {
    override val descriptor = Pickle.serializer().descriptor

    override fun serialize(encoder: Encoder, value: T) {
        val key = generateRandomKey()
        val data = value.pickle(key)
        val pickle = Pickle(key, data)
        encoder.encodeSerializableValue(Pickle.serializer(), pickle)
    }

    override fun deserialize(decoder: Decoder): T {
        val pickle = decoder.decodeSerializableValue(Pickle.serializer())
        return unpickle(pickle.key, pickle.data)
    }
}


public object AccountSerializer : KSerializer<Account> by PickleSerializer(Account::pickle, Account.Companion::unpickle)

public object SessionSerializer : KSerializer<Session> by PickleSerializer(Session::pickle, Session.Companion::unpickle)

public object InboundGroupSessionSerializer : KSerializer<InboundGroupSession> by PickleSerializer(
    InboundGroupSession::pickle, InboundGroupSession.Companion::unpickle
)

public object OutboundGroupSessionSerializer : KSerializer<OutboundGroupSession> by PickleSerializer(
    OutboundGroupSession::pickle, OutboundGroupSession.Companion::unpickle
)

public object PkDecryptionSerializer : KSerializer<PkDecryption> by PickleSerializer(
    PkDecryption::pickle, PkDecryption.Companion::unpickle
)
