package POJO;

import java.sql.Timestamp;

public class PurchaseOrder {
    private int purchaseId;
    private int supplierId;
    private Timestamp  orderDate;
    private String status;
    
    

    public PurchaseOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
	public PurchaseOrder(int supplierId, Timestamp orderDate, String status) {
		super();
		this.supplierId = supplierId;
		this.orderDate = orderDate;
		this.status = status;
	}

	

	public PurchaseOrder(int purchaseId, int supplierId, Timestamp orderDate, String status) {
		super();
		this.purchaseId = purchaseId;
		this.supplierId = supplierId;
		this.orderDate = orderDate;
		this.status = status;
	}


	public int getPurchaseId() {
        return purchaseId;
    }
    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
	@Override
	public String toString() {
		return "PurchaseOrder [purchaseId=" + purchaseId + ", supplierId=" + supplierId + ", orderDate=" + orderDate
				+ ", status=" + status + "]";
	}
    
    
}
