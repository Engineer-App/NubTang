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
import com.pft.tracker.data.local.entity.CategoryEntity;
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
public final class CategoryDao_Impl implements CategoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CategoryEntity> __insertionAdapterOfCategoryEntity;

  private final EntityDeletionOrUpdateAdapter<CategoryEntity> __deletionAdapterOfCategoryEntity;

  private final EntityDeletionOrUpdateAdapter<CategoryEntity> __updateAdapterOfCategoryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CategoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCategoryEntity = new EntityInsertionAdapter<CategoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `categories` (`id`,`name`,`categoryType`,`parentCategoryId`,`icon`,`monthlyBudget`,`displayOrder`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CategoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getCategoryType());
        if (entity.getParentCategoryId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getParentCategoryId());
        }
        if (entity.getIcon() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getIcon());
        }
        if (entity.getMonthlyBudget() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getMonthlyBudget());
        }
        statement.bindLong(7, entity.getDisplayOrder());
      }
    };
    this.__deletionAdapterOfCategoryEntity = new EntityDeletionOrUpdateAdapter<CategoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `categories` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CategoryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCategoryEntity = new EntityDeletionOrUpdateAdapter<CategoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `categories` SET `id` = ?,`name` = ?,`categoryType` = ?,`parentCategoryId` = ?,`icon` = ?,`monthlyBudget` = ?,`displayOrder` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CategoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getCategoryType());
        if (entity.getParentCategoryId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getParentCategoryId());
        }
        if (entity.getIcon() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getIcon());
        }
        if (entity.getMonthlyBudget() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getMonthlyBudget());
        }
        statement.bindLong(7, entity.getDisplayOrder());
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM categories";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final CategoryEntity category,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCategoryEntity.insertAndReturnId(category);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<CategoryEntity> categories,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCategoryEntity.insert(categories);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final CategoryEntity category,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCategoryEntity.handle(category);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CategoryEntity category,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCategoryEntity.handle(category);
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
  public Flow<List<CategoryEntity>> observeAll() {
    final String _sql = "SELECT * FROM categories ORDER BY displayOrder ASC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"categories"}, new Callable<List<CategoryEntity>>() {
      @Override
      @NonNull
      public List<CategoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfParentCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentCategoryId");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfMonthlyBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyBudget");
          final int _cursorIndexOfDisplayOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "displayOrder");
          final List<CategoryEntity> _result = new ArrayList<CategoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategoryType;
            _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            final Long _tmpParentCategoryId;
            if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
              _tmpParentCategoryId = null;
            } else {
              _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final Double _tmpMonthlyBudget;
            if (_cursor.isNull(_cursorIndexOfMonthlyBudget)) {
              _tmpMonthlyBudget = null;
            } else {
              _tmpMonthlyBudget = _cursor.getDouble(_cursorIndexOfMonthlyBudget);
            }
            final int _tmpDisplayOrder;
            _tmpDisplayOrder = _cursor.getInt(_cursorIndexOfDisplayOrder);
            _item = new CategoryEntity(_tmpId,_tmpName,_tmpCategoryType,_tmpParentCategoryId,_tmpIcon,_tmpMonthlyBudget,_tmpDisplayOrder);
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
  public Flow<List<CategoryEntity>> observeByType(final String type) {
    final String _sql = "SELECT * FROM categories WHERE categoryType = ? ORDER BY displayOrder ASC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"categories"}, new Callable<List<CategoryEntity>>() {
      @Override
      @NonNull
      public List<CategoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfParentCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentCategoryId");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfMonthlyBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyBudget");
          final int _cursorIndexOfDisplayOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "displayOrder");
          final List<CategoryEntity> _result = new ArrayList<CategoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategoryType;
            _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            final Long _tmpParentCategoryId;
            if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
              _tmpParentCategoryId = null;
            } else {
              _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final Double _tmpMonthlyBudget;
            if (_cursor.isNull(_cursorIndexOfMonthlyBudget)) {
              _tmpMonthlyBudget = null;
            } else {
              _tmpMonthlyBudget = _cursor.getDouble(_cursorIndexOfMonthlyBudget);
            }
            final int _tmpDisplayOrder;
            _tmpDisplayOrder = _cursor.getInt(_cursorIndexOfDisplayOrder);
            _item = new CategoryEntity(_tmpId,_tmpName,_tmpCategoryType,_tmpParentCategoryId,_tmpIcon,_tmpMonthlyBudget,_tmpDisplayOrder);
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
  public Object getById(final long id, final Continuation<? super CategoryEntity> $completion) {
    final String _sql = "SELECT * FROM categories WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CategoryEntity>() {
      @Override
      @Nullable
      public CategoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfParentCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentCategoryId");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfMonthlyBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyBudget");
          final int _cursorIndexOfDisplayOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "displayOrder");
          final CategoryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategoryType;
            _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            final Long _tmpParentCategoryId;
            if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
              _tmpParentCategoryId = null;
            } else {
              _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final Double _tmpMonthlyBudget;
            if (_cursor.isNull(_cursorIndexOfMonthlyBudget)) {
              _tmpMonthlyBudget = null;
            } else {
              _tmpMonthlyBudget = _cursor.getDouble(_cursorIndexOfMonthlyBudget);
            }
            final int _tmpDisplayOrder;
            _tmpDisplayOrder = _cursor.getInt(_cursorIndexOfDisplayOrder);
            _result = new CategoryEntity(_tmpId,_tmpName,_tmpCategoryType,_tmpParentCategoryId,_tmpIcon,_tmpMonthlyBudget,_tmpDisplayOrder);
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
  public Flow<CategoryEntity> observeById(final long id) {
    final String _sql = "SELECT * FROM categories WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"categories"}, new Callable<CategoryEntity>() {
      @Override
      @Nullable
      public CategoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategoryType = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryType");
          final int _cursorIndexOfParentCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "parentCategoryId");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfMonthlyBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyBudget");
          final int _cursorIndexOfDisplayOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "displayOrder");
          final CategoryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpCategoryType;
            _tmpCategoryType = _cursor.getString(_cursorIndexOfCategoryType);
            final Long _tmpParentCategoryId;
            if (_cursor.isNull(_cursorIndexOfParentCategoryId)) {
              _tmpParentCategoryId = null;
            } else {
              _tmpParentCategoryId = _cursor.getLong(_cursorIndexOfParentCategoryId);
            }
            final String _tmpIcon;
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _tmpIcon = null;
            } else {
              _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            }
            final Double _tmpMonthlyBudget;
            if (_cursor.isNull(_cursorIndexOfMonthlyBudget)) {
              _tmpMonthlyBudget = null;
            } else {
              _tmpMonthlyBudget = _cursor.getDouble(_cursorIndexOfMonthlyBudget);
            }
            final int _tmpDisplayOrder;
            _tmpDisplayOrder = _cursor.getInt(_cursorIndexOfDisplayOrder);
            _result = new CategoryEntity(_tmpId,_tmpName,_tmpCategoryType,_tmpParentCategoryId,_tmpIcon,_tmpMonthlyBudget,_tmpDisplayOrder);
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
  public Flow<Double> observeSpent(final long categoryId, final long monthStart,
      final long monthEnd) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(amount), 0.0) FROM transactions\n"
            + "        WHERE transactionType = 'EXPENSE' AND categoryId = ?\n"
            + "        AND transactionDate BETWEEN ? AND ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, monthStart);
    _argIndex = 3;
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
  public Flow<List<CategorySpentRow>> observeSpentByCategory(final long monthStart,
      final long monthEnd) {
    final String _sql = "\n"
            + "        SELECT categoryId AS categoryId, COALESCE(SUM(amount), 0.0) AS spent FROM transactions\n"
            + "        WHERE transactionType = 'EXPENSE' AND transactionDate BETWEEN ? AND ?\n"
            + "        GROUP BY categoryId\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, monthStart);
    _argIndex = 2;
    _statement.bindLong(_argIndex, monthEnd);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<CategorySpentRow>>() {
      @Override
      @NonNull
      public List<CategorySpentRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCategoryId = 0;
          final int _cursorIndexOfSpent = 1;
          final List<CategorySpentRow> _result = new ArrayList<CategorySpentRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategorySpentRow _item;
            final Long _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
            }
            final double _tmpSpent;
            _tmpSpent = _cursor.getDouble(_cursorIndexOfSpent);
            _item = new CategorySpentRow(_tmpCategoryId,_tmpSpent);
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
