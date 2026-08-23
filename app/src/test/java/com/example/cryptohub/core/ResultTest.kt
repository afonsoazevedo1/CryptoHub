package com.example.cryptohub.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultTest {

    @Test
    fun `Loading should be a Result object`() {
        val result = Result.Loading
        assertTrue(result is Result.Loading)
    }

    @Test
    fun `Success should hold data`() {
        val data = "Test Data"
        val result = Result.Success(data)
        assertEquals(data, result.data)
    }

    @Test
    fun `Error should hold exception`() {
        val exception = Exception("Test Exception")
        val result = Result.Error(exception)
        assertEquals(exception, result.exception)
    }
}
