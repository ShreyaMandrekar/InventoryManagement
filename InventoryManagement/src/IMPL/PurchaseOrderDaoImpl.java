package IMPL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.PurchaseOrderDao;
import POJO.PurchaseOrder;
import Utility.DBUtility;

public class PurchaseOrderDaoImpl implements PurchaseOrderDao {

	@Override
	public int createPurchaseOrder(PurchaseOrder order) {
		int generatedId = -1;

        String sql = "INSERT INTO purchase_order (supplierId, status) VALUES (?, ?)";

        try (Connection con = DBUtility.getconnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getSupplierId());
            ps.setString(2, order.getStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return generatedId;
	}

	@Override
	public PurchaseOrder getPurchaseOrderById(int purchaseId) {
		 String sql = "SELECT purchaseId, supplierId, orderDate, status FROM purchase_order WHERE purchaseId = ?";

	        try (Connection con = DBUtility.getconnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, purchaseId);

	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                PurchaseOrder po = new PurchaseOrder();
	                po.setPurchaseId(rs.getInt("purchaseId"));
	                po.setSupplierId(rs.getInt("supplierId"));
	                po.setOrderDate(rs.getTimestamp("orderDate"));
	                po.setStatus(rs.getString("status"));
	                return po;
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return null;
	}

	@Override
	public List<PurchaseOrder> getAllPurchaseOrders() {
		 List<PurchaseOrder> list = new ArrayList<>();

		    String sql = "SELECT * FROM purchase_order";

		    try (Connection conn = DBUtility.getconnection();
		         PreparedStatement ps = conn.prepareStatement(sql);
		         ResultSet rs = ps.executeQuery()) {

		        while (rs.next()) {
		            PurchaseOrder po = new PurchaseOrder();
		            po.setPurchaseId(rs.getInt("purchaseId"));
		            po.setSupplierId(rs.getInt("supplierId"));
		            po.setOrderDate(rs.getTimestamp("orderDate"));
		            po.setStatus(rs.getString("status"));

		            list.add(po);
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return list;
	}

	@Override
	public List<PurchaseOrder> getPurchaseOrdersBySupplier(int supplierId) {
		  List<PurchaseOrder> orders = new ArrayList<>();

		    if (supplierId <= 0) {
		        System.out.println("Invalid Supplier ID");
		        return orders;
		    }

		    String sql = "SELECT purchaseId, supplierId, orderDate, status FROM purchase_order WHERE supplierId = ?";

		    try (Connection conn = DBUtility.getconnection();
		         PreparedStatement ps = conn.prepareStatement(sql)) {

		        ps.setInt(1, supplierId);

		        try (ResultSet rs = ps.executeQuery()) {
		            while (rs.next()) {
		                PurchaseOrder order = new PurchaseOrder();
		                order.setPurchaseId(rs.getInt("purchaseId"));
		                order.setSupplierId(rs.getInt("supplierId"));
		                order.setOrderDate(rs.getTimestamp("orderDate"));
		                order.setStatus(rs.getString("status"));

		                orders.add(order);
		            }
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return orders;
	}

	@Override
	public boolean updatePurchaseOrderStatus(int purchaseId, String status) {

	    String updateStatusSQL =
	        "UPDATE purchase_order SET status = ? WHERE purchaseId = ?";

	    String selectItemsSQL =
	        "SELECT productId, quantity FROM purchase_items WHERE purchaseId = ?";

	    String updateProductSQL =
	        "UPDATE product SET quantity = quantity + ? WHERE productId = ?";

	    String insertStockSQL =
	        "INSERT INTO stock_transactions(productId, supplierId, quantity, type, remarks) " +
	        "VALUES (?, ?, ?, ?, ?)";

	    try (Connection con = DBUtility.getconnection()) {

	        con.setAutoCommit(false);
	        
	        String checkStatusSQL = "SELECT status FROM purchase_order WHERE purchaseId = ?";
	        String currentStatus = "";

	        try (PreparedStatement ps = con.prepareStatement(checkStatusSQL)) {
	            ps.setInt(1, purchaseId);
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                currentStatus = rs.getString("status");
	            }
	        }
	        
	        if (currentStatus.equals("")) {
	            System.out.println("Purchase Order ID not found.");
	            con.rollback();
	            return false;
	        }
	        
	        if (currentStatus.equalsIgnoreCase("Completed")) {
	            System.out.println("Order already completed. Stock already updated.");
	            con.rollback();
	            return false;
	        }

	       
	        int rowsAffected;
	        try (PreparedStatement ps = con.prepareStatement(updateStatusSQL)) {
	            ps.setString(1, status);
	            ps.setInt(2, purchaseId);
	            rowsAffected = ps.executeUpdate();
	        }

	        if (rowsAffected == 0) {
	            System.out.println("Status update failed.");
	            con.rollback();
	            return false;
	        }
	       
	        if (status.equalsIgnoreCase("Completed")) {

	            
	            String supplierSQL = "SELECT supplierId FROM purchase_order WHERE purchaseId = ?";
	            int supplierId = 0;

	            try (PreparedStatement ps = con.prepareStatement(supplierSQL)) {
	                ps.setInt(1, purchaseId);
	                ResultSet rs = ps.executeQuery();
	                if (rs.next()) {
	                    supplierId = rs.getInt("supplierId");
	                }
	            }

	           
	            try (PreparedStatement ps = con.prepareStatement(selectItemsSQL)) {
	                ps.setInt(1, purchaseId);
	                ResultSet rs = ps.executeQuery();

	                while (rs.next()) {
	                    int productId = rs.getInt("productId");
	                    int qty = rs.getInt("quantity");

	                   
	                    try (PreparedStatement ups = con.prepareStatement(updateProductSQL)) {
	                        ups.setInt(1, qty);
	                        ups.setInt(2, productId);
	                        ups.executeUpdate();
	                    }

	                   
	                    try (PreparedStatement sts = con.prepareStatement(insertStockSQL)) {
	                        sts.setInt(1, productId);
	                        sts.setInt(2, supplierId);         
	                        sts.setInt(3, qty);               
	                        sts.setString(4, "IN");             
	                        sts.setString(5, "Purchase Order Completed"); 
	                        sts.executeUpdate();
	                    }
	                }
	            }
	        }

	        con.commit();
	        return true;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
