package com.pft.tracker.data.local

import com.pft.tracker.data.local.entity.AccountEntity
import com.pft.tracker.data.local.entity.CategoryEntity
import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.domain.model.CategoryType

/**
 * Seed data for NabTang app with default categories and accounts.
 * Clean slate with 0 balances and no transactions.
 */
object SeedData {

    suspend fun populate(db: AppDatabase) {
        val accountDao = db.accountDao()
        val cardDao = db.creditCardDao()
        val categoryDao = db.categoryDao()

        // Default Accounts (0.0 balance)
        accountDao.insert(AccountEntity(name = "เงินสด", accountType = "CASH", openingBalance = 0.0, isOwnedBySelf = true))
        accountDao.insert(AccountEntity(name = "ธนาคารกสิกรไทย (บัญชีตัวเอง)", accountType = "BANK", bankName = "กสิกรไทย", openingBalance = 0.0, isOwnedBySelf = true))
        accountDao.insert(AccountEntity(name = "ธนาคารกรุงเทพ (บัญชีตัวเอง)", accountType = "BANK", bankName = "กรุงเทพ", openingBalance = 0.0, isOwnedBySelf = true))
        accountDao.insert(AccountEntity(name = "ธนาคารกรุงไทย (บัญชีตัวเอง)", accountType = "BANK", bankName = "กรุงไทย", openingBalance = 0.0, isOwnedBySelf = true))
        accountDao.insert(AccountEntity(name = "ธนาคารไทยพาณิชย์ (บัญชีตัวเอง)", accountType = "BANK", bankName = "ไทยพาณิชย์", openingBalance = 0.0, isOwnedBySelf = true))

        // Default Expense Categories
        val expenseCats = listOf(
            "อาหาร", "เดินทาง", "น้ำมัน", "ที่พัก", "ค่าน้ำ", "ค่าไฟ", 
            "โทรศัพท์และอินเทอร์เน็ต", "ซื้อของ", "สุขภาพ", "บันเทิง", "งาน", "อื่น ๆ (รายจ่าย)"
        )
        expenseCats.forEachIndexed { index, name ->
            categoryDao.insert(CategoryEntity(name = name, categoryType = CategoryType.EXPENSE.name, displayOrder = index))
        }

        // Default Income Categories
        val incomeCats = listOf("เงินเดือน", "รายได้พิเศษ", "โบนัส", "อื่น ๆ (รายรับ)")
        incomeCats.forEachIndexed { index, name ->
            categoryDao.insert(CategoryEntity(name = name, categoryType = CategoryType.INCOME.name, displayOrder = index))
        }

        // Default Credit Card (Optional, keeping one as template)
        cardDao.insert(
            CreditCardEntity(
                name = "บัตรเครดิต", issuer = "ธนาคาร", cardNumberLast4 = "0000",
                creditLimit = 0.0, statementDay = 1, paymentDueDay = 1
            )
        )
    }
}
