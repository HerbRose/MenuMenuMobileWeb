package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.Days;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyInfoPanelRow;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo.IMyChangeHendler;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;

public class RestaurantsOpenHours extends FlowPanel implements IManager, IObserver {
	
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private List<Restaurant> restaurantList = new ArrayList<Restaurant>();
	
	private Map<Long, FlowPanel> restaurantsPanels = new HashMap<Long, FlowPanel>();
	
	private boolean isCalculated = false;
	
	private String width;
	private int rightCellContainerWidth;
	private int properComboWidth;
	private String widthOfCombo;
	private Restaurant restaurant;
	
	private Label addressLabel = new Label(Customization.RESTAURANTADRESS);
	private Label monLabel = new Label(Days.MONDAY.dayName());
	private Label tueLabel = new Label(Days.TUESDAY.dayName());
	private Label wedLabel = new Label(Days.WEDNESDAY.dayName());
	private Label thurLabel = new Label(Days.THURSDAY.dayName());
	private Label friLabel = new Label(Days.FRIDAY.dayName());
	private Label satLabel = new Label(Days.SATURDAY.dayName());
	private Label sunLabel = new Label(Days.SUNDAY.dayName());
	
	private TextBox addressTextBox = new TextBox();
	private final TextBox monTextBox = new TextBox();
	private final TextBox tueTextBox = new TextBox();
	private final TextBox wedTextBox = new TextBox();
	private final TextBox thurTextBox = new TextBox();
	private final TextBox friTextBox = new TextBox();
	private final TextBox satTextBox = new TextBox();
	private final TextBox sunTextBox = new TextBox();
	
	
	private final MyListCombo monCombo = new MyListCombo(false);
	private final MyListCombo tueCombo = new MyListCombo(false);
	private final MyListCombo wedCombo = new MyListCombo(false);
	private final MyListCombo thurCombo = new MyListCombo(false);
	private final MyListCombo friCombo = new MyListCombo(false);
	private final MyListCombo satCombo = new MyListCombo(false);
	private final MyListCombo sunCombo = new MyListCombo(false);
	
	private FlowPanel monWrapper = new FlowPanel();
	private FlowPanel tueWrapper = new FlowPanel();
	private FlowPanel wedWrapper = new FlowPanel();
	private FlowPanel thuWrapper = new FlowPanel();
	private FlowPanel fridWrapper = new FlowPanel();
	private FlowPanel satWrapper = new FlowPanel();
	private FlowPanel sunWrapper = new FlowPanel();
	
	private Button save = new  Button(Customization.SAVE);
	
	private MyInfoPanelRow monRow;

	private final MyRestaurantInfoPanel restaurantInfoPanel = new MyRestaurantInfoPanel();
	private final MyListCombo restaurantCombo = new MyListCombo(false);
	
