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
            addTransaction(
                repository = repository,
                title = "Salary",
                amount = 150000.0,
                category = defaultCategories[6],
                source = defaultMoneySources[1],
                timestamp = now,
            )
            addTransaction(
                repository = repository,
                title = "Bonus",
                amount = 50000.0,
                category = defaultCategories[6],
                source = defaultMoneySources[1],
                timestamp = currentMoment.minus(DatePeriod(days = 2), timeZone).toLocalDateTime(timeZone),
            )
            addTransaction(
                repository = repository,
                title = "Debt return",
                amount = 15000.0,
                category = defaultCategories[5],
                source = defaultMoneySources[2],
                timestamp = currentMoment.minus(DatePeriod(days = 5), timeZone).toLocalDateTime(timeZone),
            )

            // Daily expenses
            addTransaction(
                repository = repository,
                title = "Groceries at Magnit",
                amount = -2500.0,
                category = defaultCategories[0],
                source = defaultMoneySources[1],
                timestamp = now,
            )
            addTransaction(
                repository = repository,
                title = "Lunch at cafe",
                amount = -800.0,
                category = defaultCategories[7],
                source = defaultMoneySources[1],
                timestamp = now,
            )
            addTransaction(
                repository = repository,
                title = "Metro fare",
                amount = -100.0,
                category = defaultCategories[1],
                source = defaultMoneySources[1],
                timestamp = now,
            )

            // Weekly expenses
            addTransaction(
                repository = repository,
                title = "Nike sneakers",
                amount = -8500.0,
                category = defaultCategories[4],
                source = defaultMoneySources[1],
                timestamp = currentMoment.minus(DatePeriod(days = 2), timeZone).toLocalDateTime(timeZone),
            )
            addTransaction(
                repository = repository,
                title = "Dinner at restaurant",
                amount = -3500.0,
                category = defaultCategories[7],
                source = defaultMoneySources[1],
                timestamp = currentMoment.minus(DatePeriod(days = 3), timeZone).toLocalDateTime(timeZone)
            )
            addTransaction(
                repository = repository,
                title = "Gym membership",
                amount = -4000.0,
                category = defaultCategories[8],
                source = defaultMoneySources[1],
                timestamp = currentMoment.minus(DatePeriod(days = 4), timeZone).toLocalDateTime(timeZone)
            )
            addTransaction(
                repository = repository,
                title = "Groceries at Auchan",
                amount = -4500.0,
                category = defaultCategories[0],
                source = defaultMoneySources[1],
                timestamp = currentMoment.minus(DatePeriod(days = 5), timeZone).toLocalDateTime(timeZone)
            )

            // Monthly expenses
            addTransaction(
                repository = repository,
                title = "Gift for mom",
                amount = -5000.0,
                category = defaultCategories[9],
                source = defaultMoneySources[1],
                timestamp = currentMoment.minus(DatePeriod(days = 10), timeZone).toLocalDateTime(timeZone)
            )
            addTransaction(
                repository = repository,
                title = "Gaming console",
                amount = -35000.0,
                category = defaultCategories[2],
                source = defaultMoneySources[1],
                timestamp = currentMoment.minus(DatePeriod(days = 15), timeZone).toLocalDateTime(timeZone)
            )
            addTransaction(
                repository = repository,
                title = "Dentist visit",
                amount = -15000.0,
                category = defaultCategories[3],
                source = defaultMoneySources[1],
                timestamp = currentMoment.minus(DatePeriod(days = 20), timeZone).toLocalDateTime(timeZone)
            )
            addTransaction(
                repository = repository,
                title = "Winter jacket",
                amount = -12000.0,
                category = defaultCategories[4],
                source = defaultMoneySources[1],
                timestamp = currentMoment.minus(DatePeriod(days = 25), timeZone).toLocalDateTime(timeZone)
            )
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
