package services;

import java.util.ArrayList;
import java.util.List;

import datastructures.MinHeap;
import model.Drug;

public class StockMonitor {
    private MinHeap stockPriorityHeap;

    public StockMonitor() {
        stockPriorityHeap = new MinHeap();
    }

    public void addDrug(Drug drug) {
        stockPriorityHeap.insert(drug);
    }

    public void updateStock(String drugCode, int newStock) {
        stockPriorityHeap.updateStock(drugCode, newStock);
    }

    public List<Drug> checkLowStock(int threshold) {
        List<Drug> lowStock = new ArrayList<>();
        while (true) {
            Drug drug = stockPriorityHeap.peekMin();
            if (drug == null || drug.stockLevel > threshold) break;
            lowStock.add(stockPriorityHeap.extractMin());
        }
        for (Drug drug : lowStock) {
            stockPriorityHeap.insert(drug);
        }
        return lowStock;
    }
}
