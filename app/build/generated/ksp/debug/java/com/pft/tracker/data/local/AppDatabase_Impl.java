package com.pft.tracker.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.pft.tracker.data.local.dao.AccountDao;
import com.pft.tracker.data.local.dao.AccountDao_Impl;
import com.pft.tracker.data.local.dao.CategoryDao;
import com.pft.tracker.data.local.dao.CategoryDao_Impl;
import com.pft.tracker.data.local.dao.CreditCardDao;
import com.pft.tracker.data.local.dao.CreditCardDao_Impl;
import com.pft.tracker.data.local.dao.CreditCardStatementDao;
import com.pft.tracker.data.local.dao.CreditCardStatementDao_Impl;
import com.pft.tracker.data.local.dao.CreditLimitGroupDao;
import com.pft.tracker.data.local.dao.CreditLimitGroupDao_Impl;
import com.pft.tracker.data.local.dao.RecurringTransactionDao;
import com.pft.tracker.data.local.dao.RecurringTransactionDao_Impl;
import com.pft.tracker.data.local.dao.TransactionDao;
import com.pft.tracker.data.local.dao.TransactionDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AccountDao _accountDao;

  private volatile CreditLimitGroupDao _creditLimitGroupDao;

  private volatile CreditCardDao _creditCardDao;

  private volatile CategoryDao _categoryDao;

  private volatile TransactionDao _transactionDao;

  private volatile CreditCardStatementDao _creditCardStatementDao;

  private volatile RecurringTransactionDao _recurringTransactionDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `accounts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `accountType` TEXT NOT NULL, `bankName` TEXT, `accountNumberLast4` TEXT, `openingBalance` REAL NOT NULL, `isActive` INTEGER NOT NULL, `isOwnedBySelf` INTEGER NOT NULL, `creditLimitGroupId` INTEGER, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `credit_limit_groups` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `sharedLimit` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `credit_cards` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `issuer` TEXT NOT NULL, `cardNumberLast4` TEXT NOT NULL, `creditLimit` REAL NOT NULL, `creditLimitGroupId` INTEGER, `billingFrequencyMonths` INTEGER NOT NULL, `statementDay` INTEGER NOT NULL, `paymentDueDay` INTEGER NOT NULL, `startMonth` INTEGER, `isActive` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `categoryType` TEXT NOT NULL, `parentCategoryId` INTEGER, `icon` TEXT, `monthlyBudget` REAL, `displayOrder` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `transactionDate` INTEGER NOT NULL, `transactionType` TEXT NOT NULL, `title` TEXT NOT NULL, `categoryId` INTEGER, `amount` REAL NOT NULL, `sourceAccountId` INTEGER, `sourceCreditCardId` INTEGER, `destinationAccountId` INTEGER, `destinationCreditCardId` INTEGER, `note` TEXT, `receiptPath` TEXT, `isRecurringGenerated` INTEGER NOT NULL, `recurringPlanId` INTEGER, `creditCardStatementId` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `credit_card_statements` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `creditCardId` INTEGER NOT NULL, `periodStart` INTEGER NOT NULL, `periodEnd` INTEGER NOT NULL, `statementDate` INTEGER NOT NULL, `dueDate` INTEGER NOT NULL, `statementAmount` REAL NOT NULL, `paidAmount` REAL NOT NULL, `status` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recurring_transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `amount` REAL NOT NULL, `categoryId` INTEGER, `sourceAccountId` INTEGER, `sourceCreditCardId` INTEGER, `startDate` INTEGER NOT NULL, `frequency` TEXT NOT NULL, `totalInstallments` INTEGER, `installmentsGenerated` INTEGER NOT NULL, `nextRunDate` INTEGER NOT NULL, `endDate` INTEGER, `note` TEXT, `isActive` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '637c680c170896c3fc1105b0ece1927d')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `accounts`");
        db.execSQL("DROP TABLE IF EXISTS `credit_limit_groups`");
        db.execSQL("DROP TABLE IF EXISTS `credit_cards`");
        db.execSQL("DROP TABLE IF EXISTS `categories`");
        db.execSQL("DROP TABLE IF EXISTS `transactions`");
        db.execSQL("DROP TABLE IF EXISTS `credit_card_statements`");
        db.execSQL("DROP TABLE IF EXISTS `recurring_transactions`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAccounts = new HashMap<String, TableInfo.Column>(10);
        _columnsAccounts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("accountType", new TableInfo.Column("accountType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("bankName", new TableInfo.Column("bankName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("accountNumberLast4", new TableInfo.Column("accountNumberLast4", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("openingBalance", new TableInfo.Column("openingBalance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("isOwnedBySelf", new TableInfo.Column("isOwnedBySelf", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("creditLimitGroupId", new TableInfo.Column("creditLimitGroupId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccounts.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccounts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccounts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccounts = new TableInfo("accounts", _columnsAccounts, _foreignKeysAccounts, _indicesAccounts);
        final TableInfo _existingAccounts = TableInfo.read(db, "accounts");
        if (!_infoAccounts.equals(_existingAccounts)) {
          return new RoomOpenHelper.ValidationResult(false, "accounts(com.pft.tracker.data.local.entity.AccountEntity).\n"
                  + " Expected:\n" + _infoAccounts + "\n"
                  + " Found:\n" + _existingAccounts);
        }
        final HashMap<String, TableInfo.Column> _columnsCreditLimitGroups = new HashMap<String, TableInfo.Column>(3);
        _columnsCreditLimitGroups.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditLimitGroups.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditLimitGroups.put("sharedLimit", new TableInfo.Column("sharedLimit", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCreditLimitGroups = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCreditLimitGroups = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCreditLimitGroups = new TableInfo("credit_limit_groups", _columnsCreditLimitGroups, _foreignKeysCreditLimitGroups, _indicesCreditLimitGroups);
        final TableInfo _existingCreditLimitGroups = TableInfo.read(db, "credit_limit_groups");
        if (!_infoCreditLimitGroups.equals(_existingCreditLimitGroups)) {
          return new RoomOpenHelper.ValidationResult(false, "credit_limit_groups(com.pft.tracker.data.local.entity.CreditLimitGroupEntity).\n"
                  + " Expected:\n" + _infoCreditLimitGroups + "\n"
                  + " Found:\n" + _existingCreditLimitGroups);
        }
        final HashMap<String, TableInfo.Column> _columnsCreditCards = new HashMap<String, TableInfo.Column>(11);
        _columnsCreditCards.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCards.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCards.put("issuer", new TableInfo.Column("issuer", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCards.put("cardNumberLast4", new TableInfo.Column("cardNumberLast4", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCards.put("creditLimit", new TableInfo.Column("creditLimit", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCards.put("creditLimitGroupId", new TableInfo.Column("creditLimitGroupId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCards.put("billingFrequencyMonths", new TableInfo.Column("billingFrequencyMonths", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCards.put("statementDay", new TableInfo.Column("statementDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCards.put("paymentDueDay", new TableInfo.Column("paymentDueDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCards.put("startMonth", new TableInfo.Column("startMonth", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCards.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCreditCards = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCreditCards = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCreditCards = new TableInfo("credit_cards", _columnsCreditCards, _foreignKeysCreditCards, _indicesCreditCards);
        final TableInfo _existingCreditCards = TableInfo.read(db, "credit_cards");
        if (!_infoCreditCards.equals(_existingCreditCards)) {
          return new RoomOpenHelper.ValidationResult(false, "credit_cards(com.pft.tracker.data.local.entity.CreditCardEntity).\n"
                  + " Expected:\n" + _infoCreditCards + "\n"
                  + " Found:\n" + _existingCreditCards);
        }
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(7);
        _columnsCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("categoryType", new TableInfo.Column("categoryType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("parentCategoryId", new TableInfo.Column("parentCategoryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("icon", new TableInfo.Column("icon", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("monthlyBudget", new TableInfo.Column("monthlyBudget", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("displayOrder", new TableInfo.Column("displayOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.pft.tracker.data.local.entity.CategoryEntity).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsTransactions = new HashMap<String, TableInfo.Column>(17);
        _columnsTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("transactionDate", new TableInfo.Column("transactionDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("transactionType", new TableInfo.Column("transactionType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("sourceAccountId", new TableInfo.Column("sourceAccountId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("sourceCreditCardId", new TableInfo.Column("sourceCreditCardId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("destinationAccountId", new TableInfo.Column("destinationAccountId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("destinationCreditCardId", new TableInfo.Column("destinationCreditCardId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("receiptPath", new TableInfo.Column("receiptPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("isRecurringGenerated", new TableInfo.Column("isRecurringGenerated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("recurringPlanId", new TableInfo.Column("recurringPlanId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("creditCardStatementId", new TableInfo.Column("creditCardStatementId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransactions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTransactions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTransactions = new TableInfo("transactions", _columnsTransactions, _foreignKeysTransactions, _indicesTransactions);
        final TableInfo _existingTransactions = TableInfo.read(db, "transactions");
        if (!_infoTransactions.equals(_existingTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "transactions(com.pft.tracker.data.local.entity.TransactionEntity).\n"
                  + " Expected:\n" + _infoTransactions + "\n"
                  + " Found:\n" + _existingTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsCreditCardStatements = new HashMap<String, TableInfo.Column>(9);
        _columnsCreditCardStatements.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCardStatements.put("creditCardId", new TableInfo.Column("creditCardId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCardStatements.put("periodStart", new TableInfo.Column("periodStart", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCardStatements.put("periodEnd", new TableInfo.Column("periodEnd", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCardStatements.put("statementDate", new TableInfo.Column("statementDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCardStatements.put("dueDate", new TableInfo.Column("dueDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCardStatements.put("statementAmount", new TableInfo.Column("statementAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCardStatements.put("paidAmount", new TableInfo.Column("paidAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCreditCardStatements.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCreditCardStatements = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCreditCardStatements = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCreditCardStatements = new TableInfo("credit_card_statements", _columnsCreditCardStatements, _foreignKeysCreditCardStatements, _indicesCreditCardStatements);
        final TableInfo _existingCreditCardStatements = TableInfo.read(db, "credit_card_statements");
        if (!_infoCreditCardStatements.equals(_existingCreditCardStatements)) {
          return new RoomOpenHelper.ValidationResult(false, "credit_card_statements(com.pft.tracker.data.local.entity.CreditCardStatementEntity).\n"
                  + " Expected:\n" + _infoCreditCardStatements + "\n"
                  + " Found:\n" + _existingCreditCardStatements);
        }
        final HashMap<String, TableInfo.Column> _columnsRecurringTransactions = new HashMap<String, TableInfo.Column>(14);
        _columnsRecurringTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("sourceAccountId", new TableInfo.Column("sourceAccountId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("sourceCreditCardId", new TableInfo.Column("sourceCreditCardId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("frequency", new TableInfo.Column("frequency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("totalInstallments", new TableInfo.Column("totalInstallments", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("installmentsGenerated", new TableInfo.Column("installmentsGenerated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("nextRunDate", new TableInfo.Column("nextRunDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("endDate", new TableInfo.Column("endDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecurringTransactions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRecurringTransactions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRecurringTransactions = new TableInfo("recurring_transactions", _columnsRecurringTransactions, _foreignKeysRecurringTransactions, _indicesRecurringTransactions);
        final TableInfo _existingRecurringTransactions = TableInfo.read(db, "recurring_transactions");
        if (!_infoRecurringTransactions.equals(_existingRecurringTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "recurring_transactions(com.pft.tracker.data.local.entity.RecurringTransactionEntity).\n"
                  + " Expected:\n" + _infoRecurringTransactions + "\n"
                  + " Found:\n" + _existingRecurringTransactions);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "637c680c170896c3fc1105b0ece1927d", "381d057d3d11646227a09dc335629f7e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "accounts","credit_limit_groups","credit_cards","categories","transactions","credit_card_statements","recurring_transactions");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `accounts`");
      _db.execSQL("DELETE FROM `credit_limit_groups`");
      _db.execSQL("DELETE FROM `credit_cards`");
      _db.execSQL("DELETE FROM `categories`");
      _db.execSQL("DELETE FROM `transactions`");
      _db.execSQL("DELETE FROM `credit_card_statements`");
      _db.execSQL("DELETE FROM `recurring_transactions`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AccountDao.class, AccountDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CreditLimitGroupDao.class, CreditLimitGroupDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CreditCardDao.class, CreditCardDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TransactionDao.class, TransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CreditCardStatementDao.class, CreditCardStatementDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecurringTransactionDao.class, RecurringTransactionDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AccountDao accountDao() {
    if (_accountDao != null) {
      return _accountDao;
    } else {
      synchronized(this) {
        if(_accountDao == null) {
          _accountDao = new AccountDao_Impl(this);
        }
        return _accountDao;
      }
    }
  }

  @Override
  public CreditLimitGroupDao creditLimitGroupDao() {
    if (_creditLimitGroupDao != null) {
      return _creditLimitGroupDao;
    } else {
      synchronized(this) {
        if(_creditLimitGroupDao == null) {
          _creditLimitGroupDao = new CreditLimitGroupDao_Impl(this);
        }
        return _creditLimitGroupDao;
      }
    }
  }

  @Override
  public CreditCardDao creditCardDao() {
    if (_creditCardDao != null) {
      return _creditCardDao;
    } else {
      synchronized(this) {
        if(_creditCardDao == null) {
          _creditCardDao = new CreditCardDao_Impl(this);
        }
        return _creditCardDao;
      }
    }
  }

  @Override
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }

  @Override
  public TransactionDao transactionDao() {
    if (_transactionDao != null) {
      return _transactionDao;
    } else {
      synchronized(this) {
        if(_transactionDao == null) {
          _transactionDao = new TransactionDao_Impl(this);
        }
        return _transactionDao;
      }
    }
  }

  @Override
  public CreditCardStatementDao creditCardStatementDao() {
    if (_creditCardStatementDao != null) {
      return _creditCardStatementDao;
    } else {
      synchronized(this) {
        if(_creditCardStatementDao == null) {
          _creditCardStatementDao = new CreditCardStatementDao_Impl(this);
        }
        return _creditCardStatementDao;
      }
    }
  }

  @Override
  public RecurringTransactionDao recurringTransactionDao() {
    if (_recurringTransactionDao != null) {
      return _recurringTransactionDao;
    } else {
      synchronized(this) {
        if(_recurringTransactionDao == null) {
          _recurringTransactionDao = new RecurringTransactionDao_Impl(this);
        }
        return _recurringTransactionDao;
      }
    }
  }
}
