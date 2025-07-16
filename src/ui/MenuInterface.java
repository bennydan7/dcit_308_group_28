package ui;

import java.text.SimpleDateFormat;
import java.util.*;
import model.Drug;
import model.Supplier;
import model.Transaction;
import services.DrugManager;
import services.PurchaseManager;
import services.StockMonitor;
import services.SupplierManager;

public class MenuInterface {
    private DrugManager drugManager;
    private SupplierManager supplierManager;
    private PurchaseManager purchaseManager;
    private StockMonitor stockMonitor;
    private Scanner scanner;

    public MenuInterface(DrugManager drugManager, SupplierManager supplierManager,
                        PurchaseManager purchaseManager, StockMonitor stockMonitor) {
        this.drugManager = drugManager;
        this.supplierManager = supplierManager;
        this.purchaseManager = purchaseManager;
        this.stockMonitor = stockMonitor;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\n Atinka Meds Pharmacy Inventory System");
            System.out.println("1. Add Drug");
            System.out.println("2. Remove Drug");
            System.out.println("3. Update Drug");
            System.out.println("4. List Drugs");
            System.out.println("5. Search Drug by Code");
            System.out.println("6. Search Drug by Name");
            System.out.println("7. Sort Drugs by Name");
            System.out.println("8. Sort Drugs by Price");
            System.out.println("9. Add Supplier");
            System.out.println("10. Filter Suppliers by Location");
            System.out.println("11. Filter Suppliers by Delivery Time");
            System.out.println("12. Add Purchase");
            System.out.println("13. View Recent Purchases");
            System.out.println("14. Check Low Stock");
            System.out.println("15. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter drug code: ");
                    String code = scanner.nextLine();
                    System.out.print("Enter drug name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter supplier IDs (comma-separated): ");
                    Set<String> suppliers = new HashSet<>(Arrays.asList(scanner.nextLine().split(",")));
                    System.out.print("Enter expiration date (yyyy-MM-dd): ");
                    Date expDate;
                    try {
                        expDate = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());
                    } catch (java.text.ParseException e) {
                        System.out.println("Invalid date format");
                        continue;
                    }
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter stock level: ");
                    int stock = scanner.nextInt();
                    drugManager.addDrug(code, name, suppliers, expDate, price, stock);
                    System.out.println("Drug added successfully");
                    break;
                case 2:
                    System.out.print("Enter drug code to remove: ");
                    drugManager.removeDrug(scanner.nextLine());
                    System.out.println("Drug removed successfully");
                    break;
                case 3:
                    System.out.print("Enter drug code to update: ");
                    code = scanner.nextLine();
                    System.out.print("Enter new name: ");
                    name = scanner.nextLine();
                    System.out.print("Enter new price: ");
                    price = scanner.nextDouble();
                    System.out.print("Enter new stock level: ");
                    stock = scanner.nextInt();
                    drugManager.updateDrug(code, name, price, stock);
                    System.out.println("Drug updated successfully");
                    break;
                case 4:
                    for (Drug drug : drugManager.listDrugs()) {
                        System.out.println(drug.code + ": " + drug.name + ", $" + drug.price + ", Stock: " + drug.stockLevel);
                    }
                    break;
                case 5:
                    System.out.print("Enter drug code: ");
                    Drug drug = drugManager.searchDrugByCode(scanner.nextLine());
                    if (drug != null) {
                        System.out.println(drug.code + ": " + drug.name + ", $" + drug.price + ", Stock: " + drug.stockLevel);
                    } else {
                        System.out.println("Drug not found");
                    }
                    break;
                case 6:
                    System.out.print("Enter drug name: ");
                    List<Drug> drugs = drugManager.binarySearchDrugsByName(scanner.nextLine());
                    for (Drug d : drugs) {
                        System.out.println(d.code + ": " + d.name + ", $" + d.price + ", Stock: " + d.stockLevel);
                    }
                    if (drugs.isEmpty()) System.out.println("No drugs found");
                    break;
                case 7:
                    drugManager.sortDrugsByName();
                    System.out.println("Drugs sorted by name");
                    break;
                case 8:
                    drugManager.sortDrugsByPrice();
                    System.out.println("Drugs sorted by price");
                    break;
                case 9:
                    System.out.print("Enter supplier ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter supplier name: ");
                    name = scanner.nextLine();
                    System.out.print("Enter contact: ");
                    String contact = scanner.nextLine();
                    System.out.print("Enter location: ");
                    String location = scanner.nextLine();
                    System.out.print("Enter delivery time (days): ");
                    int deliveryTime = scanner.nextInt();
                    supplierManager.addSupplier(id, name, contact, location, deliveryTime);
                    System.out.println("Supplier added successfully");
                    break;
                case 10:
                    System.out.print("Enter location: ");
                    for (Supplier supplier : supplierManager.filterSuppliersByLocation(scanner.nextLine())) {
                        System.out.println(supplier.id + ": " + supplier.name + ", " + supplier.location);
                    }
                    break;
                case 11:
                    System.out.print("Enter max delivery time (days): ");
                    for (Supplier supplier : supplierManager.filterSuppliersByDeliveryTime(scanner.nextInt())) {
                        System.out.println(supplier.id + ": " + supplier.name + ", " + supplier.deliveryTime + " days");
                    }
                    break;
                case 12:
                    System.out.print("Enter drug code: ");
                    code = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter buyer ID: ");
                    String buyerId = scanner.nextLine();
                    System.out.print("Enter total cost: ");
                    double totalCost = scanner.nextDouble();
                    purchaseManager.addPurchase(code, quantity, buyerId, totalCost);
                    System.out.println("Purchase added successfully");
                    break;
                case 13:
                    System.out.print("Enter drug code: ");
                    code = scanner.nextLine();
                    System.out.print("Enter number of recent purchases: ");
                    int limit = scanner.nextInt();
                    for (Transaction t : purchaseManager.getRecentPurchases(code, limit)) {
                        System.out.println(t.drugCode + ": " + t.quantity + " units, " + t.timestamp + ", $" + t.totalCost);
                    }
                    break;
                case 14:
                    System.out.print("Enter stock threshold: ");
                    for (Drug d : stockMonitor.checkLowStock(scanner.nextInt())) {
                        System.out.println(d.code + ": " + d.name + ", Stock: " + d.stockLevel);
                    }
                    break;
                case 15:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}