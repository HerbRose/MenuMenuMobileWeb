package com.veliasystems.menumenu.client.ui;

import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;

public class RestaurantsListScreen extends JQMPage {
	  
	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMList list;
	JQMButton backButton;
	
	public RestaurantsListScreen() {
	    
		header = new JQMHeader(Customization.RESTAURANTS);
		header.setFixed(true);
		header.setText(Customization.RESTAURANTS);
		
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		header.setBackButton(backButton);
		add(header);
	    
		list = new JQMList();
	    list.setInset(false);
	    
	    list.addItem("Pimiento", new RestInfoScreen("Pimiento") );
	    list.addItem("Babcia Malina", new RestInfoScreen("Babcia Malina"));
	    list.addItem("Warsztat", new RestInfoScreen("Warsztat"));
	    
	    add(list);
	    
	    addButton = new JQMButton(Customization.ADDRESTAURANT, new AddRestaurantScreen());
	    addButton.setIcon(DataIcon.PLUS);
	    addButton.setIconPos(IconPos.TOP);
	    addButton.setTransition(Transition.SLIDE);
	    addButton.setWidth("100%");
	     
	    footer = new JQMFooter(addButton);
	    footer.setFixed(true);
	    
	    
	    
	    add(footer);
	    
	  }
	}