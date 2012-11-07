package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.controllers.UserType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.ui.administration.AddAdminPanel;
import com.veliasystems.menumenu.client.ui.administration.AddAgentPanel;
import com.veliasystems.menumenu.client.ui.administration.AddRestauratorPanel;
import com.veliasystems.menumenu.client.ui.administration.DefaultEmptyProfilePanel;
import com.veliasystems.menumenu.client.ui.administration.EditDataPanel;
import com.veliasystems.menumenu.client.ui.administration.EmailPanel;
import com.veliasystems.menumenu.client.ui.administration.IManager;
import com.veliasystems.menumenu.client.ui.administration.RemoveUsersPanel;
import com.veliasystems.menumenu.client.ui.administration.RestaurantsManagerPanel;

public class RestaurantsManagerScreen extends JQMPage implements
		HasClickHandlers, IObserver {

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

	// pola w zakładce email

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

	private List<IManager> panelsList = new ArrayList<IManager>();

	private Map<Integer, IManager> panelList = new HashMap<Integer, IManager>();
	private Integer panelCount = 0;

	// panele pokazywane po kliknięciu przycisku (np. addAdmin)
	private IManager adminPanel;
	private IManager agentPanel;
	private IManager restauratorPanel;
	private IManager restaurantsManagerPanel;
	private IManager emailPanel;
	private IManager defaultEmptyBoard;
	private IManager editDataPanel;
	private IManager removeUsersPanel;

	// pola w zakladce edit data
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
				showPanel( panelList.get(event.getSelectedItem()));
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
		widthOfTabBarPanel = tabBarPanelWidth - leftArrowWidth
				- rightArrowWidth;

		String myStyle = "max-width: " + tabBarPanelWidth + "px; min-width: "
				+ tabBarPanelWidth + "px;";
		addStyleToElement(tabBarPanel, myStyle);
		myStyle = "max-width: " + widthOfTabBarPanel + "px; min-width: "
				+ widthOfTabBarPanel + "px;";
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
		add(setAdminPanel());
		add(setAgentPanel());
		add(setRestauratorPanel());
		add(setRestaurantsManagerTab());
		add(setEmailTab());
		add(setDefaultEmptyProfile());
		add(setEditDataPanel());
		add(setRemoveUsersPanel());
	}

	private FlowPanel setAdminPanel() {
		adminPanel = new AddAdminPanel();
		panelList.put(panelCount++, adminPanel);
		tabBar.addTab(adminPanel.getName());

		return (FlowPanel) adminPanel;
	}

	private FlowPanel setAgentPanel() {
		agentPanel = new AddAgentPanel();
		panelList.put(panelCount++, agentPanel);
		tabBar.addTab(agentPanel.getName());

		return (FlowPanel) agentPanel;
	}

	private FlowPanel setRestauratorPanel() {
		restauratorPanel = new AddRestauratorPanel(restaurant);
		panelList.put(panelCount++, restauratorPanel);
		tabBar.addTab(restauratorPanel.getName());
		if (restaurant != null) {
			whichPanelShow = panelCount - 1;
		}

		return (FlowPanel) restauratorPanel;
	}

	private FlowPanel setEditDataPanel() {
		editDataPanel = new EditDataPanel();
		panelList.put(panelCount++, editDataPanel);
		tabBar.addTab(Customization.EDIT_DATA);
		return (FlowPanel) editDataPanel;
	}
	
	private FlowPanel setRemoveUsersPanel(){
		removeUsersPanel = new RemoveUsersPanel();
		panelList.put(panelCount++, removeUsersPanel);
		tabBar.addTab(Customization.REMOVE_USERS);
		return (FlowPanel) removeUsersPanel;
	}

	private void setAgentButtons() {
		add(setRestauratorPanel());
		add(setEditDataPanel());
	}

	private void addAddUserTab(FlowPanel panel, UserType userType) {
		// panelList.put(panelCount++, panel);

		mailLabel = new Label(Customization.INPUT_EMAIL);
		passwordLabe = new Label(Customization.INPUT_PASSWORD);
		repeatPasswordLabe = new Label(Customization.REPEAT_PASSWORD);

		switch (userType) {
		case ADMIN:

			break;
		case AGENT:

			break;
		case RESTAURATOR:

			break;
		default:
			break;
		}

		add(panel);

	}

	private FlowPanel setRestaurantsManagerTab() {
		restaurantsManagerPanel = new RestaurantsManagerPanel();
		panelList.put(panelCount++, restaurantsManagerPanel);
		tabBar.addTab(restaurantsManagerPanel.getName());
		return (FlowPanel) restaurantsManagerPanel;

	}

	private FlowPanel setEmailTab() {
		emailPanel = new EmailPanel();
		panelList.put(panelCount++, emailPanel);
		tabBar.addTab(emailPanel.getName());
		return (FlowPanel) emailPanel;

	}

	private FlowPanel setDefaultEmptyProfile() {
		defaultEmptyBoard = new DefaultEmptyProfilePanel();
		panelList.put(panelCount++, defaultEmptyBoard);
		tabBar.addTab(Customization.SET_DEFAULT_EMPTY_PROFIL);
		return (FlowPanel) defaultEmptyBoard;
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	private void showPanel(IManager panel) {
		Set<Integer> panelKeys = panelList.keySet();

		for (Integer key : panelKeys) {
			panelList.get(key).show(false);
		}
		if(panel!=null) panel.show(true);
	}

	private void addUser(User user) {
		// user.setPassword(password.getValue());
		userController.addUser(user);
	}

	private void clearScreenData() {

		for (Integer key : panelList.keySet()) {
			panelList.get(key).clearData();

		}
		
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
		
		showPanel(null);
		
		
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
			Widget widget = (Widget) panelList.get(key);
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
			showPanel( panelList.get(whichPanelShow));
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
