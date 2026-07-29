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
import com.pft.tracker.data.local.entity.CreditCardEntity;
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
public final class CreditCardDao_Impl implements CreditCardDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CreditCardEntity> __insertionAdapterOfCreditCardEntity;

  private final EntityDeletionOrUpdateAdapter<CreditCardEntity> __deletionAdapterOfCreditCardEntity;

  private final EntityDeletionOrUpdateAdapter<CreditCardEntity> __updateAdapterOfCreditCardEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CreditCardDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCreditCardEntity = new EntityInsertionAdapter<CreditCardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `credit_cards` (`id`,`name`,`issuer`,`cardNumberLast4`,`creditLimit`,`creditLimitGroupId`,`billingFrequencyMonths`,`statementDay`,`paymentDueDay`,`startMonth`,`isActive`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CreditCardEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getIssuer());
        statement.bindString(4, entity.getCardNumberLast4());
        statement.bindDouble(5, entity.getCreditLimit());
        if (entity.getCreditLimitGroupId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getCreditLimitGroupId());
        }
        statement.bindLong(7, entity.getBillingFrequencyMonths());
        statement.bindLong(8, entity.getStatementDay());
        statement.bindLong(9, entity.getPaymentDueDay());
        if (entity.getStartMonth() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getStartMonth());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(11, _tmp);
      }
    };
    this.__deletionAdapterOfCreditCardEntity = new EntityDeletionOrUpdateAdapter<CreditCardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `credit_cards` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CreditCardEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCreditCardEntity = new EntityDeletionOrUpdateAdapter<CreditCardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `credit_cards` SET `id` = ?,`name` = ?,`issuer` = ?,`cardNumberLast4` = ?,`creditLimit` = ?,`creditLimitGroupId` = ?,`billingFrequencyMonths` = ?,`statementDay` = ?,`paymentDueDay` = ?,`startMonth` = ?,`isActive` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CreditCardEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getIssuer());
        statement.bindString(4, entity.getCardNumberLast4());
        statement.bindDouble(5, entity.getCreditLimit());
        if (entity.getCreditLimitGroupId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getCreditLimitGroupId());
        }
        statement.bindLong(7, entity.getBillingFrequencyMonths());
        statement.bindLong(8, entity.getStatementDay());
        statement.bindLong(9, entity.getPaymentDueDay());
        if (entity.getStartMonth() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getStartMonth());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(11, _tmp);
        statement.bindLong(12, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM credit_cards";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final CreditCardEntity card, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCreditCardEntity.insertAndReturnId(card);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<CreditCardEntity> cards,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCreditCardEntity.insert(cards);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final CreditCardEntity card, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCreditCardEntity.handle(card);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CreditCardEntity card, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCreditCardEntity.handle(card);
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
  public Flow<List<CreditCardEntity>> observeAll() {
    final String _sql = "SELECT * FROM credit_cards ORDER BY isActive DESC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"credit_cards"}, new Callable<List<CreditCardEntity>>() {
      @Override
      @NonNull
      public List<CreditCardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIssuer = CursorUtil.getColumnIndexOrThrow(_cursor, "issuer");
          final int _cursorIndexOfCardNumberLast4 = CursorUtil.getColumnIndexOrThrow(_cursor, "cardNumberLast4");
          final int _cursorIndexOfCreditLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimit");
          final int _cursorIndexOfCreditLimitGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimitGroupId");
          final int _cursorIndexOfBillingFrequencyMonths = CursorUtil.getColumnIndexOrThrow(_cursor, "billingFrequencyMonths");
          final int _cursorIndexOfStatementDay = CursorUtil.getColumnIndexOrThrow(_cursor, "statementDay");
          final int _cursorIndexOfPaymentDueDay = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDueDay");
          final int _cursorIndexOfStartMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "startMonth");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<CreditCardEntity> _result = new ArrayList<CreditCardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CreditCardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpIssuer;
            _tmpIssuer = _cursor.getString(_cursorIndexOfIssuer);
            final String _tmpCardNumberLast4;
            _tmpCardNumberLast4 = _cursor.getString(_cursorIndexOfCardNumberLast4);
            final double _tmpCreditLimit;
            _tmpCreditLimit = _cursor.getDouble(_cursorIndexOfCreditLimit);
            final Long _tmpCreditLimitGroupId;
            if (_cursor.isNull(_cursorIndexOfCreditLimitGroupId)) {
              _tmpCreditLimitGroupId = null;
            } else {
              _tmpCreditLimitGroupId = _cursor.getLong(_cursorIndexOfCreditLimitGroupId);
            }
            final int _tmpBillingFrequencyMonths;
            _tmpBillingFrequencyMonths = _cursor.getInt(_cursorIndexOfBillingFrequencyMonths);
            final int _tmpStatementDay;
            _tmpStatementDay = _cursor.getInt(_cursorIndexOfStatementDay);
            final int _tmpPaymentDueDay;
            _tmpPaymentDueDay = _cursor.getInt(_cursorIndexOfPaymentDueDay);
            final Integer _tmpStartMonth;
            if (_cursor.isNull(_cursorIndexOfStartMonth)) {
              _tmpStartMonth = null;
            } else {
              _tmpStartMonth = _cursor.getInt(_cursorIndexOfStartMonth);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new CreditCardEntity(_tmpId,_tmpName,_tmpIssuer,_tmpCardNumberLast4,_tmpCreditLimit,_tmpCreditLimitGroupId,_tmpBillingFrequencyMonths,_tmpStatementDay,_tmpPaymentDueDay,_tmpStartMonth,_tmpIsActive);
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
  public Flow<List<CreditCardEntity>> observeActive() {
    final String _sql = "SELECT * FROM credit_cards WHERE isActive = 1 ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"credit_cards"}, new Callable<List<CreditCardEntity>>() {
      @Override
      @NonNull
      public List<CreditCardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIssuer = CursorUtil.getColumnIndexOrThrow(_cursor, "issuer");
          final int _cursorIndexOfCardNumberLast4 = CursorUtil.getColumnIndexOrThrow(_cursor, "cardNumberLast4");
          final int _cursorIndexOfCreditLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimit");
          final int _cursorIndexOfCreditLimitGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimitGroupId");
          final int _cursorIndexOfBillingFrequencyMonths = CursorUtil.getColumnIndexOrThrow(_cursor, "billingFrequencyMonths");
          final int _cursorIndexOfStatementDay = CursorUtil.getColumnIndexOrThrow(_cursor, "statementDay");
          final int _cursorIndexOfPaymentDueDay = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDueDay");
          final int _cursorIndexOfStartMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "startMonth");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<CreditCardEntity> _result = new ArrayList<CreditCardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CreditCardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpIssuer;
            _tmpIssuer = _cursor.getString(_cursorIndexOfIssuer);
            final String _tmpCardNumberLast4;
            _tmpCardNumberLast4 = _cursor.getString(_cursorIndexOfCardNumberLast4);
            final double _tmpCreditLimit;
            _tmpCreditLimit = _cursor.getDouble(_cursorIndexOfCreditLimit);
            final Long _tmpCreditLimitGroupId;
            if (_cursor.isNull(_cursorIndexOfCreditLimitGroupId)) {
              _tmpCreditLimitGroupId = null;
            } else {
              _tmpCreditLimitGroupId = _cursor.getLong(_cursorIndexOfCreditLimitGroupId);
            }
            final int _tmpBillingFrequencyMonths;
            _tmpBillingFrequencyMonths = _cursor.getInt(_cursorIndexOfBillingFrequencyMonths);
            final int _tmpStatementDay;
            _tmpStatementDay = _cursor.getInt(_cursorIndexOfStatementDay);
            final int _tmpPaymentDueDay;
            _tmpPaymentDueDay = _cursor.getInt(_cursorIndexOfPaymentDueDay);
            final Integer _tmpStartMonth;
            if (_cursor.isNull(_cursorIndexOfStartMonth)) {
              _tmpStartMonth = null;
            } else {
              _tmpStartMonth = _cursor.getInt(_cursorIndexOfStartMonth);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new CreditCardEntity(_tmpId,_tmpName,_tmpIssuer,_tmpCardNumberLast4,_tmpCreditLimit,_tmpCreditLimitGroupId,_tmpBillingFrequencyMonths,_tmpStatementDay,_tmpPaymentDueDay,_tmpStartMonth,_tmpIsActive);
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
  public Object getById(final long id, final Continuation<? super CreditCardEntity> $completion) {
    final String _sql = "SELECT * FROM credit_cards WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CreditCardEntity>() {
      @Override
      @Nullable
      public CreditCardEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIssuer = CursorUtil.getColumnIndexOrThrow(_cursor, "issuer");
          final int _cursorIndexOfCardNumberLast4 = CursorUtil.getColumnIndexOrThrow(_cursor, "cardNumberLast4");
          final int _cursorIndexOfCreditLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimit");
          final int _cursorIndexOfCreditLimitGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimitGroupId");
          final int _cursorIndexOfBillingFrequencyMonths = CursorUtil.getColumnIndexOrThrow(_cursor, "billingFrequencyMonths");
          final int _cursorIndexOfStatementDay = CursorUtil.getColumnIndexOrThrow(_cursor, "statementDay");
          final int _cursorIndexOfPaymentDueDay = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDueDay");
          final int _cursorIndexOfStartMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "startMonth");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final CreditCardEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpIssuer;
            _tmpIssuer = _cursor.getString(_cursorIndexOfIssuer);
            final String _tmpCardNumberLast4;
            _tmpCardNumberLast4 = _cursor.getString(_cursorIndexOfCardNumberLast4);
            final double _tmpCreditLimit;
            _tmpCreditLimit = _cursor.getDouble(_cursorIndexOfCreditLimit);
            final Long _tmpCreditLimitGroupId;
            if (_cursor.isNull(_cursorIndexOfCreditLimitGroupId)) {
              _tmpCreditLimitGroupId = null;
            } else {
              _tmpCreditLimitGroupId = _cursor.getLong(_cursorIndexOfCreditLimitGroupId);
            }
            final int _tmpBillingFrequencyMonths;
            _tmpBillingFrequencyMonths = _cursor.getInt(_cursorIndexOfBillingFrequencyMonths);
            final int _tmpStatementDay;
            _tmpStatementDay = _cursor.getInt(_cursorIndexOfStatementDay);
            final int _tmpPaymentDueDay;
            _tmpPaymentDueDay = _cursor.getInt(_cursorIndexOfPaymentDueDay);
            final Integer _tmpStartMonth;
            if (_cursor.isNull(_cursorIndexOfStartMonth)) {
              _tmpStartMonth = null;
            } else {
              _tmpStartMonth = _cursor.getInt(_cursorIndexOfStartMonth);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new CreditCardEntity(_tmpId,_tmpName,_tmpIssuer,_tmpCardNumberLast4,_tmpCreditLimit,_tmpCreditLimitGroupId,_tmpBillingFrequencyMonths,_tmpStatementDay,_tmpPaymentDueDay,_tmpStartMonth,_tmpIsActive);
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
  public Flow<CreditCardEntity> observeById(final long id) {
    final String _sql = "SELECT * FROM credit_cards WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"credit_cards"}, new Callable<CreditCardEntity>() {
      @Override
      @Nullable
      public CreditCardEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIssuer = CursorUtil.getColumnIndexOrThrow(_cursor, "issuer");
          final int _cursorIndexOfCardNumberLast4 = CursorUtil.getColumnIndexOrThrow(_cursor, "cardNumberLast4");
          final int _cursorIndexOfCreditLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimit");
          final int _cursorIndexOfCreditLimitGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimitGroupId");
          final int _cursorIndexOfBillingFrequencyMonths = CursorUtil.getColumnIndexOrThrow(_cursor, "billingFrequencyMonths");
          final int _cursorIndexOfStatementDay = CursorUtil.getColumnIndexOrThrow(_cursor, "statementDay");
          final int _cursorIndexOfPaymentDueDay = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDueDay");
          final int _cursorIndexOfStartMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "startMonth");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final CreditCardEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpIssuer;
            _tmpIssuer = _cursor.getString(_cursorIndexOfIssuer);
            final String _tmpCardNumberLast4;
            _tmpCardNumberLast4 = _cursor.getString(_cursorIndexOfCardNumberLast4);
            final double _tmpCreditLimit;
            _tmpCreditLimit = _cursor.getDouble(_cursorIndexOfCreditLimit);
            final Long _tmpCreditLimitGroupId;
            if (_cursor.isNull(_cursorIndexOfCreditLimitGroupId)) {
              _tmpCreditLimitGroupId = null;
            } else {
              _tmpCreditLimitGroupId = _cursor.getLong(_cursorIndexOfCreditLimitGroupId);
            }
            final int _tmpBillingFrequencyMonths;
            _tmpBillingFrequencyMonths = _cursor.getInt(_cursorIndexOfBillingFrequencyMonths);
            final int _tmpStatementDay;
            _tmpStatementDay = _cursor.getInt(_cursorIndexOfStatementDay);
            final int _tmpPaymentDueDay;
            _tmpPaymentDueDay = _cursor.getInt(_cursorIndexOfPaymentDueDay);
            final Integer _tmpStartMonth;
            if (_cursor.isNull(_cursorIndexOfStartMonth)) {
              _tmpStartMonth = null;
            } else {
              _tmpStartMonth = _cursor.getInt(_cursorIndexOfStartMonth);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _result = new CreditCardEntity(_tmpId,_tmpName,_tmpIssuer,_tmpCardNumberLast4,_tmpCreditLimit,_tmpCreditLimitGroupId,_tmpBillingFrequencyMonths,_tmpStatementDay,_tmpPaymentDueDay,_tmpStartMonth,_tmpIsActive);
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
  public Flow<List<CreditCardEntity>> observeByGroup(final long groupId) {
    final String _sql = "SELECT * FROM credit_cards WHERE creditLimitGroupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"credit_cards"}, new Callable<List<CreditCardEntity>>() {
      @Override
      @NonNull
      public List<CreditCardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIssuer = CursorUtil.getColumnIndexOrThrow(_cursor, "issuer");
          final int _cursorIndexOfCardNumberLast4 = CursorUtil.getColumnIndexOrThrow(_cursor, "cardNumberLast4");
          final int _cursorIndexOfCreditLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimit");
          final int _cursorIndexOfCreditLimitGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimitGroupId");
          final int _cursorIndexOfBillingFrequencyMonths = CursorUtil.getColumnIndexOrThrow(_cursor, "billingFrequencyMonths");
          final int _cursorIndexOfStatementDay = CursorUtil.getColumnIndexOrThrow(_cursor, "statementDay");
          final int _cursorIndexOfPaymentDueDay = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDueDay");
          final int _cursorIndexOfStartMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "startMonth");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<CreditCardEntity> _result = new ArrayList<CreditCardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CreditCardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpIssuer;
            _tmpIssuer = _cursor.getString(_cursorIndexOfIssuer);
            final String _tmpCardNumberLast4;
            _tmpCardNumberLast4 = _cursor.getString(_cursorIndexOfCardNumberLast4);
            final double _tmpCreditLimit;
            _tmpCreditLimit = _cursor.getDouble(_cursorIndexOfCreditLimit);
            final Long _tmpCreditLimitGroupId;
            if (_cursor.isNull(_cursorIndexOfCreditLimitGroupId)) {
              _tmpCreditLimitGroupId = null;
            } else {
              _tmpCreditLimitGroupId = _cursor.getLong(_cursorIndexOfCreditLimitGroupId);
            }
            final int _tmpBillingFrequencyMonths;
            _tmpBillingFrequencyMonths = _cursor.getInt(_cursorIndexOfBillingFrequencyMonths);
            final int _tmpStatementDay;
            _tmpStatementDay = _cursor.getInt(_cursorIndexOfStatementDay);
            final int _tmpPaymentDueDay;
            _tmpPaymentDueDay = _cursor.getInt(_cursorIndexOfPaymentDueDay);
            final Integer _tmpStartMonth;
            if (_cursor.isNull(_cursorIndexOfStartMonth)) {
              _tmpStartMonth = null;
            } else {
              _tmpStartMonth = _cursor.getInt(_cursorIndexOfStartMonth);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new CreditCardEntity(_tmpId,_tmpName,_tmpIssuer,_tmpCardNumberLast4,_tmpCreditLimit,_tmpCreditLimitGroupId,_tmpBillingFrequencyMonths,_tmpStatementDay,_tmpPaymentDueDay,_tmpStartMonth,_tmpIsActive);
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
  public Object getByGroup(final long groupId,
      final Continuation<? super List<CreditCardEntity>> $completion) {
    final String _sql = "SELECT * FROM credit_cards WHERE creditLimitGroupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CreditCardEntity>>() {
      @Override
      @NonNull
      public List<CreditCardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIssuer = CursorUtil.getColumnIndexOrThrow(_cursor, "issuer");
          final int _cursorIndexOfCardNumberLast4 = CursorUtil.getColumnIndexOrThrow(_cursor, "cardNumberLast4");
          final int _cursorIndexOfCreditLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimit");
          final int _cursorIndexOfCreditLimitGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditLimitGroupId");
          final int _cursorIndexOfBillingFrequencyMonths = CursorUtil.getColumnIndexOrThrow(_cursor, "billingFrequencyMonths");
          final int _cursorIndexOfStatementDay = CursorUtil.getColumnIndexOrThrow(_cursor, "statementDay");
          final int _cursorIndexOfPaymentDueDay = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDueDay");
          final int _cursorIndexOfStartMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "startMonth");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final List<CreditCardEntity> _result = new ArrayList<CreditCardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CreditCardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpIssuer;
            _tmpIssuer = _cursor.getString(_cursorIndexOfIssuer);
            final String _tmpCardNumberLast4;
            _tmpCardNumberLast4 = _cursor.getString(_cursorIndexOfCardNumberLast4);
            final double _tmpCreditLimit;
            _tmpCreditLimit = _cursor.getDouble(_cursorIndexOfCreditLimit);
            final Long _tmpCreditLimitGroupId;
            if (_cursor.isNull(_cursorIndexOfCreditLimitGroupId)) {
              _tmpCreditLimitGroupId = null;
            } else {
              _tmpCreditLimitGroupId = _cursor.getLong(_cursorIndexOfCreditLimitGroupId);
            }
            final int _tmpBillingFrequencyMonths;
            _tmpBillingFrequencyMonths = _cursor.getInt(_cursorIndexOfBillingFrequencyMonths);
            final int _tmpStatementDay;
            _tmpStatementDay = _cursor.getInt(_cursorIndexOfStatementDay);
            final int _tmpPaymentDueDay;
            _tmpPaymentDueDay = _cursor.getInt(_cursorIndexOfPaymentDueDay);
            final Integer _tmpStartMonth;
            if (_cursor.isNull(_cursorIndexOfStartMonth)) {
              _tmpStartMonth = null;
            } else {
              _tmpStartMonth = _cursor.getInt(_cursorIndexOfStartMonth);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new CreditCardEntity(_tmpId,_tmpName,_tmpIssuer,_tmpCardNumberLast4,_tmpCreditLimit,_tmpCreditLimitGroupId,_tmpBillingFrequencyMonths,_tmpStatementDay,_tmpPaymentDueDay,_tmpStartMonth,_tmpIsActive);
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
  public Flow<CreditCardUsedRow> observeUsed(final long cardId) {
    final String _sql = "\n"
            + "        SELECT ? AS id, (\n"
            + "            COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'EXPENSE' AND sourceCreditCardId = ?), 0.0)\n"
            + "            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'CREDIT_CARD_PAYMENT' AND destinationCreditCardId = ?), 0.0)\n"
            + "        ) AS currentUsed\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cardId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, cardId);
    _argIndex = 3;
    _statement.bindLong(_argIndex, cardId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<CreditCardUsedRow>() {
      @Override
      @Nullable
      public CreditCardUsedRow call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfCurrentUsed = 1;
          final CreditCardUsedRow _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpCurrentUsed;
            _tmpCurrentUsed = _cursor.getDouble(_cursorIndexOfCurrentUsed);
            _result = new CreditCardUsedRow(_tmpId,_tmpCurrentUsed);
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
  public Flow<List<CreditCardUsedRow>> observeAllUsed() {
    final String _sql = "\n"
            + "        SELECT c.id AS id, (\n"
            + "            COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'EXPENSE' AND sourceCreditCardId = c.id), 0.0)\n"
            + "            - COALESCE((SELECT SUM(amount) FROM transactions WHERE transactionType = 'CREDIT_CARD_PAYMENT' AND destinationCreditCardId = c.id), 0.0)\n"
            + "        ) AS currentUsed\n"
            + "        FROM credit_cards c\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions",
        "credit_cards"}, new Callable<List<CreditCardUsedRow>>() {
      @Override
      @NonNull
      public List<CreditCardUsedRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfCurrentUsed = 1;
          final List<CreditCardUsedRow> _result = new ArrayList<CreditCardUsedRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CreditCardUsedRow _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpCurrentUsed;
            _tmpCurrentUsed = _cursor.getDouble(_cursorIndexOfCurrentUsed);
            _item = new CreditCardUsedRow(_tmpId,_tmpCurrentUsed);
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
