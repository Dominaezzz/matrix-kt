package io.github.matrixkt.client

import io.github.matrixkt.clientserver.models.MatrixError

public class MatrixException(public val error: MatrixError) : Exception(error.error)
