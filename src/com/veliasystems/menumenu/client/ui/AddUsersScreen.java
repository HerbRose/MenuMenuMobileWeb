package com.veliasystems.menumenu.client.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.form.JQMFieldContainer;
import com.sksamuel.jqm4gwt.form.JQMFieldset;
import com.sksamuel.jqm4gwt.form.elements.IsChecked;
import com.sksamuel.jqm4gwt.form.elements.JQMCheckbox;
import com.sksamuel.jqm4gwt.form.elements.JQMCheckset;
import com.sksamuel.jqm4gwt.form.elements.JQMFormWidget;
import com.sksamuel.jqm4gwt.form.elements.JQMPassword;
import com.sksamuel.jqm4gwt.html.FormLabel;
import com.sksamuel.jqm4gwt.html.Legend;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.controllers.UserType;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;

public class AddUsersScreen extends JQMPage implements HasClickHandlers{
	
	private JQMHeader header;
	private JQMButton backButton;
	private JQMPage pageToBack;
//	private JQMFooter footer;
	
	//przyciski we stopce
	private JQMButton addAdmin;
	private JQMButton addRestaurator;
	private JQMButton addAgent;

	private TabBar tabBar;
	
	//pola do dodawania użytkowników 
	private TextBox inputEmailAdmin;
	private JQMPassword passwordAdmin;
	private JQMPassword passwordAdmin2;
	private TextBox inputEmailAgent;
	private JQMPassword passwordAgent;
	private JQMPassword passwordAgent2;
	private TextBox inputEmailRestaurator;
	private JQMPassword passwordRestaurator;
	private JQMPassword passwordRestaurator2;
	private MultiWordSuggestOracle citySuggest;
	private SuggestBox citySuggestBox;
	private MultiWordSuggestOracle restaurantSuggest;
	private SuggestBox restaurantSuggestBox;
	private JQMButton addCityToCityTextBox;
	private JQMButton addRestaurantToRestaurantTextBox;
	private CellList<String> restaurantCellList;
	private CellList<String> citiesCellList;
	private List<String> addedRestauration;
	private List<String> addedCities;
	private JQMButton saveAdminButton;
	private JQMButton saveAgentButton;
	private JQMButton saveRestauratorButton;
	
	private Label mailLabel;
	private Label passwordLabe;
	private Label repeatPasswordLabe;
	
	private Map<MyJQMCheckbox, Long> checkBoxes;
	private MyJQMCheckset checkBoxesSet;
	private FlowPanel restaurantsManagerPanel;
	private JQMButton setVisibilityButton;
	
	private boolean loaded = false;
	private UserType userType;
	
	private UserController userController = UserController.getInstance();
	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController.getInstance();
	
	private Map<Integer, Widget> panelList = new HashMap<Integer, Widget>();
	private Integer panelCount = 0;
	
	// panele pokazywane po kliknięciu przycisku (np. addAdmin)
	private FlowPanel adminPanel;
	private FlowPanel agentPanel;
	private FlowPanel restauratorPanel;

	private SingleSelectionModel<String> selectionModelCities;
	private SingleSelectionModel<String> selectionModelRestaurant;
	
	private List<Long> restaurantsIdList = new ArrayList<Long>();
	
	public AddUsersScreen(JQMPage back) {
		pageToBack = back;
		userType = userController.getUserType();
		
		setHeader();
		
		setContent();
//		setFooter();
		
		
	}
	
	private void setHeader(){
		
		header = new JQMHeader(Customization.ADD_USER);
		header.setFixed(true);
		add(header);
	}
	
