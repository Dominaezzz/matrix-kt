package io.github.matrixkt.apis

import io.github.matrixkt.models.wellknown.DiscoveryInformation
import io.github.matrixkt.models.GetCapabilitiesResponse
import io.github.matrixkt.models.Versions

interface MiscApi {
    /**
     * Gets the versions of the specification supported by the server.
     *
     * Values will take the form `rX.Y.Z`.
     *
     * Only the latest `Z` value will be reported for each supported `X.Y` value.
     * i.e. if the server implements `r0.0.0`, `r0.0.1`, and `r1.2.0`, it will report `r0.0.1` and `r1.2.0`.
     *
     * The server may additionally advertise experimental features it supports through `unstable_features`.
     * These features should be namespaced and may optionally include version information within their name if desired.
     * Features listed here are not for optionally toggling parts of the Matrix specification and should only be used to
     * advertise support for a feature which has not yet landed in the spec.
     * For example, a feature currently undergoing the proposal process may appear here and eventually be taken
     * off this list once the feature lands in the spec and the server deems it reasonable to do so.
     * Servers may wish to keep advertising features here after they've been released into the spec to
     * give clients a chance to upgrade appropriately.
     * Additionally, clients should avoid using unstable features in their stable releases.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     */
    suspend fun getVersions(): Versions

    /**
     * Gets discovery information about the domain.
     * The file may include additional keys, which MUST follow the Java package naming convention, e.g. com.example.myapp.property.
     * This ensures property names are suitably namespaced for each application and reduces the risk of clashes.
     *
     * Note that this endpoint is not necessarily handled by the homeserver, but by another webserver, to be used for discovering the homeserver URL.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     */
    suspend fun getWellKnown(): DiscoveryInformation

    /**
     * Gets information about the server's supported feature set and other relevant capabilities.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     */
    suspend fun getCapabilities(): GetCapabilitiesResponse
}
