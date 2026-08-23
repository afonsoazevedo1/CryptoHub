package com.example.cryptohub.core.error

import android.content.Context
import com.example.cryptohub.R
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ErrorHandlerTest {

    private lateinit var errorHandler: ErrorHandler
    private val context: Context = mockk()

    @Before
    fun setUp() {
        errorHandler = ErrorHandler(context)
    }

    @Test
    fun `getErrorMessage should return correct string for NetworkError`() {
        every { context.getString(R.string.error_network) } returns "Network Error"
        val result = errorHandler.getErrorMessage(ErrorType.NetworkError())
        assertEquals("Network Error", result)
    }

    @Test
    fun `getErrorMessage should return correct string for ServerError`() {
        every { context.getString(R.string.error_server) } returns "Server Error"
        val result = errorHandler.getErrorMessage(ErrorType.ServerError(500))
        assertEquals("Server Error", result)
    }

    @Test
    fun `getErrorMessage should return correct string for TimeoutError`() {
        every { context.getString(R.string.error_timeout) } returns "Timeout Error"
        val result = errorHandler.getErrorMessage(ErrorType.TimeoutError())
        assertEquals("Timeout Error", result)
    }

    @Test
    fun `getErrorMessage should return correct string for ValidationError`() {
        every { context.getString(R.string.error_validation) } returns "Validation Error"
        val result = errorHandler.getErrorMessage(ErrorType.ValidationError())
        assertEquals("Validation Error", result)
    }

    @Test
    fun `getErrorMessage should return correct string for UnknownError`() {
        every { context.getString(R.string.error_unknown) } returns "Unknown Error"
        val result = errorHandler.getErrorMessage(ErrorType.UnknownError())
        assertEquals("Unknown Error", result)
    }

    @Test
    fun `getErrorMessage should return correct string for NoDataError`() {
        every { context.getString(R.string.error_no_data) } returns "No Data"
        val result = errorHandler.getErrorMessage(ErrorType.NoDataError)
        assertEquals("No Data", result)
    }
}
