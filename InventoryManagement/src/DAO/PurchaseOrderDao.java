package DAO;

import java.util.List;

import POJO.PurchaseOrder;

public interface PurchaseOrderDao {

	    int createPurchaseOrder(PurchaseOrder order); // returns generated purchaseId

	    PurchaseOrder getPurchaseOrderById(int purchaseId);

	    List<PurchaseOrder> getAllPurchaseOrders();

	    List<PurchaseOrder> getPurchaseOrdersBySupplier(int supplierId);

	    boolean updatePurchaseOrderStatus(int purchaseId, String status);
	}


