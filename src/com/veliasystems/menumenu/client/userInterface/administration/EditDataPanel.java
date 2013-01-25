package com.veliasystems.menumenu.client.userInterface.administration;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;

public class EditDataPanel extends FlowPanel implements IManager {

	private Label inputOldPasswordLabel;
	private PasswordTextBox inputOldPasswordBox;
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
	private Button saveEditedUser;

	private UserController userController = UserController.getInstance();
	
	private MyRestaurantInfoPanel container = new MyRestaurantInfoPanel();
	
	
	public EditDataPanel() {

		setStyleName("barPanel", true);
		show(false);

		inputOldPasswordLabel = new Label(Customization.INPUT_OLD_PASSWORD);

		inputOldPasswordBox = new PasswordTextBox();
		inputOldPasswordBox.getElement().setAttribute("placeHolder",Customization.INPUT_OLD_PASSWORD);
		inputOldPasswordBox.addStyleName("myTextBox nameBox");

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

		saveEditedUser = new Button(Customization.SAVE);

		saveEditedUser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (validChangedData()) {
					User user = new User(userController.getLoggedUser().getEmail());
					String oldPassword = "";
					String newPassword = "";
					if (!editNameBox.getValue().trim().equals(""))
						user.setName(editNameBox.getValue());
					
					if (!editSurnameBox.getValue().trim().equals(""))
						user.setSurname(editSurnameBox.getValue());
					
					if (!repeatPasswordBox.getValue().trim().equals("")) {
						oldPassword = inputOldPasswordBox.getValue();
						newPassword = repeatPasswordBox.getValue();
					} 
					
					if (!phoneNumerBox.getValue().trim().equals(""))
						user.setPhoneNumber(phoneNumerBox.getValue());
					
					userController.changeUserData(user, oldPassword, newPassword);
					clearShadows();
				}

			}
		});
		
		
		container.setStyleName("containerPanelAddRestaurant", true);
		
		container.addItem(inputOldPasswordLabel, inputOldPasswordBox);
		container.addItem(editPasswordLabel, editPasswordBox);
		container.addItem(repeatPasswordLabel, repeatPasswordBox);
		container.addItem(editNameLabel, editNameBox);
		container.addItem(editSurnameLabel, editSurnameBox);
		container.addItem(phoneNumberLabel, phoneNumerBox);
		container.add(saveEditedUser);
		
		add(container);
		
	}

	private boolean validChangedData() {
		boolean isCorrect = false;
		
		String msg = "";

			if (!editPasswordBox.getValue().trim().equals("")) {

				if(!inputOldPasswordBox.getValue().isEmpty()){
				
					if (editPasswordBox.getValue().trim().equals("")
							|| repeatPasswordBox.getValue().trim().equals("")
							|| !editPasswordBox.getValue().equals(repeatPasswordBox.getValue())) {
						msg += Customization.PASSWORD_AND_REPEAT_ARE_NOT_THE_SAME;
						isCorrect = false;
					} else {
						isCorrect = true;
					}
				}
				
				else{
					msg += Customization.OLD_PASSWORD_MISSING;
				}
				
			} else {
				isCorrect = true;
			}
			
			if(!msg.isEmpty()){
//				Window.alert(msg);
				PagesController.MY_POP_UP.showError(new Label(msg), new IMyAnswer() {
					
					@Override
					public void answer(Boolean answer) {
				
					}
				});
			}

		return isCorrect;
	}

	@Override
	public void clearData() {
		User user = userController.getLoggedUser();
		editNameBox.setValue(user.getName());
		editSurnameBox.setValue(user.getSurname());
		phoneNumerBox.setValue(user.getPhoneNumber());

	}
	
	private void clearShadows(){
		editPasswordBox.removeStyleName("greenShadow");
		editPasswordBox.removeStyleName("redShadow");
		
		repeatPasswordBox.removeStyleName("greenShadow");
		repeatPasswordBox.removeStyleName("redShadow");
		
		inputOldPasswordBox.removeStyleName("greenShadow");
		inputOldPasswordBox.removeStyleName("redShadow");
	}

	@Override
	public String getName() {
		return Customization.EDIT_DATA;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
		if(isVisable){
			container.setWidth( JS.getElementOffsetWidth(getParent().getElement())-40 );
		}
		
	}

}
