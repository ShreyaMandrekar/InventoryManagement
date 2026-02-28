package POJO;

public class SalesItem {

    private int itemId;
    private int saleId;
    private int productId;
    private int quantity;
    private double price;
    private double lineTotal;
    
    

    public SalesItem() {
		super();
		// TODO Auto-generated constructor stub
	}


	public SalesItem(int productId, int quantity, double price, double lineTotal) {
		super();
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.lineTotal = lineTotal;
	}

    
	public SalesItem(int itemId, int saleId, int productId, int quantity, double price, double lineTotal) {
		super();
		this.itemId = itemId;
		this.saleId = saleId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.lineTotal = lineTotal;
	}


	public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
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

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    @Override
    public String toString() {
        return "SalesItem [itemId=" + itemId + ", saleId=" + saleId +
                ", productId=" + productId + ", quantity=" + quantity +
                ", price=" + price + ", lineTotal=" + lineTotal + "]";
    }
}