	private void setContent(){
			
		
		tabBar = new TabBar();
		tabBar.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				// TODO Auto-generated method stub
				showPanel(panelList.get(event.getSelectedItem()));
			}
		});
		
		add(tabBar);
		
		switch(userType){
		case ADMIN:
			setAdminButtons();
			break;
		default:
			setAgentButtons();
		}		
	}
		
	private void setAdminButtons(){
		setAdminPanel();		
		setAgentPanel();
	
		setRestauratorPanel();
		addRestaurantsManager();
	}
	
	private void setAdminPanel(){
		adminPanel = new FlowPanel();		
		addAddUserTab(adminPanel, UserType.ADMIN);
		hidePanels();
	}
	private void setAgentPanel(){
		agentPanel = new FlowPanel();
		addAddUserTab(agentPanel, UserType.AGENT);
		hidePanels();
	}
	private void setRestauratorPanel(){
		restauratorPanel = new FlowPanel();
		addAddUserTab(restauratorPanel, UserType.RESTAURATOR);
		
		hidePanels();
	}
	
	private void setAgentButtons(){
		setRestauratorPanel();
		addRestaurator = new JQMButton(Customization.ADD_RESTAURATOR);
		addRestaurator.setIcon(DataIcon.PLUS);
		addRestaurator.setIconPos(IconPos.TOP);
	}

	private void addAddUserTab(FlowPanel panel, UserType userType){
		panelList.put(panelCount++, panel);
		mailLabel = new Label(Customization.INPUT_EMAIL);
		passwordLabe = new Label(Customization.INPUT_PASSWORD);
		repeatPasswordLabe = new Label(Customization.REPEAT_PASSWORD);
		switch (userType) {
		case ADMIN:
			tabBar.addTab(Customization.ADD_ADMIN);
			inputEmailAdmin = new TextBox();
			passwordAdmin = new JQMPassword("");
			passwordAdmin2 = new JQMPassword("");
			saveAdminButton = new JQMButton(Customization.SAVE);
			saveAdminButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(validData(UserType.ADMIN)){
						User admin = new User(inputEmailAdmin.getValue().trim());
						admin.setPassword(passwordAdmin.getValue().trim());
						admin.setAdmin(true);
						addUser(admin);
					}else{
						Window.alert("Wrong data");
					}
					
				}
			});
			
			panel.add(mailLabel);
			panel.add(inputEmailAdmin);
			panel.add(passwordLabe);
			panel.add(passwordAdmin);
			panel.add(repeatPasswordLabe);
			panel.add(passwordAdmin2);
			panel.add(saveAdminButton);
			break;
		case AGENT:
			tabBar.addTab(Customization.ADD_AGENT);
			inputEmailAgent = new TextBox();
			passwordAgent = new JQMPassword("");
			passwordAgent2 = new JQMPassword("");
			citySuggest = new MultiWordSuggestOracle("-");
			citySuggestBox = new SuggestBox(citySuggest);
			addCityToCityTextBox = new JQMButton("+");
			addCityToCityTextBox.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(checkCity(citySuggestBox.getValue().trim())){
							//addedCities = new ArrayList<String>();
							addedCities.add(citySuggestBox.getValue());					
							citiesCellList.setRowData(addedCities);
							citiesCellList.setRowCount(addedCities.size());
							citiesCellList.redraw();
					}
					
				}
			});
			
			TextCell cityTextCell = new TextCell();
			addedCities = new ArrayList<String>();
			citiesCellList = new CellList<String>(cityTextCell);
			selectionModelCities = new SingleSelectionModel<String>();
			citiesCellList.setSelectionModel(selectionModelCities);
			selectionModelCities.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				
				@Override
				public void onSelectionChange(SelectionChangeEvent event) {
					// TODO Auto-generated method stub
					String selected = selectionModelCities.getSelectedObject();
					 if (selected != null) {			 
						 addedCities.remove(selected);
						 selectionModelCities.setSelected(selectionModelCities.getSelectedObject(), false);
				         citiesCellList.setRowData(addedCities);
				         citiesCellList.setRowCount(addedCities.size());
				         citiesCellList.redraw();
				        }
				}
			});
			citiesCellList.setRowData(addedCities);
			citiesCellList.setRowCount(addedCities.size());
			saveAgentButton = new JQMButton(Customization.SAVE);
			saveAgentButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					if(validData(UserType.AGENT)){
						User agent = new User(inputEmailAgent.getValue().trim());
						agent.setPassword(passwordAgent.getValue().trim());
						List<Long> cityId = new ArrayList<Long>();
						for (String cityName : addedCities) {
							for (City city : cityController.getCitiesList()) {
								if(city.getCity().equals(cityName)){
									if(!cityId.contains(city.getId())){
										cityId.add(city.getId());
									}
								}
							}							
						}
						agent.setCitiesId(cityId);
						addUser(agent);
					}else{
						Window.alert("Wrong data");
					}
					
				}
			});
			
			panel.add(mailLabel);
			panel.add(inputEmailAgent);
			panel.add(passwordLabe);
			panel.add(passwordAgent);
			panel.add(repeatPasswordLabe);
			panel.add(passwordAgent2);
			
