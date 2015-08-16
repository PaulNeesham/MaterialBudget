package com.flatlyapps.materialBudget.data;

/**
 * Created by PaulN on 12/08/2015.
 */
public class Currency {

    private Long id;
    private String name;
    private String symbol;
    private String code;
    private String format;

    public Currency(Long id, String name, String symbol, String code, String format) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.code = code;
        this.format = format;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        return !(id != null ? !id.equals(currency.id) : currency.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
