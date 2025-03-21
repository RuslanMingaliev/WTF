package dev.mingaliev.wtf.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatar: String? = null,
    val monthlyBudget: Double = 30000.0,
    val currency: Currency = Currency.RUB,
    val notificationsEnabled: Boolean = true,
    val darkThemeEnabled: Boolean = false
)

enum class Currency(val symbol: String, val code: String) {
    RUB("₽", "RUB"),
    USD("$", "USD"),
    EUR("€", "EUR")
}
