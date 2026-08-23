package com.example.cryptohub.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.cryptohub.presentation.navigation.Routes
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class CheckModulesTest : KoinTest {

    @Before
    fun setUp() {
        stopKoin()
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `verify koin modules`() {
        val mockContext = mockk<Context>(relaxed = true)
        
        // Mock Log
        io.mockk.mockkStatic(android.util.Log::class)
        io.mockk.every { android.util.Log.d(any(), any()) } returns 0
        io.mockk.every { android.util.Log.e(any(), any()) } returns 0
        io.mockk.every { android.util.Log.e(any(), any(), any()) } returns 0
        io.mockk.every { android.util.Log.i(any(), any()) } returns 0
        io.mockk.every { android.util.Log.w(any(), any<String>()) } returns 0

        // Mock Navigation toRoute
        io.mockk.mockkStatic("androidx.navigation.SavedStateHandleKt")
        io.mockk.every { 
            any<SavedStateHandle>().toRoute<Routes.ExchangeDetail>() 
        } returns Routes.ExchangeDetail(1, "Binance", 1000.0, "2017-07-14")

        val mockSavedStateHandle = SavedStateHandle()
        
        // We override networkModule to avoid using Android engine in unit tests
        val mockNetworkModule = module {
            single {
                io.ktor.client.HttpClient(MockEngine) {
                    engine {
                        addHandler { _ ->
                            respond("")
                        }
                    }
                }
            }
        }

        checkModules {
            androidContext(mockContext)
            modules(
                appModule, 
                mockNetworkModule, 
                repositoryModule, 
                useCaseModule, 
                viewModelModule,
                module { factory { mockSavedStateHandle } }
            )
        }
    }
}
