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
import com.pft.tracker.data.local.entity.TransactionEntity;
import java.lang.Class;
import java.lang.Double;
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
public final class TransactionDao_Impl implements TransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TransactionEntity> __insertionAdapterOfTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<TransactionEntity> __deletionAdapterOfTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<TransactionEntity> __updateAdapterOfTransactionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteInRange;

  public TransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransactionEntity = new EntityInsertionAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `transactions` (`id`,`transactionDate`,`transactionType`,`title`,`categoryId`,`amount`,`sourceAccountId`,`sourceCreditCardId`,`destinationAccountId`,`destinationCreditCardId`,`note`,`receiptPath`,`isRecurringGenerated`,`recurringPlanId`,`creditCardStatementId`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTransactionDate());
        statement.bindString(3, entity.getTransactionType());
        statement.bindString(4, entity.getTitle());
        if (entity.getCategoryId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getCategoryId());
        }
        statement.bindDouble(6, entity.getAmount());
        if (entity.getSourceAccountId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getSourceAccountId());
        }
        if (entity.getSourceCreditCardId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getSourceCreditCardId());
        }
        if (entity.getDestinationAccountId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getDestinationAccountId());
        }
        if (entity.getDestinationCreditCardId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getDestinationCreditCardId());
        }
        if (entity.getNote() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getNote());
        }
        if (entity.getReceiptPath() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getReceiptPath());
        }
        final int _tmp = entity.isRecurringGenerated() ? 1 : 0;
        statement.bindLong(13, _tmp);
        if (entity.getRecurringPlanId() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getRecurringPlanId());
        }
        if (entity.getCreditCardStatementId() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getCreditCardStatementId());
        }
        statement.bindLong(16, entity.getCreatedAt());
        statement.bindLong(17, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfTransactionEntity = new EntityDeletionOrUpdateAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTransactionEntity = new EntityDeletionOrUpdateAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `transactions` SET `id` = ?,`transactionDate` = ?,`transactionType` = ?,`title` = ?,`categoryId` = ?,`amount` = ?,`sourceAccountId` = ?,`sourceCreditCardId` = ?,`destinationAccountId` = ?,`destinationCreditCardId` = ?,`note` = ?,`receiptPath` = ?,`isRecurringGenerated` = ?,`recurringPlanId` = ?,`creditCardStatementId` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTransactionDate());
        statement.bindString(3, entity.getTransactionType());
        statement.bindString(4, entity.getTitle());
        if (entity.getCategoryId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getCategoryId());
        }
        statement.bindDouble(6, entity.getAmount());
        if (entity.getSourceAccountId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getSourceAccountId());
        }
        if (entity.getSourceCreditCardId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getSourceCreditCardId());
        }
        if (entity.getDestinationAccountId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getDestinationAccountId());
        }
        if (entity.getDestinationCreditCardId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getDestinationCreditCardId());
        }
        if (entity.getNote() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getNote());
        }
        if (entity.getReceiptPath() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getReceiptPath());
        }
        final int _tmp = entity.isRecurringGenerated() ? 1 : 0;
        statement.bindLong(13, _tmp);
        if (entity.getRecurringPlanId() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getRecurringPlanId());
        }
        if (entity.getCreditCardStatementId() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getCreditCardStatementId());
        }
        statement.bindLong(16, entity.getCreatedAt());
        statement.bindLong(17, entity.getUpdatedAt());
        statement.bindLong(18, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transactions";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteInRange = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transactions WHERE transactionDate BETWEEN ? AND ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final TransactionEntity transaction,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTransactionEntity.insertAndReturnId(transaction);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<TransactionEntity> transactions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTransactionEntity.insert(transactions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final TransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTransactionEntity.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final TransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTransactionEntity.handle(transaction);
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
  public Object deleteInRange(final long start, final long end,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteInRange.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, start);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, end);
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
          __preparedStmtOfDeleteInRange.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super TransactionEntity> $completion) {
    final String _sql = "SELECT * FROM transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TransactionEntity>() {
      @Override
      @Nullable
      public TransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
          final int _cursorIndexOfTransactionType = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfSourceAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAccountId");
          final int _cursorIndexOfSourceCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceCreditCardId");
          final int _cursorIndexOfDestinationAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationAccountId");
          final int _cursorIndexOfDestinationCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationCreditCardId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfReceiptPath = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptPath");
          final int _cursorIndexOfIsRecurringGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurringGenerated");
          final int _cursorIndexOfRecurringPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPlanId");
          final int _cursorIndexOfCreditCardStatementId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardStatementId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final TransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpTransactionType;
            _tmpTransactionType = _cursor.getString(_cursorIndexOfTransactionType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final Long _tmpSourceAccountId;
            if (_cursor.isNull(_cursorIndexOfSourceAccountId)) {
              _tmpSourceAccountId = null;
            } else {
              _tmpSourceAccountId = _cursor.getLong(_cursorIndexOfSourceAccountId);
            }
            final Long _tmpSourceCreditCardId;
            if (_cursor.isNull(_cursorIndexOfSourceCreditCardId)) {
              _tmpSourceCreditCardId = null;
            } else {
              _tmpSourceCreditCardId = _cursor.getLong(_cursorIndexOfSourceCreditCardId);
            }
            final Long _tmpDestinationAccountId;
            if (_cursor.isNull(_cursorIndexOfDestinationAccountId)) {
              _tmpDestinationAccountId = null;
            } else {
              _tmpDestinationAccountId = _cursor.getLong(_cursorIndexOfDestinationAccountId);
            }
            final Long _tmpDestinationCreditCardId;
            if (_cursor.isNull(_cursorIndexOfDestinationCreditCardId)) {
              _tmpDestinationCreditCardId = null;
            } else {
              _tmpDestinationCreditCardId = _cursor.getLong(_cursorIndexOfDestinationCreditCardId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpReceiptPath;
            if (_cursor.isNull(_cursorIndexOfReceiptPath)) {
              _tmpReceiptPath = null;
            } else {
              _tmpReceiptPath = _cursor.getString(_cursorIndexOfReceiptPath);
            }
            final boolean _tmpIsRecurringGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRecurringGenerated);
            _tmpIsRecurringGenerated = _tmp != 0;
            final Long _tmpRecurringPlanId;
            if (_cursor.isNull(_cursorIndexOfRecurringPlanId)) {
              _tmpRecurringPlanId = null;
            } else {
              _tmpRecurringPlanId = _cursor.getLong(_cursorIndexOfRecurringPlanId);
            }
            final Long _tmpCreditCardStatementId;
            if (_cursor.isNull(_cursorIndexOfCreditCardStatementId)) {
              _tmpCreditCardStatementId = null;
            } else {
              _tmpCreditCardStatementId = _cursor.getLong(_cursorIndexOfCreditCardStatementId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new TransactionEntity(_tmpId,_tmpTransactionDate,_tmpTransactionType,_tmpTitle,_tmpCategoryId,_tmpAmount,_tmpSourceAccountId,_tmpSourceCreditCardId,_tmpDestinationAccountId,_tmpDestinationCreditCardId,_tmpNote,_tmpReceiptPath,_tmpIsRecurringGenerated,_tmpRecurringPlanId,_tmpCreditCardStatementId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<TransactionEntity> observeById(final long id) {
    final String _sql = "SELECT * FROM transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<TransactionEntity>() {
      @Override
      @Nullable
      public TransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
          final int _cursorIndexOfTransactionType = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfSourceAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAccountId");
          final int _cursorIndexOfSourceCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceCreditCardId");
          final int _cursorIndexOfDestinationAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationAccountId");
          final int _cursorIndexOfDestinationCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationCreditCardId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfReceiptPath = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptPath");
          final int _cursorIndexOfIsRecurringGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurringGenerated");
          final int _cursorIndexOfRecurringPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPlanId");
          final int _cursorIndexOfCreditCardStatementId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardStatementId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final TransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpTransactionType;
            _tmpTransactionType = _cursor.getString(_cursorIndexOfTransactionType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final Long _tmpSourceAccountId;
            if (_cursor.isNull(_cursorIndexOfSourceAccountId)) {
              _tmpSourceAccountId = null;
            } else {
              _tmpSourceAccountId = _cursor.getLong(_cursorIndexOfSourceAccountId);
            }
            final Long _tmpSourceCreditCardId;
            if (_cursor.isNull(_cursorIndexOfSourceCreditCardId)) {
              _tmpSourceCreditCardId = null;
            } else {
              _tmpSourceCreditCardId = _cursor.getLong(_cursorIndexOfSourceCreditCardId);
            }
            final Long _tmpDestinationAccountId;
            if (_cursor.isNull(_cursorIndexOfDestinationAccountId)) {
              _tmpDestinationAccountId = null;
            } else {
              _tmpDestinationAccountId = _cursor.getLong(_cursorIndexOfDestinationAccountId);
            }
            final Long _tmpDestinationCreditCardId;
            if (_cursor.isNull(_cursorIndexOfDestinationCreditCardId)) {
              _tmpDestinationCreditCardId = null;
            } else {
              _tmpDestinationCreditCardId = _cursor.getLong(_cursorIndexOfDestinationCreditCardId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpReceiptPath;
            if (_cursor.isNull(_cursorIndexOfReceiptPath)) {
              _tmpReceiptPath = null;
            } else {
              _tmpReceiptPath = _cursor.getString(_cursorIndexOfReceiptPath);
            }
            final boolean _tmpIsRecurringGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRecurringGenerated);
            _tmpIsRecurringGenerated = _tmp != 0;
            final Long _tmpRecurringPlanId;
            if (_cursor.isNull(_cursorIndexOfRecurringPlanId)) {
              _tmpRecurringPlanId = null;
            } else {
              _tmpRecurringPlanId = _cursor.getLong(_cursorIndexOfRecurringPlanId);
            }
            final Long _tmpCreditCardStatementId;
            if (_cursor.isNull(_cursorIndexOfCreditCardStatementId)) {
              _tmpCreditCardStatementId = null;
            } else {
              _tmpCreditCardStatementId = _cursor.getLong(_cursorIndexOfCreditCardStatementId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new TransactionEntity(_tmpId,_tmpTransactionDate,_tmpTransactionType,_tmpTitle,_tmpCategoryId,_tmpAmount,_tmpSourceAccountId,_tmpSourceCreditCardId,_tmpDestinationAccountId,_tmpDestinationCreditCardId,_tmpNote,_tmpReceiptPath,_tmpIsRecurringGenerated,_tmpRecurringPlanId,_tmpCreditCardStatementId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getAllOnce(final Continuation<? super List<TransactionEntity>> $completion) {
    final String _sql = "SELECT * FROM transactions ORDER BY transactionDate DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
          final int _cursorIndexOfTransactionType = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfSourceAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAccountId");
          final int _cursorIndexOfSourceCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceCreditCardId");
          final int _cursorIndexOfDestinationAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationAccountId");
          final int _cursorIndexOfDestinationCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationCreditCardId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfReceiptPath = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptPath");
          final int _cursorIndexOfIsRecurringGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurringGenerated");
          final int _cursorIndexOfRecurringPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPlanId");
          final int _cursorIndexOfCreditCardStatementId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardStatementId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpTransactionType;
            _tmpTransactionType = _cursor.getString(_cursorIndexOfTransactionType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final Long _tmpSourceAccountId;
            if (_cursor.isNull(_cursorIndexOfSourceAccountId)) {
              _tmpSourceAccountId = null;
            } else {
              _tmpSourceAccountId = _cursor.getLong(_cursorIndexOfSourceAccountId);
            }
            final Long _tmpSourceCreditCardId;
            if (_cursor.isNull(_cursorIndexOfSourceCreditCardId)) {
              _tmpSourceCreditCardId = null;
            } else {
              _tmpSourceCreditCardId = _cursor.getLong(_cursorIndexOfSourceCreditCardId);
            }
            final Long _tmpDestinationAccountId;
            if (_cursor.isNull(_cursorIndexOfDestinationAccountId)) {
              _tmpDestinationAccountId = null;
            } else {
              _tmpDestinationAccountId = _cursor.getLong(_cursorIndexOfDestinationAccountId);
            }
            final Long _tmpDestinationCreditCardId;
            if (_cursor.isNull(_cursorIndexOfDestinationCreditCardId)) {
              _tmpDestinationCreditCardId = null;
            } else {
              _tmpDestinationCreditCardId = _cursor.getLong(_cursorIndexOfDestinationCreditCardId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpReceiptPath;
            if (_cursor.isNull(_cursorIndexOfReceiptPath)) {
              _tmpReceiptPath = null;
            } else {
              _tmpReceiptPath = _cursor.getString(_cursorIndexOfReceiptPath);
            }
            final boolean _tmpIsRecurringGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRecurringGenerated);
            _tmpIsRecurringGenerated = _tmp != 0;
            final Long _tmpRecurringPlanId;
            if (_cursor.isNull(_cursorIndexOfRecurringPlanId)) {
              _tmpRecurringPlanId = null;
            } else {
              _tmpRecurringPlanId = _cursor.getLong(_cursorIndexOfRecurringPlanId);
            }
            final Long _tmpCreditCardStatementId;
            if (_cursor.isNull(_cursorIndexOfCreditCardStatementId)) {
              _tmpCreditCardStatementId = null;
            } else {
              _tmpCreditCardStatementId = _cursor.getLong(_cursorIndexOfCreditCardStatementId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new TransactionEntity(_tmpId,_tmpTransactionDate,_tmpTransactionType,_tmpTitle,_tmpCategoryId,_tmpAmount,_tmpSourceAccountId,_tmpSourceCreditCardId,_tmpDestinationAccountId,_tmpDestinationCreditCardId,_tmpNote,_tmpReceiptPath,_tmpIsRecurringGenerated,_tmpRecurringPlanId,_tmpCreditCardStatementId,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
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
  public Flow<List<TransactionEntity>> observeFiltered(final long monthStart, final long monthEnd,
      final String type, final Long categoryId, final Long accountId, final Long cardId) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions\n"
            + "        WHERE transactionDate BETWEEN ? AND ?\n"
            + "        AND (? IS NULL OR transactionType = ?)\n"
            + "        AND (? IS NULL OR categoryId = ?)\n"
            + "        AND (? IS NULL OR sourceAccountId = ? OR destinationAccountId = ?)\n"
            + "        AND (? IS NULL OR sourceCreditCardId = ? OR destinationCreditCardId = ?)\n"
            + "        ORDER BY transactionDate DESC, id DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 12);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, monthStart);
    _argIndex = 2;
    _statement.bindLong(_argIndex, monthEnd);
    _argIndex = 3;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    _argIndex = 4;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    _argIndex = 5;
    if (categoryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, categoryId);
    }
    _argIndex = 6;
    if (categoryId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, categoryId);
    }
    _argIndex = 7;
    if (accountId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, accountId);
    }
    _argIndex = 8;
    if (accountId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, accountId);
    }
    _argIndex = 9;
    if (accountId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, accountId);
    }
    _argIndex = 10;
    if (cardId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, cardId);
    }
    _argIndex = 11;
    if (cardId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, cardId);
    }
    _argIndex = 12;
    if (cardId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, cardId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
          final int _cursorIndexOfTransactionType = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfSourceAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAccountId");
          final int _cursorIndexOfSourceCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceCreditCardId");
          final int _cursorIndexOfDestinationAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationAccountId");
          final int _cursorIndexOfDestinationCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationCreditCardId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfReceiptPath = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptPath");
          final int _cursorIndexOfIsRecurringGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurringGenerated");
          final int _cursorIndexOfRecurringPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPlanId");
          final int _cursorIndexOfCreditCardStatementId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardStatementId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpTransactionType;
            _tmpTransactionType = _cursor.getString(_cursorIndexOfTransactionType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final Long _tmpSourceAccountId;
            if (_cursor.isNull(_cursorIndexOfSourceAccountId)) {
              _tmpSourceAccountId = null;
            } else {
              _tmpSourceAccountId = _cursor.getLong(_cursorIndexOfSourceAccountId);
            }
            final Long _tmpSourceCreditCardId;
            if (_cursor.isNull(_cursorIndexOfSourceCreditCardId)) {
              _tmpSourceCreditCardId = null;
            } else {
              _tmpSourceCreditCardId = _cursor.getLong(_cursorIndexOfSourceCreditCardId);
            }
            final Long _tmpDestinationAccountId;
            if (_cursor.isNull(_cursorIndexOfDestinationAccountId)) {
              _tmpDestinationAccountId = null;
            } else {
              _tmpDestinationAccountId = _cursor.getLong(_cursorIndexOfDestinationAccountId);
            }
            final Long _tmpDestinationCreditCardId;
            if (_cursor.isNull(_cursorIndexOfDestinationCreditCardId)) {
              _tmpDestinationCreditCardId = null;
            } else {
              _tmpDestinationCreditCardId = _cursor.getLong(_cursorIndexOfDestinationCreditCardId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpReceiptPath;
            if (_cursor.isNull(_cursorIndexOfReceiptPath)) {
              _tmpReceiptPath = null;
            } else {
              _tmpReceiptPath = _cursor.getString(_cursorIndexOfReceiptPath);
            }
            final boolean _tmpIsRecurringGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRecurringGenerated);
            _tmpIsRecurringGenerated = _tmp != 0;
            final Long _tmpRecurringPlanId;
            if (_cursor.isNull(_cursorIndexOfRecurringPlanId)) {
              _tmpRecurringPlanId = null;
            } else {
              _tmpRecurringPlanId = _cursor.getLong(_cursorIndexOfRecurringPlanId);
            }
            final Long _tmpCreditCardStatementId;
            if (_cursor.isNull(_cursorIndexOfCreditCardStatementId)) {
              _tmpCreditCardStatementId = null;
            } else {
              _tmpCreditCardStatementId = _cursor.getLong(_cursorIndexOfCreditCardStatementId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new TransactionEntity(_tmpId,_tmpTransactionDate,_tmpTransactionType,_tmpTitle,_tmpCategoryId,_tmpAmount,_tmpSourceAccountId,_tmpSourceCreditCardId,_tmpDestinationAccountId,_tmpDestinationCreditCardId,_tmpNote,_tmpReceiptPath,_tmpIsRecurringGenerated,_tmpRecurringPlanId,_tmpCreditCardStatementId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<TransactionEntity>> observeByAccount(final long accountId) {
    final String _sql = "SELECT * FROM transactions WHERE sourceAccountId = ? OR destinationAccountId = ? ORDER BY transactionDate DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, accountId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, accountId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
          final int _cursorIndexOfTransactionType = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfSourceAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAccountId");
          final int _cursorIndexOfSourceCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceCreditCardId");
          final int _cursorIndexOfDestinationAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationAccountId");
          final int _cursorIndexOfDestinationCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationCreditCardId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfReceiptPath = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptPath");
          final int _cursorIndexOfIsRecurringGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurringGenerated");
          final int _cursorIndexOfRecurringPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPlanId");
          final int _cursorIndexOfCreditCardStatementId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardStatementId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpTransactionType;
            _tmpTransactionType = _cursor.getString(_cursorIndexOfTransactionType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final Long _tmpSourceAccountId;
            if (_cursor.isNull(_cursorIndexOfSourceAccountId)) {
              _tmpSourceAccountId = null;
            } else {
              _tmpSourceAccountId = _cursor.getLong(_cursorIndexOfSourceAccountId);
            }
            final Long _tmpSourceCreditCardId;
            if (_cursor.isNull(_cursorIndexOfSourceCreditCardId)) {
              _tmpSourceCreditCardId = null;
            } else {
              _tmpSourceCreditCardId = _cursor.getLong(_cursorIndexOfSourceCreditCardId);
            }
            final Long _tmpDestinationAccountId;
            if (_cursor.isNull(_cursorIndexOfDestinationAccountId)) {
              _tmpDestinationAccountId = null;
            } else {
              _tmpDestinationAccountId = _cursor.getLong(_cursorIndexOfDestinationAccountId);
            }
            final Long _tmpDestinationCreditCardId;
            if (_cursor.isNull(_cursorIndexOfDestinationCreditCardId)) {
              _tmpDestinationCreditCardId = null;
            } else {
              _tmpDestinationCreditCardId = _cursor.getLong(_cursorIndexOfDestinationCreditCardId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpReceiptPath;
            if (_cursor.isNull(_cursorIndexOfReceiptPath)) {
              _tmpReceiptPath = null;
            } else {
              _tmpReceiptPath = _cursor.getString(_cursorIndexOfReceiptPath);
            }
            final boolean _tmpIsRecurringGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRecurringGenerated);
            _tmpIsRecurringGenerated = _tmp != 0;
            final Long _tmpRecurringPlanId;
            if (_cursor.isNull(_cursorIndexOfRecurringPlanId)) {
              _tmpRecurringPlanId = null;
            } else {
              _tmpRecurringPlanId = _cursor.getLong(_cursorIndexOfRecurringPlanId);
            }
            final Long _tmpCreditCardStatementId;
            if (_cursor.isNull(_cursorIndexOfCreditCardStatementId)) {
              _tmpCreditCardStatementId = null;
            } else {
              _tmpCreditCardStatementId = _cursor.getLong(_cursorIndexOfCreditCardStatementId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new TransactionEntity(_tmpId,_tmpTransactionDate,_tmpTransactionType,_tmpTitle,_tmpCategoryId,_tmpAmount,_tmpSourceAccountId,_tmpSourceCreditCardId,_tmpDestinationAccountId,_tmpDestinationCreditCardId,_tmpNote,_tmpReceiptPath,_tmpIsRecurringGenerated,_tmpRecurringPlanId,_tmpCreditCardStatementId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<TransactionEntity>> observeByCard(final long cardId) {
    final String _sql = "SELECT * FROM transactions WHERE sourceCreditCardId = ? OR destinationCreditCardId = ? ORDER BY transactionDate DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cardId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, cardId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
          final int _cursorIndexOfTransactionType = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfSourceAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAccountId");
          final int _cursorIndexOfSourceCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceCreditCardId");
          final int _cursorIndexOfDestinationAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationAccountId");
          final int _cursorIndexOfDestinationCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationCreditCardId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfReceiptPath = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptPath");
          final int _cursorIndexOfIsRecurringGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurringGenerated");
          final int _cursorIndexOfRecurringPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPlanId");
          final int _cursorIndexOfCreditCardStatementId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardStatementId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpTransactionType;
            _tmpTransactionType = _cursor.getString(_cursorIndexOfTransactionType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final Long _tmpSourceAccountId;
            if (_cursor.isNull(_cursorIndexOfSourceAccountId)) {
              _tmpSourceAccountId = null;
            } else {
              _tmpSourceAccountId = _cursor.getLong(_cursorIndexOfSourceAccountId);
            }
            final Long _tmpSourceCreditCardId;
            if (_cursor.isNull(_cursorIndexOfSourceCreditCardId)) {
              _tmpSourceCreditCardId = null;
            } else {
              _tmpSourceCreditCardId = _cursor.getLong(_cursorIndexOfSourceCreditCardId);
            }
            final Long _tmpDestinationAccountId;
            if (_cursor.isNull(_cursorIndexOfDestinationAccountId)) {
              _tmpDestinationAccountId = null;
            } else {
              _tmpDestinationAccountId = _cursor.getLong(_cursorIndexOfDestinationAccountId);
            }
            final Long _tmpDestinationCreditCardId;
            if (_cursor.isNull(_cursorIndexOfDestinationCreditCardId)) {
              _tmpDestinationCreditCardId = null;
            } else {
              _tmpDestinationCreditCardId = _cursor.getLong(_cursorIndexOfDestinationCreditCardId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpReceiptPath;
            if (_cursor.isNull(_cursorIndexOfReceiptPath)) {
              _tmpReceiptPath = null;
            } else {
              _tmpReceiptPath = _cursor.getString(_cursorIndexOfReceiptPath);
            }
            final boolean _tmpIsRecurringGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRecurringGenerated);
            _tmpIsRecurringGenerated = _tmp != 0;
            final Long _tmpRecurringPlanId;
            if (_cursor.isNull(_cursorIndexOfRecurringPlanId)) {
              _tmpRecurringPlanId = null;
            } else {
              _tmpRecurringPlanId = _cursor.getLong(_cursorIndexOfRecurringPlanId);
            }
            final Long _tmpCreditCardStatementId;
            if (_cursor.isNull(_cursorIndexOfCreditCardStatementId)) {
              _tmpCreditCardStatementId = null;
            } else {
              _tmpCreditCardStatementId = _cursor.getLong(_cursorIndexOfCreditCardStatementId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new TransactionEntity(_tmpId,_tmpTransactionDate,_tmpTransactionType,_tmpTitle,_tmpCategoryId,_tmpAmount,_tmpSourceAccountId,_tmpSourceCreditCardId,_tmpDestinationAccountId,_tmpDestinationCreditCardId,_tmpNote,_tmpReceiptPath,_tmpIsRecurringGenerated,_tmpRecurringPlanId,_tmpCreditCardStatementId,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getCardExpensesInPeriod(final long cardId, final long periodStart,
      final long periodEnd, final Continuation<? super List<TransactionEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions\n"
            + "        WHERE transactionType = 'EXPENSE' AND sourceCreditCardId = ?\n"
            + "        AND transactionDate BETWEEN ? AND ?\n"
            + "        ORDER BY transactionDate ASC, id ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cardId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, periodStart);
    _argIndex = 3;
    _statement.bindLong(_argIndex, periodEnd);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTransactionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionDate");
          final int _cursorIndexOfTransactionType = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionType");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfSourceAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAccountId");
          final int _cursorIndexOfSourceCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceCreditCardId");
          final int _cursorIndexOfDestinationAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationAccountId");
          final int _cursorIndexOfDestinationCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationCreditCardId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfReceiptPath = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptPath");
          final int _cursorIndexOfIsRecurringGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurringGenerated");
          final int _cursorIndexOfRecurringPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPlanId");
          final int _cursorIndexOfCreditCardStatementId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardStatementId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTransactionDate;
            _tmpTransactionDate = _cursor.getLong(_cursorIndexOfTransactionDate);
            final String _tmpTransactionType;
            _tmpTransactionType = _cursor.getString(_cursorIndexOfTransactionType);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final Long _tmpSourceAccountId;
            if (_cursor.isNull(_cursorIndexOfSourceAccountId)) {
              _tmpSourceAccountId = null;
            } else {
              _tmpSourceAccountId = _cursor.getLong(_cursorIndexOfSourceAccountId);
            }
            final Long _tmpSourceCreditCardId;
            if (_cursor.isNull(_cursorIndexOfSourceCreditCardId)) {
              _tmpSourceCreditCardId = null;
            } else {
              _tmpSourceCreditCardId = _cursor.getLong(_cursorIndexOfSourceCreditCardId);
            }
            final Long _tmpDestinationAccountId;
            if (_cursor.isNull(_cursorIndexOfDestinationAccountId)) {
              _tmpDestinationAccountId = null;
            } else {
              _tmpDestinationAccountId = _cursor.getLong(_cursorIndexOfDestinationAccountId);
            }
            final Long _tmpDestinationCreditCardId;
            if (_cursor.isNull(_cursorIndexOfDestinationCreditCardId)) {
              _tmpDestinationCreditCardId = null;
            } else {
              _tmpDestinationCreditCardId = _cursor.getLong(_cursorIndexOfDestinationCreditCardId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpReceiptPath;
            if (_cursor.isNull(_cursorIndexOfReceiptPath)) {
              _tmpReceiptPath = null;
            } else {
              _tmpReceiptPath = _cursor.getString(_cursorIndexOfReceiptPath);
            }
            final boolean _tmpIsRecurringGenerated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRecurringGenerated);
            _tmpIsRecurringGenerated = _tmp != 0;
            final Long _tmpRecurringPlanId;
            if (_cursor.isNull(_cursorIndexOfRecurringPlanId)) {
              _tmpRecurringPlanId = null;
            } else {
              _tmpRecurringPlanId = _cursor.getLong(_cursorIndexOfRecurringPlanId);
            }
            final Long _tmpCreditCardStatementId;
            if (_cursor.isNull(_cursorIndexOfCreditCardStatementId)) {
              _tmpCreditCardStatementId = null;
            } else {
              _tmpCreditCardStatementId = _cursor.getLong(_cursorIndexOfCreditCardStatementId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new TransactionEntity(_tmpId,_tmpTransactionDate,_tmpTransactionType,_tmpTitle,_tmpCategoryId,_tmpAmount,_tmpSourceAccountId,_tmpSourceCreditCardId,_tmpDestinationAccountId,_tmpDestinationCreditCardId,_tmpNote,_tmpReceiptPath,_tmpIsRecurringGenerated,_tmpRecurringPlanId,_tmpCreditCardStatementId,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
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
  public Flow<List<ExpenseBySourceRow>> observeExpenseTotalsBySource(final long monthStart,
      final long monthEnd) {
    final String _sql = "\n"
            + "        SELECT sourceAccountId AS accountId, sourceCreditCardId AS cardId, COALESCE(SUM(amount), 0.0) AS total\n"
            + "        FROM transactions\n"
            + "        WHERE transactionType = 'EXPENSE' AND transactionDate BETWEEN ? AND ?\n"
            + "        GROUP BY sourceAccountId, sourceCreditCardId\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, monthStart);
    _argIndex = 2;
    _statement.bindLong(_argIndex, monthEnd);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<ExpenseBySourceRow>>() {
      @Override
      @NonNull
      public List<ExpenseBySourceRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAccountId = 0;
          final int _cursorIndexOfCardId = 1;
          final int _cursorIndexOfTotal = 2;
          final List<ExpenseBySourceRow> _result = new ArrayList<ExpenseBySourceRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExpenseBySourceRow _item;
            final Long _tmpAccountId;
            if (_cursor.isNull(_cursorIndexOfAccountId)) {
              _tmpAccountId = null;
            } else {
              _tmpAccountId = _cursor.getLong(_cursorIndexOfAccountId);
            }
            final Long _tmpCardId;
            if (_cursor.isNull(_cursorIndexOfCardId)) {
              _tmpCardId = null;
            } else {
              _tmpCardId = _cursor.getLong(_cursorIndexOfCardId);
            }
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            _item = new ExpenseBySourceRow(_tmpAccountId,_tmpCardId,_tmpTotal);
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
  public Flow<Double> observeTotalExpense(final long monthStart, final long monthEnd) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(amount), 0.0) FROM transactions\n"
            + "        WHERE transactionType = 'EXPENSE' AND transactionDate BETWEEN ? AND ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, monthStart);
    _argIndex = 2;
    _statement.bindLong(_argIndex, monthEnd);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Flow<Double> observeTotalIncome(final long monthStart, final long monthEnd) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(amount), 0.0) FROM transactions\n"
            + "        WHERE transactionType = 'INCOME' AND transactionDate BETWEEN ? AND ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, monthStart);
    _argIndex = 2;
    _statement.bindLong(_argIndex, monthEnd);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
