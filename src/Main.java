import persistence.FilePersistence;
import services.DrugManager;
import services.PurchaseManager;
import services.StockMonitor;
import services.SupplierManager;
import ui.MenuInterface;

public class Main {
    public static void main(String[] args) {
        FilePersistence filePersistence = new FilePersistence();
        SupplierManager supplierManager = new SupplierManager(filePersistence);
        DrugManager drugManager = new DrugManager(supplierManager, filePersistence);
        StockMonitor stockMonitor = new StockMonitor();
        PurchaseManager purchaseManager = new PurchaseManager(drugManager, filePersistence);

        drugManager.loadDrugs();
        supplierManager.loadSuppliers();
        purchaseManager.loadPurchases();

        MenuInterface menu = new MenuInterface(drugManager, supplierManager, purchaseManager, stockMonitor);
        menu.run();
    }
}