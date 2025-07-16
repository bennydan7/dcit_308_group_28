package services;

import java.util.*;

import datastructures.MinHeap;
import model.Drug;
import persistence.FilePersistence;

public class DrugManager {
    private HashMap<String, Drug> drugsByCode;
    private HashMap<String, Drug> drugsByName;
    private List<Drug> sortedDrugArray;
    private MinHeap stockPriorityHeap;
    private SupplierManager supplierManager;
    private FilePersistence filePersistence;

    public DrugManager(SupplierManager supplierManager, FilePersistence filePersistence) {
        this.drugsByCode = new HashMap<>();
        this.drugsByName = new HashMap<>();
        this.sortedDrugArray = new ArrayList<>();
        this.stockPriorityHeap = new MinHeap();
        this.supplierManager = supplierManager;
        this.filePersistence = filePersistence;
    }

    public void addDrug(String code, String name, Set<String> suppliers, Date expirationDate, double price, int stockLevel) {
        Drug drug = new Drug(code, name, suppliers, expirationDate, price, stockLevel);
        drugsByCode.put(code, drug);
        drugsByName.put(name, drug);
        sortedDrugArray.add(drug);
        stockPriorityHeap.insert(drug);
        supplierManager.updateDrugSuppliers(code, suppliers);
        filePersistence.saveDrugs(drugsByCode.values());
    }

    public void removeDrug(String code) {
        Drug drug = drugsByCode.remove(code);
        if (drug != null) {
            drugsByName.remove(drug.name);
            sortedDrugArray.remove(drug);
            supplierManager.removeDrugMappings(code);
            stockPriorityHeap = new MinHeap();
            for (Drug d : sortedDrugArray) stockPriorityHeap.insert(d);
            filePersistence.saveDrugs(drugsByCode.values());
        }
    }

    public void updateDrug(String code, String name, double price, int stockLevel) {
        Drug drug = drugsByCode.get(code);
        if (drug != null) {
            drugsByName.remove(drug.name);
            drug.name = name;
            drug.price = price;
            drug.stockLevel = stockLevel;
            drugsByName.put(name, drug);
            sortedDrugArray.remove(drug);
            sortedDrugArray.add(drug);
            mergeSortDrugs();
            stockPriorityHeap.updateStock(code, stockLevel);
            filePersistence.saveDrugs(drugsByCode.values());
        }
    }

    public List<Drug> listDrugs() {
        return new ArrayList<>(drugsByCode.values());
    }

    public Drug searchDrugByCode(String code) {
        return drugsByCode.get(code);
    }

    public Drug searchDrugByName(String name) {
        return drugsByName.get(name);
    }

    public List<Drug> binarySearchDrugsByName(String name) {
        List<Drug> results = new ArrayList<>();
        int left = 0, right = sortedDrugArray.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int compare = sortedDrugArray.get(mid).name.compareTo(name);
            if (compare == 0) {
                results.add(sortedDrugArray.get(mid));
                int i = mid - 1;
                while (i >= 0 && sortedDrugArray.get(i).name.equals(name)) {
                    results.add(sortedDrugArray.get(i--));
                }
                i = mid + 1;
                while (i < sortedDrugArray.size() && sortedDrugArray.get(i).name.equals(name)) {
                    results.add(sortedDrugArray.get(i++));
                }
                break;
            } else if (compare < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return results;
    }

    public void sortDrugsByName() {
        mergeSortDrugs();
    }

    public void sortDrugsByPrice() {
        Collections.sort(sortedDrugArray, (d1, d2) -> Double.compare(d1.price, d2.price));
    }

    private void mergeSortDrugs() {
        sortedDrugArray = mergeSort(sortedDrugArray);
    }

    private List<Drug> mergeSort(List<Drug> drugs) {
        if (drugs.size() <= 1) return drugs;
        int mid = drugs.size() / 2;
        List<Drug> left = mergeSort(drugs.subList(0, mid));
        List<Drug> right = mergeSort(drugs.subList(mid, drugs.size()));
        return merge(left, right);
    }

    private List<Drug> merge(List<Drug> left, List<Drug> right) {
        List<Drug> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).name.compareTo(right.get(j).name) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        result.addAll(left.subList(i, left.size()));
        result.addAll(right.subList(j, right.size()));
        return result;
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

    public void loadDrugs() {
        Collection<Drug> drugs = filePersistence.loadDrugs();
        for (Drug drug : drugs) {
            drugsByCode.put(drug.code, drug);
            drugsByName.put(drug.name, drug);
            sortedDrugArray.add(drug);
            stockPriorityHeap.insert(drug);
        }
        mergeSortDrugs();
    }
}
