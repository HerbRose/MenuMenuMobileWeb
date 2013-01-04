package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;
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
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.controllers.UserType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.ui.administration.AddAdminPanel;
import com.veliasystems.menumenu.client.ui.administration.AddAgentPanel;
import com.veliasystems.menumenu.client.ui.administration.AddRestauratorPanel;
import com.veliasystems.menumenu.client.ui.administration.CityManagerPanel;
import com.veliasystems.menumenu.client.ui.administration.DefaultEmptyMenuPanel;
import com.veliasystems.menumenu.client.ui.administration.EditDataPanel;
import com.veliasystems.menumenu.client.ui.administration.EmailPanel;
import com.veliasystems.menumenu.client.ui.administration.IManager;
import com.veliasystems.menumenu.client.ui.administration.LastUploadedImages;
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

	private boolean loaded = false;
	private UserType userType;

	private UserController userController = UserController.getInstance();
	private CityController cityController = CityController.getInstance();
	private RestaurantController restaurantController = RestaurantController
			.getInstance();

	private List<IManager> panelsList = new ArrayList<IManager>();

	private Map<Integer, IManager> panelList = new HashMap<Integer, IManager>();
	private Integer panelCount = 0;

	// panele pokazywane po kliknięciu na zakładkę (np. addAdmin)
	private IManager adminPanel;
	private IManager agentPanel;
	private IManager restauratorPanel;
	private IManager restaurantsManagerPanel;
	private IManager emailPanel;
	private IManager defaultEmptyBoard;
	private IManager editDataPanel;
	private IManager removeUsersPanel;
	private IManager cityManagerPanel;
	private IManager lastUploadedImages;

	private IManager currentlyDisplayedPanel = null;
	
	private Restaurant restaurant = null;
	private Integer whichPanelShow = 0;

	private int widthOfTabBarPanel;

	public RestaurantsManagerScreen() {
		userController.addObserver(this);
		restaurantController.addObserver(this);
		cityController.addObserver(this);
		
		userType = userController.getUserType();
		setHeader();
		setContent();
	}

	private void setHeader() {
		header = new JQMHeader("");
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
		var element = $wnd.document.getElementById(elementId);
		//		var tbody = table.getElementsByTagName("tbody");
		if(element != "undefined" && element!= null){
			element.style.marginLeft = left + "px";
		}
	}-*/;

	/**
	 * adding panels for the administrator
	 */
	private void setAdminPanels() {
		add(setAdminPanel());
		add(setAgentPanel());
		add(setRestauratorPanel());
		add(setRestaurantsManagerTab());
		add(setEmailTab());
		add(setDefaultEmptyProfile());
		add(setEditDataPanel());
		add(setRemoveUsersPanel());
		add(setCityManager());
		add(setLastImages());
	}

	/**
	 * adding panels for the agent
	 */
	private void setAgentButtons() {
		add(setRestauratorPanel());
		add(setEditDataPanel());
	}
	
	/**
	 * create a administrator tab
	 * @return instance of AddAdminPanel() projected onto FlowPanel
	 */
	private FlowPanel setAdminPanel() {
		adminPanel = new AddAdminPanel();
		panelList.put(panelCount++, adminPanel);
		tabBar.addTab(adminPanel.getName());

		return (FlowPanel) adminPanel;
	}

	/**
	 * create a agent tab
	 * @return instance of AddAgentPanel() projected onto FlowPanel
	 */
	private FlowPanel setAgentPanel() {
		agentPanel = new AddAgentPanel();
		panelList.put(panelCount++, agentPanel);
		tabBar.addTab(agentPanel.getName());

		return (FlowPanel) agentPanel;
	}

	/**
	 * create a restorer tab
	 * @return instance of AddRestauratorPanel() projected onto FlowPanel
	 */
	private FlowPanel setRestauratorPanel() {
		restauratorPanel = new AddRestauratorPanel(restaurant);
		panelList.put(panelCount++, restauratorPanel);
		tabBar.addTab(restauratorPanel.getName());
		if (restaurant != null) {
			whichPanelShow = panelCount - 1;
		}

		return (FlowPanel) restauratorPanel;
	}

	/**
	 * create a tab to editing data
	 * @return instance of EditDataPanel projected onto FlowPanel
	 */
	private FlowPanel setEditDataPanel() {
		editDataPanel = new EditDataPanel();
		panelList.put(panelCount++, editDataPanel);
		tabBar.addTab(Customization.EDIT_DATA);
		return (FlowPanel) editDataPanel;
	}
	
	/**
	 * create a tab to removing users
	 * @return instance of RemoveUsersPanel projected onto FlowPanel
	 */
	private FlowPanel setRemoveUsersPanel(){
		removeUsersPanel = new RemoveUsersPanel();
		panelList.put(panelCount++, removeUsersPanel);
		tabBar.addTab(Customization.REMOVE_USERS);
		return (FlowPanel) removeUsersPanel;
	}

	/**
	 * create a tab to management restaurants
	 * @return instance of RestaurantsManagerPanel projected onto FlowPanel
	 */
	private FlowPanel setRestaurantsManagerTab() {
		restaurantsManagerPanel = new RestaurantsManagerPanel();
		panelList.put(panelCount++, restaurantsManagerPanel);
		tabBar.addTab(restaurantsManagerPanel.getName());
		return (FlowPanel) restaurantsManagerPanel;

	}

	/**
	 * create a tab to sending mail 
	 * @return instance of EmailPanel projected onto FlowPanel
	 */
	private FlowPanel setEmailTab() {
		emailPanel = new EmailPanel();
		panelList.put(panelCount++, emailPanel);
		tabBar.addTab(emailPanel.getName());
		return (FlowPanel) emailPanel;

	}

	/**
	 * create a tab to sets default empty profile image
	 * @return instance of DefaultEmptyProfilrPanel 
	 */
	private FlowPanel setDefaultEmptyProfile() {
		defaultEmptyBoard = new DefaultEmptyMenuPanel();
		panelList.put(panelCount++, defaultEmptyBoard);
		tabBar.addTab(defaultEmptyBoard.getName());
		return (FlowPanel) defaultEmptyBoard;
	}

	private FlowPanel setCityManager() {
		cityManagerPanel = new CityManagerPanel();
		panelList.put(panelCount++, cityManagerPanel);
		tabBar.addTab(cityManagerPanel.getName());
		return (FlowPanel) cityManagerPanel;
	}

	
	private FlowPanel setLastImages(){
		lastUploadedImages = new LastUploadedImages();
		panelList.put(panelCount++, lastUploadedImages);
		tabBar.addTab(lastUploadedImages.getName());
		return (FlowPanel) lastUploadedImages;
	}
	
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) { // <--- IS IT NECESSARY ???
		return addDomHandler(handler, ClickEvent.getType());
	}

	/**
	 * Hide all panels (tabs) and show one
	 * @param panel - panel to show
	 */
	private void showPanel(IManager panel) {
		Set<Integer> panelKeys = panelList.keySet();

		for (Integer key : panelKeys) {
			panelList.get(key).show(false);
		}
		if(panel!=null){
			panel.clearData();
			panel.show(true);
			currentlyDisplayedPanel = panel;
		}
	}

	/**
	 * clear all tabs data
	 */
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
		restaurantController.setLastOpenPage(this);
	}
	

	@Override
	public void onChange() {
		//clearScreenData();
		
		if(currentlyDisplayedPanel != null){
			currentlyDisplayedPanel.clearData();
		}
	}
}