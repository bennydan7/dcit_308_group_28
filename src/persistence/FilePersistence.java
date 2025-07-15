package persistence;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import model.Drug;
import model.Supplier;
import model.Transaction;

public class FilePersistence {
    private static final String DRUGS_FILE = "data/drugs.dat";
    private static final String SUPPLIERS_FILE = "data/suppliers.dat";
    private static final String SUPPLIER_DRUGS_FILE = "data/supplier_drugs.dat";
    private static final String DRUG_SUPPLIERS_FILE = "data/drug_suppliers.dat";
    private static final String PURCHASES_FILE = "data/purchases.dat";

    public void saveDrugs(Collection<Drug> drugs) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DRUGS_FILE))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Drug drug : drugs) {
                writer.println(String.format("%s|%s|%s|%s|%.2f|%d",
                        drug.code, drug.name, String.join(",", drug.suppliers),
                        sdf.format(drug.expirationDate), drug.price, drug.stockLevel));
            }
        } catch (IOException e) {
            System.err.println("Error saving drugs: " + e.getMessage());
        }
    }

    public Collection<Drug> loadDrugs() {
        List<Drug> drugs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DRUGS_FILE))) {
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                Set<String> suppliers = new HashSet<>(Arrays.asList(parts[2].split(",")));
                Drug drug = new Drug(parts[0], parts[1], suppliers, sdf.parse(parts[3]),
                        Double.parseDouble(parts[4]), Integer.parseInt(parts[5]));
                drugs.add(drug);
            }
        } catch (IOException | java.text.ParseException e) {
            System.err.println("Error loading drugs: " + e.getMessage());
        }
        return drugs;
    }

    public void saveSuppliers(Collection<Supplier> suppliers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SUPPLIERS_FILE))) {
            for (Supplier supplier : suppliers) {
                writer.println(String.format("%s|%s|%s|%s|%d",
                        supplier.id, supplier.name, supplier.contact, supplier.location, supplier.deliveryTime));
            }
        } catch (IOException e) {
            System.err.println("Error saving suppliers: " + e.getMessage());
        }
    }

    public Collection<Supplier> loadSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                Supplier supplier = new Supplier(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4]));
                suppliers.add(supplier);
            }
        } catch (IOException e) {
            System.err.println("Error loading suppliers: " + e.getMessage());
        }
        return suppliers;
    }

    public void saveSupplierDrugs(HashMap<String, Set<String>> supplierDrugs) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SUPPLIER_DRUGS_FILE))) {
            for (Map.Entry<String, Set<String>> entry : supplierDrugs.entrySet()) {
                writer.println(String.format("%s|%s", entry.getKey(), String.join(",", entry.getValue())));
            }
        } catch (IOException e) {
            System.err.println("Error saving supplier-drugs: " + e.getMessage());
        }
    }

    public HashMap<String, Set<String>> loadSupplierDrugs() {
        HashMap<String, Set<String>> supplierDrugs = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIER_DRUGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                supplierDrugs.put(parts[0], new HashSet<>(Arrays.asList(parts[1].split(","))));
            }
        } catch (IOException e) {
            System.err.println("Error loading supplier-drugs: " + e.getMessage());
        }
        return supplierDrugs;
    }

    public void saveDrugSuppliers(HashMap<String, Set<String>> drugSuppliers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DRUG_SUPPLIERS_FILE))) {
            for (Map.Entry<String, Set<String>> entry : drugSuppliers.entrySet()) {
                writer.println(String.format("%s|%s", entry.getKey(), String.join(",", entry.getValue())));
            }
        } catch (IOException e) {
            System.err.println("Error saving drug-suppliers: " + e.getMessage());
        }
    }

    public HashMap<String, Set<String>> loadDrugSuppliers() {
        HashMap<String, Set<String>> drugSuppliers = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DRUG_SUPPLIERS_FILE))) {
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split("\\|");
                drugSuppliers.put(parts[0], new HashSet<>(Arrays.asList(parts[1].split(","))));
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.err.println("Error loading drug-suppliers: " + e.getMessage());
        }
        return drugSuppliers;
    }

    public void savePurchases(List<Transaction> purchases) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PURCHASES_FILE, true))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Transaction t : purchases) {
                writer.println(String.format("%s|%d|%s|%s|%.2f",
                        t.drugCode, t.quantity, sdf.format(t.timestamp), t.buyerId, t.totalCost));
            }
        } catch (IOException e) {
            System.err.println("Error saving purchases: " + e.getMessage());
        }
    }

    public List<Transaction> loadPurchases() {
        List<Transaction> purchases = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PURCHASES_FILE))) {
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                Transaction transaction = new Transaction(parts[0], Integer.parseInt(parts[1]),
                        sdf.parse(parts[2]), parts[3], Double.parseDouble(parts[4]));
                purchases.add(transaction);
            }
        } catch (IOException | java.text.ParseException e) {
            System.err.println("Error loading purchases: " + e.getMessage());
        }
        return purchases;
    }
}