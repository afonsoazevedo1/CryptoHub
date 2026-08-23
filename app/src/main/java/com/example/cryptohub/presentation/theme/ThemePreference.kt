package com.example.cryptohub.presentation.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreference(dataStore: DataStore<Preferences>) {
    companion object {
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    val isDarkModeFlow: Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[IS_DARK_MODE]
    }
}
