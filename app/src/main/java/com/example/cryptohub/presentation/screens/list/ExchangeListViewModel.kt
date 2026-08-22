package com.example.cryptohub.presentation.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptohub.core.Result
import com.example.cryptohub.core.error.ErrorHandler
import com.example.cryptohub.core.error.ErrorType
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.domain.usecase.GetExchangesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExchangeListUiState(
    val isLoading: Boolean = true,
    val exchanges: List<ExchangeListItem> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ExchangeListViewModel @Inject constructor(
    private val getExchangesUseCase: GetExchangesUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeListUiState())
    val uiState: StateFlow<ExchangeListUiState> = _uiState.asStateFlow()

    init {
        loadExchanges()
    }

    fun loadExchanges() {
        viewModelScope.launch {
            getExchangesUseCase().collect { result ->
                _uiState.value = when (result) {
                    is Result.Loading -> _uiState.value.copy(isLoading = true, error = null)
                    is Result.Success -> _uiState.value.copy(
                        isLoading = false,
                        exchanges = result.data,
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
