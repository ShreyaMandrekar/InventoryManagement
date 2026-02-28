package DAO;

import java.util.List;

import POJO.Sales;
import POJO.SalesItem;

public interface SalesDao {

    boolean createSale(Sales sale, List<SalesItem> items);

    Sales getSaleById(int saleId);

    List<SalesItem> getSaleItems(int saleId);

    List<Sales> getAllSales();

    double getTotalRevenue();

    boolean deleteSale(int saleId);
    
    public List<Sales> getSalesByProductName(String productName);

    boolean updateSaleItemQuantity(int saleId, int productId, int newQty);

}
