package com.veliasystems.menumenu.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
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
	JQMPanel content;
	JQMButton backButton;
	JQMButton editButton;
	
	private String title;
	private Restaurant restaurant;
	
	RestInfoScreen restInfoScreen;
	
	private void init(){
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		header = new JQMHeader(title);
		header.setBackButton(backButton);
		header.setFixed(true);
		add(header);
		
		editButton = new JQMButton(Customization.EDIT, new RestInfoScreen(restaurant), Transition.SLIDE);
		editButton.setIcon(DataIcon.RIGHT);
		editButton.setIconPos(IconPos.RIGHT);
		add(editButton);
	
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
	
		
}
