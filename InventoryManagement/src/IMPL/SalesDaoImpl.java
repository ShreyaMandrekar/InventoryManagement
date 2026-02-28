package IMPL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.SalesDao;
import POJO.Sales;
import POJO.SalesItem;
import Utility.DBUtility;

public class SalesDaoImpl implements SalesDao{

	@Override
	public boolean createSale(Sales sale, List<SalesItem> items) {
		 Connection con = null;
	        PreparedStatement psSale = null;
	        PreparedStatement psItem = null;
	        PreparedStatement psStock = null;
	        PreparedStatement psPrice = null;
	        PreparedStatement psStockCheck=null;
	        ResultSet rs = null;
	        ResultSet rsPrice = null;
	        ResultSet rsStock=null;

	        String insertSale = "INSERT INTO sales (customerName, totalAmount) VALUES (?, ?)";
	        String insertItem = "INSERT INTO sale_items (saleId, productId, quantity, price, lineTotal) VALUES (?, ?, ?, ?, ?)";
	        String updateStock = "UPDATE product SET quantity = quantity - ? WHERE productId = ?";
	        String selectPrice = "SELECT price FROM product WHERE productId = ?";

	        try {
	            con = DBUtility.getconnection();
	            con.setAutoCommit(false); // start transaction

	            String checkStock = "SELECT quantity FROM product WHERE productId = ?";

	            psStockCheck = con.prepareStatement(checkStock);

	            for (SalesItem item : items) {
	            	
	            	 // 🛑 REJECT INVALID QUANTITY HERE
	                if (item.getQuantity() <= 0) {
	                    throw new SQLException(
	                        "Invalid quantity for productId " + item.getProductId() + 
	                        ". Quantity must be greater than 0"
	                    );
	                }
	            	
	                psStockCheck.setInt(1, item.getProductId());
	                rsStock = psStockCheck.executeQuery();

	                if (rsStock.next()) {
	                    int available = rsStock.getInt("quantity");
	                    if (available < item.getQuantity()) {
	                        throw new SQLException(
	                            "Insufficient stock for productId " + item.getProductId() +
	                            ". Available: " + available + ", Requested: " + item.getQuantity()
	                        );
	                    }
	                } else {
	                    throw new SQLException("Product ID " + item.getProductId() + " not found.");
	                }
	            }

	            
	            double totalAmount = 0;

	            // Fetch prices and calculate line totals
	            psPrice = con.prepareStatement(selectPrice);
	            for (SalesItem item : items) {
	                psPrice.setInt(1, item.getProductId());
	                rsPrice = psPrice.executeQuery();
	                if (rsPrice.next()) {
	                    double price = rsPrice.getDouble("price");
	                    item.setPrice(price);
	                    item.setLineTotal(price * item.getQuantity());
	                    totalAmount += item.getLineTotal();
	                } else {
	                    throw new SQLException("Product ID " + item.getProductId() + " not found.");
	                }
	                rsPrice.close();
	            }

	            sale.setTotalAmount(totalAmount);

	            // Insert into sales table
	            psSale = con.prepareStatement(insertSale, PreparedStatement.RETURN_GENERATED_KEYS);
	            psSale.setString(1, sale.getCustomerName());
	            psSale.setDouble(2, sale.getTotalAmount());
	            int saleRows = psSale.executeUpdate();

	            if (saleRows == 0) throw new SQLException("Creating sale failed, no rows affected.");

	            rs = psSale.getGeneratedKeys();
	            if (rs.next()) {
	                int saleId = rs.getInt(1);
	                sale.setSaleId(saleId);
	            } else {
	                throw new SQLException("Creating sale failed, no ID obtained.");
	            }

	            // Insert sale items and update product stock
	            psItem = con.prepareStatement(insertItem, PreparedStatement.RETURN_GENERATED_KEYS);
	            psStock = con.prepareStatement(updateStock);

	            for (SalesItem item : items) {
	                item.setSaleId(sale.getSaleId());

	                psItem.setInt(1, item.getSaleId());
	                psItem.setInt(2, item.getProductId());
	                psItem.setInt(3, item.getQuantity());
	                psItem.setDouble(4, item.getPrice());
	                psItem.setDouble(5, item.getLineTotal());
	                psItem.executeUpdate();

	                // Fetch the auto-generated item ID
	                ResultSet rsItem = psItem.getGeneratedKeys();
	                if (rsItem.next()) {
	                    item.setItemId(rsItem.getInt(1));
	                }
	                rsItem.close();
	                
	                psStock.setInt(1, item.getQuantity());
	                psStock.setInt(2, item.getProductId());
	                psStock.executeUpdate();
	                
	             // 🔥 Add OUT transaction into stock_transactions
	                String insertStockSQL =
	                    "INSERT INTO stock_transactions(productId, supplierId, quantity, type, remarks) " +
	                    "VALUES (?, NULL, ?, 'OUT', ?)";

	                try (PreparedStatement psTrans = con.prepareStatement(insertStockSQL)) {
	                    psTrans.setInt(1, item.getProductId());
	                    psTrans.setInt(2, item.getQuantity());
	                    psTrans.setString(3, "Sale ID: " + sale.getSaleId());
	                    psTrans.executeUpdate();
	                }
	            }

	            con.commit();
	            return true;

	        } catch (SQLException e) {
	            try {
	                if (con != null) con.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	            e.printStackTrace();
	            return false;

	        } finally {
	            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
	            try { if (rsPrice != null) rsPrice.close(); } catch (SQLException e) { e.printStackTrace(); }
	            try { if (psSale != null) psSale.close(); } catch (SQLException e) { e.printStackTrace(); }
	            try { if (psItem != null) psItem.close(); } catch (SQLException e) { e.printStackTrace(); }
	            try { if (psStock != null) psStock.close(); } catch (SQLException e) { e.printStackTrace(); }
	            try { if (psPrice != null) psPrice.close(); } catch (SQLException e) { e.printStackTrace(); }
	            try { if (con != null) con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
	        }
	    
	}


	@Override
	public Sales getSaleById(int saleId) {
		 Connection con = null;
		    PreparedStatement psSale = null;
		    PreparedStatement psItems = null;
		    ResultSet rsSale = null;
		    ResultSet rsItems = null;

		    Sales sale = null;

		    String selectSale = "SELECT * FROM sales WHERE saleId = ?";
		    String selectItems = "SELECT * FROM sale_items WHERE saleId = ?";

		    try {
		        con = DBUtility.getconnection();

		        // Fetch sale info
		        psSale = con.prepareStatement(selectSale);
		        psSale.setInt(1, saleId);
		        rsSale = psSale.executeQuery();

		        if (rsSale.next()) {
		            sale = new Sales();
		            sale.setSaleId(rsSale.getInt("saleId"));
		            sale.setCustomerName(rsSale.getString("customerName"));
		            sale.setSaleDate(rsSale.getTimestamp("saleDate"));
		            sale.setTotalAmount(rsSale.getDouble("totalAmount"));

		            // Fetch sale items
		            psItems = con.prepareStatement(selectItems);
		            psItems.setInt(1, saleId);
		            rsItems = psItems.executeQuery();

		            List<SalesItem> items = new ArrayList<>();
		            while (rsItems.next()) {
		                SalesItem item = new SalesItem();
		                item.setItemId(rsItems.getInt("itemId"));
		                item.setSaleId(rsItems.getInt("saleId"));
		                item.setProductId(rsItems.getInt("productId"));
		                item.setQuantity(rsItems.getInt("quantity"));
		                item.setPrice(rsItems.getDouble("price"));
		                item.setLineTotal(rsItems.getDouble("lineTotal"));
		                items.add(item);
		            }

		            // Optional: print items in DAO or return them with sale
		            // For now we can attach them to a method like getSaleItems(saleId) if needed
		            // Or you can return sale object and fetch items separately in test
		            System.out.println("Sale Items:");
		            for (SalesItem si : items) {
		                System.out.println(si);
		            }

		        } else {
		            System.out.println("Sale ID " + saleId + " not found.");
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        try { if (rsSale != null) rsSale.close(); } catch (SQLException e) { e.printStackTrace(); }
		        try { if (rsItems != null) rsItems.close(); } catch (SQLException e) { e.printStackTrace(); }
		        try { if (psSale != null) psSale.close(); } catch (SQLException e) { e.printStackTrace(); }
		        try { if (psItems != null) psItems.close(); } catch (SQLException e) { e.printStackTrace(); }
		        try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		    }

		    return sale;
	}

	@Override
	public List<SalesItem> getSaleItems(int saleId) {
		  List<SalesItem> items = new ArrayList<>();
		    Connection con = null;
		    PreparedStatement ps = null;
		    ResultSet rs = null;

		    String query = "SELECT * FROM sale_items WHERE saleId = ?";

		    try {
		        con = DBUtility.getconnection();
		        ps = con.prepareStatement(query);
		        ps.setInt(1, saleId);
		        rs = ps.executeQuery();

		        while (rs.next()) {
		            SalesItem item = new SalesItem();
		            item.setItemId(rs.getInt("itemId"));
		            item.setSaleId(rs.getInt("saleId"));
		            item.setProductId(rs.getInt("productId"));
		            item.setQuantity(rs.getInt("quantity"));
		            item.setPrice(rs.getDouble("price"));
		            item.setLineTotal(rs.getDouble("lineTotal"));
		            items.add(item);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
		        try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		    }

		    return items;
	}

	@Override
	public List<Sales> getAllSales() {
		 List<Sales> salesList = new ArrayList<>();

		    Connection con = null;
		    PreparedStatement ps = null;
		    ResultSet rs = null;

		    String query = "SELECT * FROM sales";

		    try {
		        con = DBUtility.getconnection();
		        ps = con.prepareStatement(query);
		        rs = ps.executeQuery();

		        while (rs.next()) {
		            Sales sale = new Sales();
		            sale.setSaleId(rs.getInt("saleId"));
		            sale.setCustomerName(rs.getString("customerName"));
		            sale.setSaleDate(rs.getTimestamp("saleDate"));
		            sale.setTotalAmount(rs.getDouble("totalAmount"));

		            salesList.add(sale);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
		        try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		    }

		    return salesList;
	}

	@Override
	public double getTotalRevenue() {
		Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    String query = "SELECT SUM(totalAmount) AS revenue FROM sales";

	    try {
	        con = DBUtility.getconnection();
	        ps = con.prepareStatement(query);
	        rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getDouble("revenue");  // SUM result
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
	        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
	        try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
	    }

	    return 0;
	}

	
	public boolean updateSaleItemQuantity(int saleId, int productId, int newQty) {

	    if (newQty <= 0) {
	        throw new IllegalArgumentException("Quantity must be greater than 0");
	    }

	    Connection con = null;
	    PreparedStatement psGetOld = null;
	    PreparedStatement psUpdateItem = null;
	    PreparedStatement psUpdateStock = null;
	    PreparedStatement psInsertTxn = null;
	    ResultSet rs = null;

	    try {
	        con = DBUtility.getconnection();
	        con.setAutoCommit(false);

	        // 1️⃣ Fetch OLD quantity
	        String getOldQty =
	            "SELECT quantity FROM sale_items WHERE saleId = ? AND productId = ?";
	        psGetOld = con.prepareStatement(getOldQty);
	        psGetOld.setInt(1, saleId);
	        psGetOld.setInt(2, productId);
	        rs = psGetOld.executeQuery();

	        if (!rs.next()) {
	            throw new SQLException("Sale item not found");
	        }

	        int oldQty = rs.getInt("quantity");

	        // 2️⃣ Calculate difference
	        int diff = newQty - oldQty;

	        if (diff == 0) {
	            return true; // nothing to change
	        }

	        // 3️⃣ Check stock if quantity increased
	        if (diff > 0) {
	            String checkStock =
	                "SELECT quantity FROM product WHERE productId = ?";
	            PreparedStatement psCheck = con.prepareStatement(checkStock);
	            psCheck.setInt(1, productId);
	            ResultSet rsStock = psCheck.executeQuery();

	            if (!rsStock.next() || rsStock.getInt("quantity") < diff) {
	                throw new SQLException("Insufficient stock for update");
	            }
	            rsStock.close();
	            psCheck.close();
	        }

	        // 4️⃣ Update sale_items
	        String updateItem =
	            "UPDATE sale_items SET quantity = ?, lineTotal = price * ? " +
	            "WHERE saleId = ? AND productId = ?";
	        psUpdateItem = con.prepareStatement(updateItem);
	        psUpdateItem.setInt(1, newQty);
	        psUpdateItem.setInt(2, newQty);
	        psUpdateItem.setInt(3, saleId);
	        psUpdateItem.setInt(4, productId);
	        psUpdateItem.executeUpdate();

	        // 5️⃣ Update product stock
	        String updateStock =
	            "UPDATE product SET quantity = quantity - ? WHERE productId = ?";
	        psUpdateStock = con.prepareStatement(updateStock);
	        psUpdateStock.setInt(1, diff);
	        psUpdateStock.setInt(2, productId);
	        psUpdateStock.executeUpdate();

	        // 6️⃣ Insert stock transaction
	        String txnType = diff > 0 ? "OUT" : "IN";
	        int txnQty = Math.abs(diff);

	        String insertTxn =
	            "INSERT INTO stock_transactions " +
	            "(productId, supplierId, quantity, type, remarks) " +
	            "VALUES (?, NULL, ?, ?, ?)";

	        psInsertTxn = con.prepareStatement(insertTxn);
	        psInsertTxn.setInt(1, productId);
	        psInsertTxn.setInt(2, txnQty);
	        psInsertTxn.setString(3, txnType);
	        psInsertTxn.setString(
	            4,
	            "Sale Updated ID: " + saleId
	        );
	        psInsertTxn.executeUpdate();

	        con.commit();
	        return true;

	    } catch (Exception e) {
	        try { if (con != null) con.rollback(); } catch (SQLException ignored) {}
	        e.printStackTrace();
	        return false;

	    } finally {
	        try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
	        try { if (psGetOld != null) psGetOld.close(); } catch (SQLException ignored) {}
	        try { if (psUpdateItem != null) psUpdateItem.close(); } catch (SQLException ignored) {}
	        try { if (psUpdateStock != null) psUpdateStock.close(); } catch (SQLException ignored) {}
	        try { if (psInsertTxn != null) psInsertTxn.close(); } catch (SQLException ignored) {}
	        try { if (con != null) con.close(); } catch (SQLException ignored) {}
	    }
	}

	
	
	@Override
	public boolean deleteSale(int saleId) {

	    Connection con = null;
	    PreparedStatement psDeleteItems = null;
	    PreparedStatement psDeleteSale = null;

	    String deleteItems = "DELETE FROM sale_items WHERE saleId = ?";
	    String deleteSale = "DELETE FROM sales WHERE saleId = ?";

	    try {
	        con = DBUtility.getconnection();
	        con.setAutoCommit(false);  // start transaction
	        
	     // 1️⃣ Fetch sale items
	        String selectItems =
	            "SELECT productId, quantity FROM sale_items WHERE saleId = ?";
	        PreparedStatement psSelect = con.prepareStatement(selectItems);
	        psSelect.setInt(1, saleId);
	        ResultSet rs = psSelect.executeQuery();

	        String restoreStock =
	            "UPDATE product SET quantity = quantity + ? WHERE productId = ?";
	        PreparedStatement psRestore = con.prepareStatement(restoreStock);

	        String insertStock =
	            "INSERT INTO stock_transactions(productId, supplierId, quantity, type, remarks) " +
	            "VALUES (?, NULL, ?, 'IN', ?)";

	        PreparedStatement psTrans = con.prepareStatement(insertStock);

	        while (rs.next()) {
	            int productId = rs.getInt("productId");
	            int qty = rs.getInt("quantity");

	            // restore product stock
	            psRestore.setInt(1, qty);
	            psRestore.setInt(2, productId);
	            psRestore.executeUpdate();

	            // stock transaction entry
	            psTrans.setInt(1, productId);
	            psTrans.setInt(2, qty);
	            psTrans.setString(3, "Sale Deleted ID: " + saleId);
	            psTrans.executeUpdate();
	        }


	        // First delete child rows – sale_items
	        psDeleteItems = con.prepareStatement(deleteItems);
	        psDeleteItems.setInt(1, saleId);
	        psDeleteItems.executeUpdate();

	        // Then delete sale
	        psDeleteSale = con.prepareStatement(deleteSale);
	        psDeleteSale.setInt(1, saleId);
	        int rows = psDeleteSale.executeUpdate();

	        if (rows == 0) {
	            con.rollback();
	            System.out.println("Sale ID not found.");
	            return false;
	        }

	        con.commit();
	        return true;

	    } catch (SQLException ex) {

	        try {
	            if (con != null) con.rollback();
	        } catch (SQLException e2) {
	            e2.printStackTrace();
	        }

	        ex.printStackTrace();
	        return false;

	    } finally {

	        try { if (psDeleteItems != null) psDeleteItems.close(); } catch (SQLException e) {}
	        try { if (psDeleteSale != null) psDeleteSale.close(); } catch (SQLException e) {}
	        try { if (con != null) con.close(); } catch (SQLException e) {}
	    }
	}


	@Override
	public List<Sales> getSalesByProductName(String productName) {
		List<Sales> salesList = new ArrayList<>();
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    String query =
	        "SELECT DISTINCT s.saleId, s.customerName, s.saleDate, s.totalAmount " +
	        "FROM sales s " +
	        "JOIN sale_items si ON s.saleId = si.saleId " +
	        "JOIN product p ON si.productId = p.productId " +
	        "WHERE p.productName LIKE ?";

	    try {
	        con = DBUtility.getconnection();
	        ps = con.prepareStatement(query);
	        ps.setString(1, "%" + productName + "%");
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            Sales sale = new Sales();
	            sale.setSaleId(rs.getInt("saleId"));
	            sale.setCustomerName(rs.getString("customerName"));
	            sale.setSaleDate(rs.getTimestamp("saleDate"));
	            sale.setTotalAmount(rs.getDouble("totalAmount"));

	            salesList.add(sale);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try { if (rs != null) rs.close(); } catch (SQLException e) {}
	        try { if (ps != null) ps.close(); } catch (SQLException e) {}
	        try { if (con != null) con.close(); } catch (SQLException e) {}
	    }

	    return salesList;
	}
	
	
}
