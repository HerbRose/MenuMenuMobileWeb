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

public class RestInfoScreen extends JQMPage implements HasClickHandlers {

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
	Label warning = new Label();

	Restaurant restaurant;

	private final StoreServiceAsync storeService = GWT
			.create(StoreService.class);

	public RestInfoScreen(Restaurant r) {

		setRestaurant(r);
		init();

	}
	
	public void setRestaurant(Restaurant r) {
		this.restaurant = r;
		
	}
	
	private void init() {

		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		header = new JQMHeader(restaurant.getName());
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

		// showButton = new JQMButton("show rest");
		// showButton.setBack(false);
		// showButton.setIcon(DataIcon.INFO);
		// content.add(showButton);
		cityLabel.setText(Customization.CITYONE + ":");
		content.add(cityLabel);

		storeService.loadCities(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<String> result) {
				// TODO Auto-generated method stub

				// size = result.size();
				for (String item : result) {
					cityList.addItem(item);
				}
				
				int k=0;
				for (String s : result) {
					
					if (s.equalsIgnoreCase(restaurant.getCity())) {
						cityList.setSelectedIndex(k);
					}
					k++;
				}
						
			}
		});
		System.out.println(restaurant.getName());
		
		content.add(cityList);
		nameLabel.setText(Customization.RESTAURANTNAME + ":");
		content.add(nameLabel);
		nameText.setTitle(Customization.RESTAURANTNAME);
		nameText.setText(restaurant.getName());
		content.add(nameText);
		adressLabel.setText(Customization.RESTAURANTADRESS + ":");
		content.add(adressLabel);
		adressText.setTitle(Customization.RESTAURANTADRESS);
		adressText.setText(restaurant.getAddress());
		content.add(adressText);

	
		add(content);
	}



	{
		this.addClickHandler(new ClickHandler() {

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

	private void meClicked(ClickEvent event) {

		if (isClicked(event, removeButton)) {
			storeService.deleteRestaurant(restaurant,
					new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(Void result) {
							// TODO Auto-generated method stub
							restaurant = null;
							Window.Location.reload();
						}
					});
		}
		if (isClicked(event, saveButton)) {

			restaurant.setName(nameText.getText());
			restaurant.setAddress(adressText.getText());
			restaurant
					.setCity(cityList.getItemText(cityList.getSelectedIndex()));
			restaurant.setBoardImages(null);
			restaurant.setMainImages(null);
			restaurant.setProfileImages(null);
			if (validate()) {
				storeService.saveRestaurant(restaurant,
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								Window.alert("error");
							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								System.out.println("Saved new"
										+ restaurant.getCity() + " "
										+ restaurant.getAddress() + " "
										+ restaurant.getName());
								Window.Location.reload();
							}
						});

			}
		}

	}

	private boolean isClicked(ClickEvent event, JQMButton button) {

		int clickedX = event.getClientX();
		int clickedY = event.getClientY();

		int ButtonX = (int) button.getElement().getAbsoluteLeft();
		int ButtonY = (int) button.getElement().getAbsoluteTop();
		int ButtonWidth = (int) button.getElement().getClientWidth();
		int ButtonHeight = (int) button.getElement().getClientWidth();

		int ButtonStartX = ButtonX;
		int ButtonStopX = ButtonX + ButtonWidth;
		int ButtonStartY = ButtonY;
		int ButtonStopY = ButtonY + ButtonHeight;

		if (clickedX >= ButtonStartX && clickedX <= ButtonStopX
				&& clickedY >= ButtonStartY && clickedY <= ButtonStopY) {
			return true;
		}
		return false;
	}

	private boolean validate() {
		if (!nameText.getText().isEmpty() && !adressText.getText().isEmpty()) {
			return true;
		} else {
			if (nameText.getText().isEmpty() && adressText.getText().isEmpty()) {
				warning.setText(Customization.EMPTYBOTHDATA);
			} else if (nameText.getText().isEmpty()) {
				warning.setText(Customization.EMPTYNAME);
			} else {
				warning.setText(Customization.EMPTYADRESS);
			}
			warning.setStyleName("warning");
			add(warning);

		}
		return false;
	}

}
