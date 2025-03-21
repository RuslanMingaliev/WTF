package dev.mingaliev.wtf.ui.feature.main

import dev.mingaliev.wtf.data.model.TransactionData
import dev.mingaliev.wtf.data.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.math.absoluteValue

data class MainUiState(
    val transactions: List<TransactionData> = emptyList(),
    val filteredTransactions: List<TransactionData> = emptyList(),
    val totalBalance: Double = 0.0,
    val totalExpense: Double = 0.0,
    val totalIncome: Double = 0.0,
    val weeklyIncome: Double = 0.0,
    val weeklyExpense: Double = 0.0,
    val targetExpense: Double = 100000.0,
    val selectedPeriod: Period = Period.DAILY,
    val currentScreen: Screen = Screen.HOME,
    val userName: String = "Ruslan"
)

enum class Period {
    DAILY, WEEKLY, MONTHLY
}

enum class Screen {
    HOME, STATISTICS, TRANSACTIONS, ACCOUNTS, PROFILE
}

class MainViewModel(
    val repository: TransactionRepository
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getStatistics().collect { statistics ->
                _uiState.update { state ->
                    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    val transactions = statistics.weeklyTransactions
                    val filteredTransactions = filterTransactionsByPeriod(
                        transactions = transactions,
                        period = state.selectedPeriod,
                        now = now
                    )

                    state.copy(
                        transactions = transactions,
                        filteredTransactions = filteredTransactions,
                        totalBalance = statistics.totalBalance,
                        totalExpense = statistics.totalExpense,
                        totalIncome = statistics.totalIncome,
                        weeklyIncome = statistics.weeklyTransactions
                            .filter { it.amount > 0 }
                            .sumOf { it.amount },
                        weeklyExpense = statistics.weeklyTransactions
                            .filter { it.amount < 0 }
                            .sumOf { it.amount }
                            .absoluteValue
                    )
                }
            }
        }
    }

    private fun filterTransactionsByPeriod(
        transactions: List<TransactionData>,
        period: Period,
        now: LocalDateTime
    ): List<TransactionData> {
        return when (period) {
            Period.DAILY -> transactions.filter {
                it.timestamp.date == now.date
            }

            Period.WEEKLY -> transactions.filter {
                val daysDiff = now.date.minus(it.timestamp.date)
                daysDiff.days <= 7
            }

            Period.MONTHLY -> transactions.filter {
                it.timestamp.month == now.month &&
                    it.timestamp.year == now.year
            }
        }
    }

    fun onAddTransaction(transaction: TransactionData) {
        viewModelScope.launch {
            repository.addTransaction(transaction)
        }
    }

    fun onPeriodSelected(period: Period) {
        _uiState.update { state ->
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val filteredTransactions = filterTransactionsByPeriod(
                transactions = state.transactions,
                period = period,
                now = now
            )
            state.copy(
                selectedPeriod = period,
                filteredTransactions = filteredTransactions
            )
        }
    }

    fun onScreenSelected(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun onNavigateBack() {
        _uiState.update { it.copy(currentScreen = Screen.HOME) }
    }
}
