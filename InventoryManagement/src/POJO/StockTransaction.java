package POJO;

import java.sql.Timestamp;

public class StockTransaction 
{
	    private int transactionId;
	    private int productId;
	    private int supplierId;   
	    private String type;      
	    private int quantity;
	    private String remarks;
	    private Timestamp date;
	    
	    
		public StockTransaction() {
			super();
			// TODO Auto-generated constructor stub
		}


		public StockTransaction(int transactionId, int productId, int supplierId, String type, int quantity,
				String remarks, Timestamp date) {
			super();
			this.transactionId = transactionId;
			this.productId = productId;
			this.supplierId = supplierId;
			this.type = type;
			this.quantity = quantity;
			this.remarks = remarks;
			this.date = date;
		}


		public StockTransaction(int productId, int supplierId, String type, int quantity, String remarks) {
			super();
			this.productId = productId;
			this.supplierId = supplierId;
			this.type = type;
			this.quantity = quantity;
			this.remarks = remarks;
		}


		public int getTransactionId() {
			return transactionId;
		}


		public void setTransactionId(int transactionId) {
			this.transactionId = transactionId;
		}


		public int getProductId() {
			return productId;
		}


		public void setProductId(int productId) {
			this.productId = productId;
		}


		public int getSupplierId() {
			return supplierId;
		}


		public void setSupplierId(int supplierId) {
			this.supplierId = supplierId;
		}


		public String getType() {
			return type;
		}


		public void setType(String type) {
			this.type = type;
		}


		public int getQuantity() {
			return quantity;
		}


		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}


		public String getRemarks() {
			return remarks;
		}


		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}


		public Timestamp getDate() {
			return date;
		}


		public void setDate(Timestamp timestamp) {
			this.date = timestamp;
		}


		@Override
		public String toString() {
			return "StockTransaction [transactionId=" + transactionId + ", productId=" + productId + ", supplierId="
					+ supplierId + ", type=" + type + ", quantity=" + quantity + ", remarks=" + remarks + ", date="
					+ date + "]";
		}
		
		
	    
	    
}
