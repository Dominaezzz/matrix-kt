package colm.internal

import com.sun.jna.IntegerType
import com.sun.jna.Native

public class NativeSize(value: Long = 0) : IntegerType(Native.SIZE_T_SIZE, value, true) {
    // constructor(value: ULong) : this(value.toLong())
    public constructor(value: Int) : this(value.toLong())

    override fun toByte(): Byte = toLong().toByte()
    override fun toChar(): Char = toLong().toInt().toChar()
    override fun toShort(): Short = toLong().toShort()

    public companion object {
        public val ZERO: NativeSize = NativeSize(0)
    }
}
