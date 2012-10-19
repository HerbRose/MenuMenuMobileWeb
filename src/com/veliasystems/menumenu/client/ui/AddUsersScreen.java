package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.form.JQMForm;
import com.sksamuel.jqm4gwt.form.elements.JQMCheckbox;
import com.sksamuel.jqm4gwt.form.elements.JQMCheckset;
import com.sksamuel.jqm4gwt.form.elements.JQMPassword;
import com.sksamuel.jqm4gwt.html.FormLabel;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.sksamuel.jqm4gwt.toolbar.JQMToolbar;
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

	
	public AddUsersScreen(JQMPage back) {
		pageToBack = back;
		userType = userController.getUserType();
		tabBar = new TabBar();
		tabBar.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				// TODO Auto-generated method stub
				showPanel(panelList.get(event.getSelectedItem()));
			}

		});
		setHeader();
		add(tabBar);
		setContent();
//		setFooter();
		
		
	}
	
	private void setHeader(){
		
		header = new JQMHeader(Customization.ADD_USER);
		header.setFixed(true);
		add(header);
	}
	
	private void setContent(){
			
			switch(userType){
			case ADMIN:
				setAdminButtons();
				break;
			default:
				setAgentButtons();
			}		
	}
	
//	private void setFooter(){
//		footer = new JQMFooter();
//		footer.setFixed(true);
//		
//		
//		add(footer);
//		
//	}
	
	private void setAdminButtons(){
		setAdminPanel();
//		addAdmin = new JQMButton(Customization.ADD_ADMIN);
//		addAdmin.setIcon(DataIcon.PLUS);
//		addAdmin.setIconPos(IconPos.TOP);
//		footer.add(addAdmin);
		
		setAgentPanel();
//		addAgent = new JQMButton(Customization.ADD_AGENT);
//		addAgent.setIcon(DataIcon.PLUS);
//		addAgent.setIconPos(IconPos.TOP);
//		footer.add(addAgent);
		
		setRestauratorPanel();
//		addRestaurator = new JQMButton(Customization.ADD_RESTAURATOR);
//		addRestaurator.setIcon(DataIcon.PLUS);
//		addRestaurator.setIconPos(IconPos.TOP);
//		footer.add(addRestaurator);
		addRestaurantsManager();
	}
	
	private void setAdminPanel(){
		adminPanel = new FlowPanel();
		
		//panelList.add(adminPanel);
		
		addAddUserTab(adminPanel, UserType.ADMIN);
		hidePanels();
	}
	private void setAgentPanel(){
		agentPanel = new FlowPanel();
		
		//panelList.add(agentPanel);
		//panelList.put(panelCount++, agentPanel);
		addAddUserTab(agentPanel, UserType.AGENT);
		hidePanels();
	}
	private void setRestauratorPanel(){
		restauratorPanel = new FlowPanel();
		
		//panelList.add(restauratorPanel);
		//panelList.put(panelCount++, restauratorPanel);
		addAddUserTab(restauratorPanel, UserType.RESTAURATOR);
		
		hidePanels();
	}
	
	private void setAgentButtons(){
		setRestauratorPanel();
		addRestaurator = new JQMButton(Customization.ADD_RESTAURATOR);
		addRestaurator.setIcon(DataIcon.PLUS);
		addRestaurator.setIconPos(IconPos.TOP);
		//footer.add(addRestaurator);
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
			passwordAdmin = new JQMPassword();
			passwordAdmin2 = new JQMPassword();
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
			passwordAgent = new JQMPassword();
			passwordAgent2 = new JQMPassword();
			citySuggest = new MultiWordSuggestOracle("-");
			citySuggestBox = new SuggestBox(citySuggest);
			addCityToCityTextBox = new JQMButton("+");
			addCityToCityTextBox.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(checkCity(citySuggestBox.getValue().trim())){
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
			
			for (City city : cityController.getCitiesList()) {
				citySuggest.add(city.getCity());
			}
			
			panel.add(citySuggestBox);
			panel.add(addCityToCityTextBox);
			panel.add(citiesCellList);
			panel.add(saveAgentButton);
			
			
			
			break;
		case RESTAURATOR:
			
			tabBar.addTab(Customization.ADD_RESTAURATOR);
			passwordRestaurator = new JQMPassword();
			passwordRestaurator2 = new JQMPassword();
			inputEmailRestaurator = new TextBox();
			restaurantSuggest = new MultiWordSuggestOracle();
			restaurantSuggestBox = new SuggestBox(restaurantSuggest);
			addRestaurantToRestaurantTextBox = new JQMButton("+");
			addRestaurantToRestaurantTextBox.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(checkRestaurant(restaurantSuggestBox.getValue())){
						addedRestauration.add(restaurantSuggestBox.getValue());
						restaurantCellList.setRowData(addedRestauration);
						restaurantCellList.setRowCount(addedRestauration.size());
						restaurantCellList.redraw();
					}
					
				}
			});
			
			TextCell restaurationTextCell = new TextCell();
			restaurantCellList = new CellList<String>(restaurationTextCell);
			addedRestauration = new ArrayList<String>();
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
						
						List<Long> restaurationId = new ArrayList<Long>();
						for (Restaurant restaurant : restaurantController.getRestaurantsList()) {
							for (String restaurationName : addedRestauration) {
								if(restaurant.getName().equals(restaurationName)){
									if(!restaurationId.contains(restaurant.getId())){
										restaurationId.add(restaurant.getId());
									}
								}
							}
						}
						restaurator.setRestaurantsId(restaurationId);
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
			for (Restaurant restaurant : restaurantController.getRestaurantsList()) {
				restaurantSuggest.add(restaurant.getName());
			}
			
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

	private List<CheckBox> checkBoxs;
	private FlowPanel checkBoxsPanel;
	
	private void addRestaurantsManager(){
		checkBoxsPanel = new FlowPanel();
		
		panelList.put(panelCount++, checkBoxsPanel);
		tabBar.addTab(Customization.RESTAURATIUN_MANAGER);
		checkBoxs = new ArrayList<CheckBox>();
		JQMCheckset aaa = new JQMCheckset("sss");
		
		for (Restaurant restaurant : restaurantController.getRestaurantsList()) {
			CheckBox cb = new CheckBox(restaurant.getName());
			
			aaa.addCheck(restaurant.getId()+"", restaurant.getName());
			
		}
		checkBoxsPanel.add(aaa);
		add(checkBoxsPanel);
	}
