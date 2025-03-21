package dev.mingaliev.wtf.ui.feature.add

import dev.mingaliev.wtf.model.Category
import dev.mingaliev.wtf.model.MoneySource

val defaultCategories = listOf(
    // Расходы
    Category("1", "Groceries", "🛒", 0xFF4CAF50),
    Category("2", "Transport", "🚌", 0xFF2196F3),
    Category("3", "Entertainment", "🎮", 0xFF9C27B0),
    Category("4", "Health", "💊", 0xFFE91E63),
    Category("5", "Clothing", "👕", 0xFF795548),
    Category("6", "Cafe", "☕️", 0xFFFF9800),
    Category("7", "Sport", "🏃", 0xFF00BCD4),
    Category("8", "Gifts", "🎁", 0xFF8BC34A),
    Category("9", "Other", "📌", 0xFF607D8B),
    // Доходы
    Category("10", "Salary", "💰", 0xFF4CAF50)
)

val defaultMoneySources = listOf(
    MoneySource("1", "Cash", "💵", 0xFF4CAF50),
    MoneySource("2", "Card", "💳", 0xFF2196F3),
    MoneySource("3", "Bank", "🏦", 0xFF9C27B0),
    MoneySource("4", "Crypto", "₿", 0xFF607D8B),
    MoneySource("5", "Other", "📌", 0xFF9E9E9E)
) 