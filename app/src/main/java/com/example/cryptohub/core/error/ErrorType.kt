package com.example.cryptohub.core.error

sealed class ErrorType : Throwable() {
    data class NetworkError(override val message: String = "") : ErrorType()
    data class ServerError(val code: Int, override val message: String = "") : ErrorType()
    data class TimeoutError(override val message: String = "") : ErrorType()
    data class ValidationError(override val message: String = "") : ErrorType()
    data class UnknownError(override val message: String = "") : ErrorType()
    data object NoDataError : ErrorType() {
        override val message: String = ""
    }
}
