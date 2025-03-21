package dev.mingaliev.wtf.ui.feature.profile

import dev.mingaliev.wtf.data.model.Currency
import dev.mingaliev.wtf.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProfileUiState(
    val user: User = User(
        id = "1",
        name = "User",
        email = "user@example.com"
    ),
    val isEditing: Boolean = false,
    val availableCurrencies: List<Currency> = Currency.entries
)

class ProfileViewModel {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.update { state ->
            state.copy(
                user = state.user.copy(name = name)
            )
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { state ->
            state.copy(
                user = state.user.copy(email = email)
            )
        }
    }

    fun updateMonthlyBudget(budget: Double) {
        _uiState.update { state ->
            state.copy(
                user = state.user.copy(monthlyBudget = budget)
            )
        }
    }

    fun updateCurrency(currency: Currency) {
        _uiState.update { state ->
            state.copy(
                user = state.user.copy(currency = currency)
            )
        }
    }

    fun toggleNotifications() {
        _uiState.update { state ->
            state.copy(
                user = state.user.copy(
                    notificationsEnabled = !state.user.notificationsEnabled
                )
            )
        }
    }

    fun toggleDarkTheme() {
        _uiState.update { state ->
            state.copy(
                user = state.user.copy(
                    darkThemeEnabled = !state.user.darkThemeEnabled
                )
            )
        }
    }

    fun startEditing() {
        _uiState.update { it.copy(isEditing = true) }
    }

    fun stopEditing() {
        _uiState.update { it.copy(isEditing = false) }
    }
}
