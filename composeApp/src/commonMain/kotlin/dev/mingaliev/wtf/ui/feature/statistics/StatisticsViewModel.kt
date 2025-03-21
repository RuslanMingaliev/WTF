package dev.mingaliev.wtf.ui.feature.statistics

import dev.mingaliev.wtf.data.model.TransactionData
import dev.mingaliev.wtf.data.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StatisticsUiState(
    val weeklyTransactions: List<TransactionData> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0
)

class StatisticsViewModel(
    private val repository: TransactionRepository
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.getStatistics().collect { statistics ->
                _uiState.update { currentState ->
                    currentState.copy(
                        weeklyTransactions = statistics.weeklyTransactions,
                        totalIncome = statistics.totalIncome,
                        totalExpense = statistics.totalExpense
                    )
                }
            }
        }
    }
}