	public RestaurantsOpenHours() {
		setStyleName("barPanel", true);
		show(false);
		
		monTextBox.addStyleName("myTextBox nameBox openTextBox");
		tueTextBox.addStyleName("myTextBox nameBox openTextBox");
		wedTextBox.addStyleName("myTextBox nameBox openTextBox");
		thurTextBox.addStyleName("myTextBox nameBox openTextBox");
		friTextBox.addStyleName("myTextBox nameBox openTextBox");
		satTextBox.addStyleName("myTextBox nameBox openTextBox");
		sunTextBox.addStyleName("myTextBox nameBox openTextBox");
		
		monCombo.addStyleName("openCombo");
		tueCombo.addStyleName("openCombo");
		wedCombo.addStyleName("openCombo");
		thurCombo.addStyleName("openCombo");
		friCombo.addStyleName("openCombo");
		satCombo.addStyleName("openCombo");
		sunCombo.addStyleName("openCombo");
		
		monWrapper.addStyleName("openWrapper");
		tueWrapper.addStyleName("openWrapper");
		wedWrapper.addStyleName("openWrapper");
		thuWrapper.addStyleName("openWrapper");
		fridWrapper.addStyleName("openWrapper");
		satWrapper.addStyleName("openWrapper");
		sunWrapper.addStyleName("openWrapper");
		
		monTextBox.setTitle(Days.MONDAY.dayName());
		tueTextBox.setTitle(Days.TUESDAY.dayName());
		wedTextBox.setTitle(Days.WEDNESDAY.dayName());
		thurTextBox.setTitle(Days.THURSDAY.dayName());
		friTextBox.setTitle(Days.FRIDAY.dayName());
		satTextBox.setTitle(Days.SATURDAY.dayName());
		sunTextBox.setTitle(Days.SUNDAY.dayName());
		
		monWrapper.add(monCombo);
		monWrapper.add(monTextBox);
		tueWrapper.add(tueCombo);
		tueWrapper.add(tueTextBox);
		wedWrapper.add(wedCombo);
		wedWrapper.add(wedTextBox);
		thuWrapper.add(thurCombo);
		thuWrapper.add(thurTextBox);
		fridWrapper.add(friCombo);
		fridWrapper.add(friTextBox);
		satWrapper.add(satCombo);
		satWrapper.add(satTextBox);
		sunWrapper.add(sunCombo);
		sunWrapper.add(sunTextBox);
		
		
		
		addressTextBox.setEnabled(false);
		addressTextBox.addStyleName("myTextBox nameBox");
		addressTextBox.getElement().getStyle().setWidth(100, Unit.PCT);
		
		restaurantInfoPanel.addItem(new Label(Customization.NAME), restaurantCombo);
		restaurantInfoPanel.addItem(addressLabel, addressTextBox);
		monRow = restaurantInfoPanel.addItem(monLabel, monWrapper);
		restaurantInfoPanel.addItem(tueLabel, tueWrapper);
		restaurantInfoPanel.addItem(wedLabel, wedWrapper);
		restaurantInfoPanel.addItem(thurLabel, thuWrapper);
		restaurantInfoPanel.addItem(friLabel, fridWrapper);
		restaurantInfoPanel.addItem(satLabel, satWrapper);
		restaurantInfoPanel.addItem(sunLabel, sunWrapper);
		restaurantInfoPanel.addItem(save, save);

	}
	@Override
	public void clearData() {
		
	}

	@Override
	public String getName() {
		return Customization.RESTAURANT_OPEN_HOURS;
	}

