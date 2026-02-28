package IMPL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.StockDao;
import POJO.Product;
import POJO.StockTransaction;
import Utility.DBUtility;

public class StockDaoImpl implements StockDao
{

	@Override
	public boolean addStockIn(StockTransaction st) 
	{

	    Connection con = DBUtility.getconnection();

	    try
	    {
	        // 1️⃣ BASIC VALIDATION — quantity must be > 0
	        if (st.getQuantity() <= 0) {
	            System.out.println("Quantity must be greater than zero!");
	            return false;
	        }

	        // 2️⃣ CHECK IF PRODUCT EXISTS
	        String checkProduct = "SELECT productId FROM product WHERE productId = ?";
	        PreparedStatement ps1 = con.prepareStatement(checkProduct);
	        ps1.setInt(1, st.getProductId());
	        ResultSet rs1 = ps1.executeQuery();

	        if (!rs1.next()) {
	            System.out.println("Invalid Product ID!");
	            return false;
	        }

	        // 3️⃣ CHECK IF SUPPLIER EXISTS
	        String checkSupplier = "SELECT supplierId FROM supplier WHERE supplierId = ?";
	        PreparedStatement ps2 = con.prepareStatement(checkSupplier);
	        ps2.setInt(1, st.getSupplierId());
	        ResultSet rs2 = ps2.executeQuery();

	        if (!rs2.next()) {
	            System.out.println("Invalid Supplier ID!");
	            return false;
	        }

	        // 4️⃣ UPDATE PRODUCT QUANTITY (increase stock)
	        String updateSql = "UPDATE product SET quantity = quantity + ? WHERE productId = ?";
	        PreparedStatement ps3 = con.prepareStatement(updateSql);
	        ps3.setInt(1, st.getQuantity());
	        ps3.setInt(2, st.getProductId());

	        int i = ps3.executeUpdate();

	        if (i > 0)
	        {
	            // 5️⃣ INSERT INTO STOCK TRANSACTIONS
	            String insertSql = "INSERT INTO stock_transactions(productId, supplierId, quantity, type, remarks) VALUES (?,?,?,?,?)";
	            PreparedStatement ps4 = con.prepareStatement(insertSql);

	            ps4.setInt(1, st.getProductId());
	            ps4.setInt(2, st.getSupplierId());
	            ps4.setInt(3, st.getQuantity());
	            ps4.setString(4, st.getType());  // "IN"
	            ps4.setString(5, st.getRemarks());

	            int j = ps4.executeUpdate();

	            return j > 0;
	        }
	        else {
	            return false;
	        }

	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }

	    return false;
	}

	@Override
	public boolean addStockOut(StockTransaction st) {

		    Connection con = DBUtility.getconnection();

		    try
		    {
		        // 1️⃣ CHECK IF PRODUCT EXISTS & GET CURRENT QUANTITY
		        String checkSql = "SELECT quantity FROM product WHERE productId = ?";
		        PreparedStatement ps1 = con.prepareStatement(checkSql);
		        ps1.setInt(1, st.getProductId());
		        ResultSet rs = ps1.executeQuery();

		        if (!rs.next()) {
		            // productId not found
		            System.out.println("Product does not exist!");
		            return false;
		        }

		        int currentQty = rs.getInt("quantity");

		        // 2️⃣ VALIDATION — quantity must be available
		        if (st.getQuantity() > currentQty) {
		            System.out.println("Insufficient Stock! Available = " + currentQty);
		            return false;
		        }

		        // 3️⃣ UPDATE PRODUCT QUANTITY (reduce stock)
		        String updateSql = "UPDATE product SET quantity = quantity - ? WHERE productId = ?";
		        PreparedStatement ps2 = con.prepareStatement(updateSql);
		        ps2.setInt(1, st.getQuantity());
		        ps2.setInt(2, st.getProductId());

		        int i = ps2.executeUpdate();

		        if (i > 0)
		        {
		            // 4️⃣ INSERT INTO stock_transactions
		            String insertSql = "INSERT INTO stock_transactions(productId, supplierId, quantity, type, remarks) VALUES (?,?,?,?,?)";
		            PreparedStatement ps3 = con.prepareStatement(insertSql);

		            ps3.setInt(1, st.getProductId());
		            ps3.setNull(2, java.sql.Types.INTEGER);
		            ps3.setInt(3, st.getQuantity());
		            ps3.setString(4, st.getType());   // "OUT"
		            ps3.setString(5, st.getRemarks());

		            int j = ps3.executeUpdate();

		            return j > 0;
		        }
		        else {
		            return false;
		        }
		    }
		    catch(Exception e)
		    {
		        e.printStackTrace();
		    }

		    return false;
		

	}

	@Override
	public List<StockTransaction> getStockByProduct(int productId) {
		
		        List<StockTransaction> stockList = new ArrayList<>();
		        Connection con = DBUtility.getconnection();
		        
		        try {
		            String sql = "SELECT * FROM stock_transactions WHERE productId=?";
		            PreparedStatement ps = con.prepareStatement(sql);
		            ps.setInt(1, productId);
		            ResultSet rs = ps.executeQuery();
		            
		            while(rs.next()) {
		                StockTransaction st = new StockTransaction();
		                st.setTransactionId(rs.getInt("transactionId"));
		                st.setProductId(rs.getInt("productId"));
		                st.setSupplierId(rs.getInt("supplierId"));
		                st.setQuantity(rs.getInt("quantity"));
		                st.setType(rs.getString("type"));
		                st.setRemarks(rs.getString("remarks"));
		                st.setDate(rs.getTimestamp("transactionDate"));
		                
		                stockList.add(st);
		            }
		        } catch(SQLException e) {
		            e.printStackTrace();
		        }
		        return stockList;
	}

	@Override
	public List<StockTransaction> getAllStock() {
		List<StockTransaction> list = new ArrayList<>();
		 Connection con = DBUtility.getconnection();
	    String sql = "SELECT * FROM stock_transactions ORDER BY transactionDate DESC";

	    try
	    {
	        PreparedStatement pst = con.prepareStatement(sql);
	        ResultSet rs = pst.executeQuery();

	        while(rs.next())
	        {
	            StockTransaction st = new StockTransaction();
	            st.setTransactionId(rs.getInt("transactionId"));
	            st.setProductId(rs.getInt("productId"));
	            st.setSupplierId(rs.getInt("supplierId"));
	            st.setQuantity(rs.getInt("quantity"));
	            st.setType(rs.getString("type"));
	            st.setRemarks(rs.getString("remarks"));
	            st.setDate(rs.getTimestamp("transactionDate"));

	            list.add(st);
	        }
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }

	    return list;
	}

	@Override
	public List<Product> getLowStockProducts(int threshold) {

	    List<Product> list = new ArrayList<>();
	    Connection con = DBUtility.getconnection();
	    String sql = "SELECT * FROM product WHERE quantity < ?";

	    try
	    {
	        PreparedStatement pst = con.prepareStatement(sql);
	        pst.setInt(1, threshold);
	        ResultSet rs = pst.executeQuery();

	        while (rs.next())
	        {
	            Product p = new Product();
	            p.setProductId(rs.getInt("productId"));
	            p.setProductName(rs.getString("productName"));
	            p.setCategory(rs.getString("category"));
	            p.setQuantity(rs.getInt("quantity"));
	            p.setPrice(rs.getDouble("price"));
	            

	            list.add(p);
	        }
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }

	    return list;
	}

}
