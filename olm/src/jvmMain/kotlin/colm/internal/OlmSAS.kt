package colm.internal

import com.sun.jna.Pointer
import com.sun.jna.PointerType

class OlmSAS : PointerType {
    constructor(address: Pointer?) : super(address)
    constructor() : super()
}
