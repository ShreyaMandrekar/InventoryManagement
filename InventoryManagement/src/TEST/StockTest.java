package TEST;

import java.util.List;
import java.util.Scanner;

import IMPL.StockDaoImpl;
import POJO.Product;
import POJO.StockTransaction;

public class StockTest {

	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
        StockDaoImpl sdimpl = new StockDaoImpl();

        int choice;

        do
        {
            System.out.println("===== Stock Management =====");
            System.out.println("1. Add Stock-IN");
            System.out.println("2. Add Stock-OUT");
            System.out.println("3. Get Stock By Product");
            System.out.println("4. View All Stock Transactions");
            System.out.println("5. View Low Stock Products");//Threshold means "show products whose quantity is lower than this number".
            System.out.println("6.Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch(choice)
            {
            case 1:   // ADD STOCK-IN
            {
                System.out.println("Enter Product ID");
                int productId = sc.nextInt();
                sc.nextLine();

                System.out.println("Enter Supplier ID");
                int supplierId = sc.nextInt();
                sc.nextLine();

                System.out.println("Enter Quantity");
                int quantity = sc.nextInt();
                sc.nextLine();

                System.out.println("Enter Remarks");
                String remarks = sc.nextLine();

                StockTransaction st = new StockTransaction();
                st.setProductId(productId);
                st.setSupplierId(supplierId);
                st.setQuantity(quantity);
                st.setRemarks(remarks);
                st.setType("IN");

                boolean flag = sdimpl.addStockIn(st);

                if (flag)
                    System.out.println("Stock-IN Added Successfully");
                else
                    System.out.println("Failed to Add Stock-IN");

                break;
            }

            
            case 2:   // ADD STOCK-OUT
            {
                System.out.println("Enter Product ID");
                int productId = sc.nextInt();
                sc.nextLine();

                System.out.println("Enter Quantity");
                int quantity = sc.nextInt();
                sc.nextLine();

                System.out.println("Enter Remarks");
                String remarks = sc.nextLine();

                StockTransaction st = new StockTransaction();
                st.setProductId(productId);
                st.setQuantity(quantity);
                st.setRemarks(remarks);
                st.setType("OUT");

                boolean flag = sdimpl.addStockOut(st);

                if (flag)
                    System.out.println("Stock-OUT Updated Successfully");
                else
                    System.out.println("Failed to Update Stock-OUT");

                break;
            }
            
            case 3: // View Stock by Product
            {
                System.out.println("Enter Product ID to view stock:");
                int productId = sc.nextInt();
                List<StockTransaction> list = sdimpl.getStockByProduct(productId);
                
                if(list.isEmpty()) {
                    System.out.println("No stock transactions found for this product.");
                } else {
                    for(StockTransaction st : list) {
                        System.out.println(st);
                    }
                }
                break;
            }
            
            case 4:
            {
                List<StockTransaction> list = sdimpl.getAllStock();

                if (list == null || list.isEmpty())
                {
                    System.out.println("No Stock Transactions Found.");
                }
                else
                {
                    for (StockTransaction st : list)
                    {
                        System.out.println("---------------------------");
                        System.out.println("Transaction ID: " + st.getTransactionId());
                        System.out.println("Product ID: " + st.getProductId());
                        System.out.println("Supplier ID: " + st.getSupplierId());
                        System.out.println("Quantity: " + st.getQuantity());
                        System.out.println("Type: " + st.getType());
                        System.out.println("Remarks: " + st.getRemarks());
                        System.out.println("Date: " + st.getDate());
                    }
                    System.out.println("---------------------------");
                }
                break;
            }

            case 5:
            {
                System.out.println("Enter Stock Threshold Value:");
                int threshold = sc.nextInt();

                List<Product> list = sdimpl.getLowStockProducts(threshold);

                if (list == null || list.isEmpty())
                {
                    System.out.println("No Low Stock Products Found.");
                }
                else
                {
                    System.out.println("===== Low Stock Products =====");
                    for (Product p : list)
                    {
                        System.out.println("------------------------------");
                        System.out.println("Product ID: " + p.getProductId());
                        System.out.println("Name: " + p.getProductName());
                        System.out.println("Category: " + p.getCategory());
                        System.out.println("Quantity: " + p.getQuantity());
                        System.out.println("Price: " + p.getPrice());
                     
                    }
                    System.out.println("------------------------------");
                }
                break;
            }



	}

        }while (choice != 6);
	}
}