package com.pft.tracker.domain

import com.pft.tracker.data.local.dao.CategoryDao
import com.pft.tracker.domain.model.CategoryType
import com.pft.tracker.domain.model.MonthRange
import com.pft.tracker.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.math.max

/**
 * Implements doc §4.4: spent/overBudget/overAmount per category per month.
 */
class BudgetUseCase(private val categoryDao: CategoryDao) {

    fun observeBudgetStatusForTransactions(
        transactionsFlow: Flow<List<TransactionEntity>>,
        accountsFlow: Flow<List<com.pft.tracker.data.local.entity.AccountEntity>> = kotlinx.coroutines.flow.flowOf(emptyList())
    ): Flow<List<CategoryBudgetStatus>> =
        combine(
            categoryDao.observeByType(CategoryType.EXPENSE.name),
            transactionsFlow,
            accountsFlow
        ) { categories, transactions, accounts ->
            val accountMap = accounts.associateBy { it.id }
            val spentMap = transactions
                .filter { tx ->
                    (tx.transactionType == "EXPENSE" || 
                    (tx.transactionType == "TRANSFER" && tx.destinationAccountId != null && accountMap[tx.destinationAccountId]?.isOwnedBySelf == false))
                    && tx.categoryId != null
                }
                .groupBy { it.categoryId!! }
                .mapValues { it.value.sumOf { tx -> tx.amount } }

            categories.map { category ->
                val spent = spentMap[category.id] ?: 0.0
                val budget = category.monthlyBudget
                val hasBudget = budget != null && budget > 0
                val overBudget = hasBudget && spent > budget!!
                val overAmount = if (hasBudget) max(spent - (budget ?: 0.0), 0.0) else 0.0
                CategoryBudgetStatus(
                    categoryId = category.id,
                    categoryName = category.name,
                    icon = category.icon,
                    budget = budget,
                    spent = spent,
                    overBudget = overBudget,
                    overAmount = overAmount
                )
            }
        }

    fun observeBudgetStatus(month: MonthRange): Flow<List<CategoryBudgetStatus>> =
        combine(
            categoryDao.observeByType(CategoryType.EXPENSE.name),
            categoryDao.observeSpentByCategory(month.start, month.end)
        ) { categories, spentRows ->
            val spentMap = spentRows.associateBy({ it.categoryId }, { it.spent })
            categories.map { category ->
                val spent = spentMap[category.id] ?: 0.0
                val budget = category.monthlyBudget
                val hasBudget = budget != null && budget > 0
                val overBudget = hasBudget && spent > budget!!
                val overAmount = if (hasBudget) max(spent - (budget ?: 0.0), 0.0) else 0.0
                CategoryBudgetStatus(
                    categoryId = category.id,
                    categoryName = category.name,
                    icon = category.icon,
                    budget = budget,
                    spent = spent,
                    overBudget = overBudget,
                    overAmount = overAmount
                )
            }
        }

    fun observeSpent(categoryId: Long, month: MonthRange): Flow<Double> =
        categoryDao.observeSpent(categoryId, month.start, month.end)
}

data class CategoryBudgetStatus(
    val categoryId: Long,
    val categoryName: String,
    val icon: String?,
    val budget: Double?,
    val spent: Double,
    val overBudget: Boolean,
    val overAmount: Double
)
