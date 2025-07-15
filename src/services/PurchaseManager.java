package services;

import java.util.*;
import dataStructures.LinkedList;
import model.Drug;
import model.Transaction;
import persistence.FilePersistence;

public class PurchaseManager {
    private LinkedList purchaseHistory;
    private DrugManager drugManager;
    private FilePersistence filePersistence;

    public PurchaseManager(DrugManager drugManager, FilePersistence filePersistence) {
        this.purchaseHistory = new LinkedList();
        this.drugManager = drugManager;
        this.filePersistence = filePersistence;
    }

    public void addPurchase(String drugCode, int quantity, String buyerId, double totalCost) {
        Transaction transaction = new Transaction(drugCode, quantity, new Date(), buyerId, totalCost);
        purchaseHistory.add(transaction);
        Drug drug = drugManager.searchDrugByCode(drugCode);
        if (drug != null) {
            drug.stockLevel -= quantity;
            drugManager.updateDrug(drug.code, drug.name, drug.price, drug.stockLevel);
        }
        filePersistence.savePurchases(purchaseHistory.toList());
    }

    public List<Transaction> getRecentPurchases(String drugCode, int limit) {
        List<Transaction> results = new ArrayList<>();
        for (Transaction t : purchaseHistory.toList()) {
            if (t.drugCode.equals(drugCode)) {
                results.add(t);
                if (results.size() >= limit) break;
            }
        }
        return mergeSortTransactions(results);
    }

    private List<Transaction> mergeSortTransactions(List<Transaction> transactions) {
        if (transactions.size() <= 1) return transactions;
        int mid = transactions.size() / 2;
        List<Transaction> left = mergeSortTransactions(transactions.subList(0, mid));
        List<Transaction> right = mergeSortTransactions(transactions.subList(mid, transactions.size()));
        return mergeTransactions(left, right);
    }

    private List<Transaction> mergeTransactions(List<Transaction> left, List<Transaction> right) {
        List<Transaction> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).timestamp.compareTo(right.get(j).timestamp) >= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        result.addAll(left.subList(i, left.size()));
        result.addAll(right.subList(j, right.size()));
        return result;
    }

    public void loadPurchases() {
        purchaseHistory = new LinkedList();
        for (Transaction t : filePersistence.loadPurchases()) {
            purchaseHistory.add(t);
        }
    }
}
