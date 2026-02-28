package TEST;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import IMPL.SalesDaoImpl;
import POJO.Sales;
import POJO.SalesItem;

public class SalesTest {

	public static void main(String[] args) {
		 Scanner sc = new Scanner(System.in);
	        SalesDaoImpl sdimpl = new SalesDaoImpl();

	        int choice;
	        do {
	            System.out.println("===== Sales Management System =====");
	            System.out.println("1. Create Sale");
	            System.out.println("2. View Sale by ID");
	            System.out.println("3. View Sale Items by Sale ID");
	            System.out.println("4. View ALL Sales");
	            System.out.println("5. Search Sales by Product Name");
	            System.out.println("6. View Total Revenue");
	            System.out.println("7. Delete Sale");
	            System.out.println("8. Update Sale Item Quantity");
	            System.out.println("9. Exit");
	            System.out.print("Enter your choice: ");
	            choice = sc.nextInt();
	            sc.nextLine(); // consume newline

	            switch (choice) {
	                case 1:
	                {
	                    Sales sale = new Sales();
	                    List<SalesItem> items = new ArrayList<>();

	                    // Get customer name
	                    System.out.println("Enter customer name:");
	                    sale.setCustomerName(sc.nextLine());

	                    // Get number of items
	                    System.out.println("Enter number of items:");
	                    int numItems = sc.nextInt();

	                    for (int i = 0; i < numItems; i++) {
	                        SalesItem item = new SalesItem();

	                        System.out.println("Enter product ID for item " + (i + 1) + ":");
	                        item.setProductId(sc.nextInt());

	                        System.out.println("Enter quantity for item " + (i + 1) + ":");
	                        item.setQuantity(sc.nextInt());

	                        items.add(item);
	                    }

	                    // Call DAO to create sale
	                    SalesDaoImpl dao = new SalesDaoImpl();
	                    boolean result = dao.createSale(sale, items);
	                    
	                    if (!result) {
	                        System.out.println("Sale FAILED due to insufficient stock or other error.");
	                    }

	                    if (result) {
	                        System.out.println("Sale created successfully! Sale ID: " + sale.getSaleId());
	                        System.out.println("Total Amount: " + sale.getTotalAmount());
	                        System.out.println("Sale Items:");
	                        for (SalesItem item : items) {
	                            System.out.println(item);
	                        }
	                    } else {
	                        System.out.println("Sale creation failed.");
	                    }
	                    break;
	                	}
	                   
	                case 2:
	                {
	                	 System.out.println("Enter Sale ID to View:");
	         	        int saleId = sc.nextInt();

	         	        Sales sale = sdimpl.getSaleById(saleId);

	         	        if (sale != null) {
	         	        	System.out.println("Sale Details:");
	         	           System.out.println("Sale ID: " + sale.getSaleId());
	         	           System.out.println("Customer: " + sale.getCustomerName());
	         	           System.out.println("Date: " + sale.getSaleDate());
	         	           System.out.println("Total Amount: " + sale.getTotalAmount());
	         	        } else {
	         	            System.out.println("No Sale found with ID: " + saleId);
	         	        }
	                	
	                	break;
	                }
	                    
	                case 3: {
	                    System.out.println("Enter Sale ID to view items:");
	                    int saleId = sc.nextInt();

	                    List<SalesItem> items = sdimpl.getSaleItems(saleId);

	                    if (!items.isEmpty()) {
	                        System.out.println("Sale Items for Sale ID " + saleId + ":");
	                        for (SalesItem item : items) {
	                            System.out.println(item);
	                        }
	                    } else {
	                        System.out.println("No items found for Sale ID " + saleId);
	                    }
	                    break;
	                }
	                
	                case 4:
	                {
	                    List<Sales> allSales = sdimpl.getAllSales();

	                    if (allSales.isEmpty()) {
	                        System.out.println("No sales found.");
	                    } else {
	                        System.out.println("===== ALL SALES =====");
	                        for (Sales s : allSales) {
	                            System.out.println("Sale ID: " + s.getSaleId());
	                            System.out.println("Customer: " + s.getCustomerName());
	                            System.out.println("Date: " + s.getSaleDate());
	                            System.out.println("Total: " + s.getTotalAmount());
	                            System.out.println("----------------------------");
	                        }
	                    }
	                    break;
	                }
	                
	                
	                case 5:
	                {
	                    System.out.println("Enter product name:");
	                    String pname = sc.nextLine();

	                    List<Sales> sales = sdimpl.getSalesByProductName(pname);

	                    if (sales.isEmpty()) {
	                        System.out.println("No sales found for product: " + pname);
	                    } else {
	                        System.out.println("Sales for product: " + pname);
	                        for (Sales s : sales) {
	                            System.out.println("Sale ID: " + s.getSaleId() +
	                                               ", Customer: " + s.getCustomerName() +
	                                               ", Date: " + s.getSaleDate() +
	                                               ", Total: " + s.getTotalAmount());
	                        }
	                    }
	                    break;
	                }
	                
	                case 6: {
	                    double revenue = sdimpl.getTotalRevenue();
	                    System.out.println("Total Revenue Earned: " + revenue);
	                    break;
	                }
	                
	                case 7:
	                {
	                    System.out.println("Enter Sale ID to delete:");
	                    int saleId = sc.nextInt();

	                    boolean result = sdimpl.deleteSale(saleId);

	                    if (result) {
	                        System.out.println("Sale deleted successfully.");
	                    } else {
	                        System.out.println("Sale deletion failed.");
	                    }
	                    break;
	                }

	                
	                case 8: {
	                    System.out.print("Enter Sale ID: ");
	                    int saleId = sc.nextInt();

	                    System.out.print("Enter Product ID: ");
	                    int productId = sc.nextInt();

	                    System.out.print("Enter NEW Quantity: ");
	                    int newQty = sc.nextInt();

	                    boolean updated = sdimpl.updateSaleItemQuantity(saleId, productId, newQty);
	                    if (updated) {
	                        System.out.println("Sale item updated and stock adjusted correctly.");
	                    } else {
	                        System.out.println("Update failed.");
	                    }
	                    break;
	                }

	               

	                default:
	                    System.out.println("Invalid choice! Try again.");
	            }

	        } while (choice != 9);

	        sc.close();
}
}