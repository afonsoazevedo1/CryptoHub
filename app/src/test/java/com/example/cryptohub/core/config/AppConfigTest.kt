package com.example.cryptohub.core.config

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AppConfigTest {

    @Test
    fun `AppConfig should have non-null API Key`() {
        // BuildConfig might be empty in unit tests, but we check if it's accessible
        assertNotNull(AppConfig.CMC_API_KEY)
    }

    @Test
    fun `AppConfig should have correct base URL`() {
        assertEquals("https://pro-api.coinmarketcap.com/", AppConfig.CMC_BASE_URL)
    }
}
