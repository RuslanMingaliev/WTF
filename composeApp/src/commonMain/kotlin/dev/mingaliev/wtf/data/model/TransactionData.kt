package dev.mingaliev.wtf.data.model

import dev.mingaliev.wtf.model.Category
import dev.mingaliev.wtf.model.Currency
import dev.mingaliev.wtf.model.MoneySource
import kotlinx.datetime.LocalDateTime

data class TransactionData(
    val id: String,
    val amount: Double,
    val currency: Currency,
    val title: String,
    val timestamp: LocalDateTime,
    val category: Category,
    val source: MoneySource
)

data class TransactionStatistics(
    val totalBalance: Double,
    val totalExpense: Double,
    val totalIncome: Double,
    val weeklyTransactions: List<TransactionData>,
    val monthlyTransactions: List<TransactionData>,
    val yearlyTransactions: List<TransactionData>
)

data class TransactionsByPeriod(
    val transactions: List<TransactionData>,
    val totalIncome: Double,
    val totalExpense: Double
) 