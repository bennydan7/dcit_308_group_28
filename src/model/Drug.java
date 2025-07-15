package model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Drug {
    public String code;
    public String name;
    public Set<String> suppliers;
    public Date expirationDate;
    public double price;
    public int stockLevel;

    public Drug(String code, String name, Set<String> suppliers, Date expirationDate, double price, int stockLevel) {
        this.code = code;
        this.name = name;
        this.suppliers = new HashSet<>(suppliers);
        this.expirationDate = expirationDate;
        this.price = price;
        this.stockLevel = stockLevel;
    }
}
