package com.flatlyapps.materialbudget.data;

public class Account {

	private Long id;
	private String name;
	private Long budget;
	private Long initialFunds;
	private Boolean useIncome;

	public Account(Long id, String name, Long budget, Long initialFunds, Boolean useIncome) {
		this.id = id;
		this.name = name;
		this.budget = budget;
		this.initialFunds = initialFunds;
		this.useIncome = useIncome;
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

	public Long getBudget() {
		return budget;
	}

	public void setBudget(Long budget) {
		this.budget = budget;
	}

	public Long getInitialFunds() {
		return initialFunds;
	}

	public void setInitialFunds(Long initialFunds) {
		this.initialFunds = initialFunds;
	}

	public Boolean isUseIncome() {
		return useIncome;
	}

	public void setUseIncome(Boolean useIncome) {
		this.useIncome = useIncome;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Account account = (Account) o;

		return !(id != null ? !id.equals(account.id) : account.id != null);

	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}
