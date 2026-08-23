package com.example.cryptohub.presentation.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.mutablePreferencesOf
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemePreferenceTest {

    private val dataStore: DataStore<Preferences> = mockk()
    private lateinit var themePreference: ThemePreference

    @Test
    fun `isDarkModeFlow should emit true when preference is set to true`() = runTest {
        val isDarkModeKey = booleanPreferencesKey("is_dark_mode")
        val preferences = mutablePreferencesOf(isDarkModeKey to true)
        
        every { dataStore.data } returns flowOf(preferences)
        themePreference = ThemePreference(dataStore)

        themePreference.isDarkModeFlow.test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `isDarkModeFlow should emit null when preference is not set`() = runTest {
        val preferences = mutablePreferencesOf()
        
        every { dataStore.data } returns flowOf(preferences)
        themePreference = ThemePreference(dataStore)

        themePreference.isDarkModeFlow.test {
            assertEquals(null, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
