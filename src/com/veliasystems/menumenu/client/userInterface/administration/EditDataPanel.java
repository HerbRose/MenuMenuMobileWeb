package com.veliasystems.menumenu.client.userInterface.administration;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.User;

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

	public EditDataPanel() {

		setStyleName("barPanel", true);
		show(false);

		inputOldPasswordLabel = new Label(Customization.INPUT_OLD_PASSWORD);

		inputOldPasswordBox = new PasswordTextBox();
		inputOldPasswordBox.getElement().setAttribute("placeHolder",
				Customization.INPUT_OLD_PASSWORD);
		inputOldPasswordBox.addStyleName("properWidth");

		editPasswordLabel = new Label(Customization.INPUT_PASSWORD);

		editPasswordBox = new PasswordTextBox();
		editPasswordBox.getElement().setAttribute("placeHolder",
				Customization.PASSWORD_PLACEHOLDER);
		editPasswordBox.addStyleName("properWidth");

		repeatPasswordLabel = new Label(Customization.REPEAT_PASSWORD);

		repeatPasswordBox = new PasswordTextBox();
		repeatPasswordBox.getElement().setAttribute("placeHolder",
				Customization.REPEAT_PASSWORD_PLACEHOLDER);
		repeatPasswordBox.addStyleName("properWidth");

		editNameLabel = new Label(Customization.USER_NAME);

		editNameBox = new TextBox();
		editNameBox.getElement().setAttribute("placeHolder",
				Customization.USER_NAME_PLACEHOLDER);
		editNameBox.addStyleName("properWidth");

		editSurnameLabel = new Label(Customization.USER_SURNAME);

		editSurnameBox = new TextBox();
		editSurnameBox.getElement().setAttribute("placeHolder",
				Customization.USER_SURNNAME_PLACEHOLDER);
		editSurnameBox.addStyleName("properWidth");

		phoneNumberLabel = new Label(Customization.USER_PHONE);

		phoneNumerBox = new TextBox();
		phoneNumerBox.getElement().setAttribute("placeHolder",
				Customization.USER_PHONE);
		phoneNumerBox.addStyleName("properWidth");

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

		add(inputOldPasswordLabel);
		add(inputOldPasswordBox);
		add(editPasswordLabel);
		add(editPasswordBox);
		add(repeatPasswordLabel);
		add(repeatPasswordBox);
		add(editNameLabel);
		add(editNameBox);
		add(editSurnameLabel);
		add(editSurnameBox);
		add(phoneNumberLabel);
		add(phoneNumerBox);
		add(saveEditedUser);
	}

	private boolean validChangedData() {
		boolean isCorrect = false;


			if (!editPasswordBox.getValue().trim().equals("")) {

				if(!inputOldPasswordBox.getValue().isEmpty()){
				
					if (editPasswordBox.getValue().trim().equals("")
							|| repeatPasswordBox.getValue().trim().equals("")
							|| !editPasswordBox.getValue().equals(repeatPasswordBox.getValue())) {
						editPasswordBox.setStyleName("redShadow", true);
						repeatPasswordBox.setStyleName("redShadow", true);
						isCorrect = false;
					} else {
						editPasswordBox.setStyleName("greenShadow", true);
						repeatPasswordBox.setStyleName("greenShadow", true);
						isCorrect = true;
					}
				}
				
				else{
					inputOldPasswordBox.setStyleName("redShadow", true);
				}
				
			} else {
				isCorrect = true;
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
	}

}
