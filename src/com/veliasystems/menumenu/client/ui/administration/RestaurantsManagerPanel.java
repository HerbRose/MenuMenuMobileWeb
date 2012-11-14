package com.veliasystems.menumenu.client.ui.administration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RestaurantsManagerPanel extends FlowPanel implements IManager {

	// controllers
	private RestaurantController restaurantController = RestaurantController
			.getInstance();
	private CityController cityController = CityController.getInstance();
	// END - controllers
	private JQMButton saveRestaurantsButton;

	// pola do obslugi tablicy
	private Map<Long, CellTable<Restaurant>> restaurantsTables = new HashMap<Long, CellTable<Restaurant>>();
	//private Map<Long, CellTable<Restaurant>> restaurantsTablesDiv = new HashMap<Long, CellTable<Restaurant>>();
	 //pola kolumn tablicy
	private TextColumn<Restaurant> nameColumn;
	private Column<Restaurant, Boolean> isVisibleForAppColumn;
	private Column<Restaurant, Boolean> isClearBoardColumn;
	private TextColumn<Restaurant> addressColumn;
	 //inne
	// END - pola tablicy
	
	private ArrayList<Restaurant> restaurantsCopy;
	//private HashMap<Long, Restaurant> restaurantsCopyMap;
	private String cityString = "city";

	public RestaurantsManagerPanel() {

		setStyleName("barPanel", true);
		show(false);

		

		//tworzy 'listy rozwijane' z restauracjami w danej dzielnicy
		List<City> cities = cityController.getCitiesList();
		for (final City city : cities) {
			CellTable<Restaurant> restaurantsCellTable = createRestaurantTable(city.getId()); //tablica z restauracjami dla danego miasta
			
			saveRestaurantsButton = new JQMButton(Customization.SAVE);
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
			add(restaurantListDiv);
			
		}
		// END - tworzy 'liste rozwijana' z restauracjami w danej dzielnicy
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
		
		return restaurantsCellTable;
	}

	
	private void fillRestaurantsCopy() {
		restaurantsCopy = new ArrayList<Restaurant>();
		restaurantsCopy.addAll(restaurantController.getRestaurantsList());//create COPY of restaurants
	}

	@Override
	public void clearData() {
		fillRestaurantsCopy();

		for (Long cityId : restaurantsTables.keySet()) {
			City city = cityController.getCity(cityId);
			if (city == null)
				continue;

//			restaurantsTables.get(cityId).setRowData(
//					restaurantController.getRestaurantsInCity(cityId));
			
			
//			ListHandler<Restaurant> columnSortHandler = new ListHandler<Restaurant>(restaurantController.getRestaurantsInCity(cityId)){
//			};
//	
//			columnSortHandler.setComparator(nameColumn, new Comparator<Restaurant>() {
//				
//				@Override
//				public int compare(Restaurant o1, Restaurant o2) {
//					if(o1 == o2) return 0;
//					
//						if(o1 != null){			
//							return (o2 != null) ? o1.getName().compareTo(o2.getName()) : 1;
//						}
//					return -1;
//				}
//			});
			
			List<Restaurant> nameSorted = restaurantController.getRestaurantsInCity(cityId);
			if(nameSorted.size() != 0){
				Collections.sort(nameSorted, new MyComparator());
			}
			restaurantsTables.get(cityId).setRowData(nameSorted);
			
			
			restaurantsTables.get(cityId).redraw();
		}

		// TODO
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
			int width = getHeight(table.getElement().getId())+45; //+45 na przycisk zapisz
			table.getParent().setHeight(width+"px");
		}else{
			table.getParent().setHeight("0px");
		}
		
		
		if(arrowToggleButton != null){
			arrowToggleButton.setStyleName("arrowToggleButtonShow", isVisable);
			arrowToggleButton.setStyleName("arrowToggleButtonHide", !isVisable);
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

