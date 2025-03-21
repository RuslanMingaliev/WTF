package dev.mingaliev.wtf.ui.feature.add

import dev.mingaliev.wtf.data.model.TransactionData
import dev.mingaliev.wtf.model.Category
import dev.mingaliev.wtf.model.Currency
import dev.mingaliev.wtf.model.MoneySource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class AddTransactionState(
    val title: String = "",
    val amount: String = "",
    val isExpense: Boolean = true,
    val selectedCategory: Category? = null,
    val selectedSource: MoneySource? = null,
    val isAmountError: Boolean = false,
    val categories: List<Category> = defaultCategories,
    val moneySources: List<MoneySource> = defaultMoneySources,
    val isSaveEnabled: Boolean = false
)

class AddTransactionViewModel {
    private val _state = MutableStateFlow(AddTransactionState())
    val state: StateFlow<AddTransactionState> = _state.asStateFlow()

    fun onTitleChange(title: String) {
        _state.update { currentState ->
            currentState.copy(
                title = title,
                isSaveEnabled = canSaveTransaction(
                    title = title,
                    amount = currentState.amount,
                    isAmountError = currentState.isAmountError,
                    selectedCategory = currentState.selectedCategory,
                    selectedSource = currentState.selectedSource
                )
            )
        }
    }

    fun onAmountChange(amount: String) {
        val isAmountError = amount.isNotBlank() && amount.toDoubleOrNull() == null
        _state.update { currentState ->
            currentState.copy(
                amount = amount,
                isAmountError = isAmountError,
                isSaveEnabled = canSaveTransaction(
                    title = currentState.title,
                    amount = amount,
                    isAmountError = isAmountError,
                    selectedCategory = currentState.selectedCategory,
                    selectedSource = currentState.selectedSource
                )
            )
        }
    }

    fun onExpenseChange(isExpense: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isExpense = isExpense,
                selectedCategory = null
            )
        }
    }

    fun onCategorySelect(category: Category) {
        _state.update { currentState ->
            currentState.copy(
                selectedCategory = category,
                isSaveEnabled = canSaveTransaction(
                    title = currentState.title,
                    amount = currentState.amount,
                    isAmountError = currentState.isAmountError,
                    selectedCategory = category,
                    selectedSource = currentState.selectedSource
                )
            )
        }
    }

    fun onSourceSelect(source: MoneySource) {
        _state.update { currentState ->
            currentState.copy(
                selectedSource = source,
                isSaveEnabled = canSaveTransaction(
                    title = currentState.title,
                    amount = currentState.amount,
                    isAmountError = currentState.isAmountError,
                    selectedCategory = currentState.selectedCategory,
                    selectedSource = source
                )
            )
        }
    }

    fun createTransaction(): TransactionData {
        val currentState = state.value
        val amountValue = currentState.amount.toDoubleOrNull() ?: 0.0
        val finalAmount = if (currentState.isExpense) -amountValue else amountValue

        return TransactionData(
            id = Clock.System.now().toEpochMilliseconds().toString(),
            title = currentState.title,
            amount = finalAmount,
            currency = Currency.RUB,
            timestamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            category = currentState.selectedCategory ?: currentState.categories.first(),
            source = currentState.selectedSource ?: currentState.moneySources.first()
        )
    }

    private fun canSaveTransaction(
        title: String,
        amount: String,
        isAmountError: Boolean,
        selectedCategory: Category?,
        selectedSource: MoneySource?
    ): Boolean {
        return title.isNotBlank() &&
            amount.isNotBlank() &&
            !isAmountError &&
            selectedCategory != null &&
            selectedSource != null
    }
}