//			for (City city : cityController.getCitiesList()) {
//				citySuggest.add(city.getCity());
//			}
			
			panel.add(citySuggestBox);
			panel.add(addCityToCityTextBox);
			panel.add(citiesCellList);
			panel.add(saveAgentButton);
			
			
			
			break;
		case RESTAURATOR:
			
			tabBar.addTab(Customization.ADD_RESTAURATOR);
			passwordRestaurator = new JQMPassword("");
			passwordRestaurator2 = new JQMPassword("");
			inputEmailRestaurator = new TextBox();
			restaurantSuggest = new MultiWordSuggestOracle();
			restaurantSuggestBox = new SuggestBox(restaurantSuggest);
			
			addRestaurantToRestaurantTextBox = new JQMButton("+");
			addRestaurantToRestaurantTextBox.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(checkRestaurant(restaurantSuggestBox.getValue())){
						
						setRestauransId(true);
						restaurantCellList.setRowData(addedRestauration);
						restaurantCellList.setRowCount(addedRestauration.size());
						restaurantCellList.redraw();
					}
					
				}
			});
			
			TextCell restaurationTextCell = new TextCell();
			restaurantCellList = new CellList<String>(restaurationTextCell);
			addedRestauration = new ArrayList<String>();
			selectionModelRestaurant = new SingleSelectionModel<String>();
			restaurantCellList.setSelectionModel(selectionModelRestaurant);
			selectionModelRestaurant.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				
				@Override
				public void onSelectionChange(SelectionChangeEvent event) {
					// TODO Auto-generated method stub
					String selected = selectionModelRestaurant.getSelectedObject();
					 if (selected != null) {
						 
						 //addedRestauration.remove(selected);
						 setRestauransId(false);
						 selectionModelRestaurant.setSelected(selectionModelRestaurant.getSelectedObject(), false);
				         restaurantCellList.setRowData(addedRestauration);
				         restaurantCellList.setRowCount(addedRestauration.size());
				         restaurantCellList.redraw();
				        }
				}
			});
			restaurantCellList.setRowData(addedRestauration);
			restaurantCellList.setRowCount(addedRestauration.size());

			//restaurantTextBox.setVisibleItemCount(20);
			saveRestauratorButton = new JQMButton(Customization.SAVE);
			saveRestauratorButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(validData(UserType.RESTAURATOR)){
						User restaurator = new User(inputEmailRestaurator.getValue().trim());
						restaurator.setPassword(passwordRestaurator.getValue().trim());
//						for (Restaurant restaurant : restaurantController.getRestaurantsList()) {
//							for (String restaurationName : addedRestauration) {
//								if(restaurant.getName().equals(restaurationName)){
//									if(!restaurationId.contains(restaurant.getId())){
//										restaurationId.add(restaurant.getId());
//									}
//								}
//							}
//						}
						System.out.println(restaurantsIdList.toString());
						restaurator.setRestaurantsId(restaurantsIdList);
						addUser(restaurator);
					}else{
						Window.alert("Wrong data");
					}
					
				}
			});
			
			panel.add(mailLabel);
			panel.add(inputEmailRestaurator);
			panel.add(passwordLabe);
			panel.add(passwordRestaurator);
			panel.add(repeatPasswordLabe);
			panel.add(passwordRestaurator2);
