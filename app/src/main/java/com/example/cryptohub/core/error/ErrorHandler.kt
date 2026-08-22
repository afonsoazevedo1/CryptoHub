package com.example.cryptohub.core.error

import android.content.Context
import com.example.cryptohub.R

class ErrorHandler(private val context: Context) {
    fun getErrorMessage(errorType: ErrorType): String {
        return when (errorType) {
            is ErrorType.NetworkError -> context.getString(R.string.error_network)
            is ErrorType.ServerError -> context.getString(R.string.error_server)
            is ErrorType.TimeoutError -> context.getString(R.string.error_timeout)
            is ErrorType.ValidationError -> context.getString(R.string.error_validation)
            is ErrorType.UnknownError -> context.getString(R.string.error_unknown)
            ErrorType.NoDataError -> context.getString(R.string.error_no_data)
        }
    }
}