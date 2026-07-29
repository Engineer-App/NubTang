package com.pft.tracker.domain.model

enum class TransactionType {
    EXPENSE, INCOME, TRANSFER, CASH_WITHDRAWAL, CREDIT_CARD_PAYMENT
}

enum class AccountType {
    CASH, BANK
}

enum class CategoryType {
    EXPENSE, INCOME
}

enum class RecurringFrequency {
    MONTHLY, WEEKLY, YEARLY, ONE_TIME, LAST_DAY_OF_MONTH
}

enum class StatementStatus {
    NOT_YET_BILLED, BILLED, PARTIALLY_PAID, PAID, OVERDUE
}
