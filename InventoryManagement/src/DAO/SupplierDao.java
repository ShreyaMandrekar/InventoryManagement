package DAO;

import java.util.List;

import POJO.Supplier;

public interface SupplierDao 
{

	    boolean addSupplier(Supplier s);
	    boolean updateSupplier(Supplier s);
	    boolean deleteSupplierById(int supplierId);
	    Supplier getSupplierById(int supplierId);
	    Supplier getSupplierByName(String supplierName);
	    List<Supplier> getAllSuppliers();
}
