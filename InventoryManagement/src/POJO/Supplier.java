package POJO;

public class Supplier 
{

	private int supplierId;
	private String supplierName;
	private String contactNumber;
	private String email;
	private String address;
	
	
	public Supplier() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Supplier(int supplierId, String supplierName, String contactNumber, String email, String address) {
		super();
		this.supplierId = supplierId;
		this.supplierName = supplierName;
		this.contactNumber = contactNumber;
		this.email = email;
		this.address = address;
	}


	public Supplier(String supplierName, String contactNumber, String email, String address) {
		super();
		this.supplierName = supplierName;
		this.contactNumber = contactNumber;
		this.email = email;
		this.address = address;
	}


	public int getSupplierId() {
		return supplierId;
	}


	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}


	public String getSupplierName() {
		return supplierName;
	}


	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}


	public String getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	@Override
	public String toString() {
		return "Supplier [supplierId=" + supplierId + ", supplierName=" + supplierName + ", contactNumber="
				+ contactNumber + ", email=" + email + ", address=" + address + "]";
	}
	
	
}