//			for (Restaurant restaurant : restaurantController.getRestaurantsList()) {
//				restaurantSuggest.add(restaurant.getName() + " (" + Customization.CITYONE + ": " + restaurant.getCity() + " ," + Customization.ADRESS +": "+ restaurant.getAddress() + ")");
//			}
			
			panel.add(restaurantSuggestBox);
			panel.add(addRestaurantToRestaurantTextBox);
			panel.add(restaurantCellList);//restaurantTextBox);
			panel.add(saveRestauratorButton);
			break;
		default:
			break;
		}
		
		add(panel);
		
	}


	
	private void addRestaurantsManager(){
		restaurantsManagerPanel = new FlowPanel();
		setVisibilityButton = new JQMButton(Customization.SET_VISIBILITY);
		setVisibilityButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				Map<Long, Boolean> restaurantMap = new HashMap<Long, Boolean>();
				
				Set<MyJQMCheckbox> key = checkBoxes.keySet();
				
				for (MyJQMCheckbox checkbox : key) {
					
					restaurantMap.put(checkBoxes.get(checkbox), checkbox.isSelected());
				}
				
				restaurantController.setRestaurantsVisable(restaurantMap);
			}
		});
		
		panelList.put(panelCount++, restaurantsManagerPanel);
		tabBar.addTab(Customization.RESTAURATIUN_MANAGER);
		
		checkBoxes = new HashMap<MyJQMCheckbox, Long>();
		checkBoxesSet = new MyJQMCheckset();
		
		for (Restaurant restaurant : restaurantController.getRestaurantsList()) {	
			MyJQMCheckbox cb = checkBoxesSet.addCheck(restaurant.getId()+"", restaurant.getName());
			
			checkBoxes.put(cb, restaurant.getId());
			
			if(restaurant.isVisibleForApp()){
				cb.setValue(true);
			}
		}
		
		restaurantsManagerPanel.add(checkBoxesSet);
		restaurantsManagerPanel.add(setVisibilityButton);
		
		
		add(restaurantsManagerPanel);
	}


	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
	
	private void hidePanels(){
		Set<Integer> panelKeys = panelList.keySet();
		
		for (Integer key : panelKeys) {
			Widget widget = panelList.get(key);
			widget.setStyleName("show", false);
			widget.setStyleName("hide", true);
		}
	}
	
	private void showPanel(Widget widget) {
		hidePanels();
		widget.setStyleName("hide", false);
		widget.setStyleName("show", true);	
	}

	
	private void addUser(User user){
		//user.setPassword(password.getValue());
		userController.addUser(user);
	}
	

	private void clearScreenData(){
		
		switch (userType) {
		case ADMIN:
			inputEmailAdmin.setValue("");
			passwordAdmin.setValue("");
			passwordAdmin2.setValue("");
			
			inputEmailAgent.setValue("");
			passwordAgent.setValue("");
			passwordAgent2.setValue("");
			addedCities.clear();
			citiesCellList.setRowData(addedCities);
			citiesCellList.redraw();
			citySuggestBox.setText("");
			citySuggest.clear();
			for (City city : cityController.getCitiesList()) {
				citySuggest.add(city.getCity());
			}
			
			
			inputEmailRestaurator.setValue("");
			passwordRestaurator.setValue("");
			passwordRestaurator2.setValue("");
			addedRestauration.clear();
			restaurantCellList.setRowData(addedRestauration);
			restaurantCellList.redraw();
			restaurantSuggestBox.setText("");
			restaurantSuggest.clear();
			for (Restaurant restaurant : restaurantController.getRestaurantsList()) {
				restaurantSuggest.add(restaurant.getName() + " (" + Customization.CITYONE + ": " + restaurant.getCity() + " ," + Customization.ADRESS +": "+ restaurant.getAddress() + ")");
			}
			
//			checkBoxesSet.clear();
//			checkBoxes.clear();
//			for (Restaurant restaurant : restaurantController.getRestaurantsList()) {	
//				MyJQMCheckbox cb = checkBoxesSet.addCheck(restaurant.getId()+"", restaurant.getName());
//				
//				checkBoxes.put(cb, restaurant.getId());
//				
//				if(restaurant.isVisibleForApp()){
//					cb.setValue(true);
//				}
//			}
			break;

		default:
			inputEmailRestaurator.setValue("");
			passwordRestaurator.setValue("");
			passwordRestaurator2.setValue("");
			addedRestauration.clear();
			restaurantCellList.setRowData(addedRestauration);
			restaurantCellList.redraw();
			restaurantSuggestBox.setText("");
			break;
		}
		
	}
	
	/**
	 * method doesn't check if lists have correct data
	 * @param userType
	 * @return
	 */
	private boolean validData(UserType userType){
		
		boolean isCorrect = false; 
		
		switch (userType) {
		case ADMIN:
			if(userController.isUserInStor(inputEmailAdmin.getValue().trim()) || inputEmailAdmin.getValue().trim().equals("") || !Util.isValidEmail(inputEmailAdmin.getValue())){
				//TODO some info to the user
			}else{
				isCorrect = true;
			}
			if(passwordAdmin.getValue().trim().equals("") || passwordAdmin2.getValue().trim().equals("") || !passwordAdmin.getValue().equals(passwordAdmin2.getValue())){
				//TODO some info to the user
				isCorrect = false;
			}else{
				
			}
			
			break;
		case AGENT:
			if(userController.isUserInStor(inputEmailAgent.getValue().trim()) ||inputEmailAgent.getValue().trim().equals("") || !Util.isValidEmail(inputEmailAgent.getValue())){
				//TODO some info to the user
			}else{
				isCorrect = true;
			}
			if(passwordAgent.getValue().trim().equals("") || passwordAgent2.getValue().trim().equals("") || !passwordAgent.getValue().equals(passwordAgent2.getValue())){
				//TODO some info to the user
				isCorrect = false;
			}
			if(addedCities.size() < 1){
				//TODO some info to the user
				isCorrect = false;
			}
			
			break;
		case RESTAURATOR:
			if(userController.isUserInStor(inputEmailRestaurator.getValue().trim()) || inputEmailRestaurator.getValue().trim().equals("") || !Util.isValidEmail(inputEmailRestaurator.getValue())){
				//TODO some info to the user
			}else{
				isCorrect = true;
			}
			if(passwordRestaurator.getValue().trim().equals("") || passwordRestaurator2.getValue().trim().equals("") || !passwordRestaurator.getValue().equals(passwordRestaurator2.getValue())){
				//TODO some info to the user
				isCorrect = false;
			}
			if(addedRestauration.size() < 1){
				//TODO some info to the user
				isCorrect = false;
			}
			
			break;
		default:
			break;
		}
		
		return isCorrect;
	}
	private boolean checkRestaurant(String restaurantName){
		int indexOfCity = restaurantName.indexOf("(" + Customization.CITYONE);
		if(indexOfCity < 1) return false;
		String restName = restaurantName.substring(0, indexOfCity - 1);
		List<Restaurant> restaurants = restaurantController.getRestaurantsList();
		List<String> restaurantList = new ArrayList<String>();
		for (Restaurant	 restaurant : restaurants) {
			restaurantList.add(restaurant.getName());
		}
		if(restaurantList.contains(restName)){
			if(!addedRestauration.contains(restaurantName)) {
				return true;
			}
			
		}
		return false;
		
	}
	
	private boolean checkCity(String CityName){
		List<City> cities = cityController.getCitiesList();
		List<String> citiesName = new ArrayList<String>();
		for (City city : cities) {
			citiesName.add(city.getCity());
		}
		if(citiesName.contains(CityName)){
			if(!addedCities.contains(CityName)) return true;
		}
		return false;
	}
	
	private void setRestauransId(boolean isAdded){
		if(isAdded){
			String fullRestName = restaurantSuggestBox.getValue();//selectionModelRestaurant.getSelectedObject();		
			String restNameNoCity = getRestaurationName(fullRestName);
			if(restNameNoCity == null) return;
			String city = getCityName(fullRestName);
			if(city == null) return;
			String adress = getAddresName(fullRestName);
			if(adress == null) return;
			
			for (Restaurant restaurant : restaurantController.getRestaurantsList()) {
				if(restaurant.getName().equals(restNameNoCity) && restaurant.getCity().equals(city) && restaurant.getAddress().equals(adress)){
					addedRestauration.add(restaurantSuggestBox.getValue());
					restaurantsIdList.add(restaurant.getId());
				}
			}
			
		}else{		
			String fullRestName = restaurantSuggestBox.getValue();//selectionModelRestaurant.getSelectedObject();		
			String restNameNoCity = getRestaurationName(fullRestName);
			if(restNameNoCity == null) return;
			String city = getCityName(fullRestName);
			if(city == null) return;
			String adress = getAddresName(fullRestName);
			if(adress == null) return;
			
			for (Restaurant restaurant : restaurantController.getRestaurantsList()) {
				if(restaurant.getName().equals(restNameNoCity) && restaurant.getCity().equals(city) && restaurant.getAddress().equals(adress)){
					addedRestauration.remove(selectionModelRestaurant.getSelectedObject());
					restaurantsIdList.remove(restaurant.getId());
				}
			}	
			
		}
		
	}
	
	private String getRestaurationName(String fullRestName){
		int indexOfCity = fullRestName.indexOf("(" + Customization.CITYONE);
		if (indexOfCity < 1) return null;
		return fullRestName.substring(0, indexOfCity - 1);
	}
	private String getCityName(String fullRestName){
		int indexOfCity = fullRestName.indexOf("(" + Customization.CITYONE);
		if (indexOfCity < 1) return null;
		indexOfCity += Customization.CITY.length();
		int indexOfAdress = fullRestName.indexOf(Customization.ADRESS + ":", indexOfCity);
		if(indexOfAdress < 0) return null;
		return fullRestName.substring(indexOfCity + 3, indexOfAdress - 2);
	}
	private String getAddresName(String fullRestName){
		int indexOfCity = fullRestName.indexOf("(" + Customization.CITYONE);
		if (indexOfCity < 1) return null;
		indexOfCity += Customization.CITY.length();
		int indexOfAdress = fullRestName.indexOf(Customization.ADRESS + ":", indexOfCity);
		if(indexOfAdress < 0) return null;
		indexOfAdress +=Customization.ADRESS.length();
		
		return fullRestName.substring(indexOfAdress + 2, fullRestName.length() - 1);
	}
	
	@Override
	protected void onPageShow() {
		
		clearScreenData();
		
		if(!loaded){
			backButton = new JQMButton("",  pageToBack, Transition.SLIDE );	
			String span = "<span class=\"ui-btn-inner ui-btn-corner-all\"><span class=\"ui-btn-text\" style=\"color: #fff\">"+Customization.BACK+"</span><span class=\"ui-icon ui-icon-arrow-l ui-icon-shadow\"></span></span>";      
			backButton.setIcon(DataIcon.LEFT);
			backButton.setIconPos(IconPos.LEFT);	
			backButton.getElement().setInnerHTML(span);
			backButton.setStyleName("ui-btn-left ui-btn ui-btn-icon-left ui-btn-corner-all ui-shadow ui-btn-down-a ui-btn-up-a ui-btn-up-undefined");		
			header.add(backButton);
			loaded = true;
		}
		Document.get().getElementById("load").setClassName(R.LOADED);
		
		if (panelList != null && !panelList.isEmpty()) {
			showPanel(panelList.get(0));
			
		}
	}
}



