package com.example.cryptohub.presentation.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptohub.core.Result
import com.example.cryptohub.core.error.ErrorHandler
import com.example.cryptohub.core.error.ErrorType
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.usecase.GetExchangeDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExchangeDetailUiState(
    val isLoading: Boolean = true,
    val exchange: ExchangeDetail? = null,
    val error: String? = null
)

@HiltViewModel
class ExchangeDetailViewModel @Inject constructor(
    private val getExchangeDetailUseCase: GetExchangeDetailUseCase,
    private val errorHandler: ErrorHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeDetailUiState())
    val uiState: StateFlow<ExchangeDetailUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<Int>("exchangeId")?.let { exchangeId ->
            loadExchangeDetail(exchangeId)
        }
    }

    fun loadExchangeDetail(exchangeId: Int) {
        viewModelScope.launch {
            getExchangeDetailUseCase(exchangeId).collect { result ->
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