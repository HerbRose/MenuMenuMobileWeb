package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPanel;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.StoreService;
import com.veliasystems.menumenu.client.StoreServiceAsync;
import com.veliasystems.menumenu.client.entities.Restaurant;

public class RestInfoScreen extends JQMPage {
	  
	JQMHeader header;
	JQMFooter footer;
	JQMButton removeButton;
	JQMButton saveButton;
	JQMList list;
	JQMButton backButton;
	JQMButton showButton;
	
	Label nameLabel;
	Label cityLabel;
	Label adressLabel;
	
	TextBox cityText;
	TextBox nameText;
	TextBox adressText;
	
	JQMPanel content;
	
	Restaurant restaurant;
	private String name;
	

	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	private void init() {

		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		header = new JQMHeader("[Restaurant]");
		header.setBackButton(backButton);
		header.setFixed(true);
		add(header);
		
		content = new JQMPanel();
		
		showButton = new JQMButton("show rest");
		showButton.setBack(false);
		showButton.setIcon(DataIcon.INFO);
		
		showButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				storeService.getRestaurant(header.getText(), new AsyncCallback<List<Restaurant>>() {
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
				@Override
					public void onSuccess(List<Restaurant> result) {
						// TODO Auto-generated method stub
					if(result.size() > 0){
							Restaurant rest = new Restaurant();
							rest = result.get(0);
							cityLabel = new Label();
							cityLabel.setText(Customization.CITYONE + ":");
							content.add(cityLabel);
							
							cityText = new TextBox();
							cityText.setTitle(Customization.CITYONE);
							cityText.setText(rest.getCity());
							content.add(cityText);
							
							nameLabel = new Label();
							nameLabel.setText(Customization.RESTAURANTNAME + ":");
							content.add(nameLabel);
							
							nameText = new TextBox();
							nameText.setTitle(Customization.RESTAURANTNAME);
							nameText.setText(rest.getName());
							content.add(nameText);		
							
							adressLabel = new Label();
							adressLabel.setText(Customization.RESTAURANTADRESS + ":");
							content.add(adressLabel);
							
							adressText = new TextBox();
							adressText.setTitle(Customization.RESTAURANTADRESS);
							adressText.setText(rest.getAddress());
							content.add(adressText);
					}
					}
				});
			}
		});
		content.add(showButton);
		
		
		
		add(content);	
		
	    removeButton = new JQMButton(Customization.REMOVEPROFILE);
	    removeButton.setIcon(DataIcon.DELETE);
	    removeButton.setIconPos(IconPos.TOP);
	    removeButton.setWidth("50%");
	    
	    saveButton = new JQMButton(Customization.SAVEPROFILE);
	    saveButton.setIcon(DataIcon.CHECK);
	    saveButton.setIconPos(IconPos.TOP);
	    saveButton.setWidth("49%");
	    
	    footer = new JQMFooter(saveButton, removeButton);
	    footer.setFixed(true);
	    
	    add(footer);
	}
	
	
	
	public RestInfoScreen() {
	    init();
	  }
	
	
	public RestInfoScreen( String text ) {
		init();
		setRestaurant(text);
	}
	
	
	public RestInfoScreen setRestaurant( String text ) {
		name = text;
		header.setText(text);
		return this;
	}
	
	
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		//System.out.println("loading");
	}

	
	private boolean getData(){
		
		storeService.getRestaurant(name, new AsyncCallback<List<Restaurant>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<Restaurant> result) {
				// TODO Auto-generated method stub
				System.out.println(result.size());
			}
		});
		
		return true;
	}
}
