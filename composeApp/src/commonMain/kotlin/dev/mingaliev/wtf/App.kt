package dev.mingaliev.wtf

import androidx.compose.runtime.Composable
import dev.mingaliev.wtf.data.model.TransactionData
import dev.mingaliev.wtf.data.repository.InMemoryTransactionRepository
import dev.mingaliev.wtf.designsystem.AppTheme
import dev.mingaliev.wtf.model.Category
import dev.mingaliev.wtf.model.Currency
import dev.mingaliev.wtf.model.MoneySource
import dev.mingaliev.wtf.ui.feature.main.MainViewModel
import dev.mingaliev.wtf.ui.navigation.NavigationContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

private val defaultCategories = listOf(
    Category("1", "Groceries", "🛒", 0xFF4CAF50),
    Category("2", "Transport", "🚌", 0xFF2196F3),
    Category("3", "Entertainment", "🎮", 0xFF9C27B0),
    Category("4", "Health", "💊", 0xFFE91E63),
    Category("5", "Clothing", "👕", 0xFF795548),
    Category("6", "Other", "📌", 0xFF607D8B),
    Category("7", "Salary", "💰", 0xFF4CAF50),
    Category("8", "Cafe", "☕️", 0xFFFF9800),
    Category("9", "Sport", "🏃", 0xFF00BCD4),
    Category("10", "Gifts", "🎁", 0xFF8BC34A)
)

private val defaultMoneySources = listOf(
    MoneySource("1", "Cash", "💵", 0xFF4CAF50),
    MoneySource("2", "Card", "💳", 0xFF4CAF50),
    MoneySource("3", "Transfer", "💸", 0xFF4CAF50)
)

@Composable
@Preview
fun App() {
    AppTheme {
        val repository = InMemoryTransactionRepository()
        val viewModel = MainViewModel(repository)

        // Add initial data
        CoroutineScope(Dispatchers.Main).launch {
            val currentMoment = Clock.System.now()
            val timeZone = TimeZone.currentSystemDefault()
            val now = currentMoment.toLocalDateTime(timeZone)

            // Income
            addTransaction(repository, "Salary", 150000.0, defaultCategories[6], defaultMoneySources[1], now)
            addTransaction(repository, "Bonus", 50000.0, defaultCategories[6], defaultMoneySources[1],
                currentMoment.minus(DatePeriod(days = 2), timeZone).toLocalDateTime(timeZone))
            addTransaction(repository, "Debt return", 15000.0, defaultCategories[5], defaultMoneySources[2],
                currentMoment.minus(DatePeriod(days = 5), timeZone).toLocalDateTime(timeZone))

            // Daily expenses
            addTransaction(repository, "Groceries at Magnit", -2500.0, defaultCategories[0], defaultMoneySources[1], now)
            addTransaction(repository, "Lunch at cafe", -800.0, defaultCategories[7], defaultMoneySources[1], now)
            addTransaction(repository, "Metro fare", -100.0, defaultCategories[1], defaultMoneySources[1], now)

            // Weekly expenses
            addTransaction(repository, "Nike sneakers", -8500.0, defaultCategories[4], defaultMoneySources[1],
                currentMoment.minus(DatePeriod(days = 2), timeZone).toLocalDateTime(timeZone))
            addTransaction(repository, "Dinner at restaurant", -3500.0, defaultCategories[7], defaultMoneySources[1],
                currentMoment.minus(DatePeriod(days = 3), timeZone).toLocalDateTime(timeZone))
            addTransaction(repository, "Gym membership", -4000.0, defaultCategories[8], defaultMoneySources[1],
                currentMoment.minus(DatePeriod(days = 4), timeZone).toLocalDateTime(timeZone))
            addTransaction(repository, "Groceries at Auchan", -4500.0, defaultCategories[0], defaultMoneySources[1],
                currentMoment.minus(DatePeriod(days = 5), timeZone).toLocalDateTime(timeZone))

            // Monthly expenses
            addTransaction(repository, "Gift for mom", -5000.0, defaultCategories[9], defaultMoneySources[1],
                currentMoment.minus(DatePeriod(days = 10), timeZone).toLocalDateTime(timeZone))
            addTransaction(repository, "Gaming console", -35000.0, defaultCategories[2], defaultMoneySources[1],
                currentMoment.minus(DatePeriod(days = 15), timeZone).toLocalDateTime(timeZone))
            addTransaction(repository, "Dentist visit", -15000.0, defaultCategories[3], defaultMoneySources[1],
                currentMoment.minus(DatePeriod(days = 20), timeZone).toLocalDateTime(timeZone))
            addTransaction(repository, "Winter jacket", -12000.0, defaultCategories[4], defaultMoneySources[1],
                currentMoment.minus(DatePeriod(days = 25), timeZone).toLocalDateTime(timeZone))
        }

        NavigationContainer(viewModel)
    }
}

private suspend fun addTransaction(
    repository: InMemoryTransactionRepository,
    title: String,
    amount: Double,
    category: Category,
    source: MoneySource,
    timestamp: LocalDateTime
) {
    repository.addTransaction(
        TransactionData(
            id = Clock.System.now().toEpochMilliseconds().toString(),
            amount = amount,
            currency = Currency.RUB,
            title = title,
            timestamp = timestamp,
            category = category,
            source = source
        )
    )
}
