package io.github.matrixkt.utils

public interface MatrixRpc<Method : RpcMethod, Location, RequestBody, ResponseBody> {
    public val url: Location
    public val body: RequestBody

    public interface WithAuth<Method : RpcMethod, Location, RequestBody, ResponseBody> {
        public val url: Location
        public val body: RequestBody
    }
}
