package com.veliasystems.menumenu.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.form.elements.JQMPassword;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.User;

public class AddUsersScreen extends JQMPage implements HasClickHandlers{
	
	private JQMHeader header;
	private JQMButton backButton;
	private JQMPage pageToBack;
	private TextBox inputEmail;
	private JQMFooter footer;
	private JQMButton addAdmin;
	private JQMButton addRestaurator;
	private JQMButton addAgent;
	private JQMPassword password;
	private Label mailLabel;
	private Label passwordLabe;
	private SuggestBox suggestBox;
	
	private boolean loaded = false;
	
	private UserController userController = UserController.getInstance();
	
	public AddUsersScreen(JQMPage back) {
		pageToBack = back;
		setHeader();
		setContent();
		setFooter();
	}
	
	@Override
	protected void onPageShow() {
		
		inputEmail.setText("");
		if(!loaded){
			backButton = new JQMButton("",  pageToBack, Transition.SLIDE );	
			String span = "<span class=\"ui-btn-inner ui-btn-corner-all\"><span class=\"ui-btn-text\" style=\"color: #fff\">"+Customization.BACK+"</span><span class=\"ui-icon ui-icon-arrow-l ui-icon-shadow\"></span></span>";      
			backButton.setIcon(DataIcon.LEFT);
			backButton.setIconPos(IconPos.LEFT);	
			backButton.getElement().setInnerHTML(span);
			backButton.setStyleName("ui-btn-left ui-btn ui-btn-icon-left ui-btn-corner-all ui-shadow ui-btn-down-a ui-btn-up-a ui-btn-up-undefined");		
			header.add(backButton);
			loaded = true;
		}	
		Document.get().getElementById("load").setClassName(R.LOADED);
	}
	
	private void setHeader(){
		header = new JQMHeader(Customization.ADD_USER);
		header.setFixed(true);
		add(header);
	}
	
	private void setContent(){
		mailLabel = new Label(Customization.INPUT_EMAIL);
		add(mailLabel);
		inputEmail = new TextBox();
		add(inputEmail);
		
		passwordLabe = new Label(Customization.INPUT_PASSWORD);
		add(passwordLabe);
		password = new JQMPassword();
		add(password);
	
	}
	
	private void setFooter(){
		footer = new JQMFooter();
		footer.setFixed(true);
		
		switch(userController.getUserType()){
		case ADMIN:
			setAdminButtons();
			break;
		default:
			setAgentButtons();
		}		
		add(footer);
		
	}
	
	private void setAdminButtons(){
		addAdmin = new JQMButton(Customization.ADD_ADMIN);
		addAdmin.setIcon(DataIcon.PLUS);
		addAdmin.setIconPos(IconPos.TOP);
		footer.add(addAdmin);
		addAgent = new JQMButton(Customization.ADD_AGENT);
		addAgent.setIcon(DataIcon.PLUS);
		addAgent.setIconPos(IconPos.TOP);
		footer.add(addAgent);
		addRestaurator = new JQMButton(Customization.ADD_RESTAURATOR);
		addRestaurator.setIcon(DataIcon.PLUS);
		addRestaurator.setIconPos(IconPos.TOP);
		footer.add(addRestaurator);
	}
	private void setAgentButtons(){
		addRestaurator = new JQMButton(Customization.ADD_RESTAURATOR);
		addRestaurator.setIcon(DataIcon.PLUS);
		addRestaurator.setIconPos(IconPos.TOP);
		footer.add(addRestaurator);
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

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
	
	{
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				meClicked(event);
			}
		});
	}
	
	private void meClicked(ClickEvent event){
		if(isClicked(event, addAdmin)){
			User admin = new User(inputEmail.getText());
			admin.setPassword(password.getValue());
			admin.setAdmin(true);
			userController.addUser(admin);
		}
	}
	
	

}
