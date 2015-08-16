package com.flatlyapps.materialBudget.data;



public class ExpenseCategoryBudget {

	private Long id;
	private Account account;
	private ExpenseCategory expenseCategory;
	private Integer percent;

	public ExpenseCategoryBudget(Long id, Account account, ExpenseCategory expenseCategory, Integer percent) {
		this.id = id;
		this.account = account;
		this.expenseCategory = expenseCategory;
		this.percent = percent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public ExpenseCategory getExpenseCategory() {
		return expenseCategory;
	}

	public void setExpenseCategory(ExpenseCategory expenseCategory) {
		this.expenseCategory = expenseCategory;
	}

	public Integer getPercent() {
		return percent;
	}

	public void setPercent(Integer percent) {
		this.percent = percent;
	}

	public Long getAccountId() {
		if(account != null) {
			return account.getId();
		}
		return null;
	}

	public Long getExpenseCategoryId() {
		if(expenseCategory != null) {
			return expenseCategory.getId();
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ExpenseCategoryBudget that = (ExpenseCategoryBudget) o;

		return !(id != null ? !id.equals(that.id) : that.id != null);

	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}