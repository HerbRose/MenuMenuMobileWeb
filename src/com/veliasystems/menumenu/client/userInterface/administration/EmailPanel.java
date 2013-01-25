package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.JS;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyInfoPanelRow;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyPopUp.IMyAnswer;
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyRestaurantInfoPanel;

public class EmailPanel extends FlowPanel implements IManager, IObserver {

	
	
	private TextBox senderTextBox;
	private TextBox subjectTextBox;
	private TextArea messageTextArea;
	
	private MyListCombo addressListCombo = new MyListCombo(true);

	
	private Label fromLabel = new Label(Customization.SENDER );
	private Label messageLabel = new Label(Customization.MESSAGE_TEXT);
	private Label subjectLabel = new Label(Customization.SUBJECT);
	private Label toLabel = new Label(Customization.ADDRESSEE);
	
	
	private Button send = new Button(Customization.SEND);
	private MyRestaurantInfoPanel container = new MyRestaurantInfoPanel();
	
	private UserController userController = UserController.getInstance();
	private List<String> emailList;
	
	private final MyInfoPanelRow messageRow;
 
	public EmailPanel() {
		userController.addObserver(this);
		setStyleName("barPanel", true);
		show(false);
			
		senderTextBox = new TextBox();
		senderTextBox.addStyleName("myTextBox nameBox");
		senderTextBox.setEnabled(false);
		senderTextBox.setText(userController.getLoggedUser().getEmail());
		
		
		subjectTextBox = new TextBox();
		subjectTextBox.addStyleName("myTextBox nameBox");
		
		messageTextArea = new TextArea();
		messageTextArea.addStyleName("myTextBox nameBox");
		
		
		subjectTextBox.getElement().setAttribute("placeHolder",Customization.SUBJECT_PLACEHOLDER);

		messageTextArea.getElement().setAttribute("placeHolder",Customization.MESSAGE_PLACEHOLDER);
		messageTextArea.getElement().getStyle().setWidth(100, Unit.PCT);
		
		
		send.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
//				if(validate()){
//					List<String> emailsToSend = new ArrayList<String>();
//					for (long item : addressListCombo.getCheckedList()) {
//						emailsToSend.add(emailList.get( (int) item)); 
//					}
//					userController.sendMail(emailsToSend, senderTextBox.getText(), subjectTextBox.getText(), messageTextArea.getText());
//				}
				String valid  = validate();
				
				if(valid.isEmpty() && !addressListCombo.getCheckedList().isEmpty()){
					sendMessage();
				}else if (!valid.isEmpty() && !addressListCombo.getCheckedList().isEmpty()){
					
					PagesController.MY_POP_UP.showConfirm(new Label(valid), new IMyAnswer() {		
						@Override
						public void answer(Boolean answer) {
							if(answer){
								sendMessage();
							}
						}
					});
				} else if(addressListCombo.getCheckedList().isEmpty()){
						PagesController.MY_POP_UP.showError(new Label(Customization.EMPTY_MAIL_LIST), new IMyAnswer() {		
						@Override
						public void answer(Boolean answer) {
	
						}
					});
				}
			}
		});
		
		container.setStyleName("containerPanelAddRestaurant", true);
		
		container.addItem(fromLabel, senderTextBox);
		container.addItem(toLabel, addressListCombo);
		container.addItem(subjectLabel, subjectTextBox);
		messageRow = container.addItem(messageLabel, messageTextArea);
		container.add(send);
		add(container);
		
		
		messageTextArea.addKeyDownHandler(new KeyDownHandler() {
		
		@Override
		public void onKeyDown(KeyDownEvent event) {
			String height = messageTextArea.getElement().getStyle().getHeight();
			height = height.replace("px", "");
			if(!height.isEmpty()){
				messageRow.setHeight(Integer.parseInt(height));
			}
			
		}
	});
	
		
	}
	
	private void sendMessage(){
		List<String> emailsToSend = new ArrayList<String>();
		for (long item : addressListCombo.getCheckedList()) {
			emailsToSend.add(emailList.get( (int) item)); 
		}
		userController.sendMail(emailsToSend, senderTextBox.getText(), subjectTextBox.getText(), messageTextArea.getText());
	}
	
	
	@Override
	public void clearData() {
		messageTextArea.setText("");
		
		
		
		//set height to css values after sending email in order to rest layout
		messageTextArea.getElement().getStyle().setHeight(44.0, Unit.PX);
		messageRow.setHeight(44);
		
		
		subjectTextBox.setText("");
		addressListCombo.clearSelection();
	}

	@Override
	public String getName() {
		return Customization.SEND_EMAIL;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
		if(isVisable){
			container.setWidth( JS.getElementOffsetWidth(getParent().getElement())-40 );
			userController.getUsersFromServer(this);
		}
	}

	
	@Override
	public void onChange() {
		clearData();	
	}

	@Override
	public void newData() {
		emailList = userController.getEmailAdresses();
		emailList.remove(userController.getLoggedUser().getEmail());
		fillMailCombo();
	}
	
	private void fillMailCombo(){
			
		for (String email : emailList) {
			addressListCombo.addListItem(addressListCombo.getNewCheckBoxItem(email), emailList.indexOf(email));
		}
		PagesController.hideWaitPanel();
		
	}
	
	private String validate(){
	String msg = "";
				if(subjectTextBox.getText().isEmpty() && messageTextArea.getText().isEmpty()){
					msg += Customization.CONFIRMATION_NO_SUBJECT_AND_NO_MESSAGE + "\n";
				} else if(subjectTextBox.getText().isEmpty()) {
					msg+=Customization.CONFIRMATION_NO_SUBJECT + "\n";		
				} else if(messageTextArea.getText().isEmpty()){				
					msg+=Customization.CONFIRMATION_NO_MESSAGE+ "\n";
				}

		return msg;
	}

}
