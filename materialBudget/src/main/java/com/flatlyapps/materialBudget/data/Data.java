package com.flatlyapps.materialBudget.data;

import org.joda.time.DateTime;


/**
 * Created by PaulN on 12/08/2015.
 */
public class Data {

    private Long id;
    private String name;
    private Account account;
    private Long cost;
    private IncomeCategory incomeCategory;
    private ExpenseCategory expenseCategory;
    private DateTime time;
    private Recur recur;
    private Account transferTo;
    private Account transferFrom;

    public Data(Long id, String name, Account account, Long cost, IncomeCategory incomeCategory, ExpenseCategory expenseCategory, DateTime time, Recur recur, Account transferTo, Account transferFrom) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.cost = cost;
        this.incomeCategory = incomeCategory;
        this.expenseCategory = expenseCategory;
        this.time = time;
        this.recur = recur;
        this.transferTo = transferTo;
        this.transferFrom = transferFrom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public IncomeCategory getIncomeCategory() {
        return incomeCategory;
    }

    public void setIncomeCategory(IncomeCategory incomeCategory) {
        this.incomeCategory = incomeCategory;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    public Recur getRecur() {
        return recur;
    }

    public void setRecur(Recur recur) {
        this.recur = recur;
    }

    public Account getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(Account transferTo) {
        this.transferTo = transferTo;
    }

    public Account getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(Account transferFrom) {
        this.transferFrom = transferFrom;
    }

    public Long getAccountId() {
        if(account != null) {
            return account.getId();
        }
        return null;
    }

    public Boolean isExpenseCategory() {
        return expenseCategory != null;
    }

    public Boolean isIncomeCategory() {
        return incomeCategory != null;
    }
    public Boolean isRecur() {
        return recur != null;
    }

    public Boolean isTransfer() {
        return transferFrom != null;
    }

    public Long getTransferFromId() {
        if(transferFrom != null) {
            return transferFrom.getId();
        }
        return null;
    }

    public Long getTransferToId() {
        if(transferTo != null) {
            return transferTo.getId();
        }
        return null;
    }

    public Long getRecurId() {
        if(recur != null) {
            return recur.getId();
        }
        return null;
    }

    public Long getExpenseCategoryId() {
        if(expenseCategory != null) {
            return expenseCategory.getId();
        }
        return null;
    }

    public Long getIncomeCategoryId() {
        if(incomeCategory != null) {
            return incomeCategory.getId();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Data data = (Data) o;

        return !(id != null ? !id.equals(data.id) : data.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
