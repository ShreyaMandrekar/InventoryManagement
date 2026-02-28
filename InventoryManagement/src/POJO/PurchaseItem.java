package POJO;

public class PurchaseItem {
    private int itemId;
    private int purchaseId;
    private int productId;
    private int quantity;
    private double price;

    
    
	public PurchaseItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PurchaseItem(int purchaseId, int productId, int quantity, double price) {
		super();
		this.purchaseId = purchaseId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}
	public PurchaseItem(int itemId, int purchaseId, int productId, int quantity, double price) {
		super();
		this.itemId = itemId;
		this.purchaseId = purchaseId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}
	public int getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getPurchaseId() {
        return purchaseId;
    }
    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
	@Override
	public String toString() {
		return "PurchaseItem [itemId=" + itemId + ", purchaseId=" + purchaseId + ", productId=" + productId
				+ ", quantity=" + quantity + ", price=" + price + "]";
	}
    
    
}
