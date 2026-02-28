package DAO;

import java.util.List;

import POJO.Product;

public interface ProductDao 
{
      boolean addproduct(Product p);
      boolean updateproduct(Product p);
      boolean deleteProductById(int productId);
      Product getProductById(int productId);
      List<Product> getAllProducts();
      Product getProductByName(String productName);
      boolean updateProductStock(int productId, int qtyToAdd);

}
