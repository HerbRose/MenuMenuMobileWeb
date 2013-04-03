package com.veliasystems.menumenu.client.userInterface.administration;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.User;
/**
 * {@link Deprecated}
 */
@Deprecated
public class AddAdminPanel extends FlowPanel implements IManager {

	
	private TextBox inputEmailAdmin;
	private PasswordTextBox passwordAdmin;
	private PasswordTextBox passwordAdmin2;
	private JQMButton saveAdminButton;

	private UserController userController = UserController.getInstance();
	private Label mailLabel;
	private Label passwordLabe;
	private Label repeatPasswordLabe;
	
	public AddAdminPanel() {
		
		setStyleName("barPanel", true);
		show(false);
		
		mailLabel = new Label(Customization.INPUT_EMAIL);
		passwordLabe = new Label(Customization.INPUT_PASSWORD);
		repeatPasswordLabe = new Label(Customization.REPEAT_PASSWORD);
		
		inputEmailAdmin = new TextBox();
		inputEmailAdmin.addStyleName("properWidth");
		setPlaceHolder(inputEmailAdmin, Customization.EMAIL_PLACEHOLDER);
		
		passwordAdmin = new PasswordTextBox();
		passwordAdmin.addStyleName("properWidth");
		setPlaceHolder(passwordAdmin, Customization.PASSWORD_PLACEHOLDER);
		
		passwordAdmin2 = new PasswordTextBox();
		passwordAdmin2.addStyleName("properWidth");
		setPlaceHolder(passwordAdmin2, Customization.REPEAT_PASSWORD_PLACEHOLDER);
		
		saveAdminButton = new JQMButton(Customization.SAVE);
		saveAdminButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (validData()) {
					User admin = new User(inputEmailAdmin.getValue().trim());
					admin.setPassword(passwordAdmin.getValue().trim());
					admin.setAdmin(true);
					addUser(admin);
				}

			}

			
		});
		add(mailLabel);
		add(inputEmailAdmin);
		add(passwordLabe);
		add(passwordAdmin);
		add(repeatPasswordLabe);
		add(passwordAdmin2);
		add(saveAdminButton);
		
		
	}
	
	private void setPlaceHolder(Widget element, String placeHolder){
		element.getElement().setAttribute("placeHolder", placeHolder);
	}
	
	private boolean validData() {
		boolean isCorrect = false;
		
		if (userController.isUserInStor(inputEmailAdmin.getValue().trim())
				|| inputEmailAdmin.getValue().trim().equals("")
				|| !Util.isValidEmail(inputEmailAdmin.getValue())) {
			setValidDataStyle(false, inputEmailAdmin);
		} else {
			isCorrect = true;
			setValidDataStyle(true, inputEmailAdmin);
		}
		if (passwordAdmin.getValue().trim().equals("")
				|| passwordAdmin2.getValue().trim().equals("")
				|| !passwordAdmin.getValue().equals(
						passwordAdmin2.getValue())) {
			isCorrect = false;
			setValidDataStyle(false, passwordAdmin);
			setValidDataStyle(false, passwordAdmin2);
			
		} else {
			setValidDataStyle(true, passwordAdmin);
			setValidDataStyle(true, passwordAdmin2);
		}

		return isCorrect;
	}
	
	/**
	 * sets the right shadow around the widget
	 * @param isCorrect - if <b>true</b> sets green shadow, if <b>false</b> sets red shadow, if <b>null</b> hide all shadows
	 * @param widget - widget
	 */
	private void setValidDataStyle(Boolean isCorrect, Widget widget){
		if(widget == null) return;
		
		String correct = "greenShadow";
		String unCorrect = "redShadow";
		
		if(isCorrect == null){
			widget.setStyleName(correct, false);
			widget.setStyleName(unCorrect, false);
			return;
		}
		widget.setStyleName(correct, isCorrect);
		widget.setStyleName(unCorrect, !isCorrect);
	}
	
	private void addUser(User user) {
		userController.addUser(user);
	}
	
	@Override
	public void clearData() {
		inputEmailAdmin.setValue("");
		setValidDataStyle(null, inputEmailAdmin);
		passwordAdmin.setValue("");
		setValidDataStyle(null, passwordAdmin);
		passwordAdmin2.setValue("");
		setValidDataStyle(null, passwordAdmin2);
	}

	@Override
	public String getName() {
		return Customization.ADD_ADMIN;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
	}

}
