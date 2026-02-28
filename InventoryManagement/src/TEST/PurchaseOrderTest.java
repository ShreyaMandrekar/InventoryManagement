package TEST;

import java.util.List;
import java.util.Scanner;

import IMPL.ProductDaoImpl;
import IMPL.PurchaseItemDaoImpl;
import IMPL.PurchaseOrderDaoImpl;

import POJO.PurchaseOrder;

public class PurchaseOrderTest {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
        PurchaseOrderDaoImpl podimpl = new PurchaseOrderDaoImpl();
        PurchaseItemDaoImpl pidimpl = new PurchaseItemDaoImpl();
        ProductDaoImpl productDao = new ProductDaoImpl();

        int choice;

        do {
            System.out.println("===== Purchase Order Menu=====");
            System.out.println("1. Create Purchase Order");
            System.out.println("2. View Purchase Order");
            System.out.println("3. View ALL Purchase Orders");
            System.out.println("4. View Purchase Orders by Supplier ID");
            System.out.println("5. Update Purchase Order Status");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1: {
                    System.out.print("Enter Supplier ID: ");
                    int supplierId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Status: ");
                    String status = sc.nextLine();

                    PurchaseOrder order = new PurchaseOrder();
                    order.setSupplierId(supplierId);
                    order.setStatus(status);

                    int purchaseId = podimpl.createPurchaseOrder(order);

                    if (purchaseId > 0) {
                        System.out.println("Purchase Order Created. ID: " + purchaseId);
                    } else {
                        System.out.println("Failed to Create Purchase Order.");
                    }
                    break;
                }

                case 2: {
                    System.out.print("Enter Purchase ID: ");
                    int id = sc.nextInt();

                    PurchaseOrder order = podimpl.getPurchaseOrderById(id);

                    if (order != null) {
                        System.out.println("Purchase Order Found:");
                        System.out.println(order);
                    } else {
                        System.out.println("No Purchase Order found with ID: " + id);
                    }
                    break;
                }

                case 3: {
                    List<PurchaseOrder> all = podimpl.getAllPurchaseOrders();
                    if (all.isEmpty()) {
                        System.out.println("No Purchase Orders found.");
                    } else {
                        System.out.println("===== All Purchase Orders =====");
                        for (PurchaseOrder p : all) {
                            System.out.println(p);
                        }
                    }
                    break;
                }

                case 4: {
                    System.out.print("Enter Supplier ID: ");
                    int supplierId = sc.nextInt();

                    List<PurchaseOrder> list = podimpl.getPurchaseOrdersBySupplier(supplierId);

                    if (list.isEmpty()) {
                        System.out.println("No orders found for this supplier.");
                    } else {
                        System.out.println("=== Orders for Supplier ID: " + supplierId + " ===");
                        for (PurchaseOrder o : list) {
                            System.out.println(o);
                        }
                    }
                    break;
                }

                case 5: {
                    System.out.print("Enter Purchase ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter New Status: ");
                    String newStatus = sc.nextLine();

                    boolean updated = podimpl.updatePurchaseOrderStatus(id, newStatus);

                    if (!updated) {
                        System.out.println("Purchase Order Not Found or Update Failed.");
                        break;
                    }

                    System.out.println("Status Updated Successfully.");

                   
                    break;
                }

                case 6:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);

        sc.close();
}
}