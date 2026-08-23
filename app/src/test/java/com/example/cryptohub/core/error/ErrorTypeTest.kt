package com.example.cryptohub.core.error

import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorTypeTest {

    @Test
    fun `NetworkError should have correct message`() {
        val error = ErrorType.NetworkError("Network failed")
        assertEquals("Network failed", error.message)
    }

    @Test
    fun `ServerError should have correct code and message`() {
        val error = ErrorType.ServerError(500, "Server failed")
        assertEquals(500, error.code)
        assertEquals("Server failed", error.message)
    }

    @Test
    fun `TimeoutError should have correct message`() {
        val error = ErrorType.TimeoutError("Timed out")
        assertEquals("Timed out", error.message)
    }

    @Test
    fun `ValidationError should have correct message`() {
        val error = ErrorType.ValidationError("Invalid data")
        assertEquals("Invalid data", error.message)
    }

    @Test
    fun `UnknownError should have correct message`() {
        val error = ErrorType.UnknownError("Unknown")
        assertEquals("Unknown", error.message)
    }

    @Test
    fun `NoDataError should have empty message`() {
        val error = ErrorType.NoDataError
        assertEquals("", error.message)
    }
}
