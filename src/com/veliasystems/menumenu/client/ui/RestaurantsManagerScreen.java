package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.controllers.UserType;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;

public class RestaurantsManagerScreen extends JQMPage implements
		HasClickHandlers, IObserver {

	private final BlobServiceAsync blobService = GWT.create(BlobService.class);

	private JQMHeader header;
	private JQMButton backButton;
	private JQMPage pageToBack;
	// private JQMFooter footer;

	private TabBar tabBar;
	private FlowPanel tabBarPanel;
	private FlowPanel divForTabBarPanel;
	private Image leftArrow;
	private Image rightArrow;
	/** szerokość panelu na zakładki ze strzałkami */
	private int tabBarPanelWidth;
	private int marginLeft = 0;
	int tabWidth = 0;

	// pola do dodawania użytkowników
	private TextBox inputEmailAdmin;
	private PasswordTextBox passwordAdmin;
	private PasswordTextBox passwordAdmin2;
	private TextBox inputEmailAgent;
	private PasswordTextBox passwordAgent;
	private PasswordTextBox passwordAgent2;
	private TextBox inputEmailRestaurator;
	private PasswordTextBox passwordRestaurator;
	private PasswordTextBox passwordRestaurator2;
	private MultiWordSuggestOracle citySuggest;
	private SuggestBox citySuggestBox;
	private MultiWordSuggestOracle restaurantSuggest;
	private SuggestBox restaurantSuggestBox;
	private JQMButton addCityToCityTextBox;
	private FlowPanel addCityFlowPanel;
	private JQMButton addRestaurantToRestaurantTextBox;
	private CellList<Restaurant> restaurantCellList;
	private CellList<String> citiesCellList;
	private List<Restaurant> addedRestauration;
	private List<String> addedCities;
	private JQMButton saveAdminButton;
	private JQMButton saveAgentButton;
	private JQMButton saveRestauratorButton;

	// pola w zakładce email
	private ListBox addresseeListBox;
	private FlowPanel chosenEmailPanel;
	private TextBox senderTextBox;
	private List<String> chosenEmailList;
	private TextBox subjectTextBox;
	private TextArea messageTextArea;

	private Label mailLabel;
	private Label passwordLabe;
	private Label repeatPasswordLabe;
	private Label emptyMailList;

	private JQMButton saveRestaurantsButton;
	private CellTable<Restaurant> restaurantsCellTable;
	private TextColumn<Restaurant> nameColumn;
	private Column<Restaurant, Boolean> isVisibleForAppColumn;
	private Column<Restaurant, Boolean> isClearBoardColumn;
	private TextColumn<Restaurant> addressColumn;

	private boolean loaded = false;
	private UserType userType;

	private UserController userController = UserController.getInstance();
	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController
			.getInstance();

	private Map<Integer, Widget> panelList = new HashMap<Integer, Widget>();
	private Integer panelCount = 0;

	// panele pokazywane po kliknięciu przycisku (np. addAdmin)
	private FlowPanel adminPanel;
	private FlowPanel agentPanel;
	private FlowPanel restauratorPanel;
	private FlowPanel restaurantsManagerPanel;
	private FlowPanel emailPanel;
	private FlowPanel defaultEmptyBoard;
	private FlowPanel editDataPanel;
	
	//pola w zakladce edit data
	private Label editNameLabel;
	private Label editSurnameLabel;
	private Label inputOldPasswordLabel;
	private Label editPasswordLabel;
	private Label repeatPasswordLabel;
	private TextBox inputOldPasswordBox;
	private TextBox editNameBox;
	private TextBox editSurnameBox;
	private TextBox editPasswordBox;
	private TextBox repeatPasswordBox;
	private Label phoneNumberLabel;
	private TextBox phoneNumerBox;
	private Button saveEditedUser;

	private SingleSelectionModel<String> selectionModelCities;
	private SingleSelectionModel<Restaurant> selectionModelRestaurant;

	private Restaurant restaurant = null;
	private Integer whichPanelShow = 0;

	private List<Long> restaurantsIdList = new ArrayList<Long>();
	private List<Restaurant> restaurantsCopy;
	private Map<Long, Restaurant> restaurantsCopyMap;

	private FormPanel formPanel;
	private FileUpload fileUpload = new FileUpload();

	private int widthOfTabBarPanel;
	
	public RestaurantsManagerScreen() {
		userController.addObserver(this);
		fillRestaurantsCopy();
		userType = userController.getUserType();
		setHeader();
		setContent();
	}

	private void setHeader() {
		header = new JQMHeader(Customization.ADD_USER);
		header.setFixed(true);
		add(header);
	}

	private void setContent() {

		tabBar = new TabBar();
		tabBar.addStyleName("tabBarMain");
		tabBar.getElement().setId("scrollerTabBar");
		tabBarPanel = new FlowPanel();
		tabBarPanel.getElement().setId("tabBarPanel");
		tabBar.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				showPanel(panelList.get(event.getSelectedItem()));
			}
		});

		leftArrow = new Image("img/leftArrow.png");
		leftArrow.getElement().setId("leftArrow");
		leftArrow.setStyleName("hide", true);
		leftArrow.setStyleName("show", false);

		rightArrow = new Image("img/rightArrow.png");
		rightArrow.getElement().setId("rightArrow");
		rightArrow.setStyleName("hide", true);
		rightArrow.setStyleName("show", false);
		setArrowActions();
		divForTabBarPanel = new FlowPanel();
		divForTabBarPanel.add(tabBar);
		divForTabBarPanel.getElement().setId("divForTabBarPanel");

		tabBarPanel.add(leftArrow);
		tabBarPanel.add(rightArrow);
		tabBarPanel.add(divForTabBarPanel);

		add(tabBarPanel);

		switch (userType) {
		case ADMIN:
			setAdminPanels();
			break;
		default:
			setAgentButtons();
		}

	}

	private void setArrowActions() {

		leftArrow.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (marginLeft < 0
						&& tabBarPanelWidth + Math.abs(marginLeft) >= tabWidth) {
					marginLeft = tabBarPanelWidth - tabWidth;
					if (Math.abs(marginLeft) < tabBarPanelWidth) {
						marginLeft = 0;
					} else {
						marginLeft += tabBarPanelWidth
								+ getWidth(rightArrow.getElement().getId());
					}
				} else {
					if (tabWidth - tabBarPanelWidth - Math.abs(marginLeft) < tabBarPanelWidth) {
						marginLeft = 0;
					} else {
						marginLeft += tabWidth - tabBarPanelWidth;
					}
				}

				if (marginLeft > 0)
					marginLeft = 0;
				moveLeft(marginLeft, tabBar.getElement().getId());
				checkArrows();

			}
		});

		rightArrow.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (marginLeft >= 0) {
					marginLeft = 0;
					if (tabWidth - tabBarPanelWidth < tabBarPanelWidth) {
						marginLeft -= tabWidth - tabBarPanelWidth;
					} else {
						marginLeft -= tabBarPanelWidth;
					}
				} else {
					if (tabWidth - Math.abs(marginLeft) + tabBarPanelWidth < tabBarPanelWidth) {
						marginLeft -= tabWidth - Math.abs(marginLeft)
								+ tabBarPanelWidth;
					} else {
						marginLeft -= tabBarPanelWidth;
					}
				}

				if (Math.abs(marginLeft) + tabBarPanelWidth > tabWidth)
					marginLeft = tabBarPanelWidth - tabWidth
							- getWidth(rightArrow.getElement().getId());
				moveLeft(marginLeft, tabBar.getElement().getId());
				checkArrows();

			}
		});

	}

	private void checkArrows() {
		tabBarPanelWidth = getWidth(tabBarPanel.getElement().getId());// Window.getClientWidth();
		tabWidth = getWidth(tabBar.getElement().getId());
		if (tabBarPanelWidth < tabWidth) {
			rightArrow.setStyleName("hide", false);
			rightArrow.setStyleName("show", true);

			if (marginLeft >= 0) {
				addStyleToElement(divForTabBarPanel, "margin-left: 0px;");

				leftArrow.setStyleName("hide", true);
				leftArrow.setStyleName("show", false);
			}

			if (marginLeft < 0
					&& tabBarPanelWidth + Math.abs(marginLeft)
							- getWidth(rightArrow.getElement().getId()) >= tabWidth) {
				addStyleToElement(divForTabBarPanel, "margin-left: 30px;");
				rightArrow.setStyleName("hide", true);
				rightArrow.setStyleName("show", false);

				leftArrow.setStyleName("hide", false);
				leftArrow.setStyleName("show", true);

			}
			if (marginLeft < 0
					&& tabBarPanelWidth + Math.abs(marginLeft)
							- getWidth(rightArrow.getElement().getId()) < tabWidth) {
				addStyleToElement(divForTabBarPanel, "margin-left: 30px;");
				leftArrow.setStyleName("hide", false);
				leftArrow.setStyleName("show", true);

				rightArrow.setStyleName("hide", false);
				rightArrow.setStyleName("show", true);
			}
		}

		int leftArrowWidth = getWidth(leftArrow.getElement().getId());// leftArrow.getWidth();
		int rightArrowWidth = getWidth(rightArrow.getElement().getId());// rightArrow.getWidth();
		widthOfTabBarPanel = tabBarPanelWidth - leftArrowWidth - rightArrowWidth;

		String myStyle = "max-width: " + tabBarPanelWidth + "px; min-width: "
				+ tabBarPanelWidth + "px;";
		addStyleToElement(tabBarPanel, myStyle);
		myStyle = "max-width: " + widthOfTabBarPanel + "px; min-width: " + widthOfTabBarPanel + "px;";
		addStyleToElement(divForTabBarPanel, myStyle);
		// tabBar.setStyleName("scrollable", true);

	}

	private native int getWidth(String elementId)/*-{
		return $wnd.document.getElementById(elementId).offsetWidth;
	}-*/;

	private native void moveLeft(int left, String elementId)/*-{
		var table = $wnd.document.getElementById(elementId);
		//		var tbody = table.getElementsByTagName("tbody");
		table.style.marginLeft = left + "px";
	}-*/;

	private void setAdminPanels() {
		setAdminPanel();
		setAgentPanel();

		setRestauratorPanel();
		setEditDataPanel();
		addRestaurantsManagerTab();
		addEmailTab();
		addDefaultEmptyProfile();
	}

	private void setAdminPanel() {
		adminPanel = new FlowPanel();
		adminPanel.setStyleName("barPanel", true);
		addAddUserTab(adminPanel, UserType.ADMIN);
		hidePanels();
	}

	private void setAgentPanel() {
		agentPanel = new FlowPanel();
		agentPanel.setStyleName("barPanel", true);
		addAddUserTab(agentPanel, UserType.AGENT);
		hidePanels();
	}

	private void setRestauratorPanel() {
		restauratorPanel = new FlowPanel();
		restauratorPanel.setStyleName("barPanel", true);
		addAddUserTab(restauratorPanel, UserType.RESTAURATOR);

		hidePanels();
	}

	private void setEditDataPanel(){
		editDataPanel = new FlowPanel();
		editDataPanel.setStyleName("barPanel", true);
		panelList.put(panelCount++, editDataPanel);
		tabBar.addTab(Customization.EDIT_DATA);
		
		inputOldPasswordLabel = new Label(Customization.INPUT_OLD_PASSWORD);
		editDataPanel.add(inputOldPasswordLabel);
		
		inputOldPasswordBox = new PasswordTextBox();
		inputOldPasswordBox.getElement().setAttribute("placeHolder", Customization.INPUT_OLD_PASSWORD);
		inputOldPasswordBox.addStyleName("properWidth");
		editDataPanel.add(inputOldPasswordBox);
		
		editPasswordLabel = new Label(Customization.INPUT_PASSWORD);
		editDataPanel.add(editPasswordLabel);
		
		editPasswordBox = new PasswordTextBox();
		editPasswordBox.getElement().setAttribute("placeHolder", Customization.PASSWORD_PLACEHOLDER);
		editPasswordBox.addStyleName("properWidth");
		editDataPanel.add(editPasswordBox);
		
		repeatPasswordLabel = new Label(Customization.REPEAT_PASSWORD);
		editDataPanel.add(repeatPasswordLabel);
		
		repeatPasswordBox = new PasswordTextBox();
		repeatPasswordBox.getElement().setAttribute("placeHolder", Customization.REPEAT_PASSWORD_PLACEHOLDER);
		repeatPasswordBox.addStyleName("properWidth");
		editDataPanel.add(repeatPasswordBox);
		
		editNameLabel = new Label(Customization.USER_NAME);
		editDataPanel.add(editNameLabel);
		
		editNameBox = new TextBox();
		editNameBox.getElement().setAttribute("placeHolder", Customization.USER_NAME_PLACEHOLDER);
		editNameBox.addStyleName("properWidth");
		editDataPanel.add(editNameBox);
		
		editSurnameLabel = new Label(Customization.USER_SURNAME);
		editDataPanel.add(editSurnameLabel);
		
		editSurnameBox = new TextBox();
		editSurnameBox.getElement().setAttribute("placeHolder", Customization.USER_SURNNAME_PLACEHOLDER);
		editSurnameBox.addStyleName("properWidth");
		editDataPanel.add(editSurnameBox);
		
		phoneNumberLabel = new Label(Customization.USER_PHONE);
		editDataPanel.add(phoneNumberLabel);
		
		phoneNumerBox = new TextBox();
		phoneNumerBox.getElement().setAttribute("placeHolder", Customization.USER_PHONE);
		phoneNumerBox.addStyleName("properWidth");
		editDataPanel.add(phoneNumerBox);
		
		saveEditedUser = new Button(Customization.SAVE);
		editDataPanel.add(saveEditedUser);
		
		saveEditedUser.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(validChangedData()){
					User user = new User("tmp");
					
					if(!editNameBox.getValue().trim().equals("")) user.setName(editNameBox.getValue());
					if(!editSurnameBox.getValue().trim().equals("")) user.setSurname(editSurnameBox.getValue());
					if(!repeatPasswordBox.getValue().trim().equals("")) {
						user.setPassword(repeatPasswordBox.getValue());
					} else{
						user.setPassword(userController.getLoggedUser().getPassword());
					}
					if(!phoneNumerBox.getValue().trim().equals("")) user.setPhoneNumber(phoneNumerBox.getValue());
					
					userController.changeUserData(user);
				}
				
			}
		});
		
		add(editDataPanel);
	}
	private void setAgentButtons() {
		setRestauratorPanel();
		setEditDataPanel();
	}

	private void addAddUserTab(FlowPanel panel, UserType userType) {
		panelList.put(panelCount++, panel);

		mailLabel = new Label(Customization.INPUT_EMAIL);
		passwordLabe = new Label(Customization.INPUT_PASSWORD);
		repeatPasswordLabe = new Label(Customization.REPEAT_PASSWORD);

		switch (userType) {
		case ADMIN:
			tabBar.addTab(Customization.ADD_ADMIN);
			inputEmailAdmin = new TextBox();
			inputEmailAdmin.addStyleName("properWidth");
			inputEmailAdmin.getElement().setAttribute("placeHolder",
					Customization.EMAIL_PLACEHOLDER);
			passwordAdmin = new PasswordTextBox();
			passwordAdmin.addStyleName("properWidth");
			passwordAdmin.getElement().setAttribute("placeHolder",
					Customization.PASSWORD_PLACEHOLDER);
			passwordAdmin2 = new PasswordTextBox();
			passwordAdmin2.addStyleName("properWidth");
			passwordAdmin2.getElement().setAttribute("placeHolder",
					Customization.REPEAT_PASSWORD_PLACEHOLDER);
			saveAdminButton = new JQMButton(Customization.SAVE);
			saveAdminButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (validData(UserType.ADMIN)) {
						User admin = new User(inputEmailAdmin.getValue().trim());
						admin.setPassword(passwordAdmin.getValue().trim());
						admin.setAdmin(true);
						addUser(admin);
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
			inputEmailAgent.addStyleName("properWidth");
			inputEmailAgent.getElement().setAttribute("placeHolder",
					Customization.EMAIL_PLACEHOLDER);
			passwordAgent = new PasswordTextBox();
			passwordAgent.addStyleName("properWidth");
			passwordAgent.getElement().setAttribute("placeHolder",
					Customization.PASSWORD_PLACEHOLDER);
			passwordAgent2 = new PasswordTextBox();
			passwordAgent2.addStyleName("properWidth");
			passwordAgent2.getElement().setAttribute("placeHolder",
					Customization.REPEAT_PASSWORD_PLACEHOLDER);
			citySuggest = new MultiWordSuggestOracle("-");
			citySuggestBox = new SuggestBox(citySuggest);
			citySuggestBox.getElement().setAttribute("placeHolder",
					Customization.CITY_PLACEHOLDER);
			citySuggestBox.addStyleName("properWidth");

			addCityToCityTextBox = new JQMButton("");
			addCityToCityTextBox.setIcon(DataIcon.PLUS);
			addCityToCityTextBox.setIconPos(IconPos.NOTEXT);
			addCityToCityTextBox.setStyleName("addButtonCity");
			addCityToCityTextBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (checkCity(citySuggestBox.getValue().trim())) {
						// addedCities = new ArrayList<String>();
						addedCities.add(citySuggestBox.getValue());
						citiesCellList.setRowData(addedCities);
						citiesCellList.setRowCount(addedCities.size());
						citiesCellList.redraw();
						citySuggestBox.setText("");
					}

				}
			});

			TextCell cityTextCell = new TextCell();
			addedCities = new ArrayList<String>();
			citiesCellList = new CellList<String>(cityTextCell);
			selectionModelCities = new SingleSelectionModel<String>();
			citiesCellList.setSelectionModel(selectionModelCities);
			selectionModelCities
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

						@Override
						public void onSelectionChange(SelectionChangeEvent event) {
							String selected = selectionModelCities
									.getSelectedObject();
							if (selected != null) {
								addedCities.remove(selected);
								selectionModelCities.setSelected(
										selectionModelCities
												.getSelectedObject(), false);
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

					if (validData(UserType.AGENT)) {
						User agent = new User(inputEmailAgent.getValue().trim());
						agent.setPassword(passwordAgent.getValue().trim());
						List<Long> cityId = new ArrayList<Long>();
						for (String cityName : addedCities) {
							for (City city : cityController.getCitiesList()) {
								if (city.getCity().equals(cityName)) {
									if (!cityId.contains(city.getId())) {
										cityId.add(city.getId());
									}
								}
							}
						}
						agent.setCitiesId(cityId);
						addUser(agent);
					}

				}
			});

			panel.add(mailLabel);
			panel.add(inputEmailAgent);
			panel.add(passwordLabe);
			panel.add(passwordAgent);
			panel.add(repeatPasswordLabe);
			panel.add(passwordAgent2);
			panel.add(citySuggestBox);
			panel.add(addCityToCityTextBox);
			panel.add(citiesCellList);
			panel.add(saveAgentButton);

			break;
		case RESTAURATOR:
			if (restaurant != null) {
				whichPanelShow = panelCount - 1;
			}
			tabBar.addTab(Customization.ADD_RESTAURATOR);
			passwordRestaurator = new PasswordTextBox();
			passwordRestaurator.addStyleName("properWidth");
			passwordRestaurator.getElement().setAttribute("placeHolder",
					Customization.PASSWORD_PLACEHOLDER);
			passwordRestaurator2 = new PasswordTextBox();
			passwordRestaurator2.addStyleName("properWidth");
			passwordRestaurator2.getElement().setAttribute("placeHolder",
					Customization.REPEAT_PASSWORD_PLACEHOLDER);
			inputEmailRestaurator = new TextBox();
			inputEmailRestaurator.addStyleName("properWidth");
			inputEmailRestaurator.getElement().setAttribute("placeHolder",
					Customization.EMAIL_PLACEHOLDER);
			restaurantSuggest = new MultiWordSuggestOracle();
			restaurantSuggestBox = new SuggestBox(restaurantSuggest);
			restaurantSuggestBox.getElement().setAttribute("placeHolder",
					Customization.RESTAURANT_PLACEHOLDER);
			restaurantSuggestBox.addStyleName("properWidth");
			addRestaurantToRestaurantTextBox = new JQMButton("");
			addRestaurantToRestaurantTextBox.setIcon(DataIcon.PLUS);
			addRestaurantToRestaurantTextBox.setIconPos(IconPos.NOTEXT);
			addRestaurantToRestaurantTextBox.setStyleName("addButtonCity");
			addRestaurantToRestaurantTextBox
					.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							if (checkRestaurant(restaurantSuggestBox.getValue())) {
								setRestauransId(true);
								restaurantCellList
										.setRowData(addedRestauration);
								restaurantCellList
										.setRowCount(addedRestauration.size());
								restaurantCellList.redraw();
								restaurantSuggestBox.setText("");

							}

						}
					});

			TextCell restaurationTextCell = new TextCell();
			restaurantCellList = new CellList<Restaurant>(
					new RestaurantCellClass());
			addedRestauration = new ArrayList<Restaurant>();
			selectionModelRestaurant = new SingleSelectionModel<Restaurant>();
			restaurantCellList.setSelectionModel(selectionModelRestaurant);
			selectionModelRestaurant
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

						@Override
						public void onSelectionChange(SelectionChangeEvent event) {
							// TODO Auto-generated method stub
							Restaurant selected = selectionModelRestaurant
									.getSelectedObject();
							if (selected != null) {
								restaurantsIdList.remove(selected.getId());
								addedRestauration.remove(selected);
								selectionModelRestaurant.setSelected(
										selectionModelRestaurant
												.getSelectedObject(), false);
								restaurantCellList
										.setRowData(addedRestauration);
								restaurantCellList
										.setRowCount(addedRestauration.size());
								restaurantCellList.redraw();
							}
						}
					});
			restaurantCellList.setRowData(addedRestauration);
			restaurantCellList.setRowCount(addedRestauration.size());
			saveRestauratorButton = new JQMButton(Customization.SAVE);
			saveRestauratorButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (validData(UserType.RESTAURATOR)) {
						User restaurator = new User(inputEmailRestaurator
								.getValue().trim());
						restaurator.setPassword(passwordRestaurator.getValue()
								.trim());
						restaurator.setRestaurantsId(restaurantsIdList);
						addUser(restaurator);
					}

				}
			});

			panel.add(mailLabel);
			panel.add(inputEmailRestaurator);
			panel.add(passwordLabe);
			panel.add(passwordRestaurator);
			panel.add(repeatPasswordLabe);
			panel.add(passwordRestaurator2);
			panel.add(restaurantSuggestBox);
			panel.add(addRestaurantToRestaurantTextBox);
			panel.add(restaurantCellList);// restaurantTextBox);
			panel.add(saveRestauratorButton);
			break;
		default:
			break;
		}

		add(panel);

	}

	private void addRestaurantsManagerTab() {

		restaurantsManagerPanel = new FlowPanel();
		restaurantsManagerPanel.setStyleName("barPanel", true);
		panelList.put(panelCount++, restaurantsManagerPanel);
		tabBar.addTab(Customization.RESTAURATIUN_MANAGER);

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
		restaurantsManagerPanel.add(restaurantsCellTable);
		restaurantsManagerPanel.add(saveRestaurantsButton);
		add(restaurantsManagerPanel);
	}

	private void addEmailTab() {
		emailPanel = new FlowPanel();
		emailPanel.setStyleName("barPanel", true);
		panelList.put(panelCount++, emailPanel);
		tabBar.addTab(Customization.SEND_EMAIL);

		Label toLabel = new Label(Customization.ADDRESSEE + ":");
		Label fromLabel = new Label(Customization.SENDER + ":");
		Label messageLabel = new Label(Customization.MESSAGE_TEXT + ":");
		Label subjectLabel = new Label(Customization.SUBJECT);

		addresseeListBox = new ListBox();
		addresseeListBox.addStyleName("properWidth");
		addresseeListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				final Label label = new Label(addresseeListBox
						.getItemText(addresseeListBox.getSelectedIndex()));
				label.setTitle(addresseeListBox.getValue(addresseeListBox
						.getSelectedIndex()));

				label.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						chosenEmailPanel.remove(label);
						chosenEmailList.remove(label.getTitle());
						if (chosenEmailList.isEmpty()) {
							chosenEmailPanel.removeStyleName("greenShadow");
						}
					}
				});
				clearChosenEmailList();
				chosenEmailPanel.add(label);
				chosenEmailPanel.setStyleName("greenShadow");
				chosenEmailList.add(addresseeListBox.getValue(addresseeListBox
						.getSelectedIndex()));
			}
		});

		chosenEmailPanel = new FlowPanel();
		chosenEmailList = new ArrayList<String>();
		senderTextBox = new TextBox();
		senderTextBox.addStyleName("properWidth");
		senderTextBox.setEnabled(false);
		senderTextBox.setText(userController.getLoggedUser().getEmail());
		subjectTextBox = new TextBox();
		subjectTextBox.addStyleName("properWidth");
		subjectTextBox.getElement().setAttribute("placeHolder",
				Customization.SUBJECT_PLACEHOLDER);
		messageTextArea = new TextArea();
		messageTextArea.addStyleName("properWidth");
		messageTextArea.getElement().setAttribute("placeHolder",
				Customization.MESSAGE_PLACEHOLDER);
		JQMButton sendButton = new JQMButton(Customization.SEND);
		sendButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (checkEmailValues()) {
					userController.sendMail(chosenEmailList,
							senderTextBox.getText(), subjectTextBox.getValue(),
							messageTextArea.getValue());
				}
			}
		});

		emailPanel.add(toLabel);
		emailPanel.add(addresseeListBox);
		emailPanel.add(chosenEmailPanel);
		emailPanel.add(fromLabel);
		emailPanel.add(senderTextBox);
		emailPanel.add(subjectLabel);
		emailPanel.add(subjectTextBox);
		emailPanel.add(messageLabel);
		emailPanel.add(messageTextArea);
		emailPanel.add(sendButton);
		add(emailPanel);
	}

	private void addDefaultEmptyProfile() {
		defaultEmptyBoard = new FlowPanel();
		defaultEmptyBoard.setStyleName("barPanel", true);
		panelList.put(panelCount++, defaultEmptyBoard);
		tabBar.addTab(Customization.SET_DEFAULT_EMPTY_PROFIL);

		formPanel = new FormPanel();
		formPanel.setVisible(true);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.add(fileUpload);
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				Document.get().getElementById("load").setClassName("loaded");
				formPanel.reset();
			}
		});
		fileUpload.setName("defaultEmptyImage");
		fileUpload.addStyleName("properWidth");
		fileUpload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				Document.get().getElementById("load").setClassName("loading");
				// clickOnInputFile(formPanel.getUploadButton().getElement());
				blobService.getBlobStoreUrl("0", ImageType.EMPTY_PROFILE,
						new AsyncCallback<String>() {

							@Override
							public void onSuccess(String result) {
								formPanel.setAction(result);
								formPanel.submit();
							}

							@Override
							public void onFailure(Throwable caught) {
								Document.get().getElementById("load")
										.setClassName("loaded");
								Window.alert("Problem with upload. Try again");

							}
						});

			}
		});
		defaultEmptyBoard.add(formPanel);
		add(defaultEmptyBoard);
	}

	private void clearChosenEmailList() {
		if (chosenEmailList.isEmpty())
			chosenEmailPanel.clear();
	}

	private boolean checkEmailValues() {

		boolean isValid = false;

		String alert = "";

		if (chosenEmailList.isEmpty()) {
			chosenEmailPanel.setStyleName("redShadow");
			emptyMailList = new Label(Customization.EMPTY_MAIL_LIST);
			clearChosenEmailList();
			chosenEmailPanel.add(emptyMailList);
		} else {
			if (subjectTextBox.getText().isEmpty()) {
				alert += Customization.CONFIRMATION_NO_SUBJECT + "\n";
			}
			if (messageTextArea.getText().isEmpty()) {
				alert += Customization.CONFIRMATION_NO_MESSAGE + "\n";
			}
		}

		if (!chosenEmailList.isEmpty() && !subjectTextBox.getText().isEmpty()
				&& !messageTextArea.getText().isEmpty()) {
			isValid = true;
		}

		if (!alert.equalsIgnoreCase("")) {
			if (Window.confirm(alert)) {
				isValid = true;
			}
		}

		return isValid;
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
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	private void hidePanels() {
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

	private void addUser(User user) {
		// user.setPassword(password.getValue());
		userController.addUser(user);
	}

	private void clearScreenData() {

		fillRestaurantsCopy();

		switch (userType) {
		case ADMIN:
			inputEmailAgent.setValue("");
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

			restaurantsCellTable.redraw();

			// back to standard css styles admin panel
			passwordAdmin.removeStyleName("greenShadow");
			passwordAdmin.removeStyleName("redShadow");
			passwordAdmin2.removeStyleName("greenShadow");
			passwordAdmin2.removeStyleName("redShadow");
			inputEmailAdmin.removeStyleName("greenShadow");
			inputEmailAdmin.removeStyleName("redShadow");

			// back to standard css styles agent panel
			passwordAgent.removeStyleName("greenShadow");
			passwordAgent.removeStyleName("redShadow");
			passwordAgent2.removeStyleName("greenShadow");
			passwordAgent2.removeStyleName("redShadow");
			inputEmailAgent.removeStyleName("greenShadow");
			inputEmailAgent.removeStyleName("redShadow");
			citySuggestBox.removeStyleName("redShadow");
			citySuggestBox.removeStyleName("greenShadow");
			
			inputEmailRestaurator.removeStyleName("greenShadow");
			inputEmailRestaurator.removeStyleName("redShadow");
			passwordRestaurator.removeStyleName("greenShadow");
			passwordRestaurator.removeStyleName("redShadow");
			passwordRestaurator2.removeStyleName("redShadow");
			passwordRestaurator2.removeStyleName("greenShadow");
			restaurantSuggestBox.removeStyleName("redShadow");
			restaurantSuggestBox.removeStyleName("greenShadow");

			addresseeListBox.clear();

			addresseeListBox.addItem(Customization.SELECT_ELEMENT);
			addresseeListBox.getElement().getFirstChildElement()
					.setAttribute("disabled", "disabled");

			for (User user : userController.getUserList()) {
				addresseeListBox
						.addItem(user.getName() != null ? user.getEmail()
								: "No Name" + " (" + user.getEmail() + ")", user
								.getEmail());
			}

			addresseeListBox.setSelectedIndex(0);
			
			break;
		}

		inputEmailRestaurator.setValue("");
		passwordRestaurator.setValue("");
		passwordRestaurator2.setValue("");
		restaurantSuggestBox.setText("");

		restaurantSuggest.clear();
		addedRestauration.clear();
		for (Restaurant restaurant : restaurantsCopy) {
			if (restaurant == this.restaurant) {
				addedRestauration.add(restaurant);
				// addedRestauration.add(restaurant.getName() + " (" +
				// Customization.CITYONE + ": " + restaurant.getCity() + " ," +
				// Customization.ADRESS +": "+ restaurant.getAddress() + ")");
			}
			restaurantSuggest.add(restaurant.getName() + " ("
					+ Customization.CITYONE + ": " + restaurant.getCity()
					+ " ," + Customization.ADRESS + ": "
					+ restaurant.getAddress() + ")");
		}


		restaurantCellList.setRowData(addedRestauration);
		restaurantCellList.redraw();

		

		// set width of tab bar

		checkArrows();

	}

	/**
	 * the method takes current style and adds new one.
	 * 
	 * @param element
	 * @param style
	 */
	private void addStyleToElement(Widget element, String style) {
		// TODO może źle działać w sytuacji gdy np dodajemy width: xx px; a
		// w stylach jest juź np: max-width: ...
		String oldStyle = element.getElement().getAttribute("style");
		for (String oneStyle : style.split(";")) {
			int indexOfClon = oneStyle.indexOf(":");
			if (indexOfClon <= 0)
				continue;
			String thisStyleName = oneStyle.substring(0, indexOfClon);
			if (oldStyle.indexOf(thisStyleName) >= 0) {// jeżeli istnieje nowy
														// styl w elemęncie
				int indexOfOldStyleName = oldStyle.indexOf(thisStyleName);
				int indexOfEndOldStyleName = oldStyle.indexOf(";",
						indexOfOldStyleName);
				oldStyle = oldStyle.replace(oldStyle.subSequence(
						indexOfOldStyleName, indexOfEndOldStyleName), oneStyle
						.subSequence(0, oneStyle.length()));
			} else {// jeżeli nie istnieje
				oldStyle += (oneStyle + ";");
			}
		}
		element.getElement().setAttribute("style", oldStyle);
	}

	/**
	 * method doesn't check if lists have correct data
	 * 
	 * @param userType
	 * @return
	 */
	private boolean validChangedData(){
		boolean isCorrect = false;
		
		if(userController.isValidPassword(inputOldPasswordBox.getValue().trim())){
				if(!editPasswordBox.getValue().trim().equals("")){
						if(editPasswordBox.getValue().trim().equals("") || repeatPasswordBox.getValue().trim().equals("") || !editPasswordBox.getValue().equals(repeatPasswordBox.getValue())){
							editPasswordBox.setStyleName("redShadow", true);
							repeatPasswordBox.setStyleName("redShadow",true);
							isCorrect = false;
						} else{
							editPasswordBox.setStyleName("greenShadow", true);
							repeatPasswordBox.setStyleName("greenShadow", true);
							isCorrect = true;
						}
					}
				else{
					isCorrect = true;
					}
			}
		else{
			inputOldPasswordBox.setStyleName("redShadow", true);
		}
		
		
		
		
		return isCorrect;
	}
	
	private boolean validData(UserType userType) {

		boolean isCorrect = false;

		switch (userType) {
		case ADMIN:
			if (userController.isUserInStor(inputEmailAdmin.getValue().trim())
					|| inputEmailAdmin.getValue().trim().equals("")
					|| !Util.isValidEmail(inputEmailAdmin.getValue())) {
				inputEmailAdmin.setStyleName("redShadow", true);
			} else {
				inputEmailAdmin.setStyleName("greenShadow", true);
				isCorrect = true;
			}
			if (passwordAdmin.getValue().trim().equals("")
					|| passwordAdmin2.getValue().trim().equals("")
					|| !passwordAdmin.getValue().equals(
							passwordAdmin2.getValue())) {
				passwordAdmin.setStyleName("redShadow", true);
				passwordAdmin2.setStyleName("redShadow", true);
				isCorrect = false;
			} else {
				passwordAdmin.setStyleName("greenShadow", true);
				passwordAdmin2.setStyleName("greenShadow", true);
			}

			break;
		case AGENT:
			if (userController.isUserInStor(inputEmailAgent.getValue().trim())
					|| inputEmailAgent.getValue().trim().equals("")
					|| !Util.isValidEmail(inputEmailAgent.getValue())) {
				inputEmailAgent.setStyleName("redShadow", true);
			} else {
				inputEmailAgent.setStyleName("greenShadow", true);
				isCorrect = true;
			}
			if (passwordAgent.getValue().trim().equals("")
					|| passwordAgent2.getValue().trim().equals("")
					|| !passwordAgent.getValue().equals(
							passwordAgent2.getValue())) {
				passwordAgent.setStyleName("redShadow", true);
				passwordAgent2.setStyleName("redShadow", true);
				isCorrect = false;
			} else {
				passwordAgent.setStyleName("greenShadow", true);
				passwordAgent2.setStyleName("greenShadow", true);
			}
			if (addedCities.size() < 1) {
				citySuggestBox.setStyleName("redShadow", true);
				isCorrect = false;
			} else {
				citySuggestBox.setStyleName("greenShadow", true);

			}

			break;
		case RESTAURATOR:
			if (userController.isUserInStor(inputEmailRestaurator.getValue()
					.trim())
					|| inputEmailRestaurator.getValue().trim().equals("")
					|| !Util.isValidEmail(inputEmailRestaurator.getValue())) {
				inputEmailRestaurator.setStyleName("redShadow", true);
			} else {
				inputEmailRestaurator.setStyleName("greenShadow", true);
				isCorrect = true;
			}
			if (passwordRestaurator.getValue().trim().equals("")
					|| passwordRestaurator2.getValue().trim().equals("")
					|| !passwordRestaurator.getValue().equals(
							passwordRestaurator2.getValue())) {
				passwordRestaurator.setStyleName("redShadow", true);
				passwordRestaurator2.setStyleName("redShadow", true);
				isCorrect = false;
			} else {
				passwordRestaurator.setStyleName("greenShadow", true);
				passwordRestaurator2.setStyleName("greenShadow", true);
			}
			if (addedRestauration.size() < 1) {
				restaurantSuggestBox.setStyleName("redShadow", true);
				isCorrect = false;
			} else {
				restaurantSuggestBox.setStyleName("greenShadow", true);
			}

			break;
		default:
			break;
		}

		return isCorrect;
	}

	private boolean checkRestaurant(String restaurantName) {
		boolean is = false;
		int indexOfCity = restaurantName.indexOf("(" + Customization.CITYONE);
		if (indexOfCity < 1)
			return false;
		String restName = restaurantName.substring(0, indexOfCity - 1);
		List<Restaurant> restaurants = restaurantController
				.getRestaurantsList();
		List<String> restaurantNameList = new ArrayList<String>();
		for (Restaurant restaurant : restaurants) {
			restaurantNameList.add(restaurant.getName());
		}

		if (restaurantNameList.contains(restName)) {
			if (addedRestauration.isEmpty())
				return true;

			is = true;

			for (Restaurant restaurant : addedRestauration) {
				if (restaurant.getName().equals(restName))
					is = false;
			}
		}
		return is;
	}

	private boolean checkCity(String CityName) {
		List<City> cities = cityController.getCitiesList();
		List<String> citiesName = new ArrayList<String>();
		for (City city : cities) {
			citiesName.add(city.getCity());
		}
		if (citiesName.contains(CityName)) {
			if (!addedCities.contains(CityName))
				return true;
		}
		return false;
	}

	private void setRestauransId(boolean isAdded) {
		if (isAdded) {
			String fullRestName = restaurantSuggestBox.getValue();// selectionModelRestaurant.getSelectedObject();
			String restNameNoCity = getRestaurationName(fullRestName);
			if (restNameNoCity == null)
				return;
			String city = getCityName(fullRestName);
			if (city == null)
				return;
			String adress = getAddresName(fullRestName);
			if (adress == null)
				return;

			for (Restaurant restaurant : restaurantController
					.getRestaurantsList()) {
				if (restaurant.getName().equals(restNameNoCity)
						&& restaurant.getCity().equals(city)
						&& restaurant.getAddress().equals(adress)) {
					addedRestauration.add(restaurant);
					// addedRestauration.add(restaurantSuggestBox.getValue());
					restaurantsIdList.add(restaurant.getId());
				}
			}

		} else {
			String fullRestName = restaurantSuggestBox.getValue();// selectionModelRestaurant.getSelectedObject();
			String restNameNoCity = getRestaurationName(fullRestName);
			if (restNameNoCity == null)
				return;
			String city = getCityName(fullRestName);
			if (city == null)
				return;
			String adress = getAddresName(fullRestName);
			if (adress == null)
				return;

			for (Restaurant restaurant : restaurantController
					.getRestaurantsList()) {
				if (restaurant.getName().equals(restNameNoCity)
						&& restaurant.getCity().equals(city)
						&& restaurant.getAddress().equals(adress)) {
					addedRestauration.remove(selectionModelRestaurant
							.getSelectedObject());
					restaurantsIdList.remove(restaurant.getId());
				}
			}

		}

	}

	private String getRestaurationName(String fullRestName) {
		int indexOfCity = fullRestName.indexOf("(" + Customization.CITYONE);
		if (indexOfCity < 1)
			return null;
		return fullRestName.substring(0, indexOfCity - 1);
	}

	private String getCityName(String fullRestName) {
		int indexOfCity = fullRestName.indexOf("(" + Customization.CITYONE);
		if (indexOfCity < 1)
			return null;
		indexOfCity += Customization.CITY.length();
		int indexOfAdress = fullRestName.indexOf(Customization.ADRESS + ":",
				indexOfCity);
		if (indexOfAdress < 0)
			return null;
		return fullRestName.substring(indexOfCity + 3, indexOfAdress - 2);
	}

	private String getAddresName(String fullRestName) {
		int indexOfCity = fullRestName.indexOf("(" + Customization.CITYONE);
		if (indexOfCity < 1)
			return null;
		indexOfCity += Customization.CITY.length();
		int indexOfAdress = fullRestName.indexOf(Customization.ADRESS + ":",
				indexOfCity);
		if (indexOfAdress < 0)
			return null;
		indexOfAdress += Customization.ADRESS.length();

		return fullRestName.substring(indexOfAdress + 2,
				fullRestName.length() - 1);
	}

	@Override
	protected void onPageShow() {
		pageToBack = restaurantController.getLastOpenPage();
		restaurantController.setLastOpenPage(this);
		if (pageToBack instanceof RestaurantImageView) {
			restaurant = ((RestaurantImageView) pageToBack).getRestaurant();
			for (Integer key : panelList.keySet()) {
				if (panelList.get(key) == restauratorPanel) {
					whichPanelShow = key;
				}
			}
		}

		Set<Integer> panelKeys = panelList.keySet();
		int maxSize = 0;
		for (Integer key : panelKeys) {
			Widget widget = panelList.get(key);
			if (maxSize < widget.getElement().getClientHeight()) {
				maxSize = widget.getElement().getClientHeight();
			}
		}

		clearScreenData();

		if (maxSize > getElement().getClientHeight()) {
			maxSize += 300;
			// getElement().setAttribute("style", "min-height:" + maxSize+"px");
			setHeight(maxSize + "px");
		}

		if (!loaded) {
			backButton = new JQMButton("", pageToBack, Transition.SLIDE);
			String span = "<span class=\"ui-btn-inner ui-btn-corner-all\"><span class=\"ui-btn-text\" style=\"color: #fff\">"
					+ Customization.BACK
					+ "</span><span class=\"ui-icon ui-icon-arrow-l ui-icon-shadow\"></span></span>";
			backButton.setIcon(DataIcon.LEFT);
			backButton.setIconPos(IconPos.LEFT);
			backButton.getElement().setInnerHTML(span);
			backButton
					.setStyleName("ui-btn-left ui-btn ui-btn-icon-left ui-btn-corner-all ui-shadow ui-btn-down-a ui-btn-up-a ui-btn-up-undefined");
			header.add(backButton);
			loaded = true;
		}
		Document.get().getElementById("load").setClassName(R.LOADED);

		if (panelList != null && !panelList.isEmpty()) {
			tabBar.selectTab(whichPanelShow);
			showPanel(panelList.get(whichPanelShow));
		}
		
		if(editDataPanel != null){
			User user = userController.getLoggedUser();
			editNameBox.setValue(user.getName());
			editSurnameBox.setValue(user.getSurname());
			phoneNumerBox.setValue(user.getPhoneNumber());
		}
		
		
	}
	
	@Override
	public void onChange() {
		clearScreenData();

	}
}

class RestaurantCellClass extends AbstractCell<Restaurant> {

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			Restaurant restaurant, SafeHtmlBuilder sb) {
		sb.appendEscaped(restaurant.getName() + " (" + Customization.CITYONE
				+ ": " + restaurant.getCity() + " ," + Customization.ADRESS
				+ ": " + restaurant.getAddress() + ")");

	}

}
