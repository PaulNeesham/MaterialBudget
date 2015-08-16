package com.flatlyapps.materialbudget.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.flatlyapps.materialbudget.MainActivity;
import com.flatlyapps.materialbudget.R;
import com.flatlyapps.materialbudget.data.Account;
import com.flatlyapps.materialbudget.data.Recur;
import com.flatlyapps.materialbudget.utilMethods.UtilMethods;

import org.joda.time.DateTime;

import java.util.List;

public class RecurringService extends Service {

    // Check interval: every 24 hours

    private CheckForUpdatesTask mTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        (mTask = new CheckForUpdatesTask()).execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
            mTask.cancel(false);
        }
    }

    public static void schedule(Context context) {

        final Intent intent = new Intent(context, RecurringService.class);
        final PendingIntent pendingMidnight = PendingIntent.getService(context, 0, intent, 0);

        final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingMidnight);

        DateTime next = UtilMethods.nextBillOrMidnight(context);
        Log.d("RecurringService", "next check: " + next.toString());
        alarm.set(AlarmManager.RTC, next.getMillis(), pendingMidnight);

    }

    private class CheckForUpdatesTask extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
        }

        public Void doInBackground(Void... params) {

            List<Recur> checkForUpdates = UtilMethods.checkForRecurUpdates(RecurringService.this);
            Log.d("RecurringService", "new recurs: " + checkForUpdates.size());

            Intent intentUpdate = new Intent();
            intentUpdate.setAction(MainActivity.UPDATE_INTENT);
            sendBroadcast(intentUpdate);
            if (checkForUpdates.size() > 0) {

                NotificationCompat.Builder mBuilder;
                if (checkForUpdates.size() == 1) {

                    Recur recurData = checkForUpdates.get(0);
                    if (!recurData.isTransfer()) {
                        String incomeOrExpense;
                        String category;
                        if(recurData.isExpenseCategory()){
                            incomeOrExpense = "Expense: ";
                            category = recurData.getExpenseCategory().getName();
                        } else {
                            incomeOrExpense = "Income: ";
                            category = recurData.getIncomeCategory().getName();
                        }


                        mBuilder = new NotificationCompat.Builder(RecurringService.this)
                                .setSmallIcon(R.drawable.ic_noti)
                                .setContentTitle(incomeOrExpense + recurData.getName() + "  " + UtilMethods.getCostToString(RecurringService.this, recurData.getCost()))
                                .setContentText("Category: " + category);
                    } else {
                        Account fromAccount = recurData.getTransferFrom();
                        Account toAccount = recurData.getTransferTo();
                        String transferTitle = "Transfer: " + fromAccount.getName() + " to " + toAccount.getName();
                        String amount = UtilMethods.getCostToString(getApplicationContext(), recurData.getCost());

                        mBuilder = new NotificationCompat.Builder(RecurringService.this)
                                .setSmallIcon(R.drawable.ic_noti)
                                .setContentTitle(transferTitle)
                                .setContentText("Amount: " + amount);
                    }
                } else {

                    String message = "";
                    String subMessage = "";
                    int numberOfIncomes = 0;
                    int numberOfExpenses = 0;
                    int numberOfTransfers = 0;
                    long totalIncome = 0;
                    long totalTransfer = 0;
                    long totalExpense = 0;
                    for (Recur tran : checkForUpdates) {
                        if (!tran.isTransfer()) {
                            if (tran.isExpenseCategory()) {
                                numberOfExpenses++;
                                totalExpense += tran.getCost();
                            } else {
                                numberOfIncomes++;
                                totalIncome += tran.getCost();
                            }
                        } else {
                            numberOfTransfers++;
                            totalTransfer += tran.getCost();
                        }
                    }
                    if (numberOfExpenses > 0) {
                        if (numberOfExpenses == 1) {
                            message += "1 Expense, ";
                        } else {
                            message += numberOfExpenses + " Expenses, ";
                        }
                        subMessage += "Expense Total: " + UtilMethods.getCostToString(RecurringService.this, totalExpense);
                    }
                    if (numberOfIncomes > 0) {
                        if (!subMessage.isEmpty()) {
                            subMessage += ", ";
                        }
                        if (numberOfIncomes == 1) {
                            message += "1 Income, ";
                        } else {
                            message += numberOfIncomes + " Incomes, ";
                        }
                        subMessage += "Income Total: " + UtilMethods.getCostToString(RecurringService.this, totalIncome);
                    }
                    if (numberOfTransfers > 0) {
                        if (!subMessage.isEmpty()) {
                            subMessage += ", ";
                        }
                        if (numberOfTransfers == 1) {
                            message += "1 Transfer, ";
                        } else {
                            message += numberOfTransfers + " Transfers, ";
                        }
                        subMessage += "Transfer Total: " + UtilMethods.getCostToString(RecurringService.this, totalTransfer);
                    }
                    mBuilder = new NotificationCompat.Builder(RecurringService.this)
                            .setSmallIcon(R.drawable.ic_noti)
                            .setContentTitle(message)
                            .setContentText(subMessage);
                }
                // Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(RecurringService.this, MainActivity.class);

                // The stack builder object will contain an artificial back stack for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(RecurringService.this);
                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(MainActivity.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                        0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // mId allows you to update the notification later on.
                mNotificationManager.notify(5, mBuilder.build());
            }
            schedule(getApplicationContext());

            return null;
        }

        @Override
        public void onPostExecute(Void aVoid) {
            stopSelf();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}