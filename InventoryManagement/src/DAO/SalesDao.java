package DAO;

import java.util.List;

import POJO.Sales;
import POJO.SalesItem;

public interface SalesDao {

	// 1. Create a new sales order (insert into sales + sales_items)
    boolean createSale(Sales sale, List<SalesItem> items);

    // 2. Fetch single sale by ID (header only)
    Sales getSaleById(int saleId);

    // 3. Fetch items of a sale
    List<SalesItem> getSaleItems(int saleId);

    // 4. Fetch ALL sales
    List<Sales> getAllSales();

    // 5. Get total revenue
    double getTotalRevenue();

    // 6. Delete a sale (optional)
    boolean deleteSale(int saleId);
    
    public List<Sales> getSalesByProductName(String productName);

    boolean updateSaleItemQuantity(int saleId, int productId, int newQty);

}
