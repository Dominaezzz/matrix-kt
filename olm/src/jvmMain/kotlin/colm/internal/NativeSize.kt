package colm.internal

import com.sun.jna.IntegerType
import com.sun.jna.Native

class NativeSize(value: Long = 0) : IntegerType(Native.SIZE_T_SIZE, value, true) {
    // constructor(value: ULong) : this(value.toLong())
    constructor(value: Int) : this(value.toLong())

    override fun toByte(): Byte = toLong().toByte()
    override fun toChar(): Char = toLong().toChar()
    override fun toShort(): Short = toLong().toShort()

    companion object {
        val ZERO = NativeSize(0)
    }
}
