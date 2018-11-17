package application.model.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import application.model.Item;
import application.model.Market;

/*
*The Class MarketTest.
* 
* @author Charles Garza / ewn133
* This Class tests if all methods are working and functioning properly
*/
public class MarketTest {
	
	private Market test;
	private HashMap<String, Item> item;
	
	/**
	 * Method runs before each test is run, setting up any necessary items.
	 */
	@Before
	public void setUp(){
		item = new HashMap<String, Item>();
		test = new Market(item);
		
		/*
		 * Adding items for set up
		 */
		test.addItem(new Item("Lemons", 45, 0.78));
		test.addItem(new Item("Mangos", 85, 1.79));
		test.addItem(new Item("Watermelon", 65, 3.50));
		test.addItem(new Item("Peaches", 90, 1.43));
	}
	
	/**
	 * Method tests to see if the items are being added
	 */
	@Test
	public void testAddItem() {
		String name = "Lemons";
		
		if(item.containsKey(name))
			return;
		else
			fail("Failed at testAddItem");
	}
	
	/*
	 * Method test to see if the quantity is correctly captured
	 */
	@Test
	public void testGetQuantity() {
		String name = "Mangos";
		Integer quantity = 85;
		
		if(item.get(name).getItemQuantity() == quantity)
			return;
		else
			fail("Failed at testGetQuantity");
	}
	
	/*
	 * Method tests to see if the price was correctly captured
	 */
	@Test
	public void testGetPrice() {
		String name = "Watermelon";
		Double price = 3.50;
		
		if(test.getPrice(name).doubleValue() == price)
			return;
		else
			fail("Failed at testGetPrice");
	}
	
	/*
	 * Method test to see if the quantity was correctly updated
	 */
	@Test
	public void testUpdateQuantity() {
		String name = "Peaches";
		Integer newQuantity = 100;
		
		item.get(name).setItemQuantity(newQuantity);
		
		if(item.get(name).getItemQuantity() == newQuantity)
			return;
		else
			fail("Failed at testUpdateQuantity");
	}
}
