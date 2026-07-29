package com.pft.tracker.backup

import androidx.room.withTransaction
import com.pft.tracker.data.local.entity.AccountEntity
import com.pft.tracker.data.local.entity.CategoryEntity
import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.data.local.entity.CreditLimitGroupEntity
import com.pft.tracker.data.local.entity.RecurringTransactionEntity
import com.pft.tracker.data.local.entity.TransactionEntity
import com.pft.tracker.di.AppContainer
import com.pft.tracker.security.CryptoManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class BackupPayload(
    val version: Int = 1,
    val accounts: List<AccountEntity>,
    val creditLimitGroups: List<CreditLimitGroupEntity>,
    val creditCards: List<CreditCardEntity>,
    val categories: List<CategoryEntity>,
    val transactions: List<TransactionEntity>,
    val recurringTransactions: List<RecurringTransactionEntity>
)

/**
 * Exports/imports the whole database as an AES-256-GCM-encrypted JSON blob
 * (doc §7: "เข้ารหัสไฟล์สำรอง"). The key lives in the Android Keystore, so a
 * backup can only be restored on the same device/app install that made it —
 * this covers "undo a mistake" / "reinstall the app" use cases for v1.
 */
class BackupManager(private val container: AppContainer) {
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = false }

    suspend fun exportEncrypted(): ByteArray {
        val payload = BackupPayload(
            accounts = container.accountRepository.getAllOnce(),
            creditLimitGroups = container.creditLimitGroupRepository.getAllOnce(),
            creditCards = container.creditCardRepository.getAllOnce(),
            categories = container.categoryRepository.getAllOnce(),
            transactions = container.transactionRepository.getAllOnce(),
            recurringTransactions = container.recurringTransactionRepository.getAllOnce()
        )
        val jsonBytes = json.encodeToString(BackupPayload.serializer(), payload).toByteArray(Charsets.UTF_8)
        return CryptoManager.encrypt(jsonBytes)
    }

    suspend fun importEncrypted(encryptedBytes: ByteArray) {
        val jsonBytes = CryptoManager.decrypt(encryptedBytes)
        val payload = json.decodeFromString(BackupPayload.serializer(), String(jsonBytes, Charsets.UTF_8))

        container.database.withTransaction {
            container.transactionRepository.deleteAll()
            container.recurringTransactionRepository.deleteAll()
            container.creditCardRepository.deleteAll()
            container.creditLimitGroupRepository.deleteAll()
            container.categoryRepository.deleteAll()
            container.accountRepository.deleteAll()

            container.accountRepository.insertAll(payload.accounts)
            container.creditLimitGroupRepository.insertAll(payload.creditLimitGroups)
            container.creditCardRepository.insertAll(payload.creditCards)
            container.categoryRepository.insertAll(payload.categories)
            container.transactionRepository.insertAll(payload.transactions)
            container.recurringTransactionRepository.insertAll(payload.recurringTransactions)
        }
    }
}
