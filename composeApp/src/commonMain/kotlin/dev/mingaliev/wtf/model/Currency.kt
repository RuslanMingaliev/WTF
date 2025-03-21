package dev.mingaliev.wtf.model

data class Currency(
    val id: Long,
    val short: Char,
) {
    companion object {
        val RUB = Currency(0, '₽')
    }
}