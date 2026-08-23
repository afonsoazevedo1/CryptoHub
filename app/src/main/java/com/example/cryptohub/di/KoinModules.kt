package com.example.cryptohub.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.cryptohub.core.config.AppConfig
import com.example.cryptohub.core.error.ErrorHandler
import com.example.cryptohub.data.repository.ExchangeRepositoryImpl
import com.example.cryptohub.domain.repository.ExchangeRepository
import com.example.cryptohub.domain.usecase.GetExchangeDetailUseCase
import com.example.cryptohub.domain.usecase.GetExchangesUseCase
import com.example.cryptohub.presentation.screens.detail.ExchangeDetailViewModel
import com.example.cryptohub.presentation.screens.list.ExchangeListViewModel
import com.example.cryptohub.presentation.theme.ThemePreference
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

val appModule = module {
    single { androidContext().dataStore }
    single { Dispatchers.IO }
    singleOf(::ThemePreference)
    singleOf(::ErrorHandler)
}

val networkModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                    }
                )
            }
            expectSuccess = true
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("KtorLogging", message)
                    }
                }
                level = LogLevel.INFO
            }
            install(Resources)

            defaultRequest {
                url(AppConfig.CMC_BASE_URL)
                header("X-CMC_PRO_API_KEY", AppConfig.CMC_API_KEY)
            }

            // Simular o comportamento do CurlInterceptor no Ktor
            install("CurlLogger") {
                sendPipeline.intercept(HttpSendPipeline.Monitoring) {
                    val method = context.method.value
                    val url = context.url.buildString()
                    val headers = context.headers.entries().joinToString(" ") { (key, values) ->
                        "-H \"$key: ${values.joinToString(", ")}\""
                    }
                    val curlCommand = "curl -X $method \"$url\" $headers"
                    Log.d("CurlInterceptor", "🚀 REQUEST CURL:\n$curlCommand")
                }
            }
        }
    }
}

val repositoryModule = module {
    singleOf(::ExchangeRepositoryImpl) bind ExchangeRepository::class
}

val useCaseModule = module {
    factoryOf(::GetExchangesUseCase)
    factoryOf(::GetExchangeDetailUseCase)
}

val viewModelModule = module {
    viewModelOf(::ExchangeListViewModel)
    viewModelOf(::ExchangeDetailViewModel)
}
