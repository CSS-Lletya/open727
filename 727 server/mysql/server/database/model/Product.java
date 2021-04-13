package server.database.model;

public enum Product {
	DONATOR_TICKET(13663, 1, 10.00f),
	SUPER_DONATOR(-1, 1, 10.00f),
	CASH_STACK(995, 500000, 4.00f),
	;

	private final int itemID;
	private final int itemAmount;
	private final float itemPrice;
	
	private Product(int itemID, int itemAmount, float itemPrice) {
		this.itemID = itemID;
		this.itemAmount = itemAmount;
		this.itemPrice = itemPrice;
	}
	
	public static Product getProduct(int itemID, float itemPrice) {
		for (Product product : Product.values()) {
			if (product.itemID == itemID && product.itemPrice == itemPrice) {
				return product;
			}
		}
		//Product with such price not found.
		return null;
	}
	
	public int getItemAmount() {
		return itemAmount;
	}
	
	public float getItemPrice() {
		return itemPrice;
	}
	
	public int getItemID() {
		return itemID;
	}
}
