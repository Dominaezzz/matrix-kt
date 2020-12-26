package io.github.matrixkt.olm

import colm.internal.olm_error
import kotlinx.cinterop.*
import platform.posix.size_t
import kotlin.random.Random

@OptIn(ExperimentalUnsignedTypes::class)
internal inline fun <T> withRandomBuffer(size: size_t, random: Random, block: (CPointer<ByteVar>?) -> T): T {
    val arraySize = size.convert<Int>()
    return if (arraySize == 0) {
        block(null)
    } else {
        require(arraySize > 0)
        val buffer = ByteArray(arraySize)
        random.nextBytes(buffer) // FIXME: Use SecureRandom
        try {
            buffer.usePinned {
                block(it.addressOf(0))
            }
        } finally {
            buffer.fill(0)
        }
    }
}

internal inline fun <T> String.withNativeRead(block: (CPointer<ByteVar>?, size_t) -> T): T {
    return if (isEmpty()) {
        block(null, 0.convert())
    } else {
        val bytes = encodeToByteArray()
        bytes.usePinned {
            block(it.addressOf(0), bytes.size.convert())
        }
    }
}


internal inline fun <T : CPointed> genericInit(init: (CValuesRef<*>?) -> CPointer<T>?, getSize: () -> size_t): CPointer<T> {
    val size = getSize()
    val memory = nativeHeap.allocArray<ByteVar>(size.convert())
    try {
        val ptr = init(memory)
        return checkNotNull(ptr)
    } catch (e: Exception) {
        nativeHeap.free(memory)
        throw e
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
internal inline fun <T : CPointed> genericCheckError(
    ptr: CPointer<T>,
    result: size_t,
    getLastError: (CPointer<T>?) -> CPointer<ByteVar>?
) {
    check(result != olm_error()) {
        getLastError(ptr)!!.toKString()
    }
}

internal inline fun <T : CPointed> genericPickle(
    ptr: CPointer<T>,
    key: ByteArray,
    getPickleLength: (CPointer<T>?) -> size_t,
    pickle: (ptr: CPointer<T>, key: CValuesRef<*>?, keyLength: size_t, pickled: CValuesRef<*>?, pickledLength: size_t) -> size_t,
    checkError: (size_t) -> Unit
): String {
    val keyPtr = key.refTo(0)

    val pickledLength = getPickleLength(ptr)
    val keyLength = key.size

    val pickled = ByteArray(pickledLength.convert())
    val result = pickle(ptr, keyPtr, keyLength.convert(), pickled.refTo(0), pickledLength)
    checkError(result)
    return pickled.decodeToString()
}

internal inline fun <T : CPointed> genericUnpickle(
    ptr: CPointer<T>,
    key: ByteArray,
    data: String,
    unpickle: (ptr: CPointer<T>, key: CValuesRef<*>?, keyLength: size_t, pickled: CValuesRef<*>?, pickledLength: size_t) -> size_t,
    checkError: (size_t) -> Unit
) {
    data.withNativeRead { pickle, pickleLength ->
        val result = unpickle(ptr, key.refTo(0), key.size.convert(), pickle, pickleLength)
        checkError(result)
    }
}
