package com.flatlyapps.materialbudget.utilMethods;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.flatlyapps.materialbudget.data.Account;
import com.flatlyapps.materialbudget.data.Currency;
import com.flatlyapps.materialbudget.data.Data;
import com.flatlyapps.materialbudget.data.ExpenseCategory;
import com.flatlyapps.materialbudget.data.ExpenseCategoryBudget;
import com.flatlyapps.materialbudget.data.IncomeCategory;
import com.flatlyapps.materialbudget.data.Recur;
import com.google.ical.values.RRule;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.flatlyapps.materialbudget.utilMethods.TransactionDataContract.AccountColumns;
import static com.flatlyapps.materialbudget.utilMethods.TransactionDataContract.CurrencyColumns;
import static com.flatlyapps.materialbudget.utilMethods.TransactionDataContract.CurrentCurrencyColumns;
import static com.flatlyapps.materialbudget.utilMethods.TransactionDataContract.DataColumns;
import static com.flatlyapps.materialbudget.utilMethods.TransactionDataContract.DefaultAccountColumns;
import static com.flatlyapps.materialbudget.utilMethods.TransactionDataContract.ExpenseCategoryBudgetColumns;
import static com.flatlyapps.materialbudget.utilMethods.TransactionDataContract.ExpenseCategoryColumns;
import static com.flatlyapps.materialbudget.utilMethods.TransactionDataContract.IncomeCategoryColumns;
import static com.flatlyapps.materialbudget.utilMethods.TransactionDataContract.RecurColumns;

/**
 * Created by PaulN on 12/08/2015.
 */
class DatabaseQueryHandler {

    private DatabaseQueryHandler(){}

    private static final String TAG = "DataBase";
    private static final Lock lock = new ReentrantLock();

    private static Map<Long, Data> data_cache;
    private static Map<Long, Recur> recur_cache;
    private static Map<Long, Account> account_cache;
    private static Map<Long, ExpenseCategory> expense_cache;
    private static Map<Long, IncomeCategory> income_cache;
    private static Map<Long, Currency> currency_cache;
    private static Map<Long, ExpenseCategoryBudget> category_budget_cache;
    private static Account default_account_cache;
    private static Currency current_currency_cache;

    private static SQLiteDatabase getReadableDatabase(Context context) {TransactionDataContract contract = new TransactionDataContract();
        TransactionDataContract.TransactionDataDbHelper mDbHelper = contract.new TransactionDataDbHelper(context);
        return mDbHelper.getReadableDatabase();
    }

    private static SQLiteDatabase getWritableDatabase(Context context) {TransactionDataContract contract = new TransactionDataContract();
        TransactionDataContract.TransactionDataDbHelper mDbHelper = contract.new TransactionDataDbHelper(context);
        return mDbHelper.getWritableDatabase();
    }

    private static DateTime getDate(Cursor cursor, int index){
        return new DateTime().withMillis(cursor.getInt(index));
    }

    public static Account getDefaultAccount(Context context) {
        lock.lock();
        try {
            if (default_account_cache == null) {
                SQLiteDatabase db = getReadableDatabase(context);
                String[] selectionArgs = new String[0];
                String sql = "SELECT * from " + DefaultAccountColumns.TABLE_NAME;
                Cursor myCursor = db.rawQuery(sql, selectionArgs);

                if (myCursor.moveToFirst()) {
                    Long accountId = myCursor.getLong(
                            myCursor.getColumnIndexOrThrow(DefaultAccountColumns.COLUMN_ACCOUNT_ID));
                    default_account_cache = getAccount(context, accountId);
                }

                myCursor.close();
                db.close();
            }
            return default_account_cache;
        } finally {
            lock.unlock();
        }
    }

    public static Account changeDefaultAccount(Context context, Long accountId) {
        lock.lock();
        try {
            SQLiteDatabase db = getWritableDatabase(context);
            ContentValues values = new ContentValues();

            values.put(DefaultAccountColumns.COLUMN_ACCOUNT_ID, accountId);
            // Insert the new row, returning the primary key value of the new row
            db.update(DefaultAccountColumns.TABLE_NAME, values, null, null);
            db.close();
            default_account_cache = getAccount(context, accountId);
            return default_account_cache;
        } finally {
            lock.unlock();
        }
    }

    public static Currency getCurrentCurrency(Context context) {
        lock.lock();
        try {
            if (current_currency_cache == null) {
                SQLiteDatabase db = getReadableDatabase(context);
                String[] selectionArgs = new String[0];
                String sql = "SELECT * from " + CurrentCurrencyColumns.TABLE_NAME;
                Cursor myCursor = db.rawQuery(sql, selectionArgs);

                if (myCursor.moveToFirst()) {
                    Long currencyId = myCursor.getLong(
                            myCursor.getColumnIndexOrThrow(CurrentCurrencyColumns.COLUMN_CURRENCY_ID));
                    current_currency_cache = getCurrency(context, currencyId);
                }

                myCursor.close();
                db.close();
            }
            return current_currency_cache;
        } finally {
            lock.unlock();
        }
    }

