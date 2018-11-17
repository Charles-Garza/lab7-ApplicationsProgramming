package application.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import application.model.Item;
import application.model.Market;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * The Class MainController.
 * 
 * @author Charles Garza / ewn133
 * This class handles every aspect of the GUI aside from what id handled originally by Main.
 *  This class populates all of the data from the given file into a tableview and allows the 
 *  user to either edit each item's name, price, and/or quantity. The user also will have 
 *  the choice to create and/or delete specific items for the tableview.
 */
public class MainController implements EventHandler, Initializable{
	
	/** The remove btn. */
	@FXML private Button saveBtn, deleteBtn, createBtn, addBtn, removeBtn;
	
	/** The price txt fld. */
	@FXML private TextField deleteNameTxtFld, nameTxtFld, quantityTxtFld, priceTxtFld;
	
	/** The item tbl colm. */
	@FXML private TableColumn<Item, String> itemTblColm;
	
	/** The quant tbl colm. */
	@FXML private TableColumn<Item, Integer>quantTblColm;
	
	/** The price tbl colm. */
	@FXML private TableColumn<Item, Double> priceTblColm;
	
	/** The table. */
	@FXML private TableView<Item> table; 
	
	/** The delete err msg. */
	@FXML private Label errMsgLbl, nameErrImg, quantityErrImg, priceErrImg, createErrMsg, 
	deleteErrMsg;
	
	/** The nav delete list. */
	@FXML private AnchorPane navCreateList, navDeleteList;
	
	/** The open create nav. */
	private TranslateTransition openCreateNav;
    
    /** The open delete nav. */
    private TranslateTransition openDeleteNav;
	
	/** The close create nav. */
	private TranslateTransition closeCreateNav;
    
    /** The close delete nav. */
    private TranslateTransition closeDeleteNav;
	
	/** The item. */
	private HashMap<String, Item> item = new HashMap<String, Item>();
	
	/** The data. */
	private ObservableList<Item> data = FXCollections.observableArrayList();
	
	/** The market. */
	private Market market = new Market(item);
	
	/** The sorted items. */
	private List<Entry<String, Item>> sortedItems;
	
	/** The duplicate item name. */
	private String duplicateItemName;
	
	/** The flag. */
	private Boolean flag = false;
	
	/** The price. */
	public String name = "", quantity = "", price = "";
	
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		/* Prepares the sliding animation of the sidebars */
		prepareSlideCreateAnimation();
		prepareSlideDeleteAnimation();
		/*Tries to read in the data from the file provided */
		try{
			sortedItems = market.loadItemsFromFile();
			
			for(Entry<String, Item> specificData : sortedItems){
				data.add(specificData.getValue());
			}
		} catch(Exception e){
			System.out.println(e);
		}
		
		/* Sets the data into the table */
		table.setItems(data);
		/* Highlights the first row so the user can see where they are currently at */
		table.getSelectionModel().selectFirst();
		
