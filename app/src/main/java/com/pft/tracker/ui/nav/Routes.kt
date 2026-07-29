package com.pft.tracker.ui.nav

object Routes {
    const val DASHBOARD = "dashboard"
    const val TRANSACTIONS = "transactions"
    const val ACCOUNTS = "accounts"
    const val ACCOUNT_DETAIL = "accounts/{accountId}"
    const val ACCOUNT_EDIT = "account_edit/{accountId}"
    const val CREDIT_CARDS = "credit_cards"
    const val CREDIT_CARD_DETAIL = "credit_cards/{cardId}"
    const val CREDIT_CARD_EDIT = "credit_card_edit/{cardId}"
    const val CREDIT_LIMIT_GROUPS = "credit_limit_groups"
    const val CATEGORIES = "categories"
    const val RECURRING = "recurring"
    const val RECURRING_EDIT = "recurring_edit/{planId}"
    const val SETTINGS = "settings"
    const val TRANSACTION_EDIT = "transaction_edit/{transactionId}/{type}"

    const val NEW_ID = "0"

    fun accountDetail(id: Long) = "accounts/$id"
    fun accountEdit(id: Long = 0) = "account_edit/$id"
    fun creditCardDetail(id: Long) = "credit_cards/$id"
    fun creditCardEdit(id: Long = 0) = "credit_card_edit/$id"
    fun recurringEdit(id: Long = 0) = "recurring_edit/$id"
    fun transactionEdit(id: Long = 0, type: String) = "transaction_edit/$id/$type"
}
