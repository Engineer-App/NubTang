package com.pft.tracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pft.tracker.data.local.dao.AccountDao
import com.pft.tracker.data.local.dao.CategoryDao
import com.pft.tracker.data.local.dao.CreditCardDao
import com.pft.tracker.data.local.dao.CreditCardStatementDao
import com.pft.tracker.data.local.dao.CreditLimitGroupDao
import com.pft.tracker.data.local.dao.RecurringTransactionDao
import com.pft.tracker.data.local.dao.TransactionDao
import com.pft.tracker.data.local.entity.AccountEntity
import com.pft.tracker.data.local.entity.CategoryEntity
import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.data.local.entity.CreditCardStatementEntity
import com.pft.tracker.data.local.entity.CreditLimitGroupEntity
import com.pft.tracker.data.local.entity.RecurringTransactionEntity
import com.pft.tracker.data.local.entity.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        AccountEntity::class,
        CreditLimitGroupEntity::class,
        CreditCardEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        CreditCardStatementEntity::class,
        RecurringTransactionEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun creditLimitGroupDao(): CreditLimitGroupDao
    abstract fun creditCardDao(): CreditCardDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun creditCardStatementDao(): CreditCardStatementDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao

    companion object {
        private const val DB_NAME = "pft_tracker.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context, applicationScope: CoroutineScope): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: build(context, applicationScope).also { instance = it }
            }
        }

        private fun build(context: Context, applicationScope: CoroutineScope): AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        applicationScope.launch(Dispatchers.IO) {
                            instance?.let { SeedData.populate(it) }
                        }
                    }
                })
                .build()
        }
    }
}
