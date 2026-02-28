package DAO;

import java.util.List;

import POJO.Product;
import POJO.StockTransaction;

public interface StockDao
{
    boolean addStockIn(StockTransaction st);
    boolean addStockOut(StockTransaction st);
    List<StockTransaction> getStockByProduct(int productId);
    List<StockTransaction> getAllStock();
    List<Product> getLowStockProducts(int threshold);
}
