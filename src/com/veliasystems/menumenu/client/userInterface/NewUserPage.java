package com.veliasystems.menumenu.client.userInterface;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Transition;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.CookieController;
import com.veliasystems.menumenu.client.controllers.CookieNames;
import com.veliasystems.menumenu.client.controllers.ErrorCodes;
import com.veliasystems.menumenu.client.controllers.Pages;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.responseWrappers.ResponseUserWrapper;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.entities.UserToAdd;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPage;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;

public class NewUserPage extends MyPage{
	
	
	private FlowPanel logoPanel = new FlowPanel();
	
	private Label userEmailLabel;
	private TextBox userEmailBox;
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
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	private CookieController cookieController = CookieController.getInstance();
	private String email = "";
	private String confirmId = "";
	public NewUserPage() {
		super();
		
		email = cookieController.getCookie(CookieNames.NEW_USER_EMAIL);
		confirmId = cookieController.getCookie(CookieNames.NEW_USER_CONFIRME_ID);
		
		cookieController.clearCookie(CookieNames.NEW_USER_CONFIRME_ID);
		cookieController.clearCookie(CookieNames.NEW_USER_EMAIL);
		
		logoPanel.add(new Image("img/layout/menuMenuLogo.png"));
		logoPanel.setStyleName("logoPanel", true);
		getHeader().addImageHeader(logoPanel);  
		 
			
		userEmailLabel = new Label(Customization.LOGIN);

		userEmailBox = new TextBox();
		userEmailBox.setText(email);
		userEmailBox.setEnabled(false);
		userEmailBox.addStyleName("myTextBox nameBox");
		
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
						PagesController.showWaitPanel();
						User user = new User(email);
						user.setPhoneNumber(phoneNumerBox.getText());
						user.setPassword(editPasswordBox.getText());
						user.setName(editNameBox.getText());
						user.setSurname(editSurnameBox.getText());
						
						UserToAdd userToAdd = new UserToAdd(email);
						userToAdd.setConfirmId(confirmId);
//						JS.consolLog(confirmId);
//						JS.consolLog(email);
						storeService.confirmUser(user, userToAdd, new AsyncCallback<ResponseUserWrapper>() {
							
							@Override
							public void onSuccess(ResponseUserWrapper result) {
								PagesController.hideWaitPanel();
								if(result.getErrorCodes().isEmpty()){
									
									long date = new Date().getTime();
									date += 1000*60*60*24*3; //three days
									cookieController.setCookie(CookieNames.LOGIN, email, date);
//									JQMContext.changePage(Pages.PAGE_LOGIN_OK, Transition.SLIDE);
									JQMContext.changePage(PagesController.getPage(Pages.PAGE_LOGIN_CORRECT));
								} else{
									String msg = "";
									for (int i : result.getErrorCodes()) {
										msg += ErrorCodes.getError(i) + "\n\n";
									}
									PagesController.MY_POP_UP.showError(new Label(msg), new IMyAnswer() {
											
										@Override
										public void answer(Boolean answer) {
										}
									});
								}
							}
							
							@Override
							public void onFailure(Throwable caught) {
								PagesController.hideWaitPanel();
								PagesController.MY_POP_UP.showError(new Label(Customization.CONNECTION_ERROR), new IMyAnswer() {
									
									@Override
									public void answer(Boolean answer) {
										// TODO Auto-generated method stub	
									}
								});
								
							}
						});
					}
				}
			});
			
			container.setStyleName("containerPanelAddRestaurant", true);	
			container.addItem(userEmailLabel, userEmailBox);
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
		container.setWidth( JS.getElementOffsetWidth(getElement())-40 );
		PagesController.hideWaitPanel();
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