/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 15:42:39
 * 
 * 
 */
class MyJQMCheckbox implements HasText, IsChecked, HasValue<Boolean> {

        private final InputElement      input;

        private final FormLabel         label;

        private final String            id;

        MyJQMCheckbox(InputElement input, FormLabel label, String id) {
                this.input = input;
                this.label = label;
                this.id = id;
        }

        @Override
        public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
                return null;
        }

        @Override
        public void fireEvent(GwtEvent<?> event) {
        }

        public String getId() {
                return id;
        }

        public InputElement getInput() {
                return input;
        }

        @Override
        public String getText() {
                return label.getText();
        }

        @Override
        public Boolean getValue() {
                return isSelected();
        }

        @Override
        public boolean isSelected() {
                String style = label.getStyleName();
                
                if (style == null)
                        return false;
//                if (style.contains("ui-btn-down")) {
//                        return !style.contains("ui-checkbox-on");
//                } else {
//                        return style.contains("ui-checkbox-on");
//                }
                return style.contains("ui-btn-active");

        }

        @Override
        public void setText(String text) {
                label.setText(text);
        }

        @Override
        public void setValue(Boolean value) {
                setValue(value, false);
        }

        @Override
        public void setValue(Boolean value, boolean ignored) {
                input.setChecked(value);
                input.setDefaultChecked(value);
        }
}


