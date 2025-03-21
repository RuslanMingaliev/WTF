package dev.mingaliev.wtf.data.repository

import dev.mingaliev.wtf.data.model.TransactionData
import dev.mingaliev.wtf.data.model.TransactionStatistics
import dev.mingaliev.wtf.data.model.TransactionsByPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.math.absoluteValue

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<TransactionData>>
    fun getTransactionsByPeriod(from: LocalDateTime, to: LocalDateTime): Flow<TransactionsByPeriod>
    fun getStatistics(): Flow<TransactionStatistics>
    suspend fun addTransaction(transaction: TransactionData)
    suspend fun deleteTransaction(transactionId: String)
}

class InMemoryTransactionRepository : TransactionRepository {
    private val _transactions = MutableStateFlow<List<TransactionData>>(emptyList())

    override fun getAllTransactions(): Flow<List<TransactionData>> = _transactions

    override fun getTransactionsByPeriod(
        from: LocalDateTime,
        to: LocalDateTime
    ): Flow<TransactionsByPeriod> = _transactions.map { transactions ->
        val filteredTransactions = transactions.filter {
            it.timestamp in from..to
        }
        
        TransactionsByPeriod(
            transactions = filteredTransactions,
            totalIncome = filteredTransactions.filter { it.amount > 0 }.sumOf { it.amount },
            totalExpense = filteredTransactions.filter { it.amount < 0 }.sumOf { it.amount }.absoluteValue
        )
    }

    override fun getStatistics(): Flow<TransactionStatistics> = _transactions.map { transactions ->
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val startOfWeek = now.date.minus(DatePeriod(days = now.dayOfWeek.ordinal))
        val startOfMonth = now.date.minus(DatePeriod(days = now.dayOfMonth - 1))
        val startOfYear = now.date.minus(DatePeriod(months = now.month.ordinal))

        val weeklyTransactions = transactions.filter { it.timestamp.date >= startOfWeek }
        val monthlyTransactions = transactions.filter { it.timestamp.date >= startOfMonth }
        val yearlyTransactions = transactions.filter { it.timestamp.date >= startOfYear }

        TransactionStatistics(
            totalBalance = transactions.sumOf { it.amount },
            totalExpense = transactions.filter { it.amount < 0 }.sumOf { it.amount }.absoluteValue,
            totalIncome = transactions.filter { it.amount > 0 }.sumOf { it.amount },
            weeklyTransactions = weeklyTransactions,
            monthlyTransactions = monthlyTransactions,
            yearlyTransactions = yearlyTransactions
        )
    }

    override suspend fun addTransaction(transaction: TransactionData) {
        _transactions.value += transaction
    }

    override suspend fun deleteTransaction(transactionId: String) {
        _transactions.value = _transactions.value.filter { it.id != transactionId }
    }
} 