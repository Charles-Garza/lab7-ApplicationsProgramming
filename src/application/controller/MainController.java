package application.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import application.model.Item;
import application.model.Market;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
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

public class MainController implements EventHandler, Initializable{
	@FXML private Button saveBtn, deleteBtn, createBtn, addBtn, removeBtn;
	@FXML private TextField deleteNameTxtFld, nameTxtFld, quantityTxtFld, priceTxtFld;
	@FXML private TableColumn<Item, String> itemTblColm;
	@FXML private TableColumn<Item, Integer>quantTblColm;
	@FXML private TableColumn<Item, Double> priceTblColm;
	@FXML private TableView<Item> table; 
	@FXML private Label errMsgLbl;
	@FXML private AnchorPane navCreateList, navDeleteList;
	
	private TranslateTransition openCreateNav;
    private TranslateTransition openDeleteNav;
	private TranslateTransition closeCreateNav;
    private TranslateTransition closeDeleteNav;
	
	private HashMap<String, Item> item = new HashMap<String, Item>();
	private ObservableList<Item> data = FXCollections.observableArrayList();
	private ObservableMap<String, Item> map = FXCollections.observableMap(item);
	
	private Market market = new Market();
	private List<Entry<String, Item>> sortedItems;
	private String duplicateItemName;
	private Boolean flag = false;
	
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		prepareSlideCreateAnimation();
		prepareSlideDeleteAnimation();
		try{
			sortedItems = market.loadItemsFromFile();
			
			for(Entry<String, Item> specificData : sortedItems){
				data.add(specificData.getValue());
			}
		} catch(Exception e){
			System.out.println(e);
		}
		
		table.setItems(data);
		table.getSelectionModel().selectFirst();
		
		itemTblColm.setCellValueFactory(new PropertyValueFactory("itemName"));
		itemTblColm.setCellFactory(TextFieldTableCell.forTableColumn());
		
		//Webpage on checking if something equals something on edit
		//https://stackoverflow.com/questions/52060305/check-if-textfieldtablecell-equals-something-on-edit-commit
//		itemTblColm.setCellFactory(cell -> new TextFieldTableCell(new StringConverter<String>() {
//            @Override
//            public String toString(String object) {
//                return object;
//            }
//
//            @Override
//            public String fromString(String string) {
//                return string;
//            }
//        }));
		
		itemTblColm.setOnEditCommit(t -> {
			for(int i = 0; i < table.getItems().size(); i++)
			{
				String info = t.getTableView().getItems().get(
		                i).getItemName();
				
				System.out.println(info);
				
				if(t.getNewValue().equals(data.get(i).getItemName())){
					duplicateItemName = t.getNewValue();
					flag = true;
					break;
				}else{
					flag = false;
				}
			}
			if(flag != true)
			{
				((Item) t.getTableView().getItems().get(
	                t.getTablePosition().getRow())
	                ).setItemName(t.getNewValue());
//				System.out.println("userData: " + t.getTableView().getFocusModel().getFocusedCell().getTableView().getItems());
			}
			else{	
				((Item) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setItemName(t.getOldValue());
			}
	    });
		
		
		quantTblColm.setCellValueFactory(new PropertyValueFactory("itemQuantity"));
		quantTblColm.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter())); 
		quantTblColm.setOnEditCommit(t -> {
	        ((Item) t.getTableView().getItems().get(
	                t.getTablePosition().getRow())
	                ).setItemQuantity((t.getNewValue().intValue()));
	    });
		
		priceTblColm.setCellValueFactory(new PropertyValueFactory("itemPrice"));
		priceTblColm.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		priceTblColm.setOnEditCommit(t -> {
	        ((Item) t.getTableView().getItems().get(
	                t.getTablePosition().getRow())
	                ).setItemPrice((t.getNewValue().doubleValue()));
	    });
		
		table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
                itemTblColm.setText("");
                return;
            }
//                       System.out.println(newValue.getItemName());
        });
	}
	
	/**
     * Handle when the save button is pressed
     * @param event Event
     */
    @Override
	public void handle(Event event) {
    	
    	closeCreateNav.setToX(-(navCreateList.getWidth()));
    	closeCreateNav.play();
    	createBtn.setText("Create");
    	closeDeleteNav.setToX(-(navDeleteList.getWidth()));
    	closeDeleteNav.play();
    	deleteBtn.setText("Delete");
    	
    	if(flag == true){
    		errMsgLbl.setText("Error! Item already exists: " + duplicateItemName);
    		errMsgLbl.setOpacity(1);
    	} else{
    		errMsgLbl.setOpacity(0);
    		market.saveItemToFiles();
    	}
    	
    }
    
    @FXML
    private void createAction(Event event) {
    	System.out.println("Create");
    	closeCreateNav.setToX(-(navCreateList.getWidth()));
    	closeCreateNav.play();
    	createBtn.setText("Create");
    }
    
    @FXML
    private void deleteAction(Event event) {
    	System.out.println("Delete");
    	closeDeleteNav.setToX(-(navDeleteList.getWidth()));
    	closeDeleteNav.play();
    	deleteBtn.setText("Delete");
    }
    
    private void prepareSlideCreateAnimation() {
        openCreateNav = new TranslateTransition(new Duration(350), navCreateList);
        openCreateNav.setToX(0);
        closeCreateNav=new TranslateTransition(new Duration(350), navCreateList);
        closeDeleteNav=new TranslateTransition(new Duration(350), navDeleteList);
        createBtn.setOnAction(e ->{
        	
            if(navCreateList.getTranslateX() != 0 || navDeleteList.getTranslateX() == 0){
            	closeDeleteNav.setToX(-(navDeleteList.getWidth()));
            	closeDeleteNav.play();
            	openCreateNav.play();
            	deleteBtn.setText("Delete");
            	createBtn.setText("Close");
            } else{
            	closeCreateNav.setToX(-(navCreateList.getWidth()));
            	closeCreateNav.play();
            	createBtn.setText("Create");
            }
        });
    }
    
    private void prepareSlideDeleteAnimation() {
        openDeleteNav = new TranslateTransition(new Duration(350), navDeleteList);
        openDeleteNav.setToX(0);
        closeDeleteNav=new TranslateTransition(new Duration(350), navDeleteList);
        closeCreateNav=new TranslateTransition(new Duration(350), navCreateList);
        deleteBtn.setOnAction(e ->{
            if(navDeleteList.getTranslateX() != 0 || navCreateList.getTranslateX() == 0){
            	closeCreateNav.setToX(-(navCreateList.getWidth()));
            	closeCreateNav.play();
            	openDeleteNav.play();
            	createBtn.setText("Create");
            	deleteBtn.setText("Close");
            } else{
            	closeDeleteNav.setToX(-(navDeleteList.getWidth()));
            	closeDeleteNav.play();
            	deleteBtn.setText("Delete");
            }
        });
    }
}
