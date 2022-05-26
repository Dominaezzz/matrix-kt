package io.github.matrixkt.clientserver

public interface MatrixRpc<Method : RpcMethod, Location, RequestBody : Any, ResponseBody> {
    public val url: Location
    public val body: RequestBody

    public interface WithAuth<Method : RpcMethod, Location, RequestBody : Any, ResponseBody> {
        public val url: Location
        public val body: RequestBody
    }
}
