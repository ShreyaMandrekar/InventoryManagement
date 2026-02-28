package IMPL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.SupplierDao;
import POJO.Supplier;
import Utility.DBUtility;

public class SupplierDaoImpl implements SupplierDao
{

	@Override
	public boolean addSupplier(Supplier s) {

		 Connection con = DBUtility.getconnection();
		    try 
		    {
		        String sql = "INSERT INTO supplier(supplierName, contactNumber, email, address) VALUES(?,?,?,?)";
		        PreparedStatement ps = con.prepareStatement(sql);

		        ps.setString(1, s.getSupplierName());
		        ps.setString(2, s.getContactNumber());
		        ps.setString(3, s.getEmail());
		        ps.setString(4, s.getAddress());

		        int i = ps.executeUpdate();

		        if(i>0)
		        {
		        	return true;
		        }
		        else
		        {
		        	return false;
		        }
		    }
		    catch (SQLException e) 
		    {
		        e.printStackTrace();
		    }
		    return false;
	}

	@Override
	public boolean updateSupplier(Supplier s) {
	    Connection con = DBUtility.getconnection();
	    try 
	    {
	        String sql = "UPDATE supplier SET supplierName=?, contactNumber=?, email=?, address=? WHERE supplierId=?";
	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setString(1, s.getSupplierName());
	        ps.setString(2, s.getContactNumber());
	        ps.setString(3, s.getEmail());
	        ps.setString(4, s.getAddress());
	        ps.setInt(5, s.getSupplierId());

	        int i = ps.executeUpdate();

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
	public boolean deleteSupplierById(int supplierId) {

	    Connection con = DBUtility.getconnection();
	    try 
	    {
	        String sql = "DELETE FROM supplier WHERE supplierId=?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setInt(1, supplierId);

	        int i = ps.executeUpdate();
	        if(i>0)
	        {
	        	return true;
	        }
	        else
	        {
	        	return false;
	        }
	    } 
	    catch (SQLException e) 
	    {
	        e.printStackTrace();
	    }
	    return false;
	}

	@Override
	public Supplier getSupplierById(int supplierId) {
		Supplier s=null;
	    Connection con = DBUtility.getconnection();
	    try 
	    {
	        String sql = "SELECT * FROM supplier WHERE supplierId=?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setInt(1, supplierId);

	        ResultSet rs = ps.executeQuery();
	        if(rs.next()) 
	        {
	             s = new Supplier();
	               s.setSupplierId(rs.getInt("supplierId"));
	               s.setSupplierName(rs.getString("supplierName"));
	               s.setContactNumber(rs.getString("contactNumber"));
	               s.setEmail(rs.getString("email"));
	               s.setAddress(rs.getString("address"));
	        }
	    } 
	    catch (SQLException e) 
	    {
	        e.printStackTrace();
	    }
	    return s;
	}

	@Override
	public Supplier getSupplierByName(String supplierName) {
		 Supplier s = null;
		    Connection con = DBUtility.getconnection();
		    try 
		    {
		        String sql = "SELECT * FROM supplier WHERE supplierName=?";
		        PreparedStatement ps = con.prepareStatement(sql);
		        ps.setString(1, supplierName);

		        ResultSet rs = ps.executeQuery();
		        if(rs.next()) 
		        {
		        	  s = new Supplier();
		               s.setSupplierId(rs.getInt("supplierId"));
		               s.setSupplierName(rs.getString("supplierName"));
		               s.setContactNumber(rs.getString("contactNumber"));
		               s.setEmail(rs.getString("email"));
		               s.setAddress(rs.getString("address"));
		        }
		    } 
		    catch (SQLException e) 
		    {
		        e.printStackTrace();
		    }
		    return s;
	}

	@Override
	public List<Supplier> getAllSuppliers() {
		List<Supplier> suppliers = new ArrayList<>();
	    Connection con = DBUtility.getconnection();

	    try 
	    {
	        String sql = "SELECT * FROM supplier";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        while(rs.next()) 
	        {
	            Supplier s = new Supplier();
	            s.setSupplierId(rs.getInt("supplierId"));
	            s.setSupplierName(rs.getString("supplierName"));
	            s.setContactNumber(rs.getString("contactNumber"));
	            s.setEmail(rs.getString("email"));
	            s.setAddress(rs.getString("address"));

	            suppliers.add(s);
	        }
	    } 
	    catch(SQLException e) 
	    {
	        e.printStackTrace();
	    }

	    return suppliers;
	}

}
