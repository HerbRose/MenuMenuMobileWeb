package com.veliasystems.menumenu.client.ui.administration;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RestaurantsManagerPanel extends FlowPanel implements IManager {

	
	private JQMButton saveRestaurantsButton;

	
	private RestaurantController restaurantController = RestaurantController.getInstance();


	private CellTable<Restaurant> restaurantsCellTable;


	private TextColumn<Restaurant> nameColumn;


	private Column<Restaurant, Boolean> isVisibleForAppColumn;


	private Column<Restaurant, Boolean> isClearBoardColumn;


	private TextColumn<Restaurant> addressColumn;


	private ArrayList<Restaurant> restaurantsCopy;


	private HashMap<Long, Restaurant> restaurantsCopyMap;
	
	public RestaurantsManagerPanel() {
		
		setStyleName("barPanel", true);
		show(false);
		
		saveRestaurantsButton = new JQMButton(Customization.SAVE);
		saveRestaurantsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				restaurantController.saveRestaurants(restaurantsCopy);

			}
		});

		restaurantsCellTable = new CellTable<Restaurant>();

		// restaurant name Column
		nameColumn = new TextColumn<Restaurant>() {

			@Override
			public String getValue(Restaurant object) {
				return object.getName();
			}
		};

		// if restaurant is visible the checkBox is checked
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

		// restaurant address Column
		addressColumn = new TextColumn<Restaurant>() {

			@Override
			public String getValue(Restaurant object) {
				return object.getCity() + ", " + object.getAddress();
			}
		};
		restaurantsCellTable.addColumn(nameColumn, "Restaurant name");
		restaurantsCellTable.addColumn(isVisibleForAppColumn, "Visibility");
		restaurantsCellTable.addColumn(addressColumn, "Address");
		restaurantsCellTable.addColumn(isClearBoardColumn, "Clear Board");

		restaurantsCellTable.setRowData(restaurantController
				.getRestaurantsList());
		add(restaurantsCellTable);
		add(saveRestaurantsButton);
	}
	
	private void fillRestaurantsCopy() {
		restaurantsCopy = new ArrayList<Restaurant>();
		restaurantsCopy.addAll(restaurantController.getRestaurantsList());

		restaurantsCopyMap = new HashMap<Long, Restaurant>();

		for (Restaurant restaurant : restaurantsCopy) {
			restaurantsCopyMap.put(restaurant.getId(), restaurant);
		}
	}
	
	@Override
	public void clearData() {
		fillRestaurantsCopy();
		restaurantsCellTable.redraw();
		
		//TODO
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

}
