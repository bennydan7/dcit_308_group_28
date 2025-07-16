package model;

import java.util.Date;

public class Transaction {
    public String drugCode;
    public int quantity;
    public Date timestamp;
    public String buyerId;
    public double totalCost;

    public Transaction(String drugCode, int quantity, Date timestamp, String buyerId, double totalCost) {
        this.drugCode = drugCode;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.buyerId = buyerId;
        this.totalCost = totalCost;
    }
}