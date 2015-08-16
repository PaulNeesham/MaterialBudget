package com.flatlyapps.materialBudget.utilMethods;

import android.content.Context;

import com.flatlyapps.materialBudget.data.Account;
import com.flatlyapps.materialBudget.data.Currency;
import com.flatlyapps.materialBudget.data.Data;
import com.flatlyapps.materialBudget.data.Recur;
import com.google.ical.compat.jodatime.DateTimeIterator;
import com.google.ical.compat.jodatime.DateTimeIteratorFactory;
import com.google.ical.iter.RecurrenceIterator;
import com.google.ical.iter.RecurrenceIteratorFactory;
import com.google.ical.values.DateTimeValue;
import com.google.ical.values.DateTimeValueImpl;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class UtilMethods {

	private final static DateTimeFormatter MONTH_NAME_AND_YEAR_FORMAT  = DateTimeFormat.forPattern("MMMM yyyy");
	private final static DateTimeFormatter DAY_AND_MONTH_FORMAT  = DateTimeFormat.forPattern("dd MMM");

	private UtilMethods(){}

	public static String getMonthNameAndYear(DateTime dateTime) {
		return MONTH_NAME_AND_YEAR_FORMAT.print(dateTime);
	}
	
	public static String getDayAndMonth(DateTime dateTime) {
		return DAY_AND_MONTH_FORMAT.print(dateTime);
	}

	public static String getCostToString(Context context, Long cost) {
		Currency currency = DatabaseQueryHandler.getCurrentCurrency(context);
		NumberFormat numberFormat = new DecimalFormat(currency.getFormat());
		return numberFormat.format(cost);
	}

	public static Long getAccountTotal(Context context, Long accountId) {
		Account account = DatabaseQueryHandler.getAccount(context, accountId);
		long total = account.getInitialFunds();
		for (Data data : DatabaseQueryHandler.getAllData(context).values()) {
			if(data.getAccount().equals(account)) {
				if (data.isIncomeCategory()) {
					total += data.getCost();
				} else if (data.isExpenseCategory()) {
					total -= data.getCost();
				}
			} else if (data.isTransfer()) {
				if (data.getTransferTo().equals(account)) {
					total += data.getCost();
				} else if (data.getTransferFrom().equals(account)) {
					total -= data.getCost();
				}
			}
		}
		return total;
	}

	public static DateTime nextBillOrMidnight(Context context) {
		DateTime nextDate = new DateTime().plusDays(1).withTimeAtStartOfDay();
		for(Recur recur : DatabaseQueryHandler.getAllRecur(context).values()){
			RecurrenceIterator recurrenceIterator = RecurrenceIteratorFactory.createRecurrenceIterator(recur.getRRule(), getDateTimeValue(DateTime.now()), DateTime.now().getZone().toTimeZone());
			DateTimeIterator iterable = DateTimeIteratorFactory.createDateTimeIterator(recurrenceIterator);
			if(iterable.hasNext()){
				DateTime dateTime = iterable.next();
				if(dateTime.isBefore(nextDate)){
					nextDate = dateTime;
				}
				break;
			}
		}
		return nextDate;
	}

	public static List<Recur> checkForRecurUpdates(Context context) {
        DateTime now = DateTime.now();
        List<Recur> recurs = new ArrayList<>();
        for(Recur recur : DatabaseQueryHandler.getAllRecur(context).values()){
            RecurrenceIterator recurrenceIterator = RecurrenceIteratorFactory.createRecurrenceIterator(recur.getRRule(), getDateTimeValue(DateTime.now()), DateTime.now().getZone().toTimeZone());
            DateTimeIterator iterable = DateTimeIteratorFactory.createDateTimeIterator(recurrenceIterator);
            if(iterable.hasNext()){
                DateTime dateTime = iterable.next();
                if(dateTime.isBefore(now)) {
                    recurs.add(recur);
                    DatabaseQueryHandler.addData(context, recur.createData(dateTime));
                }
                break;
            }
        }
		return recurs;
	}

	private static DateTimeValue getDateTimeValue(DateTime dateTime){
		return new DateTimeValueImpl(dateTime.getYear(),
									dateTime.getMonthOfYear(),
									dateTime.getDayOfMonth(),
									dateTime.getHourOfDay(),
									dateTime.getMinuteOfHour(),
									dateTime.getSecondOfMinute());
	}
}
