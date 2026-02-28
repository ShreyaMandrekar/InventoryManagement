package TEST;

import java.util.List;
import java.util.Scanner;

import IMPL.SupplierDaoImpl;
import POJO.Supplier;

public class SupplierTest {

	public static void main(String[] args)
	{

        Scanner sc = new Scanner(System.in);
        SupplierDaoImpl sdimpl = new SupplierDaoImpl();

        int choice;
        boolean flag;

        do {
            System.out.println("===== Supplier Management =====");
            System.out.println("1. Add Supplier");
            System.out.println("2. Update Supplier");
            System.out.println("3. Delete Supplier");
            System.out.println("4. View Supplier by ID");
            System.out.println("5. View Supplier by Name");
            System.out.println("6. View All Suppliers");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch(choice)
            {
                case 1:
                {
                    sc.nextLine(); 

                    System.out.println("Enter Supplier Name:");
                    String supplierName = sc.nextLine();

                    System.out.println("Enter Contact Number:");
                    String contact = sc.nextLine();

                    System.out.println("Enter Email:");
                    String email = sc.nextLine();

                    System.out.println("Enter Address:");
                    String address = sc.nextLine();

                    Supplier s = new Supplier(supplierName, contact, email, address);
                    flag = sdimpl.addSupplier(s);

                    if(flag)
                    {
                    	System.out.println("Supplier Added Successfully");
                    }
                    else
                    {
                    	System.out.println("Failed to Add Supplier");
                    }

                    break;
                }
                
                case 2:
                {
                    System.out.println("Enter Supplier ID to update:");
                    int supplierId = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Enter New Supplier Name:");
                    String supplierName = sc.nextLine();

                    System.out.println("Enter New Contact Number:");
                    String contact = sc.nextLine();

                    System.out.println("Enter New Email:");
                    String email = sc.nextLine();

                    System.out.println("Enter New Address:");
                    String address = sc.nextLine();

                    Supplier s = new Supplier(supplierId, supplierName, contact, email, address);

                    flag = sdimpl.updateSupplier(s);

                    if(flag)
                    {
                    	System.out.println("Supplier Updated Successfully");
                    }
                    else
                    {
                    	System.out.println("Failed to Update Supplier");
                    }

                    break;
                }
                
                case 3:
                {
                    System.out.println("Enter Supplier ID to delete:");
                    int supplierId = sc.nextInt();
                    sc.nextLine();

                    flag = sdimpl.deleteSupplierById(supplierId);

                    if(flag)
                    {
                    	System.out.println("Supplier Deleted Successfully");
                    }
                    else
                    {
                    	System.out.println("Failed to Delete Supplier");
                    }

                    break;
                }
                
                case 4:
                {
                    System.out.println("Enter Supplier ID to view:");
                    int supplierId = sc.nextInt();
                    sc.nextLine();

                    Supplier s = sdimpl.getSupplierById(supplierId);

                    if(s != null)
                    {
                    	System.out.println(s);
                    }
                    else
                    {
                    	System.out.println("Supplier not found");
                    }

                    break;
                }

                case 5:
                {
                	sc.nextLine();
                    System.out.println("Enter Supplier Name to view:");
                    String supplierName = sc.nextLine();

                    Supplier s = sdimpl.getSupplierByName(supplierName);

                    if(s != null)
                        System.out.println(s);
                    else
                        System.out.println("Supplier not found");

                    break;
                }
                
                case 6:
                {
                    List<Supplier> suppliers = sdimpl.getAllSuppliers();

                    if(suppliers.isEmpty())
                        System.out.println("No suppliers found.");
                    else
                        for(Supplier s : suppliers)
                            System.out.println(s);

                    break;
                }


            }

        } while(choice != 7);

	}

}
