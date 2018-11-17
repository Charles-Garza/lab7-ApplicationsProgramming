package application.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Market {
	String itemName, info = "";
	Integer itemQuantity = 0;
	Double itemPrice = 0.0;
	private HashMap<String, Item> itemValues;
	public int count = 0;
	
	public Market(){
		itemValues = new HashMap<String, Item>();
	}
	
	public Market(HashMap<String, Item> item) {
		itemValues = item;
	}
	
	public List<Entry<String, Item>> loadItemsFromFile()
	{
		try{
			/** Creates a new scanner class */
			Scanner scan = new Scanner(new File("data/market.csv"));
			
			/** Reads in the data from the file and stores it in local variables */
			while(scan.hasNext())
			{
				info = scan.nextLine();
				/**
				 * Splits the information read in on the comma and stores it in a 
				 * String array
				 */
				String parts[] = info.split(",");
				itemName = parts[0];
				itemQuantity = Integer.parseInt(parts[1]);
				itemPrice = Double.parseDouble(parts[2]);
				
				addItem(itemValues);
			} 	
			List<Entry<String, Item>> sortedValues = new ArrayList<Entry<String, Item>>(itemValues.entrySet());
			
			Collections.sort(sortedValues, new Comparator<Entry<String, Item>>(){
				@Override
				public int compare(Entry<String, Item> a, Entry<String, Item> b){
					return a.getKey().compareTo(b.getKey());
				}
			});
			
			/** Closes scanner */
			scan.close();
			return sortedValues;
		} catch (IOException e){
			System.out.println(e);
		}
		return null;
	}
	
	public void addItem(HashMap<String, Item> itemValues2){
		if(itemValues.containsKey(itemName) == false)
		{
			itemValues.put(itemName, new Item(itemName, itemQuantity, itemPrice));
			count++;
		}
	}
	
	public Integer getQuantity(String name) {
		if(itemValues.containsKey(name))
			return itemValues.get(name).getItemQuantity();
		return 0;
	}
	
	public Double getPrice(String name){
		if(itemValues.containsKey(name))
			return itemValues.get(name).getItemPrice();
		return 0.0;
	}
	
	public void updateQuantity(String name, Integer quantity){
		if(itemValues.containsKey(name))
		{
			itemValues.get(name).setItemQuantity(quantity);
		}
	}
	
	public void saveItemToFiles(){
		try {
			File file = new File("data/test.csv");
			FileWriter writer = new FileWriter(file);
			
			count = 0;
			for(Entry<String, Item> specificData : itemValues.entrySet()){
				System.out.println(specificData.getValue().getItemName());
				
				if(specificData.getValue().getItemName().equals(itemValues.get(itemName)))
				{
					System.out.println("\nHERE\n");
				}
				
				if(count < itemValues.size() - 1){
					writer.write(specificData.getValue().getItemName() + "," + 
							specificData.getValue().getItemQuantity() + "," + specificData.getValue().getItemPrice() + "\n");
				}
				else{
					writer.write(specificData.getValue().getItemName() + "," + 
							specificData.getValue().getItemQuantity() + "," + specificData.getValue().getItemPrice());
				}
				count++;
			}
			writer.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public Boolean isNumeric(String num){
		try{
			Integer.parseInt(num);
			Double.parseDouble(num);
		} catch (NumberFormatException e){
			System.out.println(e);
			return false;
		}
		return true;
	}
}
