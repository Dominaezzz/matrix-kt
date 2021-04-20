package io.github.matrixkt.utils.resource

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS)
@SerialInfo
public annotation class Resource(val path: String)
