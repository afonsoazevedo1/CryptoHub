package com.example.cryptohub.presentation.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.cryptohub.core.Result
import com.example.cryptohub.core.error.ErrorHandler
import com.example.cryptohub.core.error.ErrorType
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.usecase.GetExchangeDetailUseCase
import com.example.cryptohub.presentation.navigation.Routes
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExchangeDetailUiState(
    val isLoading: Boolean = true,
    val exchange: ExchangeDetail? = null,
    val error: String? = null
)

class ExchangeDetailViewModel(
    private val getExchangeDetailUseCase: GetExchangeDetailUseCase,
    private val errorHandler: ErrorHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route: Routes.ExchangeDetail = savedStateHandle.toRoute<Routes.ExchangeDetail>()
    private val exchangeId: Int = route.id
    private var fetchJob: Job? = null

    val initialExchange = ExchangeDetail(
        id = route.id,
        name = route.name,
        logo = null,
        description = null,
        website = null,
        makerFee = null,
        takerFee = null,
        spotVolumeUsd = route.spotVolumeUsd,
        dateLaunched = route.dateLaunched,
        currencies = emptyList()
    )

    private val _uiState = MutableStateFlow(ExchangeDetailUiState(exchange = initialExchange))
    val uiState: StateFlow<ExchangeDetailUiState> = _uiState.asStateFlow()

    init {
        loadExchangeDetail(exchangeId)
    }

    fun loadExchangeDetail(id: Int = exchangeId) {
        // Evita chamadas duplicadas se já estiver carregando o mesmo ID
        if (_uiState.value.isLoading &&
            fetchJob?.isActive == true &&
            _uiState.value.exchange?.description != null) return

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            getExchangeDetailUseCase(id).collect { result ->
                _uiState.value = when (result) {
                    is Result.Loading -> _uiState.value.copy(isLoading = true, error = null)
                    is Result.Success -> _uiState.value.copy(
                        isLoading = false,
                        exchange = result.data,
                        error = null
                    )
                    is Result.Error -> _uiState.value.copy(
                        isLoading = false,
                        error = errorHandler.getErrorMessage(result.exception as? ErrorType ?: ErrorType.UnknownError())
                    )
                }
            }
        }
    }
}
