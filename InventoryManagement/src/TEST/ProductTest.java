package TEST;

import java.util.List;
import java.util.Scanner;

import IMPL.ProductDaoImpl;
import POJO.Product;

public class ProductTest {

	public static void main(String[] args) 
	{
		Scanner sc=new Scanner(System.in);
		
		ProductDaoImpl pdimpl=new ProductDaoImpl();
		
		 int productId;
		 String productName;
	     String category;
		 int quantity;
		 double price;
		
		 boolean flag;
		 
		int choice;
		do 
		{
		System.out.println("===== Inventory Management System =====");
        System.out.println("1. Add Product");
        System.out.println("2. Update Product");
        System.out.println("3. Delete Product");
        System.out.println("4. View Product by ID");
        System.out.println("5. View All Products");
        System.out.println("6. View Product by Name");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
        choice = sc.nextInt();
        
        switch(choice)
        {
        
        case 1:
        {
            sc.nextLine();
        	System.out.println("Enter ProductName");
        	productName=sc.nextLine();
        	
        	System.out.println("Enter Category");
        	category=sc.nextLine();
        	
        	System.out.println("Enter Quantity");
        	quantity=sc.nextInt();
        	
        	System.out.println("Enter Price");
        	price=sc.nextDouble();
        	
        	
        	
        	Product p=new Product(productName,category,quantity,price);
        	flag=pdimpl.addproduct(p);
        	
        	if(flag)
        	{
        		System.out.println("Inserted Successfully");
        	}
        	else
        	{
        		System.out.println("Failed to Inserted");
        	}
        
        	
        	break;
        }
        case 2:
        {
            
            System.out.println("Enter the Product Id which you want to update");
            productId=sc.nextInt();
            sc.nextLine();
            
        	System.out.println("Enter ProductName");
        	productName=sc.nextLine();
        	
        	System.out.println("Enter Category");
        	category=sc.nextLine();
        	
        	System.out.println("Enter Quantity");
        	quantity=sc.nextInt();
        	
        	System.out.println("Enter Price");
        	price=sc.nextDouble();
        
        	
        	Product p=new Product(productId,productName,category,quantity,price);
        	flag=pdimpl.updateproduct(p);
        	
        	if(flag)
        	{
        		System.out.println("Updated Successfully");
        	}
        	else
        	{
        		System.out.println("Failed to Update");
        	}
        	
        	break;
        }
        case 3:
        {
        	System.out.println("Enter the Product Id which you want to delete");
            productId=sc.nextInt();
            sc.nextLine();
            
            flag=pdimpl.deleteProductById(productId);
        	
        	if(flag)
        	{
        		System.out.println("Deleted Successfully");
        	}
        	else
        	{
        		System.out.println("Failed to Delete");
        	}
        	
        	break;
        }
        
        case 4:
        {
        	System.out.println("Enter the Product Id which you want to search");
            productId=sc.nextInt();
           
            Product p=pdimpl.getProductById(productId);
            
            if(p!=null)
            {
            	  System.out.println("Product Details:");
                  System.out.println(p);
              }
              else
              {
                  System.out.println("No product found with ID: " + productId);
              }
        	
        	break;
        }
        
        case 5:
        {
            System.out.println("All Products in Inventory:");
            List<Product> productList = pdimpl.getAllProducts();
            
            if (productList.isEmpty())
            {
                System.out.println("No products available in the inventory.");
            }
            else
            {
                for (Product p : productList)
                {
                    System.out.println(p);
                }
            }
            break;
        }
        case 6:
        {
            sc.nextLine();
            System.out.println("Enter Product Name to Search:");
            String pname = sc.nextLine();

            Product p = pdimpl.getProductByName(pname);

            if (p != null)
            {
                System.out.println("Product Found:");
                System.out.println(p);
            }
            else
            {
                System.out.println("No product found with the name: " + pname);
            }

            break;
        }

        
        
        }
		}
		while(choice!=7);
	}

}
