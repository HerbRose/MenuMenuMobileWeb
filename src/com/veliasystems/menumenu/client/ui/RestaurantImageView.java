package com.veliasystems.menumenu.client.ui;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CustomScrollPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPanel;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RestaurantImageView extends JQMPage{
	
	JQMHeader header;
	JQMPanel content ;
	JQMButton backButton;
	JQMButton editButton;
	ScrollPanel contentScroll;
	VerticalPanel scrollerDiv;
	
	private String title;
	private Restaurant restaurant;
	
	RestInfoScreen restInfoScreen;
	
	private void init(){
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		editButton = new JQMButton(Customization.EDIT, new RestInfoScreen(restaurant), Transition.SLIDE);
		editButton.setIcon(DataIcon.RIGHT);
		editButton.setIconPos(IconPos.RIGHT);
		
		header = new JQMHeader(title);
		header.setBackButton(backButton);
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

	
	
	public RestaurantImageView() {
		// TODO Auto-generated constructor stub
			init();
	}
	
	public RestaurantImageView(Restaurant r){
		this.restaurant = r;
		this.title = r.getName();
		init();
		
	}
	
	public void addToContent(Widget widget){
		
		//content.add(widget);
		scrollerDiv.add(widget);
		
	}
		
}
