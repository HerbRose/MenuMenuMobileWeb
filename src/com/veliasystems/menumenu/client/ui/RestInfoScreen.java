package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class RestInfoScreen extends JQMPage implements HasClickHandlers{
	  
	JQMHeader header;
	JQMFooter footer;
	JQMButton removeButton;
	JQMButton saveButton;
	JQMList list;
	JQMButton backButton;
	JQMButton showButton;
	
	Label nameLabel = new Label();
	Label cityLabel = new Label();
	Label adressLabel = new Label();
	
	ListBox cityList = new ListBox();
	TextBox nameText = new TextBox();
	TextBox adressText = new TextBox();
	
	JQMPanel content;

	
	Restaurant restaurant = new Restaurant();
	
	String city;
	String name;
	String adress;
	
	private int size;

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
		
		removeButton = new JQMButton(Customization.REMOVEPROFILE);
		removeButton.setIcon(DataIcon.DELETE);
		removeButton.setIconPos(IconPos.TOP);
		removeButton.setWidth("49%");
		
		saveButton = new JQMButton(Customization.SAVEPROFILE);
		saveButton.setIcon(DataIcon.STAR);
		saveButton.setIconPos(IconPos.TOP);
		saveButton.setWidth("49%");
		footer = new JQMFooter();
		
		footer.setFixed(true);
		footer.add(removeButton);
		footer.add(saveButton);
		
		add(footer);
		content = new JQMPanel();
		
		
		
		showButton = new JQMButton("show rest");
		showButton.setBack(false);
		showButton.setIcon(DataIcon.INFO);

		content.add(showButton);
		cityLabel.setText(Customization.CITYONE + ":");
		content.add(cityLabel);

		
		cityLabel = new Label();
		content.add(cityLabel);
		cityText = new TextBox();
		content.add(cityText);
		nameText = new TextBox();
		content.add(nameText);	
		nameLabel = new Label();
		content.add(nameLabel);
		adressLabel = new Label();
		content.add(adressLabel);
		adressText = new TextBox();
		content.add(adressText);
		
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
							
							cityLabel.setText(Customization.CITYONE + ":");
							
							cityText.setTitle(Customization.CITYONE);
							cityText.setText(rest.getCity());
							
							nameLabel.setText(Customization.RESTAURANTNAME + ":");
							
							nameText.setTitle(Customization.RESTAURANTNAME);
							nameText.setText(rest.getName());
							
							adressLabel.setText(Customization.RESTAURANTADRESS + ":");
							
							adressText.setTitle(Customization.RESTAURANTADRESS);
							adressText.setText(rest.getAddress());
					}
					}
				});
			}
		});
>>>>>>> branch 'master' of http://github.com/pjakwert/MenuMenuMobileWeb.git
		
<<<<<<< HEAD
		storeService.loadCities(new AsyncCallback<List<String>>() {
=======
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
>>>>>>> branch 'master' of http://github.com/pjakwert/MenuMenuMobileWeb.git

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<String> result) {
				// TODO Auto-generated method stub
				
				size = result.size();
				for(String item: result){
					cityList.addItem(item);
				}
			}
		});
		

		content.add(cityList);
		nameLabel.setText(Customization.RESTAURANTNAME + ":");		
		content.add(nameLabel);
		nameText.setTitle(Customization.RESTAURANTNAME);
		content.add(nameText);
		adressLabel.setText(Customization.RESTAURANTADRESS + ":");	
		content.add(adressLabel);
		adressText.setTitle(Customization.RESTAURANTADRESS);
		content.add(adressText);
		
		
		
		
		showButton.addClickHandler(new ClickHandler() {
			
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
					
				storeService.getRestaurant(restaurant, new AsyncCallback<Restaurant>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Restaurant result) {
						// TODO Auto-generated method stub
						
						nameText.setText(result.getName());
						adressText.setText(result.getAddress());
						int index = -1;
						for(int i=0; i< size; i++){
							if (cityList.getItemText(i).equals(result.getCity())){
								System.out.println("index of item " + i);
								index = i;
								break;
							}
						}
						
						if(index >= 0){
							System.out.println("index of item " + index);
							cityList.setSelectedIndex(index);
						}
	
					}
				});
			}
		});
	
		add(content);	
				
		
		
//		removeButton.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				// TODO Auto-generated method stub
//					storeService.deleteRestaurant(restaurant, new AsyncCallback<Void>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//							// TODO Auto-generated method stub
//							
//						}
//
//						@Override
//						public void onSuccess(Void result) {
//							// TODO Auto-generated method stub
//							Window.Location.reload();
//						}
//					});
//			}
//		});
//		
//		add(removeButton);
		
		
		
//		saveButton.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				// TODO Auto-generated method stub
//				
//				
//				name = nameText.getText();
//				adress = adressText.getText();
//				
//				if(name.isEmpty() && adress.isEmpty()){
//					showWarning();
//				}
//				else
//				{
//					final Restaurant tmpr = new Restaurant();
//					tmpr.setName(name);
//					tmpr.setAddress(adress);
//					tmpr.setCity(cityList.getItemText(cityList.getSelectedIndex()));
//						
//					storeService.saveRestaurant(tmpr, new AsyncCallback<Void>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//							// TODO Auto-generated method stub
//							
//						}
//
//						@Override
//						public void onSuccess(Void result) {
//							// TODO Auto-generated method stub
//							System.out.println("Saved new" + tmpr.getCity() + " " + tmpr.getAddress() + " " + tmpr.getName());
//						}
//					});
//					
//					storeService.deleteRestaurant(restaurant, new AsyncCallback<Void>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//							// TODO Auto-generated method stub
//							
//						}
//
//						@Override
//						public void onSuccess(Void result) {
//							// TODO Auto-generated method stub
//							System.out.println("old restaurant deleted");
//							Window.Location.reload();
//						}
//					});
//				}
//				
//			}
//		});
//		
//		add(saveButton);
	}
	
	
	public RestInfoScreen() {
	    init();
	  }
	
	
	public RestInfoScreen(Restaurant r) {
		init();
		setRestaurant(r);
	}
	
	
	public RestInfoScreen setRestaurant(Restaurant r) {

		header.setText(r.getName());
		this.restaurant = r;
		return this;
	}

	private void showWarning(){
		
	}

	{
		this.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {			
					meClicked(event);
			}
		});
	}
		
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
	
	private void meClicked(ClickEvent event){
		
		if(isClicked(event, removeButton)){
			Window.alert("remove");
		}
		if(isClicked(event, saveButton)){
			Window.alert("save");
		}
		
		
	}
	
	private boolean isClicked(ClickEvent event, JQMButton button){
		
		int clickedX =event.getClientX();
		int clickedY= event.getClientY();
		
		int ButtonX = (int) button.getElement().getAbsoluteLeft();
		int ButtonY = (int) button.getElement().getAbsoluteTop();
		int ButtonWidth = (int) button.getElement().getClientWidth();
		int ButtonHeight = (int) button.getElement().getClientWidth();
		
		int ButtonStartX = ButtonX;
		int ButtonStopX = ButtonX + ButtonWidth;
		int ButtonStartY = ButtonY;
		int ButtonStopY = ButtonY + ButtonHeight;
		
		if(clickedX >= ButtonStartX && clickedX <= ButtonStopX && clickedY>= ButtonStartY && clickedY <= ButtonStopY){
			return true;
		}
		
		
		return false;
	}
	
}
