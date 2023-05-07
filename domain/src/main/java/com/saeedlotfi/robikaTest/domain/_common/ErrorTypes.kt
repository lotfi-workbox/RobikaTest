package com.saeedlotfi.robikaTest.domain._common

sealed class ErrorTypes : Throwable() {

    data class ModelValidationException(
        val modelName: String,
        val propertyName: String
    ) : ErrorTypes()

    object EmptyOutFlowException : ErrorTypes()

}

