package IMPL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.ProductDao;
import POJO.Product;
import Utility.DBUtility;

public class ProductDaoImpl implements ProductDao
{

	@Override
	public boolean addproduct(Product p) 
	{
		Connection con=DBUtility.getconnection();
		try
		{
			if (p.getQuantity() < 0) {
			    System.out.println("Quantity cannot be negative");
			    return false;
			}

			if (p.getPrice() < 0) {
			    System.out.println("Price cannot be negative");
			    return false;
			}
			
			String sql="insert into product(productName,category,quantity,price)values(?,?,?,?)";
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1,p.getProductName());
			ps.setString(2, p.getCategory());
			ps.setInt(3, p.getQuantity());
			ps.setDouble(4, p.getPrice());
			
			
			int i=ps.executeUpdate();
			
			if(i>0)
			{
				return true;
			}
			else
			{
				return false;
			}
				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean updateproduct(Product p)
	{
		Connection con=DBUtility.getconnection();
		try
		{
			String sql="update product set productName=?,category=?,quantity=?,price=? where productId=?";
			
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1,p.getProductName());
			ps.setString(2, p.getCategory());
			ps.setInt(3, p.getQuantity());
			ps.setDouble(4, p.getPrice());
			ps.setInt(5, p.getProductId());
			
			int i=ps.executeUpdate();
			
			if(i>0)
			{
				return true;
			}
			else
			{
				return false;
			}
				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return false;
	}

	@Override
	public boolean deleteProductById(int productId) 
	{
		Connection con=DBUtility.getconnection();
		try
		{
			String sql="delete from product where productId=?";
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1,productId);
			int i=ps.executeUpdate();
			
			if(i>0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Product getProductById(int productId) 
	{
		Product p=null;
		Connection con=DBUtility.getconnection();
		try
		{
			String sql="select * from product where productId=?";
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, productId);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
			    p = new Product();
	            p.setProductId(rs.getInt("productId"));
	            p.setProductName(rs.getString("productName"));
	            p.setCategory(rs.getString("category"));
	            p.setQuantity(rs.getInt("quantity"));
	            p.setPrice(rs.getDouble("price"));
	            
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return p;
	}

	@Override
	public List<Product> getAllProducts() {
		 Connection con = DBUtility.getconnection();
		    List<Product> productList = new ArrayList<>();
		    
		    try
		    {
		        String sql = "SELECT * FROM product";
		        PreparedStatement ps = con.prepareStatement(sql);
		        ResultSet rs = ps.executeQuery();
		        
		        while (rs.next())
		        {
		            Product p = new Product();
		            p.setProductId(rs.getInt("productId"));
		            p.setProductName(rs.getString("productName"));
		            p.setCategory(rs.getString("category"));
		            p.setQuantity(rs.getInt("quantity"));
		            p.setPrice(rs.getDouble("price"));
		           
		            
		            productList.add(p);
		        }
		    }
		    catch (SQLException e)
		    {
		        e.printStackTrace();
		    }
		    
		    return productList;
	}

	@Override
	public Product getProductByName(String productName) {

	    Connection con = DBUtility.getconnection();
	    Product p = null;

	    try
	    {
	        String sql = "SELECT * FROM product WHERE productName = ?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, productName);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next())
	        {
	            p = new Product();
	            p.setProductId(rs.getInt("productId"));
	            p.setProductName(rs.getString("productName"));
	            p.setCategory(rs.getString("category"));
	            p.setQuantity(rs.getInt("quantity"));
	            p.setPrice(rs.getDouble("price"));
	           
	        }
	    }
	    catch (SQLException e)
	    {
	        e.printStackTrace();
	    }

	    return p;
	}

	@Override
	public boolean updateProductStock(int productId, int qtyToAdd) {
		String sql = "UPDATE product SET quantity = quantity + ? WHERE productId = ?";

	    try (Connection con =DBUtility.getconnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, qtyToAdd);
	        ps.setInt(2, productId);

	        int rows = ps.executeUpdate();
	        return rows > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}
