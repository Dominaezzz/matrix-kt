package colm.internal

import com.sun.jna.Pointer
import com.sun.jna.PointerType

public class OlmSAS : PointerType {
    public constructor(address: Pointer?) : super(address)
    public constructor() : super()
}
