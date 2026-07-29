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
import com.pft.tracker.data.local.entity.RecurringTransactionEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class RecurringTransactionDao_Impl implements RecurringTransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecurringTransactionEntity> __insertionAdapterOfRecurringTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<RecurringTransactionEntity> __deletionAdapterOfRecurringTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<RecurringTransactionEntity> __updateAdapterOfRecurringTransactionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RecurringTransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecurringTransactionEntity = new EntityInsertionAdapter<RecurringTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `recurring_transactions` (`id`,`name`,`amount`,`categoryId`,`sourceAccountId`,`sourceCreditCardId`,`startDate`,`frequency`,`totalInstallments`,`installmentsGenerated`,`nextRunDate`,`endDate`,`note`,`isActive`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurringTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindDouble(3, entity.getAmount());
        if (entity.getCategoryId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCategoryId());
        }
        if (entity.getSourceAccountId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getSourceAccountId());
        }
        if (entity.getSourceCreditCardId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getSourceCreditCardId());
        }
        statement.bindLong(7, entity.getStartDate());
        statement.bindString(8, entity.getFrequency());
        if (entity.getTotalInstallments() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getTotalInstallments());
        }
        statement.bindLong(10, entity.getInstallmentsGenerated());
        statement.bindLong(11, entity.getNextRunDate());
        if (entity.getEndDate() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getEndDate());
        }
        if (entity.getNote() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getNote());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(14, _tmp);
      }
    };
    this.__deletionAdapterOfRecurringTransactionEntity = new EntityDeletionOrUpdateAdapter<RecurringTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `recurring_transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurringTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRecurringTransactionEntity = new EntityDeletionOrUpdateAdapter<RecurringTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `recurring_transactions` SET `id` = ?,`name` = ?,`amount` = ?,`categoryId` = ?,`sourceAccountId` = ?,`sourceCreditCardId` = ?,`startDate` = ?,`frequency` = ?,`totalInstallments` = ?,`installmentsGenerated` = ?,`nextRunDate` = ?,`endDate` = ?,`note` = ?,`isActive` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecurringTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindDouble(3, entity.getAmount());
        if (entity.getCategoryId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCategoryId());
        }
        if (entity.getSourceAccountId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getSourceAccountId());
        }
        if (entity.getSourceCreditCardId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getSourceCreditCardId());
        }
        statement.bindLong(7, entity.getStartDate());
        statement.bindString(8, entity.getFrequency());
        if (entity.getTotalInstallments() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getTotalInstallments());
        }
        statement.bindLong(10, entity.getInstallmentsGenerated());
        statement.bindLong(11, entity.getNextRunDate());
        if (entity.getEndDate() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getEndDate());
        }
        if (entity.getNote() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getNote());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(14, _tmp);
        statement.bindLong(15, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM recurring_transactions";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final RecurringTransactionEntity plan,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRecurringTransactionEntity.insertAndReturnId(plan);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<RecurringTransactionEntity> plans,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRecurringTransactionEntity.insert(plans);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RecurringTransactionEntity plan,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRecurringTransactionEntity.handle(plan);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final RecurringTransactionEntity plan,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRecurringTransactionEntity.handle(plan);
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
  public Flow<List<RecurringTransactionEntity>> observeAll() {
    final String _sql = "SELECT * FROM recurring_transactions ORDER BY isActive DESC, nextRunDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recurring_transactions"}, new Callable<List<RecurringTransactionEntity>>() {
      @Override
      @NonNull
      public List<RecurringTransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfSourceAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAccountId");
          final int _cursorIndexOfSourceCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceCreditCardId");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTotalInstallments = CursorUtil.getColumnIndexOrThrow(_cursor, "totalInstallments");
          final int _cursorIndexOfInstallmentsGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "installmentsGenerated");
          final int _cursorIndexOfNextRunDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRunDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<RecurringTransactionEntity> _result = new ArrayList<RecurringTransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecurringTransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
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
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final Integer _tmpTotalInstallments;
            if (_cursor.isNull(_cursorIndexOfTotalInstallments)) {
              _tmpTotalInstallments = null;
            } else {
              _tmpTotalInstallments = _cursor.getInt(_cursorIndexOfTotalInstallments);
            }
            final int _tmpInstallmentsGenerated;
            _tmpInstallmentsGenerated = _cursor.getInt(_cursorIndexOfInstallmentsGenerated);
            final long _tmpNextRunDate;
            _tmpNextRunDate = _cursor.getLong(_cursorIndexOfNextRunDate);
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new RecurringTransactionEntity(_tmpId,_tmpName,_tmpAmount,_tmpCategoryId,_tmpSourceAccountId,_tmpSourceCreditCardId,_tmpStartDate,_tmpFrequency,_tmpTotalInstallments,_tmpInstallmentsGenerated,_tmpNextRunDate,_tmpEndDate,_tmpNote,_tmpIsActive);
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
      final Continuation<? super RecurringTransactionEntity> $completion) {
    final String _sql = "SELECT * FROM recurring_transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RecurringTransactionEntity>() {
      @Override
      @Nullable
      public RecurringTransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfSourceAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAccountId");
          final int _cursorIndexOfSourceCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceCreditCardId");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTotalInstallments = CursorUtil.getColumnIndexOrThrow(_cursor, "totalInstallments");
          final int _cursorIndexOfInstallmentsGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "installmentsGenerated");
          final int _cursorIndexOfNextRunDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRunDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final RecurringTransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
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
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final Integer _tmpTotalInstallments;
            if (_cursor.isNull(_cursorIndexOfTotalInstallments)) {
              _tmpTotalInstallments = null;
            } else {
              _tmpTotalInstallments = _cursor.getInt(_cursorIndexOfTotalInstallments);
            }
            final int _tmpInstallmentsGenerated;
            _tmpInstallmentsGenerated = _cursor.getInt(_cursorIndexOfInstallmentsGenerated);
            final long _tmpNextRunDate;
            _tmpNextRunDate = _cursor.getLong(_cursorIndexOfNextRunDate);
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new RecurringTransactionEntity(_tmpId,_tmpName,_tmpAmount,_tmpCategoryId,_tmpSourceAccountId,_tmpSourceCreditCardId,_tmpStartDate,_tmpFrequency,_tmpTotalInstallments,_tmpInstallmentsGenerated,_tmpNextRunDate,_tmpEndDate,_tmpNote,_tmpIsActive);
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
  public Flow<RecurringTransactionEntity> observeById(final long id) {
    final String _sql = "SELECT * FROM recurring_transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recurring_transactions"}, new Callable<RecurringTransactionEntity>() {
      @Override
      @Nullable
      public RecurringTransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfSourceAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAccountId");
          final int _cursorIndexOfSourceCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceCreditCardId");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTotalInstallments = CursorUtil.getColumnIndexOrThrow(_cursor, "totalInstallments");
          final int _cursorIndexOfInstallmentsGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "installmentsGenerated");
          final int _cursorIndexOfNextRunDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRunDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final RecurringTransactionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
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
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final Integer _tmpTotalInstallments;
            if (_cursor.isNull(_cursorIndexOfTotalInstallments)) {
              _tmpTotalInstallments = null;
            } else {
              _tmpTotalInstallments = _cursor.getInt(_cursorIndexOfTotalInstallments);
            }
            final int _tmpInstallmentsGenerated;
            _tmpInstallmentsGenerated = _cursor.getInt(_cursorIndexOfInstallmentsGenerated);
            final long _tmpNextRunDate;
            _tmpNextRunDate = _cursor.getLong(_cursorIndexOfNextRunDate);
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new RecurringTransactionEntity(_tmpId,_tmpName,_tmpAmount,_tmpCategoryId,_tmpSourceAccountId,_tmpSourceCreditCardId,_tmpStartDate,_tmpFrequency,_tmpTotalInstallments,_tmpInstallmentsGenerated,_tmpNextRunDate,_tmpEndDate,_tmpNote,_tmpIsActive);
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
  public Object getDue(final long today,
      final Continuation<? super List<RecurringTransactionEntity>> $completion) {
    final String _sql = "SELECT * FROM recurring_transactions WHERE isActive = 1 AND nextRunDate <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, today);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RecurringTransactionEntity>>() {
      @Override
      @NonNull
      public List<RecurringTransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfSourceAccountId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAccountId");
          final int _cursorIndexOfSourceCreditCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceCreditCardId");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTotalInstallments = CursorUtil.getColumnIndexOrThrow(_cursor, "totalInstallments");
          final int _cursorIndexOfInstallmentsGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "installmentsGenerated");
          final int _cursorIndexOfNextRunDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRunDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<RecurringTransactionEntity> _result = new ArrayList<RecurringTransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecurringTransactionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
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
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final Integer _tmpTotalInstallments;
            if (_cursor.isNull(_cursorIndexOfTotalInstallments)) {
              _tmpTotalInstallments = null;
            } else {
              _tmpTotalInstallments = _cursor.getInt(_cursorIndexOfTotalInstallments);
            }
            final int _tmpInstallmentsGenerated;
            _tmpInstallmentsGenerated = _cursor.getInt(_cursorIndexOfInstallmentsGenerated);
            final long _tmpNextRunDate;
            _tmpNextRunDate = _cursor.getLong(_cursorIndexOfNextRunDate);
            final Long _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new RecurringTransactionEntity(_tmpId,_tmpName,_tmpAmount,_tmpCategoryId,_tmpSourceAccountId,_tmpSourceCreditCardId,_tmpStartDate,_tmpFrequency,_tmpTotalInstallments,_tmpInstallmentsGenerated,_tmpNextRunDate,_tmpEndDate,_tmpNote,_tmpIsActive);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
