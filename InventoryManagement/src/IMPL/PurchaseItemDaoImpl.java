package IMPL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.PurchaseItemDao;
import POJO.PurchaseItem;
import Utility.DBUtility;

public class PurchaseItemDaoImpl implements PurchaseItemDao {

	    @Override
	    public boolean addPurchaseItems(List<PurchaseItem> items) {
	    	String sql = "INSERT INTO purchase_items (purchaseId, productId, quantity, price) VALUES (?, ?, ?, ?)";

	        try (Connection con = DBUtility.getconnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            for (PurchaseItem item : items) {
	                ps.setInt(1, item.getPurchaseId());
	                ps.setInt(2, item.getProductId());
	                ps.setInt(3, item.getQuantity());
	                ps.setDouble(4, item.getPrice());
	                ps.addBatch();
	            }

	            ps.executeBatch(); // <-- If this fails, exception goes to catch

	        } catch (SQLException e) {
	            throw new RuntimeException("Error inserting purchase items: " + e.getMessage(), e);
	        }
			return false;
	    
	    }
	

	@Override
	public List<PurchaseItem> getItemsByPurchaseId(int purchaseId) {
		 List<PurchaseItem> list = new ArrayList<>();

		    String sql = "SELECT * FROM purchase_items WHERE purchaseId = ?";

		    try (Connection con = DBUtility.getconnection();
		         PreparedStatement ps = con.prepareStatement(sql)) {

		        ps.setInt(1, purchaseId);

		        try (ResultSet rs = ps.executeQuery()) {
		            while (rs.next()) {
		                PurchaseItem item = new PurchaseItem();
		                item.setItemId(rs.getInt("itemId"));
		                item.setPurchaseId(rs.getInt("purchaseId"));
		                item.setProductId(rs.getInt("productId"));
		                item.setQuantity(rs.getInt("quantity"));
		                item.setPrice(rs.getDouble("price"));

		                list.add(item);
		            }
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return list;
	}

}