//	private boolean isClicked(ClickEvent event, JQMButton button) {
//
//		int clickedX = event.getClientX();
//		int clickedY = event.getClientY();
//
//		int ButtonX = (int) button.getElement().getAbsoluteLeft();
//		int ButtonY = (int) button.getElement().getAbsoluteTop();
//		int ButtonWidth = (int) button.getElement().getClientWidth();
//		int ButtonHeight = (int) button.getElement().getClientWidth();
//
//		int ButtonStartX = ButtonX;
//		int ButtonStopX = ButtonX + ButtonWidth;
//		int ButtonStartY = ButtonY;
//		int ButtonStopY = ButtonY + ButtonHeight;
//
//		if (clickedX >= ButtonStartX && clickedX <= ButtonStopX
//				&& clickedY >= ButtonStartY && clickedY <= ButtonStopY) {
//			return true;
//		}
//		return false;
//	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
	
//	{
//		this.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				meClicked(event);
//			}
//		});
//	}
	
//	private void meClicked(ClickEvent event){
//		if(isClicked(event, addAdmin)){
//			showPanel(adminPanel);
//		}else if(isClicked(event, addAgent)){
//			showPanel(agentPanel);
//		}else if(isClicked(event, addRestaurator)){
//			showPanel(restauratorPanel);
//		}
//	}


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
			citiesCellList.redraw();
			
			inputEmailRestaurator.setValue("");
			passwordRestaurator.setValue("");
			passwordRestaurator2.setValue("");
			addedRestauration.clear();
			restaurantCellList.redraw();
			break;

		default:
			inputEmailRestaurator.setValue("");
			passwordRestaurator.setValue("");
			passwordRestaurator2.setValue("");
			addedRestauration.clear();
			restaurantCellList.redraw();
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
		Window.alert("ZAIMPLEMENTOWAC SPRAWDZANIE RESTAURACJI");
		return true;
	}
	
	private boolean checkCity(String CityName){
		Window.alert("ZAIMPLEMENTOWAC SPRAWDZANIE MIASTA");
		return true;
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
