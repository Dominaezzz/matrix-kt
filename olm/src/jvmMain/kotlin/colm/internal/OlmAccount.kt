package colm.internal

import com.sun.jna.Pointer
import com.sun.jna.PointerType

public class OlmAccount : PointerType {
    public constructor(address: Pointer?) : super(address)
    public constructor() : super()
}
