package GUI;

public interface InventoryManager {
	//Polymorphism(overriding , overloading)
	public void DatabaseOperation();
	public void DatabaseOperation(Item item);
	public String[] getItemNames();
    public String[] getItemPrice();
	public String[] getCartItems();
	public String[] getMatchingItemNames();
}