		/* Populate the item table column with information */
		itemTblColm.setCellValueFactory(new PropertyValueFactory("itemName"));
		itemTblColm.setCellFactory(TextFieldTableCell.forTableColumn());
		/* When the field has been edited we check certain conditions before allowing the field to update */
		itemTblColm.setOnEditCommit(t -> {
			/* Checks to see if the new key entered is different from the previous */
			if(!item.containsKey((t.getNewValue().toString())) && (!t.getNewValue().equals(item.get(t.getOldValue()))))
			{
				String oldName = t.getOldValue();
				String name = t.getNewValue();
				Integer quantity = table.getItems().get(t.getTableColumn().getTableView().getEditingCell().getRow()).getItemQuantity();
				Double price = table.getItems().get(t.getTableColumn().getTableView().getEditingCell().getRow()).getItemPrice();
				item.remove(itemTblColm.getCellData(item.get(oldName)));
				item.put(name, new Item(name, quantity, price));
				
				/* Set the item price for the specified cell to the new name */
				((Item) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setItemName(t.getNewValue());
				errMsgLbl.setOpacity(0);
			} /* If the new key is the same as the old key, just revert back to original */
			else if(t.getOldValue().equals((t.getNewValue()))) {
				errMsgLbl.setOpacity(0);
				t.getTableView().setRowFactory(new PropertyValueFactory("itemName"));
			} /* otherwise display an error message with the error */
			else {
				errMsgLbl.setText("Duplicate Item: " + t.getNewValue());
				errMsgLbl.setOpacity(1);
				t.getTableView().setRowFactory(new PropertyValueFactory("itemName"));
			}
	    });
		
		/* Populate the quantity table colum with information */
		quantTblColm.setCellValueFactory(new PropertyValueFactory("itemQuantity"));
		/* Override the build in functions in order to format the way the price points are displayed */
		quantTblColm.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
			@Override
			public Integer fromString(String string) {
				try {
	                Integer intValue = Integer.parseInt(string);
	                return intValue;
	            } catch (NumberFormatException e) {
	                System.out.println(e);
	            }
	            return 0;
			}
			
		}));
		/* When the field has been edited we check certain conditions before allowing the field to update */
		quantTblColm.setOnEditCommit(t -> {
			quantity = t.getOldValue().toString();
			String formattedQuantity = new DecimalFormat("#.##").format(t.getNewValue());
			/* Set the item price for the specified cell to the new quantity */
	        ((Item) t.getTableView().getItems().get(
	                t.getTablePosition().getRow())
	                ).setItemQuantity(Integer.parseInt(formattedQuantity));
	        errMsgLbl.setOpacity(0);
	    });
		
		/* Populate the price table colum with information */
		priceTblColm.setCellValueFactory(new PropertyValueFactory("itemPrice"));
		/* Override the build in functions in order to format the way the price points are displayed */
		priceTblColm.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter() {
			@Override
			public String toString(Double number){
				return new DecimalFormat("$#.##").format(number);
			}
			@Override
			public Double fromString(String string){
				try{
					double value = new DecimalFormat().parse(string).doubleValue();
					return value;
				} catch(ParseException e){
					System.out.println(e);
					return 0.0;
				}
			}
		}));
		/* When the field has been edited we check certain conditions before allowing the field to update */
		priceTblColm.setOnEditCommit(t -> {
			price = t.getOldValue() + "";
			System.out.println(price);
			String formattedPrice = new DecimalFormat("#.##").format(t.getNewValue());
			/* Set the item price for the specified cell to the new price */
	        ((Item) t.getTableView().getItems().get(
	                t.getTablePosition().getRow())
	                ).setItemPrice(Double.parseDouble(formattedPrice));
	        errMsgLbl.setOpacity(0);
	    });
		
		/* Allows the window to highlight wherever the mnouse clicks */
		table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
                itemTblColm.setText("");
                return;
            }
        });
	}
	
	
	/**
	 * Handle when the save button is pressed.
	 *
	 * @param event Event
	 */
    @Override
	public void handle(Event event) {
    	/* Resets all changed values back to default */
    	closeCreateNav.setToX(-(navCreateList.getWidth()));
    	closeCreateNav.play();
    	createBtn.setText("Create");
    	closeDeleteNav.setToX(-(navDeleteList.getWidth()));
    	closeDeleteNav.play();
    	deleteBtn.setText("Delete");
    	
    	/* Check to see if there was a word that already exists in our hashmap */
    	if(flag == true){
    		errMsgLbl.setText("Error! Item already exists: " + duplicateItemName);
    		errMsgLbl.setOpacity(1);
    	} else{
    		errMsgLbl.setOpacity(0);
    		market.saveItemToFiles();
    	}
    	
    }
    
    /**
     * Method handles the creating of new items wanting to be added to the table 
     *
     * @param event Event
     */
    @FXML
    private void createAction(Event event) {
    	String invalidWords = "";
    	name = nameTxtFld.getText(); 
    	quantity = quantityTxtFld.getText(); 
    	price = priceTxtFld.getText(); 
    	
    	int flagCount = 0;
    	
    	/* Checks to verify the field was not empty */
    	if(name.length() > 0){
    		nameErrImg.setOpacity(0);
    	} else{
    		nameErrImg.setOpacity(1);
    		invalidWords += nameTxtFld.getText() + ", ";
    		flagCount++;
    	}
    	
    	/* Checks to see if the field is a integer digit */
    	if(quantity != null && market.isNumeric(quantity)){
    		quantityErrImg.setOpacity(0);
    	} else{
    		quantityErrImg.setOpacity(1);
    		invalidWords += quantityTxtFld.getText() + ", ";
    		flagCount++;
    	}
    	
    	/* Checks to see if the field is a double digit */
    	if(price != null && market.isDouble(price)){
    		priceErrImg.setOpacity(0);
    	} else{
    		priceErrImg.setOpacity(1);
    		invalidWords += priceTxtFld.getText() + ", ";
    		flagCount++;
    	}
    	/* If there were no errors than display and update the changes made */
    	if(flagCount == 0 && !(item.containsKey(name))){
    		createErrMsg.setOpacity(0);
    		
    		String formattedPrice = new DecimalFormat("#.##").format(Double.parseDouble(price));
    		item.put(name, new Item(name, Integer.parseInt(quantity), Double.parseDouble(formattedPrice)));
        	data.add(item.get(name));
        	
        	/* Reset changed values back to default */
        	nameTxtFld.setText("");
        	quantityTxtFld.setText("");
        	priceTxtFld.setText("");
        	closeCreateNav.setToX(-(navCreateList.getWidth()));
        	closeCreateNav.play();
        	createBtn.setText("Create");
    	} /* If the name is in the hashmap, then display an error and do not add it */
    	else if(item.containsKey(name))
    	{
    		createErrMsg.setText("Error: Item already exists!\n" + name);
    		createErrMsg.setOpacity(1);
    	} /* Otherwise display the items that caused the errors */
    	else{
    		if(flagCount > 0)
    		{
    			invalidWords = invalidWords.substring(0, invalidWords.length()-2);
    		}
    		createErrMsg.setText("Error: Invalid formatting!\n" + invalidWords);
    		createErrMsg.setOpacity(1);
    	}
    }
    
    /**
     * Mathod handles the deleting of any items wanting to be removed.
     *
     * @param event Event
     */
    @FXML
    private void deleteAction(Event event) {
    	String name = deleteNameTxtFld.getText();
    	/* Checks to see if the field is not empty and that the table is not empty either */
    	if(name.length() > 0 && item.containsKey(name) && table.getColumns().size() > 0){ 
    		data.remove(item.get(name));
    		item.remove(itemTblColm.getCellData(item.get(name)));
    		
    		data.removeAll(data);
			for(Entry<String, Item> entry : item.entrySet()) {
			    data.add(entry.getValue());
			}
    		
    		deleteErrMsg.setOpacity(0);
    		closeDeleteNav.setToX(-(navDeleteList.getWidth()));
        	closeDeleteNav.play();
        	deleteBtn.setText("Delete");
        	deleteNameTxtFld.setText("");
    	} /* If the field is empty display to enter an item */
    	else if(name.length() == 0){
    		deleteErrMsg.setText("Please enter an item to remove from the inventory!");
    		deleteErrMsg.setOpacity(1);
    	} /* Otherwise display that the item doesn't exist */
    	else{
    		deleteErrMsg.setText("This item does not exist in the inventory!");
    		deleteErrMsg.setOpacity(1);
    	}
    }
    
    /**
     * Method prepares the create sliding animation.
     */
    private void prepareSlideCreateAnimation() {
    	/* Creates a new animatino effect for the sidebar */
        openCreateNav = new TranslateTransition(new Duration(350), navCreateList);
        openCreateNav.setToX(0);
        closeCreateNav = new TranslateTransition(new Duration(350), navCreateList);
        closeDeleteNav = new TranslateTransition(new Duration(350), navDeleteList);
        createBtn.setOnAction(e -> {
        	/* Resets any changed values back to default */
        	nameTxtFld.setText("");
        	quantityTxtFld.setText("");
        	priceTxtFld.setText("");
        	deleteNameTxtFld.setText("");
        	deleteErrMsg.setOpacity(0);
        	createErrMsg.setOpacity(0);
        	priceErrImg.setOpacity(0);
        	quantityErrImg.setOpacity(0);
        	
        	/* Close the other pane if the other is going to be opened */
            if(navCreateList.getTranslateX() != 0 || navDeleteList.getTranslateX() == 0){
            	closeDeleteNav.setToX(-(navDeleteList.getWidth()));
            	closeDeleteNav.play();
            	openCreateNav.play();
            	deleteBtn.setText("Delete");
            	createBtn.setText("Close");
            } /* Close the side bar when the information has been properly entered */
            else{
            	closeCreateNav.setToX(-(navCreateList.getWidth()));
            	closeCreateNav.play();
            	createBtn.setText("Create");
            }
        });
    }
    
    /**
     * Method prepares the delete sliding animation.
     */
    private void prepareSlideDeleteAnimation() {
    	/* Creates a new animatino effect for the sidebar */
        openDeleteNav = new TranslateTransition(new Duration(350), navDeleteList);
        openDeleteNav.setToX(0);
        closeDeleteNav = new TranslateTransition(new Duration(350), navDeleteList);
        closeCreateNav = new TranslateTransition(new Duration(350), navCreateList);
        deleteBtn.setOnAction(e -> {
        	/* Resets any changed values back to default */
        	nameTxtFld.setText("");
        	quantityTxtFld.setText("");
        	priceTxtFld.setText("");
        	deleteNameTxtFld.setText("");
        	deleteErrMsg.setOpacity(0);
        	createErrMsg.setOpacity(0);
        	priceErrImg.setOpacity(0);
        	quantityErrImg.setOpacity(0);
        	
        	/* Close the other pane if the other is going to be opened */
        	if(navDeleteList.getTranslateX() != 0 || navCreateList.getTranslateX() == 0){
            	closeCreateNav.setToX(-(navCreateList.getWidth()));
            	closeCreateNav.play();
            	openDeleteNav.play();
            	createBtn.setText("Create");
            	deleteBtn.setText("Close");
            } /* Close the side bar when the information has been properly entered */
        	else{
            	closeDeleteNav.setToX(-(navDeleteList.getWidth()));
            	closeDeleteNav.play();
            	deleteBtn.setText("Delete");
            }
        });
    }
}
