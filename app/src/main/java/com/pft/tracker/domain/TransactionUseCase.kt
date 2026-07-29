package com.pft.tracker.domain

import androidx.room.withTransaction
import com.pft.tracker.data.local.AppDatabase
import com.pft.tracker.data.local.dao.TransactionDao
import com.pft.tracker.data.local.entity.TransactionEntity
import com.pft.tracker.domain.model.TransactionType

/**
 * The only place allowed to write balance-affecting transactions (doc §2).
 * Validates the source/destination mutual-exclusivity rules, then persists
 * inside a single Room transaction — every transaction type here writes a
 * single row, so atomicity is automatic, but the explicit `withTransaction`
 * wrapper is kept per the doc's requirement and to keep room for any future
 * multi-row side effect (e.g. statement bookkeeping) without revisiting callers.
 */
class TransactionUseCase(
    private val db: AppDatabase,
    private val transactionDao: TransactionDao
) {

    suspend fun add(transaction: TransactionEntity): Result<Long> = runCatching {
        validate(transaction)
        db.withTransaction { transactionDao.insert(transaction) }
    }

    suspend fun update(transaction: TransactionEntity): Result<Unit> = runCatching {
        validate(transaction)
        db.withTransaction {
            transactionDao.update(transaction.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    suspend fun delete(transaction: TransactionEntity): Result<Unit> = runCatching {
        db.withTransaction { transactionDao.delete(transaction) }
    }

    suspend fun deleteInRange(start: Long, end: Long): Result<Unit> = runCatching {
        db.withTransaction { transactionDao.deleteInRange(start, end) }
    }

    suspend fun deleteAll(): Result<Unit> = runCatching {
        db.withTransaction {
            transactionDao.deleteAll()
        }
    }

    suspend fun resetEverything(): Result<Unit> = runCatching {
        db.withTransaction {
            db.transactionDao().deleteAll()
            db.accountDao().deleteAll()
            db.categoryDao().deleteAll()
            db.creditCardDao().deleteAll()
            db.creditLimitGroupDao().deleteAll()
            db.creditCardStatementDao().deleteAll()
            db.recurringTransactionDao().deleteAll()
        }
    }

    private fun validate(t: TransactionEntity) {
        val type = TransactionType.valueOf(t.transactionType)
        require(t.amount > 0) { "จำนวนเงินต้องมากกว่า 0" }

        val hasSourceAccount = t.sourceAccountId != null
        val hasSourceCard = t.sourceCreditCardId != null
        val hasDestAccount = t.destinationAccountId != null
        val hasDestCard = t.destinationCreditCardId != null

        require(!(hasSourceAccount && hasSourceCard)) { "เลือกบัญชีต้นทางได้เพียงอย่างเดียว (บัญชีหรือบัตร)" }
        require(!(hasDestAccount && hasDestCard)) { "เลือกบัญชีปลายทางได้เพียงอย่างเดียว (บัญชีหรือบัตร)" }

        when (type) {
            TransactionType.EXPENSE -> {
                require(hasSourceAccount || hasSourceCard) { "ต้องเลือกบัญชีหรือบัตรที่ใช้จ่าย" }
                require(!hasDestAccount && !hasDestCard) { "รายจ่ายไม่ต้องมีบัญชีปลายทาง" }
            }
            TransactionType.INCOME -> {
                require(hasDestAccount) { "ต้องเลือกบัญชีที่รับเงิน" }
                require(!hasSourceAccount && !hasSourceCard) { "รายรับไม่ต้องมีบัญชีต้นทาง" }
                require(!hasDestCard) { "รายรับไม่สามารถเข้าบัตรเครดิตได้" }
            }
            TransactionType.TRANSFER -> {
                require(hasSourceAccount) { "ต้องเลือกบัญชีต้นทาง" }
                require(hasDestAccount) { "ต้องเลือกบัญชีปลายทาง" }
                require(t.sourceAccountId != t.destinationAccountId) { "บัญชีต้นทางและปลายทางต้องไม่ใช่บัญชีเดียวกัน" }
            }
            TransactionType.CASH_WITHDRAWAL -> {
                require(hasSourceAccount) { "ต้องเลือกบัญชีธนาคารต้นทาง" }
                require(hasDestAccount) { "ต้องเลือกบัญชีเงินสดปลายทาง" }
            }
            TransactionType.CREDIT_CARD_PAYMENT -> {
                require(hasSourceAccount) { "ต้องเลือกบัญชีที่ใช้ชำระ" }
                require(hasDestCard) { "ต้องเลือกบัตรเครดิตที่ต้องการชำระ" }
            }
        }
    }
}
