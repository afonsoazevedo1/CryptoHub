package com.example.cryptohub.presentation.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptohub.core.Result
import com.example.cryptohub.core.error.ErrorHandler
import com.example.cryptohub.core.error.ErrorType
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.domain.usecase.GetExchangesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExchangeListUiState(
    val isLoading: Boolean = false,
    val exchanges: List<ExchangeListItem> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val isPaginationLoading: Boolean = false
)

class ExchangeListViewModel(
    private val getExchangesUseCase: GetExchangesUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeListUiState())
    val uiState: StateFlow<ExchangeListUiState> = _uiState.asStateFlow()

    private var currentStart = INITIAL_PAGE_START
    private val pageSize = PAGE_SIZE

    init {
        loadExchanges()
    }

    fun loadExchanges(isNextPage: Boolean = false) {
        if (_uiState.value.isLoading || _uiState.value.isPaginationLoading || _uiState.value.endReached) return

        viewModelScope.launch {
            getExchangesUseCase(start = currentStart, limit = pageSize).collect { result ->
                _uiState.value = when (result) {
                    is Result.Loading -> {
                        if (isNextPage) {
                            _uiState.value.copy(isPaginationLoading = true)
                        } else {
                            _uiState.value.copy(isLoading = true, error = null)
                        }
                    }
                    is Result.Success -> {
                        currentStart += pageSize
                        _uiState.value.copy(
                            isLoading = false,
                            isPaginationLoading = false,
                            exchanges = _uiState.value.exchanges + result.data,
                            error = null,
                            endReached = result.data.size < pageSize
                        )
                    }
                    is Result.Error -> _uiState.value.copy(
                        isLoading = false,
                        isPaginationLoading = false,
                        error = errorHandler.getErrorMessage(result.exception as? ErrorType ?: ErrorType.UnknownError())
                    )
                }
            }
        }
    }

    fun refresh() {
        currentStart = INITIAL_PAGE_START
        _uiState.value = ExchangeListUiState()
        loadExchanges()
    }

    companion object {
        private const val INITIAL_PAGE_START = 1
        private const val PAGE_SIZE = 8
    }
}
