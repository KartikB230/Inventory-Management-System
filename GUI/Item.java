package GUI;

public class Item {
private int id,quantity;
private String name;
private double price;


public Item(int id, String name, double price, int quantity) {
	this.quantity = quantity;
    this.id = id;
    this.name = name;
    this.price = price;
}

public int getId() {
    return id;
}

public String getName() {
    return name;
}

public double getPrice() {
    return price;
}
public int getquantity()
{
	return quantity;
}
public void setquantity(int quantity) {
    this.quantity = quantity;
}
}
