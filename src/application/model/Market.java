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

/**
 * The Class Market.
 * 
 * @author Charles Garza / ewn133
 * This class handles the loading of data to a hashmap and also handles any further 
 *  information that is wanting to be added/edited/deleted. This Class also saves all of 
 *  the information to the original file with the updated information.
 */
public class Market {
	
	/** The info. */
	private String itemName, info = "";
	
	/** The item quantity. */
	private Integer itemQuantity = 0;
	
	/** The item price. */
	private Double itemPrice = 0.0;
	
	/** The item values. */
	private HashMap<String, Item> itemValues;
	
	/** The count. */
	private int count = 0;
	
	/**
	 * Instantiates a new market.
	 */
	/*
	 * Constructor to instantiate and declare a new hashmap of items
	 */
	public Market(){
		itemValues = new HashMap<String, Item>();
	}
	
	/**
	 * Instantiates a new market.
	 * Constructor to link the paramter passed to the hashmap
	 *
	 * @param item the item
	 */
	public Market(HashMap<String, Item> item) {
		itemValues = item;
	}
	
	/**
	 * Load items from file and loads the information into a hashmap in alphabetic order as well.
	 *
	 * @return List<Entry<String, Item>>
	 */
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
				
				/*
				 * Loads information into the hashmap
				 */
				itemValues.put(itemName, new Item(itemName, itemQuantity, itemPrice));
			}
			/* Creates a list to reference with the other entrySet from itemnValues. */
			List<Entry<String, Item>> sortedValues = new ArrayList<Entry<String, Item>>(itemValues.entrySet());
			
			/* Sorts the information in alphabetic order */
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
	
	/**
	 * Adds the item.
	 *
	 * @param item Item
	 */
	public void addItem(Item item){
		itemValues.put(item.getItemName(), item);
		count++;
	}
	
	/**
	 * Gets the quantity.
	 *
	 * @param name String
	 * @return Integer
	 */
	public Integer getQuantity(String name) {
		if(itemValues.containsKey(name))
			return itemValues.get(name).getItemQuantity();
		return 0;
	}
	
	/**
	 * Gets the price.
	 *
	 * @param name String
	 * @return Double
	 */
	public Double getPrice(String name){
		if(itemValues.containsKey(name))
			return getPrice(name).doubleValue();
		return 0.0;
	}
	
	/**
	 * Update quantity.
	 *
	 * @param name String
	 * @param quantity Integer
	 */
	public void updateQuantity(String name, Integer quantity){
		if(itemValues.containsKey(name))
		{
			itemValues.get(name).setItemQuantity(quantity);
		}
	}
	
	/**
	 * Save item to files.
	 */
	public void saveItemToFiles(){
		try {
			File file = new File("data/market.csv");
			FileWriter writer = new FileWriter(file);
			count = 0;
			
			/* For each loop to iterate through every instance of data in our entry map */
			for(Entry<String, Item> specificData : itemValues.entrySet()){
				/* Used in order to save back to the file properly without having extra commas */
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
	
	/**
	 * Checks if is numeric.
	 *
	 * @param num String
	 * @return true, if is integer
	 */
	public Boolean isNumeric(String num){
		try{
			Integer.parseInt(num);
		} catch (NumberFormatException e){
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if is double.
	 *
	 * @param num String
	 * @return true, if is double
	 */
	public boolean isDouble(String num){
		try{
			Double.parseDouble(num);
		} catch (NumberFormatException e){
			return false;
		}
		return true;
	}
}
