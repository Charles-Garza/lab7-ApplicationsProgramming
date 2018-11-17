package application.model;

import java.util.ArrayList;

import application.controller.MainController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Labeled;

public class Item {
	private String item;
	private Integer quantity;
	private Double price;
	MainController controller = new MainController();
	
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