    public static Currency changeCurrentCurrency(Context context, Long currencyId) {
        lock.lock();
        try {
            SQLiteDatabase db = getWritableDatabase(context);
            ContentValues values = new ContentValues();

            values.put(CurrentCurrencyColumns.COLUMN_CURRENCY_ID, currencyId);
            // Insert the new row, returning the primary key value of the new row
            db.update(CurrentCurrencyColumns.TABLE_NAME, values, null, null);
            db.close();
            current_currency_cache = getCurrency(context, currencyId);
            return current_currency_cache;
        } finally {
            lock.unlock();
        }
    }

    public static Map<Long, IncomeCategory> getIncomeCategories(Context context) {
        lock.lock();
        try {
            if(income_cache == null) {
                income_cache = new HashMap<>();
                SQLiteDatabase db = getReadableDatabase(context);
                String[] selectionArgs = new String[0];
                String sql = "SELECT * from " +  IncomeCategoryColumns.TABLE_NAME;
                Cursor myCursor = db.rawQuery(sql, selectionArgs);

                if (myCursor.moveToFirst()) {
                    do {
                        Long id = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(IncomeCategoryColumns._ID));
                        String name = myCursor.getString(
                                myCursor.getColumnIndexOrThrow(IncomeCategoryColumns.COLUMN_NAME));
                        Integer color = myCursor.getInt(
                                myCursor.getColumnIndexOrThrow(IncomeCategoryColumns.COLUMN_COLOR));
                        income_cache.put(id, new IncomeCategory(id, name, color));
                    } while (myCursor.moveToNext());
                }

                myCursor.close();
                db.close();
            }
            return income_cache;
        } finally {
            lock.unlock();
        }
    }

    public static IncomeCategory getIncomeCategory(Context context, Long incomeCategoryId) {
        lock.lock();
        try {
            return getIncomeCategories(context).get(incomeCategoryId);
        } finally {
            lock.unlock();
        }
    }

    public static IncomeCategory getIncomeCategory(Context context, String name) {
        lock.lock();
        try {
            for(IncomeCategory incomeCategory : getIncomeCategories(context).values()) {
                if(incomeCategory.getName().equalsIgnoreCase(name)) {
                    return incomeCategory;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static void addIncomeCategory(Context context, IncomeCategory category) throws Exception {
        lock.lock();
        try {
            IncomeCategory incomeCategory = getIncomeCategory(context, category.getName());

            if (incomeCategory == null) {
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(IncomeCategoryColumns.COLUMN_NAME, category.getName());
                values.put(IncomeCategoryColumns.COLUMN_COLOR, category.getColor());
                Long id = db.insert(IncomeCategoryColumns.TABLE_NAME, "NULL", values);
                category.setId(id);
                income_cache.put(id, category);
                db.close();
            } else {
                throw new Exception(category.getName() + " IncomeCategory already exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static IncomeCategory editIncomeCategory(Context context, IncomeCategory category) throws Exception {
        lock.lock();
        try {
            IncomeCategory incomeCategory = getIncomeCategory(context, category.getId());

            if (incomeCategory != null) {
                incomeCategory.setColor(category.getColor());
                incomeCategory.setName(category.getName());
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(IncomeCategoryColumns._ID, incomeCategory.getId());
                values.put(IncomeCategoryColumns.COLUMN_COLOR, category.getName());
                values.put(IncomeCategoryColumns.COLUMN_NAME, category.getColor());
                // Insert the new row, returning the primary key value of the new row
                db.update(IncomeCategoryColumns.TABLE_NAME, values, null, null);
                db.close();
                return incomeCategory;
            } else {
                throw new Exception(category.getName() + " IncomeCategory doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static void removeIncomeCategory(Context context, IncomeCategory category) throws Exception {
        lock.lock();
        try {
            IncomeCategory incomeCategory = getIncomeCategory(context, category.getId());

            if (incomeCategory != null) {
                SQLiteDatabase db = getWritableDatabase(context);
                db.delete(IncomeCategoryColumns.TABLE_NAME, IncomeCategoryColumns._ID + "=" + category.getId(), null);
                db.close();
            } else {
                throw new Exception(category.getName() + " IncomeCategory doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static Map<Long,ExpenseCategory> getExpenseCategories(Context context) {
        lock.lock();
        try {
            if(expense_cache == null) {
                expense_cache = new HashMap<>();
                SQLiteDatabase db = getReadableDatabase(context);
                String[] selectionArgs = new String[0];
                String sql = "SELECT * from " +  ExpenseCategoryColumns.TABLE_NAME;
                Cursor myCursor = db.rawQuery(sql, selectionArgs);

                if (myCursor.moveToFirst()) {
                    do {
                        Long id = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(ExpenseCategoryColumns._ID));
                        String name = myCursor.getString(
                                myCursor.getColumnIndexOrThrow(ExpenseCategoryColumns.COLUMN_NAME));
                        Integer color = myCursor.getInt(
                                myCursor.getColumnIndexOrThrow(ExpenseCategoryColumns.COLUMN_COLOR));
                        expense_cache.put(id, new ExpenseCategory(id, name, color));
                    } while (myCursor.moveToNext());
                }

                myCursor.close();
                db.close();
            }
            return expense_cache;
        } finally {
            lock.unlock();
        }
    }

    public static ExpenseCategory getExpenseCategory(Context context, Long expenseCategoryId) {
        lock.lock();
        try {
            return getExpenseCategories(context).get(expenseCategoryId);
        } finally {
            lock.unlock();
        }
    }

    public static ExpenseCategory getExpenseCategory(Context context, String name) {
        lock.lock();
        try {
            for(ExpenseCategory expenseCategory : getExpenseCategories(context).values()) {
                if(expenseCategory.getName().equalsIgnoreCase(name)) {
                    return expenseCategory;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static void addExpenseCategory(Context context, ExpenseCategory category) throws Exception {
        lock.lock();
        try {
            ExpenseCategory expenseCategory = getExpenseCategory(context, category.getName());

            if (expenseCategory == null) {
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(IncomeCategoryColumns.COLUMN_NAME, category.getName());
                values.put(IncomeCategoryColumns.COLUMN_COLOR, category.getColor());
                Long id = db.insert(IncomeCategoryColumns.TABLE_NAME, "NULL", values);
                category.setId(id);
                expense_cache.put(id, category);
                db.close();
            } else {
                throw new Exception(category.getName() + " ExpenseCategory already exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static ExpenseCategory editExpenseCategory(Context context, ExpenseCategory category) throws Exception {
        lock.lock();
        try {
            ExpenseCategory expenseCategory = getExpenseCategory(context, category.getId());

            if (expenseCategory != null) {
                expenseCategory.setColor(category.getColor());
                expenseCategory.setName(category.getName());
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(ExpenseCategoryColumns._ID, expenseCategory.getId());
                values.put(ExpenseCategoryColumns.COLUMN_COLOR, category.getName());
                values.put(ExpenseCategoryColumns.COLUMN_NAME, category.getColor());
                // Insert the new row, returning the primary key value of the new row
                db.update(ExpenseCategoryColumns.TABLE_NAME, values, null, null);
                db.close();
                return expenseCategory;
            } else {
                throw new Exception(category.getName() + " ExpenseCategory doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static void removeExpenseCategory(Context context, ExpenseCategory category) throws Exception {
        lock.lock();
        try {
            ExpenseCategory expenseCategory = getExpenseCategory(context, category.getId());

            if (expenseCategory != null) {
                SQLiteDatabase db = getWritableDatabase(context);
                db.delete(ExpenseCategoryColumns.TABLE_NAME, ExpenseCategoryColumns._ID + "=" + category.getId(), null);
                db.close();
            } else {
                throw new Exception(category.getName() + " ExpenseCategory doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static Map<Long, Account> getAccounts(Context context) {
        lock.lock();
        try {
            if(account_cache == null) {
                account_cache = new HashMap<>();
                SQLiteDatabase db = getReadableDatabase(context);
                String[] selectionArgs = new String[0];
                String sql = "SELECT * from " +  AccountColumns.TABLE_NAME;
                Cursor myCursor = db.rawQuery(sql, selectionArgs);

                if (myCursor.moveToFirst()) {
                    do {
                        Long id = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(AccountColumns._ID));
                        String name = myCursor.getString(
                                myCursor.getColumnIndexOrThrow(AccountColumns.COLUMN_NAME));
                        Long budget = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(AccountColumns.COLUMN_BUDGET));
                        Boolean budgetUseIncome = myCursor.getInt(
                                myCursor.getColumnIndexOrThrow(AccountColumns.COLUMN_USE_INCOME)) == 1;
                        Long initialFunds = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(AccountColumns.COLUMN_INITIAL_FUNDS));

                        account_cache.put(id, new Account(id, name, budget, initialFunds, budgetUseIncome));
                    } while (myCursor.moveToNext());
                }

                myCursor.close();
                db.close();
            }
            return account_cache;
        } finally {
            lock.unlock();
        }
    }

    public static Account getAccount(Context context, Long accountId) {
        lock.lock();
        try {
            return getAccounts(context).get(accountId);
        } finally {
            lock.unlock();
        }
    }

    public static Account getAccount(Context context, String name) {
        lock.lock();
        try {
            for(Account account : getAccounts(context).values()) {
                if(account.getName().equalsIgnoreCase(name)) {
                    return account;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static void addAccount(Context context, Account account) throws Exception {
        lock.lock();
        try {
            Account currentAccount = getAccount(context, account.getName());

            if (currentAccount == null) {
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(AccountColumns.COLUMN_NAME, account.getName());
                values.put(AccountColumns.COLUMN_BUDGET, account.getBudget());
                values.put(AccountColumns.COLUMN_INITIAL_FUNDS, account.getInitialFunds());
                values.put(AccountColumns.COLUMN_USE_INCOME, account.isUseIncome());
                Long id = db.insert(AccountColumns.TABLE_NAME, "NULL", values);
                account.setId(id);
                account_cache.put(id, account);
                db.close();
            } else {
                throw new Exception(account.getName() + " Account already exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static Account editAccount(Context context, Account account) throws Exception {
        lock.lock();
        try {
            Account currentAccount = getAccount(context, account.getId());

            if (currentAccount != null) {
                currentAccount.setName(account.getName());
                currentAccount.setBudget(account.getBudget());
                currentAccount.setInitialFunds(account.getInitialFunds());
                currentAccount.setUseIncome(account.isUseIncome());
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(AccountColumns._ID, currentAccount.getId());
                values.put(AccountColumns.COLUMN_NAME, account.getName());
                values.put(AccountColumns.COLUMN_BUDGET, account.getBudget());
                values.put(AccountColumns.COLUMN_INITIAL_FUNDS, account.getInitialFunds());
                values.put(AccountColumns.COLUMN_USE_INCOME, account.isUseIncome());
                // Insert the new row, returning the primary key value of the new row
                db.update(ExpenseCategoryColumns.TABLE_NAME, values, null, null);
                db.close();
                return currentAccount;
            } else {
                throw new Exception(account.getName() + " Account doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static void removeAccount(Context context, Account account) throws Exception {
        lock.lock();
        try {
            Account currentAccount = getAccount(context, account.getId());

            if (currentAccount != null) {
                SQLiteDatabase db = getWritableDatabase(context);
                db.delete(AccountColumns.TABLE_NAME, AccountColumns._ID + "=" + account.getId(), null);
                db.close();
            } else {
                throw new Exception(account.getName() + " Account doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static Map<Long, Currency> getCurrencies(Context context) {
        lock.lock();
        try {
            if(currency_cache == null) {
                currency_cache = new HashMap<>();
                SQLiteDatabase db = getReadableDatabase(context);
                String[] selectionArgs = new String[0];
                String sql = "SELECT * from " +  CurrencyColumns.TABLE_NAME;
                Cursor myCursor = db.rawQuery(sql, selectionArgs);

                if (myCursor.moveToFirst()) {
                    do {
                        Long id = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(CurrencyColumns._ID));
                        String name = myCursor.getString(
                                myCursor.getColumnIndexOrThrow(CurrencyColumns.COLUMN_NAME));
                        String code = myCursor.getString(
                                myCursor.getColumnIndexOrThrow(CurrencyColumns.COLUMN_CODE));
                        String symbol = myCursor.getString(
                                myCursor.getColumnIndexOrThrow(CurrencyColumns.COLUMN_SYMBOL));
                        String format = myCursor.getString(
                                myCursor.getColumnIndexOrThrow(CurrencyColumns.COLUMN_FORMAT));

                        currency_cache.put(id, new Currency(id, name, symbol, code, format));
                    } while (myCursor.moveToNext());
                }

                myCursor.close();
                db.close();
            }
            return currency_cache;
        } finally {
            lock.unlock();
        }
    }

    public static Currency getCurrency(Context context, Long currencyId) {
        lock.lock();
        try {
            return getCurrencies(context).get(currencyId);
        } finally {
            lock.unlock();
        }
    }

    public static Currency getCurrency(Context context, String name) {
        lock.lock();
        try {
            for(Currency currency : getCurrencies(context).values()) {
                if(currency.getName().equalsIgnoreCase(name)) {
                    return currency;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static void addCurrency(Context context, Currency currency) throws Exception {
        lock.lock();
        try {
            Currency currentCurrency = getCurrency(context, currency.getName());

            if (currentCurrency == null) {
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(CurrencyColumns.COLUMN_NAME, currency.getName());
                values.put(CurrencyColumns.COLUMN_CODE, currency.getCode());
                values.put(CurrencyColumns.COLUMN_FORMAT, currency.getFormat());
                values.put(CurrencyColumns.COLUMN_SYMBOL, currency.getSymbol());
                Long id = db.insert(CurrencyColumns.TABLE_NAME, "NULL", values);
                currency.setId(id);
                currency_cache.put(id, currency);
                db.close();
            } else {
                throw new Exception(currency.getName() + " Currency already exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static Currency editCurrency(Context context, Currency currency) throws Exception {
        lock.lock();
        try {
            Currency currentCurrency = getCurrency(context, currency.getId());

            if (currentCurrency != null) {
                currentCurrency.setName(currency.getName());
                currentCurrency.setCode(currency.getCode());
                currentCurrency.setFormat(currency.getFormat());
                currentCurrency.setSymbol(currency.getSymbol());
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(CurrencyColumns._ID, currentCurrency.getId());
                values.put(CurrencyColumns.COLUMN_NAME, currentCurrency.getName());
                values.put(CurrencyColumns.COLUMN_CODE, currentCurrency.getCode());
                values.put(CurrencyColumns.COLUMN_FORMAT, currentCurrency.getFormat());
                values.put(CurrencyColumns.COLUMN_SYMBOL, currentCurrency.getSymbol());
                // Insert the new row, returning the primary key value of the new row
                db.update(CurrencyColumns.TABLE_NAME, values, null, null);
                db.close();
                return currentCurrency;
            } else {
                throw new Exception(currency.getName() + " Account doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static void removeCurrency(Context context, Currency currency) throws Exception {
        lock.lock();
        try {
            Currency currentCurrency = getCurrency(context, currency.getId());

            if (currentCurrency != null) {
                SQLiteDatabase db = getWritableDatabase(context);
                db.delete(CurrencyColumns.TABLE_NAME, CurrencyColumns._ID + "=" + currency.getId(), null);
                db.close();
            } else {
                throw new Exception(currency.getName() + " Currency doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static Map<Long, Data> getAllData(Context context) {
        lock.lock();
        try {
            if(data_cache == null) {
                data_cache = new HashMap<>();
                SQLiteDatabase db = getReadableDatabase(context);
                String[] selectionArgs = new String[0];
                String sql = "SELECT * from " +  DataColumns.TABLE_NAME;
                Cursor myCursor = db.rawQuery(sql, selectionArgs);

                if (myCursor.moveToFirst()) {
                    do {
                        Long id = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(DataColumns._ID));
                        String name = myCursor.getString(
                                myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_NAME));
                        Long accountId = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_ACCOUNT_ID));
                        Account account = getAccount(context, accountId);
                        Long cost = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_COST));
                        DateTime time = getDate(myCursor, myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_TIME));
                        Boolean isExpense = myCursor.getInt(
                                myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_EXPENSE)) == 1;
                        Boolean isIncome = myCursor.getInt(
                                myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_INCOME)) == 1;
                        IncomeCategory incomeCategory = null;
                        ExpenseCategory expenseCategory = null;
                        if (isIncome) {
                            Long incomeCategoryId = myCursor.getLong(
                                    myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_INCOME_CATEGORY_ID));
                            incomeCategory = getIncomeCategory(context, incomeCategoryId);
                        } else if (isExpense) {
                            Long expenseCategoryId = myCursor.getLong(
                                    myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_EXPENSE_CATEGORY_ID));
                            expenseCategory = getExpenseCategory(context, expenseCategoryId);
                        }

                        Boolean isTransfer = myCursor.getInt(
                                myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_TRANSFER)) == 1;
                        Account transferTo = null;
                        Account transferFrom = null;
                        if (isTransfer) {
                            Long transferToId = myCursor.getLong(
                                    myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_TRANSFER_TO));
                            transferTo = getAccount(context, transferToId);

                            Long transferFromId = myCursor.getLong(
                                    myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_TRANSFER_FROM));
                            transferFrom = getAccount(context, transferFromId);
                        }

                        Boolean isRecur = myCursor.getInt(
                                myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_RECUR)) == 1;
                        Recur recur = null;
                        if (isRecur) {
                            Long recurId = myCursor.getLong(
                                    myCursor.getColumnIndexOrThrow(DataColumns.COLUMN_RECUR_ID));
                            recur = getRecur(context, recurId);
                        }



                        data_cache.put(id, new Data(id, name, account, cost, incomeCategory, expenseCategory, time, recur, transferTo, transferFrom));
                    } while (myCursor.moveToNext());
                }

                myCursor.close();
                db.close();
            }
            return data_cache;
        } finally {
            lock.unlock();
        }
    }

    public static Data getData(Context context, Long dataId) {
        lock.lock();
        try {
            return getAllData(context).get(dataId);
        } finally {
            lock.unlock();
        }
    }

    public static void addData(Context context, Data data) {
        lock.lock();
        try {
            SQLiteDatabase db = getWritableDatabase(context);
            ContentValues values = new ContentValues();
            values.put(DataColumns.COLUMN_NAME, data.getName());
            values.put(DataColumns.COLUMN_ACCOUNT_ID, data.getAccountId());
            values.put(DataColumns.COLUMN_COST, data.getCost());
            values.put(DataColumns.COLUMN_EXPENSE, data.isExpenseCategory());
            values.put(DataColumns.COLUMN_EXPENSE_CATEGORY_ID, data.getExpenseCategoryId());
            values.put(DataColumns.COLUMN_INCOME, data.isIncomeCategory());
            values.put(DataColumns.COLUMN_INCOME_CATEGORY_ID,  data.getIncomeCategoryId());
            values.put(DataColumns.COLUMN_RECUR, data.isRecur());
            values.put(DataColumns.COLUMN_RECUR_ID, data.getRecurId());
            values.put(DataColumns.COLUMN_TIME, data.getTime().getMillis());
            values.put(DataColumns.COLUMN_TRANSFER, data.isTransfer());
            values.put(DataColumns.COLUMN_TRANSFER_FROM, data.getTransferFromId());
            values.put(DataColumns.COLUMN_TRANSFER_TO, data.getTransferToId());

            Long id = db.insert(DataColumns.TABLE_NAME, "NULL", values);
            data.setId(id);
            data_cache.put(id, data);
            db.close();
        } finally {
            lock.unlock();
        }
    }

    public static Data editData(Context context, Data data) throws Exception {
        lock.lock();
        try {
            Data currentData = getData(context, data.getId());

            if (currentData != null) {
                currentData.setName(data.getName());
                currentData.setAccount(data.getAccount());
                currentData.setCost(data.getCost());
                currentData.setExpenseCategory(data.getExpenseCategory());
                currentData.setIncomeCategory(data.getIncomeCategory());
                currentData.setRecur(data.getRecur());
                currentData.setTime(data.getTime());
                currentData.setTransferFrom(data.getTransferFrom());
                currentData.setTransferTo(data.getTransferTo());
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(DataColumns._ID, data.getId());
                values.put(DataColumns.COLUMN_NAME, data.getName());
                values.put(DataColumns.COLUMN_ACCOUNT_ID, data.getAccountId());
                values.put(DataColumns.COLUMN_COST, data.getCost());
                values.put(DataColumns.COLUMN_EXPENSE, data.isExpenseCategory());
                values.put(DataColumns.COLUMN_EXPENSE_CATEGORY_ID, data.getExpenseCategoryId());
                values.put(DataColumns.COLUMN_INCOME, data.isIncomeCategory());
                values.put(DataColumns.COLUMN_INCOME_CATEGORY_ID,  data.getIncomeCategoryId());
                values.put(DataColumns.COLUMN_RECUR, data.isRecur());
                values.put(DataColumns.COLUMN_RECUR_ID, data.getRecurId());
                values.put(DataColumns.COLUMN_TIME, data.getTime().getMillis());
                values.put(DataColumns.COLUMN_TRANSFER, data.isTransfer());
                values.put(DataColumns.COLUMN_TRANSFER_FROM, data.getTransferFromId());
                values.put(DataColumns.COLUMN_TRANSFER_TO, data.getTransferToId());
                // Insert the new row, returning the primary key value of the new row
                db.update(DataColumns.TABLE_NAME, values, null, null);
                db.close();
                return data;
            } else {
                throw new Exception(data.getName() + " Data doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static void removeData(Context context, Data data) throws Exception {
        lock.lock();
        try {
            Data currentData = getData(context, data.getId());

            if (currentData != null) {
                SQLiteDatabase db = getWritableDatabase(context);
                db.delete(DataColumns.TABLE_NAME, DataColumns._ID + "=" + data.getId(), null);
                db.close();
            } else {
                throw new Exception(data.getName() + " Data doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }


    public static Map<Long,Recur> getAllRecur(Context context) {
        lock.lock();
        try {
            if(recur_cache == null) {
                recur_cache = new HashMap<>();
                SQLiteDatabase db = getReadableDatabase(context);
                String[] selectionArgs = new String[0];
                String sql = "SELECT * from " +  RecurColumns.TABLE_NAME;
                Cursor myCursor = db.rawQuery(sql, selectionArgs);

                if (myCursor.moveToFirst()) {
                    do {
                        Long id = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(RecurColumns._ID));
                        String name = myCursor.getString(
                                myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_NAME));
                        Long accountId = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_ACCOUNT_ID));
                        Account account = getAccount(context, accountId);
                        Long cost = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_COST));
                        DateTime startDate = getDate(myCursor, myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_START_DATE));
                        Boolean isExpense = myCursor.getInt(
                                myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_EXPENSE)) == 1;
                        Boolean isIncome = myCursor.getInt(
                                myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_INCOME)) == 1;
                        IncomeCategory incomeCategory = null;
                        ExpenseCategory expenseCategory = null;
                        if (isIncome) {
                            Long incomeCategoryId = myCursor.getLong(
                                    myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_INCOME_CATEGORY_ID));
                            incomeCategory = getIncomeCategory(context, incomeCategoryId);
                        } else if (isExpense) {
                            Long expenseCategoryId = myCursor.getLong(
                                    myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_EXPENSE_CATEGORY_ID));
                            expenseCategory = getExpenseCategory(context, expenseCategoryId);
                        }

                        Boolean isTransfer = myCursor.getInt(
                                myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_TRANSFER)) == 1;
                        Account transferTo = null;
                        Account transferFrom = null;
                        if (isTransfer) {
                            Long transferToId = myCursor.getLong(
                                    myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_TRANSFER_TO));
                            transferTo = getAccount(context, transferToId);

                            Long transferFromId = myCursor.getLong(
                                    myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_TRANSFER_FROM));
                            transferFrom = getAccount(context, transferFromId);
                        }

                        String rRuleString = myCursor.getString(
                                myCursor.getColumnIndexOrThrow(RecurColumns.COLUMN_RRULE));
                        RRule rRule = null;
                        try {
                            rRule = new RRule(rRuleString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        recur_cache.put(id, new Recur(id, name, account, cost, incomeCategory, expenseCategory, transferTo, transferFrom, rRule, startDate));
                    } while (myCursor.moveToNext());
                }

                myCursor.close();
                db.close();
            }
            return recur_cache;
        } finally {
            lock.unlock();
        }
    }

    public static Recur getRecur(Context context, Long recurId) {
        lock.lock();
        try {
            return getAllRecur(context).get(recurId);
        } finally {
            lock.unlock();
        }
    }

    public static Recur getRecur(Context context, String name) {
        lock.lock();
        try {
            for(Recur recur : getAllRecur(context).values()) {
                if(recur.getName().equalsIgnoreCase(name)) {
                    return recur;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static void addRecur(Context context, Recur recur) throws Exception {
        lock.lock();
        try {
            Recur currentRecur = getRecur(context, recur.getName());

            if (currentRecur == null) {
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(RecurColumns.COLUMN_NAME, recur.getName());
                values.put(RecurColumns.COLUMN_ACCOUNT_ID, recur.getAccountId());
                values.put(RecurColumns.COLUMN_COST, recur.getCost());
                values.put(RecurColumns.COLUMN_EXPENSE, recur.isExpenseCategory());
                values.put(RecurColumns.COLUMN_EXPENSE_CATEGORY_ID, recur.getExpenseCategoryId());
                values.put(RecurColumns.COLUMN_INCOME, recur.isIncomeCategory());
                values.put(RecurColumns.COLUMN_INCOME_CATEGORY_ID,  recur.getIncomeCategoryId());
                values.put(RecurColumns.COLUMN_START_DATE, recur.getStartDate().getMillis());
                values.put(RecurColumns.COLUMN_TRANSFER, recur.isTransfer());
                values.put(RecurColumns.COLUMN_TRANSFER_FROM, recur.getTransferFromId());
                values.put(RecurColumns.COLUMN_TRANSFER_TO, recur.getTransferToId());
                values.put(RecurColumns.COLUMN_RRULE, recur.getRRuleIcal());
                Long id = db.insert(RecurColumns.TABLE_NAME, "NULL", values);
                recur.setId(id);
                recur_cache.put(id, recur);
                db.close();
            } else {
                throw new Exception(recur.getName() + " Recur already exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static Recur editRecur(Context context, Recur recur) throws Exception {
        lock.lock();
        try {
            Recur currentRecur = getRecur(context, recur.getId());

            if (currentRecur != null) {
                currentRecur.setName(recur.getName());
                currentRecur.setAccount(recur.getAccount());
                currentRecur.setCost(recur.getCost());
                currentRecur.setExpenseCategory(recur.getExpenseCategory());
                currentRecur.setIncomeCategory(recur.getIncomeCategory());
                currentRecur.setStartDate(recur.getStartDate());
                currentRecur.setTransferFrom(recur.getTransferFrom());
                currentRecur.setTransferTo(recur.getTransferTo());
                currentRecur.setRRule(recur.getRRule());
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(RecurColumns._ID, recur.getId());
                values.put(RecurColumns.COLUMN_NAME, recur.getName());
                values.put(RecurColumns.COLUMN_ACCOUNT_ID, recur.getAccountId());
                values.put(RecurColumns.COLUMN_COST, recur.getCost());
                values.put(RecurColumns.COLUMN_EXPENSE, recur.isExpenseCategory());
                values.put(RecurColumns.COLUMN_EXPENSE_CATEGORY_ID, recur.getExpenseCategoryId());
                values.put(RecurColumns.COLUMN_INCOME, recur.isIncomeCategory());
                values.put(RecurColumns.COLUMN_INCOME_CATEGORY_ID,  recur.getIncomeCategoryId());
                values.put(RecurColumns.COLUMN_START_DATE, recur.getStartDate().getMillis());
                values.put(RecurColumns.COLUMN_TRANSFER, recur.isTransfer());
                values.put(RecurColumns.COLUMN_TRANSFER_FROM, recur.getTransferFromId());
                values.put(RecurColumns.COLUMN_TRANSFER_TO, recur.getTransferToId());
                values.put(RecurColumns.COLUMN_RRULE, recur.getRRuleIcal());
                // Insert the new row, returning the primary key value of the new row
                db.update(RecurColumns.TABLE_NAME, values, null, null);
                db.close();
                return currentRecur;
            } else {
                throw new Exception(recur.getName() + " Recur doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static void removeRecur(Context context, Recur recur) throws Exception {
        lock.lock();
        try {
            Recur currentRecur = getRecur(context, recur.getId());

            if (currentRecur != null) {
                SQLiteDatabase db = getWritableDatabase(context);
                db.delete(RecurColumns.TABLE_NAME, RecurColumns._ID + "=" + recur.getId(), null);
                db.close();
            } else {
                throw new Exception(recur.getName() + " Recur doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }


    public static Map<Long, ExpenseCategoryBudget> getExpenseCategoryBudgets(Context context) {
        lock.lock();
        try {
            if(category_budget_cache == null) {
                category_budget_cache = new HashMap<>();
                SQLiteDatabase db = getReadableDatabase(context);
                String[] selectionArgs = new String[0];
                String sql = "SELECT * from " +  ExpenseCategoryBudgetColumns.TABLE_NAME;
                Cursor myCursor = db.rawQuery(sql, selectionArgs);

                if (myCursor.moveToFirst()) {
                    do {
                        Long id = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(ExpenseCategoryBudgetColumns._ID));
                        Long accountId = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(ExpenseCategoryBudgetColumns.COLUMN_ACCOUNT_ID));
                        Account account = getAccount(context, accountId);
                        Integer percent = myCursor.getInt(
                                myCursor.getColumnIndexOrThrow(ExpenseCategoryBudgetColumns.COLUMN_PERCENT));
                        Long expenseCategoryId = myCursor.getLong(
                                myCursor.getColumnIndexOrThrow(ExpenseCategoryBudgetColumns.COLUMN_EXPENSE_CATEGORY_ID));
                        ExpenseCategory expenseCategory = getExpenseCategory(context, expenseCategoryId);
                        category_budget_cache.put(id, new ExpenseCategoryBudget(id, account, expenseCategory, percent));
                    } while (myCursor.moveToNext());
                }

                myCursor.close();
                db.close();
            }
            return category_budget_cache;
        } finally {
            lock.unlock();
        }
    }

    public static ExpenseCategoryBudget getExpenseCategoryBudget(Context context, Long expenseCategoryBudgetId) {
        lock.lock();
        try {
            return getExpenseCategoryBudgets(context).get(expenseCategoryBudgetId);
        } finally {
            lock.unlock();
        }
    }

    public static ExpenseCategoryBudget getExpenseCategoryBudget(Context context, Long accountId, Long expenseCategoryId) {
        lock.lock();
        try {
            for(ExpenseCategoryBudget expenseCategoryBudget : getExpenseCategoryBudgets(context).values()) {
                if(expenseCategoryBudget.getAccountId().equals(accountId) && expenseCategoryBudget.getExpenseCategoryId().equals(expenseCategoryId)) {
                    return expenseCategoryBudget;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static void addExpenseCategoryBudget(Context context, ExpenseCategoryBudget expenseCategoryBudget) throws Exception {
        lock.lock();
        try {
            ExpenseCategoryBudget currentExpenseCategoryBudget = getExpenseCategoryBudget(context, expenseCategoryBudget.getAccountId(), expenseCategoryBudget.getExpenseCategoryId());

            if (currentExpenseCategoryBudget == null) {
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(ExpenseCategoryBudgetColumns.COLUMN_ACCOUNT_ID, expenseCategoryBudget.getAccountId());
                values.put(ExpenseCategoryBudgetColumns.COLUMN_EXPENSE_CATEGORY_ID, expenseCategoryBudget.getExpenseCategoryId());
                values.put(ExpenseCategoryBudgetColumns.COLUMN_PERCENT, expenseCategoryBudget.getPercent());
                Long id = db.insert(ExpenseCategoryBudgetColumns.TABLE_NAME, "NULL", values);
                expenseCategoryBudget.setId(id);
                category_budget_cache.put(id, expenseCategoryBudget);
                db.close();
            } else {
                throw new Exception(expenseCategoryBudget.getAccountId()+ "-account " + expenseCategoryBudget.getExpenseCategoryId() + "-category ExpenseCategoryBudget already exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static ExpenseCategoryBudget editExpenseCategoryBudget(Context context, ExpenseCategoryBudget expenseCategoryBudget) throws Exception {
        lock.lock();
        try {
            ExpenseCategoryBudget currentExpenseCategoryBudget = getExpenseCategoryBudget(context, expenseCategoryBudget.getId());

            if (currentExpenseCategoryBudget != null) {
                currentExpenseCategoryBudget.setPercent(expenseCategoryBudget.getPercent());
                currentExpenseCategoryBudget.setAccount(expenseCategoryBudget.getAccount());
                currentExpenseCategoryBudget.setExpenseCategory(expenseCategoryBudget.getExpenseCategory());
                SQLiteDatabase db = getWritableDatabase(context);
                ContentValues values = new ContentValues();
                values.put(ExpenseCategoryBudgetColumns._ID, expenseCategoryBudget.getId());
                values.put(ExpenseCategoryBudgetColumns.COLUMN_ACCOUNT_ID, expenseCategoryBudget.getAccountId());
                values.put(ExpenseCategoryBudgetColumns.COLUMN_EXPENSE_CATEGORY_ID, expenseCategoryBudget.getExpenseCategoryId());
                values.put(ExpenseCategoryBudgetColumns.COLUMN_PERCENT, expenseCategoryBudget.getPercent());
                // Insert the new row, returning the primary key value of the new row
                db.update(ExpenseCategoryBudgetColumns.TABLE_NAME, values, null, null);
                db.close();
                return currentExpenseCategoryBudget;
            } else {
                throw new Exception(expenseCategoryBudget.getId() + " ExpenseCategoryBudget doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

    public static void removeExpenseCategoryBudget(Context context, ExpenseCategoryBudget expenseCategoryBudget) throws Exception {
        lock.lock();
        try {
            ExpenseCategoryBudget currentExpenseCategoryBudget = getExpenseCategoryBudget(context, expenseCategoryBudget.getId());

            if (currentExpenseCategoryBudget != null) {
                SQLiteDatabase db = getWritableDatabase(context);
                db.delete(ExpenseCategoryBudgetColumns.TABLE_NAME, ExpenseCategoryBudgetColumns._ID + "=" + expenseCategoryBudget.getId(), null);
                db.close();
            } else {
                throw new Exception(expenseCategoryBudget.getId() + " ExpenseCategoryBudget doesn't exists");
            }
        } finally {
            lock.unlock();
        }
    }

}