/**
 * @author Stephen K Samuel samspade79@gmail.com 24 May 2011 08:17:31
 * 
 *         A widget that is composed of 1 or more checkboxes.
 * 
 *         The child checkboxes are grouped together and can be set to be
 *         vertical or horizontal.
 * 
 * @link http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/forms/forms-
 *       checkboxes.html
 * 
 */
class MyJQMCheckset extends JQMFieldContainer implements HasText, HasSelectionHandlers<String>, HasClickHandlers, JQMFormWidget{

        private JQMFieldset             fieldset;

        private Legend                  legend;

        private List<TextBox>           inputs  = new ArrayList<TextBox>();
        private List<FormLabel>         labels  = new ArrayList<FormLabel>();
        private List<MyJQMCheckbox>       checks  = new ArrayList<MyJQMCheckbox>();

        /**
         * Creates a new {@link JQMCheckset} with no label text
         */
        public MyJQMCheckset() {
                this(null);
        }

        /**
         * Creates a new {@link JQMCheckset} with the label set to the given text.
         * 
         * @param text
         *              the display text for the label
         */
        public MyJQMCheckset(String text) {

                fieldset = new JQMFieldset();
                add(fieldset);

                legend = new Legend();
                fieldset.add(legend);

                setText(text);
        }

        @Override
        public HandlerRegistration addBlurHandler(final BlurHandler handler) {
                for (FormLabel label : labels)
                        label.addDomHandler(new ClickHandler() {

                                @Override
                                public void onClick(ClickEvent event) {
                                        handler.onBlur(null);
                                }
                        }, ClickEvent.getType());
                return null;
        }

