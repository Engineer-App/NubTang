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
import com.pft.tracker.data.local.entity.CreditCardStatementEntity;
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
public final class CreditCardStatementDao_Impl implements CreditCardStatementDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CreditCardStatementEntity> __insertionAdapterOfCreditCardStatementEntity;

  private final EntityDeletionOrUpdateAdapter<CreditCardStatementEntity> __deletionAdapterOfCreditCardStatementEntity;

  private final EntityDeletionOrUpdateAdapter<CreditCardStatementEntity> __updateAdapterOfCreditCardStatementEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CreditCardStatementDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCreditCardStatementEntity = new EntityInsertionAdapter<CreditCardStatementEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `credit_card_statements` (`id`,`creditCardId`,`periodStart`,`periodEnd`,`statementDate`,`dueDate`,`statementAmount`,`paidAmount`,`status`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CreditCardStatementEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCreditCardId());
        statement.bindLong(3, entity.getPeriodStart());
        statement.bindLong(4, entity.getPeriodEnd());
        statement.bindLong(5, entity.getStatementDate());
        statement.bindLong(6, entity.getDueDate());
        statement.bindDouble(7, entity.getStatementAmount());
        statement.bindDouble(8, entity.getPaidAmount());
        statement.bindString(9, entity.getStatus());
      }
    };
    this.__deletionAdapterOfCreditCardStatementEntity = new EntityDeletionOrUpdateAdapter<CreditCardStatementEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `credit_card_statements` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CreditCardStatementEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCreditCardStatementEntity = new EntityDeletionOrUpdateAdapter<CreditCardStatementEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `credit_card_statements` SET `id` = ?,`creditCardId` = ?,`periodStart` = ?,`periodEnd` = ?,`statementDate` = ?,`dueDate` = ?,`statementAmount` = ?,`paidAmount` = ?,`status` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CreditCardStatementEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCreditCardId());
        statement.bindLong(3, entity.getPeriodStart());
        statement.bindLong(4, entity.getPeriodEnd());
        statement.bindLong(5, entity.getStatementDate());
        statement.bindLong(6, entity.getDueDate());
        statement.bindDouble(7, entity.getStatementAmount());
        statement.bindDouble(8, entity.getPaidAmount());
        statement.bindString(9, entity.getStatus());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM credit_card_statements";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final CreditCardStatementEntity statement,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCreditCardStatementEntity.insertAndReturnId(statement);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<CreditCardStatementEntity> statements,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCreditCardStatementEntity.insert(statements);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final CreditCardStatementEntity statement,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCreditCardStatementEntity.handle(statement);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CreditCardStatementEntity statement,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCreditCardStatementEntity.handle(statement);
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
  public Flow<List<CreditCardStatementEntity>> observeByCard(final long cardId) {
    final String _sql = "SELECT * FROM credit_card_statements WHERE creditCardId = ? ORDER BY periodStart DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cardId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"credit_card_statements"}, new Callable<List<CreditCardStatementEntity>>() {
      @Override
      @NonNull
      public List<CreditCardStatementEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardId");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStart");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEnd");
          final int _cursorIndexOfStatementDate = CursorUtil.getColumnIndexOrThrow(_cursor, "statementDate");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfStatementAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "statementAmount");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<CreditCardStatementEntity> _result = new ArrayList<CreditCardStatementEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CreditCardStatementEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCreditCardId;
            _tmpCreditCardId = _cursor.getLong(_cursorIndexOfCreditCardId);
            final long _tmpPeriodStart;
            _tmpPeriodStart = _cursor.getLong(_cursorIndexOfPeriodStart);
            final long _tmpPeriodEnd;
            _tmpPeriodEnd = _cursor.getLong(_cursorIndexOfPeriodEnd);
            final long _tmpStatementDate;
            _tmpStatementDate = _cursor.getLong(_cursorIndexOfStatementDate);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final double _tmpStatementAmount;
            _tmpStatementAmount = _cursor.getDouble(_cursorIndexOfStatementAmount);
            final double _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getDouble(_cursorIndexOfPaidAmount);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _item = new CreditCardStatementEntity(_tmpId,_tmpCreditCardId,_tmpPeriodStart,_tmpPeriodEnd,_tmpStatementDate,_tmpDueDate,_tmpStatementAmount,_tmpPaidAmount,_tmpStatus);
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
  public Object getById(final long id,
      final Continuation<? super CreditCardStatementEntity> $completion) {
    final String _sql = "SELECT * FROM credit_card_statements WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CreditCardStatementEntity>() {
      @Override
      @Nullable
      public CreditCardStatementEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardId");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStart");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEnd");
          final int _cursorIndexOfStatementDate = CursorUtil.getColumnIndexOrThrow(_cursor, "statementDate");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfStatementAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "statementAmount");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final CreditCardStatementEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCreditCardId;
            _tmpCreditCardId = _cursor.getLong(_cursorIndexOfCreditCardId);
            final long _tmpPeriodStart;
            _tmpPeriodStart = _cursor.getLong(_cursorIndexOfPeriodStart);
            final long _tmpPeriodEnd;
            _tmpPeriodEnd = _cursor.getLong(_cursorIndexOfPeriodEnd);
            final long _tmpStatementDate;
            _tmpStatementDate = _cursor.getLong(_cursorIndexOfStatementDate);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final double _tmpStatementAmount;
            _tmpStatementAmount = _cursor.getDouble(_cursorIndexOfStatementAmount);
            final double _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getDouble(_cursorIndexOfPaidAmount);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _result = new CreditCardStatementEntity(_tmpId,_tmpCreditCardId,_tmpPeriodStart,_tmpPeriodEnd,_tmpStatementDate,_tmpDueDate,_tmpStatementAmount,_tmpPaidAmount,_tmpStatus);
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
  public Object findByPeriod(final long cardId, final long periodStart, final long periodEnd,
      final Continuation<? super CreditCardStatementEntity> $completion) {
    final String _sql = "SELECT * FROM credit_card_statements WHERE creditCardId = ? AND periodStart = ? AND periodEnd = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cardId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, periodStart);
    _argIndex = 3;
    _statement.bindLong(_argIndex, periodEnd);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CreditCardStatementEntity>() {
      @Override
      @Nullable
      public CreditCardStatementEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardId");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStart");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEnd");
          final int _cursorIndexOfStatementDate = CursorUtil.getColumnIndexOrThrow(_cursor, "statementDate");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfStatementAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "statementAmount");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final CreditCardStatementEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCreditCardId;
            _tmpCreditCardId = _cursor.getLong(_cursorIndexOfCreditCardId);
            final long _tmpPeriodStart;
            _tmpPeriodStart = _cursor.getLong(_cursorIndexOfPeriodStart);
            final long _tmpPeriodEnd;
            _tmpPeriodEnd = _cursor.getLong(_cursorIndexOfPeriodEnd);
            final long _tmpStatementDate;
            _tmpStatementDate = _cursor.getLong(_cursorIndexOfStatementDate);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final double _tmpStatementAmount;
            _tmpStatementAmount = _cursor.getDouble(_cursorIndexOfStatementAmount);
            final double _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getDouble(_cursorIndexOfPaidAmount);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _result = new CreditCardStatementEntity(_tmpId,_tmpCreditCardId,_tmpPeriodStart,_tmpPeriodEnd,_tmpStatementDate,_tmpDueDate,_tmpStatementAmount,_tmpPaidAmount,_tmpStatus);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
