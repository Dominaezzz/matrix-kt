package io.github.matrixkt.models

public class MatrixException(public val error: MatrixError) : Exception(error.error)
