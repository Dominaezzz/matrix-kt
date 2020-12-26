package io.github.matrixkt.olm

import colm.internal.NativeSize
import colm.internal.OlmLibrary.olm_error
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.PointerType
import kotlin.random.Random

internal inline fun <T> withAllocation(size: Long, block: (Pointer) -> T): T {
    val buffer = Native.malloc(size)
    return try {
        block(Pointer(buffer))
    } finally {
        Native.free(buffer)
    }
}

internal inline fun <T> withSecureAllocation(size: Long, block: (Pointer) -> T): T {
    return withAllocation(size) {
        try {
            block(it)
        } finally {
            it.clear(size)
        }
    }
}

internal inline fun <T> withRandomBuffer(size: NativeSize, random: Random, block: (Pointer?) -> T): T {
    return if (size.toLong() == 0L) {
        block(null)
    } else {
        withSecureAllocation(size.toLong()) { buffer ->
            val steps = size.toLong() / 4

            for (offset in 0L until steps) {
                buffer.setInt(offset * 4, random.nextInt())
            }

            val remainder = (size.toLong() % 4).toInt()
            val vr = random.nextBits(remainder * 8)
            for (i in 0 until remainder) {
                buffer.setByte((steps * 4) + i, vr.ushr(i * 8).toByte())
            }

            block(buffer)
        }
    }
}

internal inline fun <T> ByteArray.withNativeRead(block: (Pointer?) -> T): T {
    return withAllocation(size.toLong()) {
        it.write(0, this, 0, size)
        block(it)
    }
}

internal inline fun <T> ByteArray.withNativeWrite(block: (Pointer?) -> T): T {
    return withAllocation(size.toLong()) {
        try {
            block(it)
        } finally {
            it.read(0, this, 0, size)
        }
    }
}

internal inline fun <T> String.withNativeRead(block: (Pointer?, NativeSize) -> T): T {
    val bytes = encodeToByteArray()
    return bytes.withNativeRead {
        block(it, NativeSize(bytes.size))
    }
}

internal inline fun Pointer.toKString(length: Int): String {
    return getByteArray(0, length).decodeToString()
}

internal inline fun <T : PointerType> genericInit(init: (Pointer?) -> T?, getSize: () -> NativeSize): T {
    val size = getSize()
    val memory = Pointer(Native.malloc(size.toLong()))
    try {
        val ptr = init(memory)
        checkNotNull(ptr)
        return ptr
    } catch (e: Exception) {
        Native.free(Pointer.nativeValue(memory))
        throw e
    }
}

internal inline fun <T : PointerType> genericCheckError(
    ptr: T,
    result: NativeSize,
    getLastError: (T?) -> String?
) {
    check(result != olm_error()) {
        getLastError(ptr)!!
    }
}

internal inline fun <T : PointerType> genericPickle(
    ptr: T,
    key: ByteArray,
    getPickleLength: (T?) -> NativeSize,
    pickle: (T?, Pointer?, NativeSize, Pointer?, NativeSize) -> NativeSize,
    checkError: (NativeSize) -> Unit
): String {
    val pickledLength = getPickleLength(ptr)
    val pickled = ByteArray(pickledLength.toInt())

    val keyLength = key.size
    val result = key.withNativeRead { keyPtr ->
        pickled.withNativeWrite { pickledPtr ->
            pickle(ptr, keyPtr, NativeSize(keyLength), pickledPtr, pickledLength)
        }
    }
    checkError(result)
    return pickled.decodeToString()
}

internal inline fun <T : PointerType> genericUnpickle(
    ptr: T,
    key: ByteArray,
    data: String,
    unpickle: (T?, Pointer?, NativeSize, Pointer?, NativeSize) -> NativeSize,
    checkError: (NativeSize) -> Unit
) {
    val result = key.withNativeRead { keyPtr ->
        data.withNativeRead { picklePtr, pickleLength ->
            unpickle(ptr, keyPtr, NativeSize(key.size), picklePtr, pickleLength)
        }
    }
    checkError(result)
}
