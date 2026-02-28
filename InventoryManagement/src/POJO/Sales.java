package POJO;

import java.sql.Timestamp;

public class Sales {

    private int saleId;
    private Timestamp saleDate;
    private String customerName;
    private double totalAmount;

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public Timestamp getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Sales [saleId=" + saleId + ", saleDate=" + saleDate +
                ", customerName=" + customerName + ", totalAmount=" + totalAmount + "]";
    }
}
