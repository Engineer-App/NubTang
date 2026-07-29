package com.pft.tracker.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.pft.tracker.data.local.entity.AccountEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AccountDao_Impl implements AccountDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AccountEntity> __insertionAdapterOfAccountEntity;

  private final EntityDeletionOrUpdateAdapter<AccountEntity> __deletionAdapterOfAccountEntity;

  private final EntityDeletionOrUpdateAdapter<AccountEntity> __updateAdapterOfAccountEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public AccountDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAccountEntity = new EntityInsertionAdapter<AccountEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `accounts` (`id`,`name`,`accountType`,`bankName`,`accountNumberLast4`,`openingBalance`,`isActive`,`isOwnedBySelf`,`creditLimitGroupId`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AccountEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getAccountType());
        if (entity.getBankName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBankName());
        }
        if (entity.getAccountNumberLast4() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAccountNumberLast4());
        }
        statement.bindDouble(6, entity.getOpeningBalance());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isOwnedBySelf() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getCreditLimitGroupId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getCreditLimitGroupId());
        }
        statement.bindLong(10, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfAccountEntity = new EntityDeletionOrUpdateAdapter<AccountEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `accounts` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AccountEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfAccountEntity = new EntityDeletionOrUpdateAdapter<AccountEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `accounts` SET `id` = ?,`name` = ?,`accountType` = ?,`bankName` = ?,`accountNumberLast4` = ?,`openingBalance` = ?,`isActive` = ?,`isOwnedBySelf` = ?,`creditLimitGroupId` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AccountEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getAccountType());
        if (entity.getBankName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBankName());
        }
        if (entity.getAccountNumberLast4() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAccountNumberLast4());
        }
        statement.bindDouble(6, entity.getOpeningBalance());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isOwnedBySelf() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getCreditLimitGroupId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getCreditLimitGroupId());
        }
        statement.bindLong(10, entity.getCreatedAt());
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM accounts";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final AccountEntity account, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAccountEntity.insertAndReturnId(account);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<AccountEntity> accounts,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAccountEntity.insert(accounts);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final AccountEntity account, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAccountEntity.handle(account);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final AccountEntity account, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAccountEntity.handle(account);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AccountEntity>> observeAll() {
    final String _sql = "SELECT * FROM accounts ORDER BY isActive DESC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"accounts"}, new Callable<List<AccountEntity>>() {
      @Override
      @NonNull
      public List<AccountEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAccountType = CursorUtil.getColumnIndexOrThrow(_cursor, "accountType");
          final int _cursorIndexOfBankName = CursorUtil.getColumnIndexOrThrow(_cursor, "bankName");
          final int _cursorIndexOfAccountNumberLast4 = CursorUtil.getColumnIndexOrThrow(_cursor, "accountNumberLast4");
          final int _cursorIndexOfOpeningBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "openingBalance");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfIsOwnedBySelf = CursorUtil.getColumnIndexOrThrow(_cursor, "isOwnedBySelf");
          final int _cursorIndexOfCreditLimitGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimitGroupId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<AccountEntity> _result = new ArrayList<AccountEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AccountEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAccountType;
            _tmpAccountType = _cursor.getString(_cursorIndexOfAccountType);
            final String _tmpBankName;
            if (_cursor.isNull(_cursorIndexOfBankName)) {
              _tmpBankName = null;
            } else {
              _tmpBankName = _cursor.getString(_cursorIndexOfBankName);
            }
            final String _tmpAccountNumberLast4;
            if (_cursor.isNull(_cursorIndexOfAccountNumberLast4)) {
              _tmpAccountNumberLast4 = null;
            } else {
              _tmpAccountNumberLast4 = _cursor.getString(_cursorIndexOfAccountNumberLast4);
            }
            final double _tmpOpeningBalance;
            _tmpOpeningBalance = _cursor.getDouble(_cursorIndexOfOpeningBalance);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final boolean _tmpIsOwnedBySelf;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsOwnedBySelf);
            _tmpIsOwnedBySelf = _tmp_1 != 0;
            final Long _tmpCreditLimitGroupId;
            if (_cursor.isNull(_cursorIndexOfCreditLimitGroupId)) {
              _tmpCreditLimitGroupId = null;
            } else {
              _tmpCreditLimitGroupId = _cursor.getLong(_cursorIndexOfCreditLimitGroupId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AccountEntity(_tmpId,_tmpName,_tmpAccountType,_tmpBankName,_tmpAccountNumberLast4,_tmpOpeningBalance,_tmpIsActive,_tmpIsOwnedBySelf,_tmpCreditLimitGroupId,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AccountEntity>> observeActive() {
    final String _sql = "SELECT * FROM accounts WHERE isActive = 1 ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"accounts"}, new Callable<List<AccountEntity>>() {
      @Override
      @NonNull
      public List<AccountEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAccountType = CursorUtil.getColumnIndexOrThrow(_cursor, "accountType");
          final int _cursorIndexOfBankName = CursorUtil.getColumnIndexOrThrow(_cursor, "bankName");
          final int _cursorIndexOfAccountNumberLast4 = CursorUtil.getColumnIndexOrThrow(_cursor, "accountNumberLast4");
          final int _cursorIndexOfOpeningBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "openingBalance");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfIsOwnedBySelf = CursorUtil.getColumnIndexOrThrow(_cursor, "isOwnedBySelf");
          final int _cursorIndexOfCreditLimitGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimitGroupId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<AccountEntity> _result = new ArrayList<AccountEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AccountEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAccountType;
            _tmpAccountType = _cursor.getString(_cursorIndexOfAccountType);
            final String _tmpBankName;
            if (_cursor.isNull(_cursorIndexOfBankName)) {
              _tmpBankName = null;
            } else {
              _tmpBankName = _cursor.getString(_cursorIndexOfBankName);
            }
            final String _tmpAccountNumberLast4;
            if (_cursor.isNull(_cursorIndexOfAccountNumberLast4)) {
              _tmpAccountNumberLast4 = null;
            } else {
              _tmpAccountNumberLast4 = _cursor.getString(_cursorIndexOfAccountNumberLast4);
            }
            final double _tmpOpeningBalance;
            _tmpOpeningBalance = _cursor.getDouble(_cursorIndexOfOpeningBalance);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final boolean _tmpIsOwnedBySelf;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsOwnedBySelf);
            _tmpIsOwnedBySelf = _tmp_1 != 0;
            final Long _tmpCreditLimitGroupId;
            if (_cursor.isNull(_cursorIndexOfCreditLimitGroupId)) {
              _tmpCreditLimitGroupId = null;
            } else {
              _tmpCreditLimitGroupId = _cursor.getLong(_cursorIndexOfCreditLimitGroupId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AccountEntity(_tmpId,_tmpName,_tmpAccountType,_tmpBankName,_tmpAccountNumberLast4,_tmpOpeningBalance,_tmpIsActive,_tmpIsOwnedBySelf,_tmpCreditLimitGroupId,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getById(final long id, final Continuation<? super AccountEntity> $completion) {
    final String _sql = "SELECT * FROM accounts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AccountEntity>() {
      @Override
      @Nullable
      public AccountEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAccountType = CursorUtil.getColumnIndexOrThrow(_cursor, "accountType");
          final int _cursorIndexOfBankName = CursorUtil.getColumnIndexOrThrow(_cursor, "bankName");
          final int _cursorIndexOfAccountNumberLast4 = CursorUtil.getColumnIndexOrThrow(_cursor, "accountNumberLast4");
          final int _cursorIndexOfOpeningBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "openingBalance");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfIsOwnedBySelf = CursorUtil.getColumnIndexOrThrow(_cursor, "isOwnedBySelf");
          final int _cursorIndexOfCreditLimitGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimitGroupId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final AccountEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAccountType;
            _tmpAccountType = _cursor.getString(_cursorIndexOfAccountType);
            final String _tmpBankName;
            if (_cursor.isNull(_cursorIndexOfBankName)) {
              _tmpBankName = null;
            } else {
              _tmpBankName = _cursor.getString(_cursorIndexOfBankName);
            }
            final String _tmpAccountNumberLast4;
            if (_cursor.isNull(_cursorIndexOfAccountNumberLast4)) {
              _tmpAccountNumberLast4 = null;
            } else {
              _tmpAccountNumberLast4 = _cursor.getString(_cursorIndexOfAccountNumberLast4);
            }
            final double _tmpOpeningBalance;
            _tmpOpeningBalance = _cursor.getDouble(_cursorIndexOfOpeningBalance);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final boolean _tmpIsOwnedBySelf;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsOwnedBySelf);
            _tmpIsOwnedBySelf = _tmp_1 != 0;
            final Long _tmpCreditLimitGroupId;
            if (_cursor.isNull(_cursorIndexOfCreditLimitGroupId)) {
              _tmpCreditLimitGroupId = null;
            } else {
              _tmpCreditLimitGroupId = _cursor.getLong(_cursorIndexOfCreditLimitGroupId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new AccountEntity(_tmpId,_tmpName,_tmpAccountType,_tmpBankName,_tmpAccountNumberLast4,_tmpOpeningBalance,_tmpIsActive,_tmpIsOwnedBySelf,_tmpCreditLimitGroupId,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<AccountEntity> observeById(final long id) {
    final String _sql = "SELECT * FROM accounts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"accounts"}, new Callable<AccountEntity>() {
      @Override
      @Nullable
      public AccountEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAccountType = CursorUtil.getColumnIndexOrThrow(_cursor, "accountType");
          final int _cursorIndexOfBankName = CursorUtil.getColumnIndexOrThrow(_cursor, "bankName");
          final int _cursorIndexOfAccountNumberLast4 = CursorUtil.getColumnIndexOrThrow(_cursor, "accountNumberLast4");
          final int _cursorIndexOfOpeningBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "openingBalance");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfIsOwnedBySelf = CursorUtil.getColumnIndexOrThrow(_cursor, "isOwnedBySelf");
          final int _cursorIndexOfCreditLimitGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimitGroupId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final AccountEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAccountType;
            _tmpAccountType = _cursor.getString(_cursorIndexOfAccountType);
            final String _tmpBankName;
            if (_cursor.isNull(_cursorIndexOfBankName)) {
              _tmpBankName = null;
            } else {
              _tmpBankName = _cursor.getString(_cursorIndexOfBankName);
            }
            final String _tmpAccountNumberLast4;
            if (_cursor.isNull(_cursorIndexOfAccountNumberLast4)) {
              _tmpAccountNumberLast4 = null;
            } else {
              _tmpAccountNumberLast4 = _cursor.getString(_cursorIndexOfAccountNumberLast4);
            }
            final double _tmpOpeningBalance;
            _tmpOpeningBalance = _cursor.getDouble(_cursorIndexOfOpeningBalance);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final boolean _tmpIsOwnedBySelf;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsOwnedBySelf);
            _tmpIsOwnedBySelf = _tmp_1 != 0;
            final Long _tmpCreditLimitGroupId;
            if (_cursor.isNull(_cursorIndexOfCreditLimitGroupId)) {
              _tmpCreditLimitGroupId = null;
            } else {
              _tmpCreditLimitGroupId = _cursor.getLong(_cursorIndexOfCreditLimitGroupId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new AccountEntity(_tmpId,_tmpName,_tmpAccountType,_tmpBankName,_tmpAccountNumberLast4,_tmpOpeningBalance,_tmpIsActive,_tmpIsOwnedBySelf,_tmpCreditLimitGroupId,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<AccountBalanceRow> observeBalance(final long accountId) {
    final String _sql = "\n"
            + "        SELECT ? AS id, (\n"
            + "            (SELECT openingBalance FROM accounts WHERE id = ?)\n"
            + "            + COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'INCOME' AND destinationAccountId = ?), 0.0)\n"
            + "            + COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType IN ('TRANSFER', 'CASH_WITHDRAWAL') AND destinationAccountId = ?), 0.0)\n"
            + "            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'EXPENSE' AND sourceAccountId = ?), 0.0)\n"
            + "            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType IN ('TRANSFER', 'CASH_WITHDRAWAL', 'CREDIT_CARD_PAYMENT') AND sourceAccountId = ?), 0.0)\n"
            + "        ) AS currentBalance\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 6);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, accountId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, accountId);
    _argIndex = 3;
    _statement.bindLong(_argIndex, accountId);
    _argIndex = 4;
    _statement.bindLong(_argIndex, accountId);
    _argIndex = 5;
    _statement.bindLong(_argIndex, accountId);
    _argIndex = 6;
    _statement.bindLong(_argIndex, accountId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"accounts",
        "transactions"}, new Callable<AccountBalanceRow>() {
      @Override
      @Nullable
      public AccountBalanceRow call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfCurrentBalance = 1;
          final AccountBalanceRow _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpCurrentBalance;
            _tmpCurrentBalance = _cursor.getDouble(_cursorIndexOfCurrentBalance);
            _result = new AccountBalanceRow(_tmpId,_tmpCurrentBalance);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AccountBalanceRow>> observeAllBalances() {
    final String _sql = "\n"
            + "        SELECT a.id AS id, (\n"
            + "            a.openingBalance\n"
            + "            + COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'INCOME' AND destinationAccountId = a.id), 0.0)\n"
            + "            + COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType IN ('TRANSFER', 'CASH_WITHDRAWAL') AND destinationAccountId = a.id), 0.0)\n"
            + "            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'EXPENSE' AND sourceAccountId = a.id), 0.0)\n"
            + "            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType IN ('TRANSFER', 'CASH_WITHDRAWAL', 'CREDIT_CARD_PAYMENT') AND sourceAccountId = a.id), 0.0)\n"
            + "        ) AS currentBalance\n"
            + "        FROM accounts a\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions",
        "accounts"}, new Callable<List<AccountBalanceRow>>() {
      @Override
      @NonNull
      public List<AccountBalanceRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfCurrentBalance = 1;
          final List<AccountBalanceRow> _result = new ArrayList<AccountBalanceRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AccountBalanceRow _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpCurrentBalance;
            _tmpCurrentBalance = _cursor.getDouble(_cursorIndexOfCurrentBalance);
            _item = new AccountBalanceRow(_tmpId,_tmpCurrentBalance);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
