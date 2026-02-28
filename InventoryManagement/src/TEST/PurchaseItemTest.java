package TEST;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import IMPL.PurchaseItemDaoImpl;

import POJO.PurchaseItem;

public class PurchaseItemTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		 Scanner sc = new Scanner(System.in);
	     PurchaseItemDaoImpl pidimpl = new PurchaseItemDaoImpl();
	     int choice;
	     do {
	            System.out.println("===== Purchase Order Menu=====");
	            System.out.println("1. Create Purchase Items");
	            System.out.println("2. View Purchase Items By Id");
	            System.out.println("3. Exit");
	          
	            System.out.print("Enter your choice: ");
	            choice = sc.nextInt();
	            sc.nextLine(); // consume newline

	  switch (choice)          
	       {
	    case 1:
	      {
	    	  System.out.print("Enter Purchase ID: ");
              int pid = sc.nextInt();

              System.out.print("Enter how many items you want to add: ");
              int count = sc.nextInt();

              List<PurchaseItem> items = new ArrayList<>();

              for (int i = 0; i < count; i++) {
                  System.out.println("Enter details for item " + (i + 1));

                  System.out.print("Product ID: ");
                  int productId = sc.nextInt();

                  System.out.print("Quantity: ");
                  int qty = sc.nextInt();

                  System.out.print("Price: ");
                  double price = sc.nextDouble();

                  PurchaseItem pi = new PurchaseItem(0, pid, productId, qty, price);
                  items.add(pi);
              }

              try {
                  pidimpl.addPurchaseItems(items);
                  System.out.println("Items added successfully.");
              } catch (Exception e) {
                  System.out.println("Failed to add items: " + e.getMessage());
              }

              break;
	       }
	  
	    case 2:
	      { 
	    	    System.out.print("Enter Purchase ID: ");
	    	    int pid = sc.nextInt();

	    	    List<PurchaseItem> items = pidimpl.getItemsByPurchaseId(pid);

	    	    if (items.isEmpty()) {
	    	        System.out.println("No items found for this Purchase ID.");
	    	    } else {
	    	        System.out.println("===== Purchase Items =====");
	    	        for (PurchaseItem item : items) {
	    	            System.out.println(item);
	    	        }
	    	    }
	    	    break;

		
	      }
	       }
      
	 	}while(choice !=3);
sc.close();
	 }
	 }
