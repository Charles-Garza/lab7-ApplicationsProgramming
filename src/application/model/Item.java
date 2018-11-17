package application.model;

/**
 * The Class Item.
 * 
 * @author Charles Garza / ewn133
 * This class handles the setting and retrieving of data for our hashmap.
 */
public class Item {
	private String item;
	private Integer quantity;
	private Double price;
	
	public Item(){ }
	
	public Item(String name, int num, double price){
		item = name;
		quantity = num;
		this.price = price;
	}
	
	public String getItemName(){ return item; }
	public void setItemName(String name){ item = name; }
	public Integer getItemQuantity(){ return quantity; }
	public void setItemQuantity(Integer num){ quantity = num; }
	public Double getItemPrice(){ return price; }
	public void setItemPrice(Double price){ this.price = price; }
	
	public String toString(){
		return (getItemName() + "," + getItemQuantity() + "," + getItemPrice());
	}
}
