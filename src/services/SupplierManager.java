package services;

import java.util.*;
import model.Supplier;
import persistence.FilePersistence;

public class SupplierManager {
    private HashMap<String, Supplier> suppliersById;
    private HashMap<String, Set<String>> supplierDrugs;
    private HashMap<String, Set<String>> drugSuppliers;
    private FilePersistence filePersistence;

    public SupplierManager(FilePersistence filePersistence) {
        this.suppliersById = new HashMap<>();
        this.supplierDrugs = new HashMap<>();
        this.drugSuppliers = new HashMap<>();
        this.filePersistence = filePersistence;
    }

    public void addSupplier(String id, String name, String contact, String location, int deliveryTime) {
        suppliersById.put(id, new Supplier(id, name, contact, location, deliveryTime));
        filePersistence.saveSuppliers(suppliersById.values());
    }

    public void updateDrugSuppliers(String drugCode, Set<String> supplierIds) {
        drugSuppliers.put(drugCode, new HashSet<>(supplierIds));
        for (String supplierId : supplierIds) {
            supplierDrugs.computeIfAbsent(supplierId, k -> new HashSet<>()).add(drugCode);
        }
        filePersistence.saveSupplierDrugs(supplierDrugs);
        filePersistence.saveDrugSuppliers(drugSuppliers);
    }

    public void removeDrugMappings(String drugCode) {
        Set<String> suppliers = drugSuppliers.remove(drugCode);
        if (suppliers != null) {
            for (String supplierId : suppliers) {
                Set<String> drugs = supplierDrugs.getOrDefault(supplierId, new HashSet<>());
                drugs.remove(drugCode);
                if (drugs.isEmpty()) {
                    supplierDrugs.remove(supplierId);
                } else {
                    supplierDrugs.put(supplierId, drugs);
                }
            }
        }
        filePersistence.saveSupplierDrugs(supplierDrugs);
        filePersistence.saveDrugSuppliers(drugSuppliers);
    }

    public List<Supplier> filterSuppliersByLocation(String location) {
        List<Supplier> results = new ArrayList<>();
        for (Supplier supplier : suppliersById.values()) {
            if (supplier.location.equalsIgnoreCase(location)) {
                results.add(supplier);
            }
        }
        return results;
    }

    public List<Supplier> filterSuppliersByDeliveryTime(int maxDays) {
        List<Supplier> results = new ArrayList<>();
        for (Supplier supplier : suppliersById.values()) {
            if (supplier.deliveryTime <= maxDays) {
                results.add(supplier);
            }
        }
        return results;
    }

    public void loadSuppliers() {
        Collection<Supplier> suppliers = filePersistence.loadSuppliers();
        for (Supplier supplier : suppliers) {
            suppliersById.put(supplier.id, supplier);
        }
        supplierDrugs.putAll(filePersistence.loadSupplierDrugs());
        drugSuppliers.putAll(filePersistence.loadDrugSuppliers());
    }
}