	@Override
	public void show(boolean isVisable) {
		clearTableAndFillDaysCombo();
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
		
		if(isVisable){
			PagesController.showWaitPanel();
			restaurantController.refreshRestaurants(this);
		}
		
	}
	@Override
	public void onChange() {	
	}
	@Override
	public void newData() {
		restaurantList.clear();
		restaurantList.addAll(restaurantController.getRestaurantsList());
		fillContentTable();
		PagesController.hideWaitPanel();
	}
	

	
	private void fillContentTable(){
				
		
		restaurantInfoPanel.setStyleName("containerPanelAddRestaurant", true);
		restaurantInfoPanel.setWidth(JS.getElementOffsetWidth(getParent().getElement())-20 );
		for (Restaurant restaurantItem : restaurantList) {
			restaurantCombo.addListItem(restaurantCombo.getNewItem(restaurantItem.getName()), restaurantItem.getId());
		}
		restaurantCombo.addMyChangeHendler(new IMyChangeHendler() {
			
			@Override
			public void onChange() {
				restaurant = restaurantController.getRestaurant(restaurantCombo.getSelectedOrder());
				if(restaurant!= null) fillDetail();
			}
		});
		save.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Map<MyListCombo, TextBox> elementsToValidate = new HashMap<MyListCombo, TextBox>();
				elementsToValidate.put(monCombo, monTextBox);
				elementsToValidate.put(tueCombo, tueTextBox);
				elementsToValidate.put(wedCombo, wedTextBox);
				elementsToValidate.put(thurCombo, thurTextBox);
				elementsToValidate.put(friCombo, friTextBox);
				elementsToValidate.put(satCombo, satTextBox);
				elementsToValidate.put(sunCombo, sunTextBox);
				
				if(restaurant != null && validate(elementsToValidate)){				
					ArrayList<String> openHours = new ArrayList<String>();
					openHours.add(monTextBox.getText().replace(",", ";"));
					openHours.add(tueTextBox.getText().replace(",", ";"));
					openHours.add(wedTextBox.getText().replace(",", ";"));
					openHours.add(thurTextBox.getText().replace(",", ";"));
					openHours.add(friTextBox.getText().replace(",", ";"));
					openHours.add(satTextBox.getText().replace(",", ";"));
					openHours.add(sunTextBox.getText().replace(",", ";"));
					restaurant.setOpenHours(openHours);
					restaurantController.saveRestaurant(restaurant, false);	
				}else{
					
				}
			}
		});
			
				if(!isCalculated){
					width = monRow.getRightCellContainerWidth();
					width = width.replace("px", "");
					
					rightCellContainerWidth = Integer.parseInt(width);
					properComboWidth = rightCellContainerWidth/2;
					widthOfCombo = String.valueOf(properComboWidth + "px");
					isCalculated = true;
				}
				
				monCombo.setWidth(widthOfCombo);
				tueCombo.setWidth(widthOfCombo);
				wedCombo.setWidth(widthOfCombo);
				thurCombo.setWidth(widthOfCombo);
				friCombo.setWidth(widthOfCombo);
				satCombo.setWidth(widthOfCombo);
				sunCombo.setWidth(widthOfCombo);
				
				//position of text box
				monTextBox.getElement().getStyle().setLeft(properComboWidth + 10, Unit.PX);
				tueTextBox.getElement().getStyle().setLeft(properComboWidth + 10, Unit.PX);
				wedTextBox.getElement().getStyle().setLeft(properComboWidth + 10, Unit.PX);
				thurTextBox.getElement().getStyle().setLeft(properComboWidth + 10, Unit.PX);
				friTextBox.getElement().getStyle().setLeft(properComboWidth + 10, Unit.PX);
				satTextBox.getElement().getStyle().setLeft(properComboWidth + 10, Unit.PX);
				sunTextBox.getElement().getStyle().setLeft(properComboWidth + 10, Unit.PX);
				
				//setting width of text box, in order to display correctly
				String widthOfTextBox = String.valueOf(rightCellContainerWidth/2 -20) + "px";
				monTextBox.setWidth(widthOfTextBox);
				tueTextBox.setWidth(widthOfTextBox);
				wedTextBox.setWidth(widthOfTextBox);
				thurTextBox.setWidth(widthOfTextBox);
				friTextBox.setWidth(widthOfTextBox);
				satTextBox.setWidth(widthOfTextBox);
				sunTextBox.setWidth(widthOfTextBox);

				add(restaurantInfoPanel);	
		
		
	}
	
	private void clearTableAndFillDaysCombo(){
		restaurant = null;
		restaurantList.clear();
		restaurantCombo.clear();
		restaurantCombo.selectItem(-1);
		addressTextBox.setText("");
		
		setPlaceholderAndEnable(monTextBox, "", false);
		setPlaceholderAndEnable(tueTextBox, "", false);
		setPlaceholderAndEnable(wedTextBox, "", false);
		setPlaceholderAndEnable(thurTextBox, "", false);
		setPlaceholderAndEnable(friTextBox, "", false);
		setPlaceholderAndEnable(satTextBox, "", false);
		setPlaceholderAndEnable(sunTextBox, "", false);
		
		monCombo.clear();
		wedCombo.clear();
		tueCombo.clear();
		thurCombo.clear();
		friCombo.clear();
		satCombo.clear();
		sunCombo.clear();
		
		
		monCombo.addListItem(monCombo.getNewItem(Customization.CLOSED), 1);
		monCombo.addListItem(monCombo.getNewItem(Customization.OPEN), 2);
		tueCombo.addListItem(tueCombo.getNewItem(Customization.CLOSED), 1);
		tueCombo.addListItem(tueCombo.getNewItem(Customization.OPEN), 2);
		wedCombo.addListItem(wedCombo.getNewItem(Customization.CLOSED), 1);
		wedCombo.addListItem(wedCombo.getNewItem(Customization.OPEN), 2);
		thurCombo.addListItem(thurCombo.getNewItem(Customization.CLOSED), 1);
		thurCombo.addListItem(thurCombo.getNewItem(Customization.OPEN), 2);
		friCombo.addListItem(friCombo.getNewItem(Customization.CLOSED), 1);
		friCombo.addListItem(friCombo.getNewItem(Customization.OPEN), 2);
		satCombo.addListItem(satCombo.getNewItem(Customization.CLOSED), 1);
		satCombo.addListItem(satCombo.getNewItem(Customization.OPEN), 2);
		sunCombo.addListItem(sunCombo.getNewItem(Customization.CLOSED), 1);
		sunCombo.addListItem(sunCombo.getNewItem(Customization.OPEN), 2);
		
		
	}
	private void fillDetail(){
		PagesController.showWaitPanel();
		addressTextBox.setText(restaurant.getAddress());
	
		//preselect items 
		preSelectCombo(restaurant, Days.MONDAY, monCombo, monTextBox);
		preSelectCombo(restaurant, Days.TUESDAY, tueCombo, tueTextBox);
		preSelectCombo(restaurant, Days.WEDNESDAY, wedCombo, wedTextBox);
		preSelectCombo(restaurant, Days.THURSDAY, thurCombo, thurTextBox);
		preSelectCombo(restaurant, Days.FRIDAY, friCombo,  friTextBox);
		preSelectCombo(restaurant, Days.SATURDAY, satCombo, satTextBox);
		preSelectCombo(restaurant, Days.SUNDAY, sunCombo, sunTextBox);
		
		/**
		 * add click handlers to combo box, if is open then add placeholder
		 */
		setClickHandler(restaurant, Days.MONDAY, monCombo, monTextBox);
		setClickHandler(restaurant, Days.TUESDAY, tueCombo, tueTextBox);
		setClickHandler(restaurant, Days.WEDNESDAY, wedCombo, wedTextBox);
		setClickHandler(restaurant, Days.THURSDAY, thurCombo, thurTextBox);
		setClickHandler(restaurant, Days.FRIDAY, friCombo, friTextBox);
		setClickHandler(restaurant, Days.SATURDAY, satCombo, satTextBox);
		setClickHandler(restaurant, Days.SUNDAY, sunCombo, sunTextBox);
		
		PagesController.hideWaitPanel();
							
	}
	
	private boolean validate(Map<MyListCombo,TextBox> elementsToValidate){
		
		Set<Entry<MyListCombo, TextBox>> set = elementsToValidate.entrySet();
		boolean valid = true;
		List<String> errorList = new ArrayList<String>();
		for (Entry<MyListCombo, TextBox> entry : set) {
			if(entry.getKey().getSelectedOrder() == 2){
				if(entry.getValue().getText().isEmpty()){
					valid = false;
					errorList.add(entry.getValue().getTitle());
				}
			}
		}
		if(!valid){
			String msg = Customization.MISSING_OPEN_HOURS +"\n";
			for (String string : errorList) {
				msg += string + "\n";
			}
			PagesController.MY_POP_UP.showError(new Label(msg), new IMyAnswer() {
				
				@Override
				public void answer(Boolean answer) {
					
				}
			});
		}
		return valid;
	}
	
	private void preSelectCombo(Restaurant r,Days day,final MyListCombo myListCombo, final TextBox textBox){

		if(r != null){
			if(r.getOpenHours()!= null){
				String hours = getOpenHoursFromRestaurant(r, day);
				if(hours.isEmpty()){
					myListCombo.selectItem(1);
					setPlaceholderAndEnable(textBox, "", false);
				}else{
					myListCombo.selectItem(2);
					textBox.setText(hours);
				}
			}else{
				myListCombo.selectItem(1);
				setPlaceholderAndEnable(textBox, "", false);
			}
		}
		
	}
	
	private String getOpenHoursFromRestaurant(Restaurant r, Days day){
		if(r.getOpenHours() == null) return "";
		return r.getOpenHours().get(day.getOrder());
	}
	private void setClickHandler(final Restaurant r,final Days day,final MyListCombo widget, final TextBox textBox){
		
		widget.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(widget.getSelectedOrder() == 1){
						setPlaceholderAndEnable(textBox, "", false);
					}
					if(widget.getSelectedOrder() == 2){
						String hours = getOpenHoursFromRestaurant(r, day);
						if(hours.isEmpty()){
							setPlaceholderAndEnable(textBox, Customization.INPUT_HOURS, true);
						}else{
							textBox.setText(hours);
							textBox.setEnabled(true);
						}	
					}
				}
			});
	}
	
	private void setPlaceholderAndEnable(TextBox w, String text, boolean isEnabled){
		if(text.isEmpty()) w.setText("");
		w.setEnabled(isEnabled);
		w.getElement().setAttribute("placeHolder",text);
	}
	
	private native int getHeight(String elementId)/*-{
	
	var children = $wnd.document.getElementById(elementId).childNodes;
	var length = children.length;
	var height = 0;
	for( i=0; i<=length-1; i++ ){
		height+= children[i].offsetHeight;
	}
	
	return height;
}-*/;
}
