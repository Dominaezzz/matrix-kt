package colm.internal

import com.sun.jna.Pointer
import com.sun.jna.PointerType

class OlmAccount : PointerType {
    constructor(address: Pointer?) : super(address)
    constructor() : super()
}