        /**
         * Add a new check option to the checkset.
         * 
         * @param id
         *              the name of the checkbox
         * @param text
         *              the text to display for the checkbox
         * 
         * @return the {@link JQMCheckbox} instance used to control the added
         *         checkbox
         */
        public MyJQMCheckbox addCheck(String id, String text) {

                TextBox input = new TextBox();
                input.setName(id);
                input.getElement().setId(id);
                input.getElement().setAttribute("type", "checkbox");
                input.setStyleName("gwt-TextBox", true);
                inputs.add(input);

                FormLabel label = new FormLabel();
                label.setFor(id);
                label.setText(text);
                label.setStyleName("ui-btn ui-btn-icon-left ui-btn-down-c ui-btn-up-c", true);
                labels.add(label);

                fieldset.add(input);
                fieldset.add(label);

                InputElement e = input.getElement().cast();
                final MyJQMCheckbox check = new MyJQMCheckbox(e, label, id);
                checks.add(check);
                return check;
        }

        @Override
        public HandlerRegistration addClickHandler(ClickHandler handler) {
                return addDomHandler(handler, ClickEvent.getType());
        }

        @Override
        public Label addErrorLabel() {
                return null;
        }

        @Override
        public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
                return null;
        }

        @Override
        public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
                return null;
        }

        /**
         * Returns the value of the legend text
         */
        @Override
        public String getText() {
                return legend.getText();
        }

        /**
         * Returns the id of any checkbox that is checked or null if no checkbox
         * in this checkset has been selected
         * 
         * @return the first selected value
         */
        @Override
        public String getValue() {
                for (MyJQMCheckbox box : checks) {
                        if (box.isSelected())
                                return box.getId();
                }
                return null;
        }

        private String getValue(Element element) {
                while (element != null) {
                        if ("label".equalsIgnoreCase(element.getTagName())
                                        && element.getAttribute("class") != null
                                        && (element.getAttribute("class").contains("ui-btn-active") || element
                                                        .getAttribute("class").contains("ui-btn-down")))
                                return element.getAttribute("for");
                        String value = getValue(element.getFirstChildElement());
                        if (value != null)
                                return value;
                        element = element.getNextSiblingElement();
                }
                return null;
        }

        private native void getValueC(String id) /*-{
                alert($wnd.$('#' + id).is(':checked'));
        }-*/;

        /**
         * Returns true if at least one checkbox in this checkset is selected.
         */
        public boolean hasSelection() {
                return getValue() != null;
        }






        public void removeCheck(String id, String label) {
                // TODO traverse all elements removing anything that has a "for" for
                // this id or actually has this id
        }


        /**
         * Sets the value of the legend text.
         */
        @Override
        public void setText(String text) {
                legend.setText(text);
        }



        @Override
        public void setValue(String id, boolean ignored) {
                // for (TextBox check : checks) {
                // if (id.equals(check.getValue())) {
                // check.getElement().setAttribute("defaultChecked", "true");
                // check.getElement().setAttribute("checked", "true");
                // return;
                // }
                // }
        }

		@Override
		public void setValue(String value) {
			// TODO Auto-generated method stub
			
		}

}