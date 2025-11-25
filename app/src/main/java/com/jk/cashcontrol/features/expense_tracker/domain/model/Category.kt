package com.jk.cashcontrol.features.expense_tracker.domain.model

data class Category(
    val name : String,
    val icon : Int
)

data class CategoryWithAmount(
    val name: String,
    val amount: Float
)

fun Map<String, Float>.toCategoryWithAmounts(): List<CategoryWithAmount> {
    return this.toList()
        .sortedByDescending { (_, amount) -> amount }
        .map { CategoryWithAmount(name = it.first, amount = it.second) }
}