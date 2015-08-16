package com.flatlyapps.materialBudget.utilMethods;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.flatlyapps.materialBudget.R;

/**
 * Created by PaulN on 12/08/2015.
 */
    class TransactionDataContract {

    public TransactionDataContract(){}

    private static String DB_PATH;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String LONG_TYPE = " BIGINT";
    private static final String BOOLEAN_TYPE = " BOOLEAN";
    private static final String DATETIME_TYPE = " BIGINT";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String UNIQUE = " UNIQUE";
    private static final String COMMA_SEP = ",";

    public static abstract class DataColumns implements BaseColumns {
        public static final String TABLE_NAME = "data_New";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ACCOUNT_ID = "account_id";
        public static final String COLUMN_COST = "cost";
        public static final String COLUMN_INCOME_CATEGORY_ID = "income_category_id";
        public static final String COLUMN_EXPENSE_CATEGORY_ID = "expense_category_id";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_EXPENSE = "expense";
        public static final String COLUMN_INCOME = "income";
        public static final String COLUMN_RECUR = "recur";
        public static final String COLUMN_TRANSFER = "transfer";
        public static final String COLUMN_TRANSFER_TO = "transfer_to";
        public static final String COLUMN_TRANSFER_FROM = "transfer_from";
        public static final String COLUMN_RECUR_ID = "recur_id";
    }

    private static final String SQL_CREATE_DATA = "CREATE TABLE "
            + DataColumns.TABLE_NAME + " ("
            + DataColumns._ID + LONG_TYPE + PRIMARY_KEY + COMMA_SEP
            + DataColumns.COLUMN_NAME + TEXT_TYPE + COMMA_SEP
            + DataColumns.COLUMN_ACCOUNT_ID + LONG_TYPE + COMMA_SEP
            + DataColumns.COLUMN_COST + LONG_TYPE + COMMA_SEP
            + DataColumns.COLUMN_INCOME_CATEGORY_ID + LONG_TYPE + COMMA_SEP
            + DataColumns.COLUMN_EXPENSE_CATEGORY_ID + LONG_TYPE + COMMA_SEP
            + DataColumns.COLUMN_TIME + DATETIME_TYPE + COMMA_SEP
            + DataColumns.COLUMN_EXPENSE + BOOLEAN_TYPE + COMMA_SEP
            + DataColumns.COLUMN_INCOME + BOOLEAN_TYPE + COMMA_SEP
            + DataColumns.COLUMN_RECUR + BOOLEAN_TYPE + COMMA_SEP
            + DataColumns.COLUMN_TRANSFER + BOOLEAN_TYPE + COMMA_SEP
            + DataColumns.COLUMN_TRANSFER_TO + LONG_TYPE + COMMA_SEP
            + DataColumns.COLUMN_TRANSFER_FROM + LONG_TYPE + COMMA_SEP
            + DataColumns.COLUMN_RECUR_ID + LONG_TYPE
            + " )";

    public static abstract class RecurColumns implements BaseColumns {
        public static final String TABLE_NAME = "recur_New";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ACCOUNT_ID = "account_id";
        public static final String COLUMN_COST = "cost";
        public static final String COLUMN_INCOME_CATEGORY_ID = "income_category_id";
        public static final String COLUMN_EXPENSE_CATEGORY_ID = "expense_category_id";
        public static final String COLUMN_EXPENSE = "expense";
        public static final String COLUMN_INCOME = "income";
        public static final String COLUMN_TRANSFER = "transfer";
        public static final String COLUMN_TRANSFER_TO = "transfer_to";
        public static final String COLUMN_TRANSFER_FROM = "transfer_from";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_RRULE = "rrule";
    }

    private static final String SQL_CREATE_RECUR = "CREATE TABLE "
            + RecurColumns.TABLE_NAME + " ("
            + RecurColumns._ID + LONG_TYPE + PRIMARY_KEY + COMMA_SEP
            + RecurColumns.COLUMN_NAME + TEXT_TYPE + UNIQUE + COMMA_SEP
            + RecurColumns.COLUMN_ACCOUNT_ID + LONG_TYPE + COMMA_SEP
            + RecurColumns.COLUMN_COST + LONG_TYPE + COMMA_SEP
            + RecurColumns.COLUMN_INCOME_CATEGORY_ID + LONG_TYPE + COMMA_SEP
            + RecurColumns.COLUMN_EXPENSE_CATEGORY_ID + LONG_TYPE + COMMA_SEP
            + RecurColumns.COLUMN_EXPENSE + BOOLEAN_TYPE + COMMA_SEP
            + RecurColumns.COLUMN_INCOME + BOOLEAN_TYPE + COMMA_SEP
            + RecurColumns.COLUMN_TRANSFER + BOOLEAN_TYPE + COMMA_SEP
            + RecurColumns.COLUMN_TRANSFER_TO + LONG_TYPE + COMMA_SEP
            + RecurColumns.COLUMN_TRANSFER_FROM + LONG_TYPE + COMMA_SEP
            + RecurColumns.COLUMN_RRULE + TEXT_TYPE + COMMA_SEP
            + RecurColumns.COLUMN_START_DATE + DATETIME_TYPE
            + " )";

    public static abstract class AccountColumns implements BaseColumns {
        public static final String TABLE_NAME = "account_New";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BUDGET = "budget";
        public static final String COLUMN_USE_INCOME = "budgetuseincome";
        public static final String COLUMN_INITIAL_FUNDS = "initialfunds";
    }

    private static final String SQL_CREATE_ACCOUNT = "CREATE TABLE "
            + AccountColumns.TABLE_NAME + " ("
            + AccountColumns._ID + LONG_TYPE + PRIMARY_KEY + COMMA_SEP
            + AccountColumns.COLUMN_NAME + TEXT_TYPE + UNIQUE + COMMA_SEP
            + AccountColumns.COLUMN_BUDGET + LONG_TYPE + COMMA_SEP
            + AccountColumns.COLUMN_INITIAL_FUNDS + LONG_TYPE + COMMA_SEP
            + AccountColumns.COLUMN_USE_INCOME + BOOLEAN_TYPE
            + " )";

    public static abstract class DefaultAccountColumns implements BaseColumns {
        public static final String TABLE_NAME = "defaultAccount_New";
        public static final String COLUMN_ACCOUNT_ID = "account_id";
    }

    private static final String SQL_CREATE_DEFAULT_ACCOUNT = "CREATE TABLE "
            + DefaultAccountColumns.TABLE_NAME + " ("
            + DefaultAccountColumns._ID + LONG_TYPE + PRIMARY_KEY + COMMA_SEP
            + DefaultAccountColumns.COLUMN_ACCOUNT_ID + LONG_TYPE
            + " )";

    public static abstract class ExpenseCategoryColumns implements BaseColumns {
        public static final String TABLE_NAME = "expense_New";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COLOR = "color";
    }

    private static final String SQL_CREATE_EXPENSE = "CREATE TABLE "
            + ExpenseCategoryColumns.TABLE_NAME + " ("
            + ExpenseCategoryColumns._ID + LONG_TYPE + PRIMARY_KEY + COMMA_SEP
            + ExpenseCategoryColumns.COLUMN_NAME + TEXT_TYPE + UNIQUE + COMMA_SEP
            + ExpenseCategoryColumns.COLUMN_COLOR + INTEGER_TYPE
            + " )";

    public static abstract class IncomeCategoryColumns implements BaseColumns {
        public static final String TABLE_NAME = "income_New";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COLOR = "color";
    }

    private static final String SQL_CREATE_INCOME = "CREATE TABLE "
            + IncomeCategoryColumns.TABLE_NAME + " ("
            + IncomeCategoryColumns._ID + LONG_TYPE + PRIMARY_KEY + COMMA_SEP
            + IncomeCategoryColumns.COLUMN_NAME + TEXT_TYPE + UNIQUE + COMMA_SEP
            + IncomeCategoryColumns.COLUMN_COLOR + INTEGER_TYPE
            + " )";

    public static abstract class ExpenseCategoryBudgetColumns implements BaseColumns {
        public static final String TABLE_NAME = "category_New";
        public static final String COLUMN_EXPENSE_CATEGORY_ID = "category";
        public static final String COLUMN_ACCOUNT_ID = "account";
        public static final String COLUMN_PERCENT = "precent";
    }

    private static final String SQL_CREATE_EXPENSE_CATEGORY_BUDGET = "CREATE TABLE "
            + ExpenseCategoryBudgetColumns.TABLE_NAME + " ("
            + ExpenseCategoryBudgetColumns._ID + LONG_TYPE + PRIMARY_KEY + COMMA_SEP
            + ExpenseCategoryBudgetColumns.COLUMN_EXPENSE_CATEGORY_ID + LONG_TYPE + COMMA_SEP
            + ExpenseCategoryBudgetColumns.COLUMN_ACCOUNT_ID + LONG_TYPE + COMMA_SEP
            + ExpenseCategoryBudgetColumns.COLUMN_PERCENT + INTEGER_TYPE
            + " )";

    public static abstract class CurrencyColumns implements BaseColumns {
        public static final String TABLE_NAME = "currency_New";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_SYMBOL = "symbol";
        public static final String COLUMN_FORMAT = "format";
    }

    private static final String SQL_CREATE_CURRENCY = "CREATE TABLE "
            + CurrencyColumns.TABLE_NAME + " ("
            + CurrencyColumns._ID + LONG_TYPE + PRIMARY_KEY + COMMA_SEP
            + CurrencyColumns.COLUMN_NAME + TEXT_TYPE + UNIQUE + COMMA_SEP
            + CurrencyColumns.COLUMN_CODE + TEXT_TYPE + COMMA_SEP
            + CurrencyColumns.COLUMN_SYMBOL + TEXT_TYPE + COMMA_SEP
            + CurrencyColumns.COLUMN_FORMAT + TEXT_TYPE
            + " )";

    public static abstract class CurrentCurrencyColumns implements BaseColumns {
        public static final String TABLE_NAME = "currentCurrency_New";
        public static final String COLUMN_CURRENCY_ID = "currency_id";
    }

    private static final String SQL_CREATE_CURRENT_CURRENCY = "CREATE TABLE "
            + CurrentCurrencyColumns.TABLE_NAME + " ("
            + CurrentCurrencyColumns._ID + LONG_TYPE + PRIMARY_KEY + COMMA_SEP
            + CurrentCurrencyColumns.COLUMN_CURRENCY_ID + LONG_TYPE
            + " )";

    public class TransactionDataDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Budget2.db";
        private Context context;

        public TransactionDataDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_DATA);
            db.execSQL(SQL_CREATE_RECUR);
            db.execSQL(SQL_CREATE_ACCOUNT);
            db.execSQL(SQL_CREATE_DEFAULT_ACCOUNT);
            db.execSQL(SQL_CREATE_EXPENSE);
            db.execSQL(SQL_CREATE_INCOME);
            db.execSQL(SQL_CREATE_EXPENSE_CATEGORY_BUDGET);
            db.execSQL(SQL_CREATE_CURRENCY);
            db.execSQL(SQL_CREATE_CURRENT_CURRENCY);

            int[] colors = context.getResources().getIntArray(R.array.colors);
            String[] catsNameArray = context.getResources().getStringArray(R.array.income_cats);
            int[] catsIDArray = context.getResources().getIntArray(R.array.income_cats_ids);
            int i = 0;
            for(String catName: catsNameArray ){
                ContentValues content = new ContentValues();
                content.put(IncomeCategoryColumns._ID, catsIDArray[i]);
                content.put(IncomeCategoryColumns.COLUMN_NAME, catName);
                content.put(IncomeCategoryColumns.COLUMN_COLOR, colors[catsIDArray[i]]);
                db.insert(IncomeCategoryColumns.TABLE_NAME, null, content);
                i++;
            }

            catsNameArray = context.getResources().getStringArray(R.array.expense_cats);
            catsIDArray = context.getResources().getIntArray(R.array.expense_cats_ids);
            i = 0;
            for(String catName: catsNameArray ){
                ContentValues content = new ContentValues();
                content.put(ExpenseCategoryColumns._ID, catsIDArray[i]);
                content.put(ExpenseCategoryColumns.COLUMN_NAME, catName);
                content.put(ExpenseCategoryColumns.COLUMN_COLOR, colors[catsIDArray[i]]);
                db.insert(ExpenseCategoryColumns.TABLE_NAME, null, content);
                i++;
            }

            DB_PATH = db.getPath();
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
