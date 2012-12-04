package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPanel;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RestaurantImageView extends JQMPage{
	
	private JQMHeader header;
	private JQMPanel content ;
	private JQMButton backButton;
	private JQMButton editButton;
	private ScrollPanel contentScroll;
	private VerticalPanel scrollerDiv;
	private Label infoLabel;
	
	private Restaurant restaurant;
	private boolean loaded = false;
	private List<SwipeView> swipeViews = new ArrayList<SwipeView>(); 
	private RestaurantController restaurantController = RestaurantController.getInstance();

	private JQMPage previousLastOpoenPage;
	private RestInfoScreen restInfoScreen;
	
	public RestaurantImageView(Restaurant r, JQMPage back){

		this.restaurant = r;

		editButton = new JQMButton(Customization.EDIT, new RestInfoScreen(restaurant, this), Transition.SLIDE);
		editButton.setIcon(DataIcon.RIGHT);
		editButton.setIconPos(IconPos.RIGHT);
		
		header = new JQMHeader(r.getName());
		
		header.setFixed(true);
		header.setRightButton(editButton);
		add(header);
		
		content = new JQMPanel();
		contentScroll = new ScrollPanel();
		contentScroll.setStyleName("mainWrapper");
		int height = Window.getClientHeight();
		height -= 62;
		
		contentScroll.setHeight(height+"px");
		scrollerDiv = new VerticalPanel();
		scrollerDiv.setStyleName("mainScroller");
		
		contentScroll.setWidget(scrollerDiv);
		add(contentScroll);
		
		
	}

	
	@Override
	protected void onPageShow() {
		super.onPageShow();
		
		
		if(!loaded){
			
			SwipeView swipeView = new SwipeView(restaurant, ImageType.MENU , this);
			swipeViews.add(swipeView);
			addToContent( swipeView );
			swipeView = new SwipeView(restaurant, ImageType.PROFILE , this);
			swipeViews.add(swipeView);
			addToContent( swipeView );
			swipeView = new SwipeView(restaurant, ImageType.LOGO , this);
			swipeViews.add(swipeView);
			addToContent( swipeView );
			
			Image addUserImage = new Image("/img/addUser.png");
			addUserImage.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					JQMContext.changePage(PagesController.getPage(Pages.PAGE_RESTAURANT_MANAGER), Transition.SLIDE);
				}
			});
			addUserImage.setStyleName("addUserImage", true);
			addToContent(addUserImage);
			
			Image trashImage = new Image("/img/trash.png");
			trashImage.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
				}
			});
			trashImage.setStyleName("addTrashImage", true);
			//addToContent(trashImage);
			
			infoLabel = new Label(Customization.INFO);
			addToContent(infoLabel);
			
			loaded = true;
		}else{
			header.remove(backButton);
		}
//		if(restaurantController.isFromCityView()){
//			backButton = new JQMButton(Customization.BACK, back, Transition.SLIDE);
//		}else{
//			backButton = new JQMButton(Customization.BACK, PagesController.getPage(Pages.PAGE_RESTAURANT_LIST), Transition.SLIDE);
//		}
//		
		JQMPage page = restaurantController.getLastOpenPage();
		
		restaurantController.setLastOpenPage(this);
		if(page != null) {
			if(page == this) page = previousLastOpoenPage;
			backButton = new JQMButton(Customization.BACK, page, Transition.SLIDE);
		}else{
			backButton = new JQMButton(Customization.BACK, PagesController.getPage(Pages.PAGE_RESTAURANT_LIST), Transition.SLIDE);
		}
		previousLastOpoenPage = page;
		
		String span = "<span class=\"ui-btn-inner ui-btn-corner-all\"><span class=\"ui-btn-text\" style=\"color: #fff\">"+Customization.BACK+"</span><span class=\"ui-icon ui-icon-arrow-l ui-icon-shadow\"></span></span>";      
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		backButton.getElement().setInnerHTML(span);
		backButton.setStyleName("ui-btn-left ui-btn ui-btn-icon-left ui-btn-corner-all ui-shadow ui-btn-down-a ui-btn-up-a ui-btn-up-undefined");
		
		header.add(backButton);
		if(Cookies.getCookie(R.LAST_PAGE) != null){
			Cookies.removeCookie(R.LAST_PAGE); // <<---is it necessary?
		}
//		Date data = new Date(); 
//		Long milisec = data.getTime();
//		milisec += (1000 * 5 * 60); 
		
		Cookies.setCookie(R.LAST_PAGE, restaurant.getId()+"");//, new Date(milisec));
		header.setText(restaurant.getName());
		if(loaded) checkChanges();
		
		Document.get().getElementById("load").setClassName(R.LOADED); 
	
		
	};

	
	public void checkChanges(){
		for (SwipeView swipeView : swipeViews) {
			swipeView.checkChanges();
		}
	}
	
	private JQMPage getMe(){
		return this;
	}
	public void addToContent(Widget widget){
		scrollerDiv.add(widget);
	}
	
	public Restaurant getRestaurant() {
		return restaurant;
	}
		

}
