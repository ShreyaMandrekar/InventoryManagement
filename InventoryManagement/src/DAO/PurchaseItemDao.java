package DAO;

import java.util.List;

import POJO.PurchaseItem;

public interface PurchaseItemDao {

	    boolean addPurchaseItems(List<PurchaseItem> items);

	    List<PurchaseItem> getItemsByPurchaseId(int purchaseId);
	}

