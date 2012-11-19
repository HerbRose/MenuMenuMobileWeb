package com.veliasystems.menumenu.client.ui.administration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMContext;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RestaurantsManagerPanel extends FlowPanel implements IManager, IObserver{

	// controllers
	private RestaurantController restaurantController = RestaurantController
			.getInstance();
	private CityController cityController = CityController.getInstance();
	// END - controllers
	private Button saveRestaurantsButton;

	// pola do obslugi tablicy
	private FlowPanel cointainer = new FlowPanel();
	private Map<Long, CellTable<Restaurant>> restaurantsTables = new HashMap<Long, CellTable<Restaurant>>();
	//private Map<Long, CellTable<Restaurant>> restaurantsTablesDiv = new HashMap<Long, CellTable<Restaurant>>();
	 //pola kolumn tablicy
	private TextColumn<Restaurant> nameColumn;
	private Column<Restaurant, Boolean> isVisibleForAppColumn;
	private Column<Restaurant, Boolean> isClearBoardColumn;
	private TextColumn<Restaurant> addressColumn;
	private Column<Restaurant, String> removeColumn;
	private Column<Restaurant, String> goToRestaruantColumn;
	 //inne
	// END - pola tablicy
	
	private ArrayList<Restaurant> restaurantsCopy;
	//private HashMap<Long, Restaurant> restaurantsCopyMap;
	private String cityString = "city";
	
	
	private long justDeletedItemCity = 0;

	public RestaurantsManagerPanel() {
		restaurantController.addObserver(this);
		setStyleName("barPanel", true);
		show(false);
		add(cointainer);
		
		
	}

	
	private CellTable<Restaurant> createRestaurantTable(Long cityId) {
		CellTable<Restaurant> restaurantsCellTable = new CellTable<Restaurant>();
		restaurantsCellTable.getElement().setId(cityString+cityId);
		restaurantsCellTable.setStyleName("restaurantsCellTable", true);
		nameColumn = new TextColumn<Restaurant>() {

			@Override
			public String getValue(Restaurant restaurant) {
				return restaurant.getName();
			}
		};
		isVisibleForAppColumn = new Column<Restaurant, Boolean>(
				new CheckboxCell(true, false)) {

			@Override
			public Boolean getValue(Restaurant object) {
				return object.isVisibleForApp();
			}
		};
		FieldUpdater<Restaurant, Boolean> visibilityFieldUpdater = new FieldUpdater<Restaurant, Boolean>() {

			@Override
			public void update(int index, Restaurant restaurant,
					Boolean isVisibleForApp) {
				restaurant.setVisibleForApp(isVisibleForApp);
			}
		};
		isVisibleForAppColumn.setFieldUpdater(visibilityFieldUpdater);

		addressColumn = new TextColumn<Restaurant>() {

			@Override
			public String getValue(Restaurant object) {
				return object.getCity() + ", " + object.getAddress();
			}
		};

		// if board is clear the checkBox is checked
		isClearBoardColumn = new Column<Restaurant, Boolean>(new CheckboxCell(
				true, false)) {

			@Override
			public Boolean getValue(Restaurant restaurant) {
				if (restaurant.getMainMenuImageString() == null
						|| restaurant.getMainMenuImageString().equals("")
						|| restaurant.getMainMenuImageString().equals(
								restaurant.getEmptyMenuImageString())) {
					
					return true;
				}
				return false;
			}
		};
		
		removeColumn = new Column<Restaurant, String>(new ButtonCell()) {
			
			@Override
			public String getValue(Restaurant object) {
				return Customization.DELETE;
			}
			 
			@Override
			public void onBrowserEvent(Context context, Element elem,
					Restaurant object, NativeEvent event) {
				if(Window.confirm(Customization.ARE_YOU_SURE_WANT_DELETE + "\n" + object.getName() + " " + object.getCity())){
					justDeletedItemCity = object.getCityId();
					restaurantController.deleteRestaurant(object, RestaurantsManagerPanel.class.getName());
				}
				super.onBrowserEvent(context, elem, object, event);
			}
			
		};
		
		goToRestaruantColumn = new Column<Restaurant, String>(new ButtonCell()) {
			
			@Override
			public String getValue(Restaurant object) {
				return Customization.GO_TO_RESTAURANT;
			}
			
			@Override
			public void onBrowserEvent(Context context, Element elem,
					Restaurant object, NativeEvent event) {
				JQMContext.changePage(restaurantController.restMapView.get(object.getId()));
				super.onBrowserEvent(context, elem, object, event);
			}
		};
		
		FieldUpdater<Restaurant, Boolean> clearBoardFieldUpdater = new FieldUpdater<Restaurant, Boolean>() {

			@Override
			public void update(int index, Restaurant restaurant, Boolean value) {
				restaurant.setClearBoard(value);
				restaurant.setMainMenuImageString(restaurant
						.getEmptyMenuImageString());

			}
		};
		isClearBoardColumn.setFieldUpdater(clearBoardFieldUpdater);
		nameColumn.setSortable(true);
		restaurantsCellTable.addColumn(nameColumn, "Restaurant name");
		restaurantsCellTable.addColumn(addressColumn, "Address");
		restaurantsCellTable.addColumn(isVisibleForAppColumn, "Visibility");
		restaurantsCellTable.addColumn(isClearBoardColumn, "Clear Board");
		restaurantsCellTable.addColumn(removeColumn, "Delete restaurant");
		restaurantsCellTable.addColumn(goToRestaruantColumn, "See details");
		
		return restaurantsCellTable;
	}

	
	private void fillRestaurantsCopy() {
		restaurantsCopy = new ArrayList<Restaurant>();
		restaurantsCopy.addAll(restaurantController.getRestaurantsList());//create COPY of restaurants
	}

	@Override
	public void clearData() {
		fillRestaurantsCopy();

		refreshCitiesLists();
		
		for (Long cityId : restaurantsTables.keySet()) {
			City city = cityController.getCity(cityId);
			if (city == null){
				CellTable<Restaurant> toDelete =  restaurantsTables.get(cityId);
				remove(toDelete.getParent().getParent());
				restaurantsTables.remove(cityId);
				continue;
			}
			
			List<Restaurant> nameSorted = new ArrayList<Restaurant>();
			nameSorted.addAll(restaurantController.getRestaurantsInCity(cityId));
			
			if(nameSorted.size() != 0){
				Collections.sort(nameSorted, new MyComparator());
			}
			restaurantsTables.get(cityId).setRowData(nameSorted);
			restaurantsTables.get(cityId).redraw();
		}

		// TODO
	}

	private void refreshCitiesLists(){//destroy all lists and makes new
		cointainer.clear();
		//tworzy 'listy rozwijane' z restauracjami w danej dzielnicy
				List<City> cities = cityController.getCitiesList();
				restaurantsTables.clear();
				for (final City city : cities) {
					
					CellTable<Restaurant> restaurantsCellTable = createRestaurantTable(city.getId()); //tablica z restauracjami dla danego miasta
					
					saveRestaurantsButton = new Button(Customization.SAVE);
					saveRestaurantsButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							List<Restaurant> restaurantsToSave = new ArrayList<Restaurant>();
							
							for (Restaurant restaurant : restaurantsCopy) {
								if(city.getId() == restaurant.getCityId()){
									restaurantsToSave.add(restaurant);
								}
							}
							
							restaurantController.saveRestaurants(restaurantsToSave);

						}
					});
					
					FlowPanel restaurantTableDiv = new FlowPanel(); //div na strzalke i nazwe dzielnicy
					restaurantTableDiv.setStyleName("restaurantTableDiv", true);
					
					FlowPanel restaurantListDiv = new FlowPanel(); //div ze strzalka nazwa dzielnicy i tablica
					restaurantListDiv.setStyleName("restaurantListDiv", true);
					
					FlowPanel restaurantHeaderDiv = new FlowPanel(); //div na strzalke i nazwe dzielnicy
					restaurantHeaderDiv.setStyleName("restaurantHeaderDiv", true);
					
					Image blackArrowImage = new Image("img/blackArrow.png"); //strzalka
					blackArrowImage.setStyleName("blackArrowImage", true);
					
					final ToggleButton arrowToggleButton = new ToggleButton(blackArrowImage);
					arrowToggleButton.setStyleName("arrowToggleButton", true);
					
					arrowToggleButton.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							if(arrowToggleButton.isDown()){
								showRestaurantTable(restaurantsTables.get(city.getId()), arrowToggleButton, true);
							}else {
								showRestaurantTable(restaurantsTables.get(city.getId()), arrowToggleButton, false);
							}
						}
					});

					
					Label cityNameLabel = new Label(city.getCity()); //nazwa dzielnicy
					cityNameLabel.setStyleName("cityNameLabel", true);
					
					restaurantHeaderDiv.add(arrowToggleButton);
					restaurantHeaderDiv.add(cityNameLabel);
					restaurantListDiv.add(restaurantHeaderDiv);
					restaurantTableDiv.add(restaurantsCellTable);
					restaurantTableDiv.add(saveRestaurantsButton);
					restaurantListDiv.add(restaurantTableDiv);
					
					restaurantsTables.put(city.getId(), restaurantsCellTable);
					showRestaurantTable(restaurantsTables.get(city.getId()), arrowToggleButton, false);
					cointainer.add(restaurantListDiv);
					
				}
				// END - tworzy 'liste rozwijana' z restauracjami w danej dzielnicy
	}
	
	@Override
	public String getName() {
		return Customization.RESTAURATIUN_MANAGER;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
	}

	public void showRestaurantTable( Widget table, ToggleButton arrowToggleButton, boolean isVisable) {
		
		if(isVisable){
			int height = getHeight(table.getElement().getId())+45; //+45 na przycisk zapisz
			table.getParent().setHeight(height+"px");
		}else{
			table.getParent().setHeight("0px");
		}
		
		if(arrowToggleButton != null){
			arrowToggleButton.setStyleName("arrowToggleButtonShow", isVisable);
			arrowToggleButton.setStyleName("arrowToggleButtonHide", !isVisable);
		}
		
	}
	
	public void setProperHeight(Widget table){
		int height = getHeight(table.getElement().getId()) + 15;
		table.getParent().setHeight(height + "px");
	}
	
	@Override
	public void onChange() {
		clearData();
		if(justDeletedItemCity != 0){
			//setProperHeight(restaurantsTables.get(justDeletedItemCity));
		}
		
	}
	
	private native int getHeight(String elementId)/*-{
	return $wnd.document.getElementById(elementId).offsetHeight;
}-*/;
}

class MyComparator implements Comparator<Restaurant> {
	public int compare(Restaurant o1, Restaurant o2) {	
		return o1.getName().compareToIgnoreCase(o2.getName());
	}

}

