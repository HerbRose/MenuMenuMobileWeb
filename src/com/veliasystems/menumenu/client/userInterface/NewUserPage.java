package com.veliasystems.menumenu.client.userInterface;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;

public class NewUserPage extends MyPage{
	
	
	private FlowPanel logoPanel = new FlowPanel();
	
	private Label editPasswordLabel;
	private PasswordTextBox editPasswordBox;
	private Label repeatPasswordLabel;
	private PasswordTextBox repeatPasswordBox;
	private Label editNameLabel;
	private TextBox editNameBox;
	private Label editSurnameLabel;
	private TextBox editSurnameBox;
	private Label phoneNumberLabel;
	private TextBox phoneNumerBox;
	private Button saveNewUser;
	
	
	private MyRestaurantInfoPanel container = new MyRestaurantInfoPanel();
	
	public NewUserPage() {
		super();
		 logoPanel.add(new Image("img/layout/menuMenuLogo.png"));
		 logoPanel.setStyleName("logoPanel", true);
		 getHeader().addImageHeader(logoPanel);  
		 
		
			editPasswordLabel = new Label(Customization.INPUT_PASSWORD);

			editPasswordBox = new PasswordTextBox();
			editPasswordBox.getElement().setAttribute("placeHolder",Customization.PASSWORD_PLACEHOLDER);
			editPasswordBox.addStyleName("myTextBox nameBox");

			repeatPasswordLabel = new Label(Customization.REPEAT_PASSWORD);

			repeatPasswordBox = new PasswordTextBox();
			repeatPasswordBox.getElement().setAttribute("placeHolder",Customization.REPEAT_PASSWORD_PLACEHOLDER);
			repeatPasswordBox.addStyleName("myTextBox nameBox");

			editNameLabel = new Label(Customization.USER_NAME);

			editNameBox = new TextBox();
			editNameBox.getElement().setAttribute("placeHolder",Customization.USER_NAME_PLACEHOLDER);
			editNameBox.addStyleName("myTextBox nameBox");

			editSurnameLabel = new Label(Customization.USER_SURNAME);

			editSurnameBox = new TextBox();
			editSurnameBox.getElement().setAttribute("placeHolder",Customization.USER_SURNNAME_PLACEHOLDER);
			editSurnameBox.addStyleName("myTextBox nameBox");

			phoneNumberLabel = new Label(Customization.USER_PHONE);

			phoneNumerBox = new TextBox();
			phoneNumerBox.getElement().setAttribute("placeHolder",Customization.USER_PHONE);
			phoneNumerBox.addStyleName("myTextBox nameBox");
			
			saveNewUser = new Button(Customization.SAVE);
			saveNewUser.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(validate()){
						PagesController.MY_POP_UP.showSuccess(new Label(Customization.OK), new IMyAnswer() {
							
							@Override
							public void answer(Boolean answer) {
								
							}
						});
					}
				}
			});
			
			container.setStyleName("containerPanelAddRestaurant", true);	
			container.addItem(editPasswordLabel, editPasswordBox);
			container.addItem(repeatPasswordLabel, repeatPasswordBox);
			container.addItem(editNameLabel, editNameBox);
			container.addItem(editSurnameLabel, editSurnameBox);
			container.addItem(phoneNumberLabel, phoneNumerBox);
			container.add(saveNewUser);
			
			add(container);
		
	}
	
	@Override
	protected void onPageShow() {
		super.onPageShow();
		container.setWidth( JS.getElementOffsetWidth(getParent().getElement())-40 );
	}
	
	private boolean validate(){
		boolean isOk = true;
		String msg ="";
		
		if(!editPasswordBox.getText().trim().equalsIgnoreCase(repeatPasswordBox.getText().trim())){
			msg += Customization.WRONG_PASSWORDS;
			isOk = false;
		}
		
		if(!phoneNumerBox.getText().isEmpty() && !Util.isValidPhoneNumber(phoneNumerBox.getText())){
			msg += Customization.WRONG_PHONE_NUMBER;
			isOk = false;
		}
		if(!msg.isEmpty()){
			PagesController.MY_POP_UP.showError(new Label(msg), new IMyAnswer() {
				
				@Override
				public void answer(Boolean answer) {
					
				}
			});
		}
		return isOk;
	}
}
