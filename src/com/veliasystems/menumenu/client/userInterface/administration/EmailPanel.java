package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.List;

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
import com.veliasystems.menumenu.client.userInterface.myWidgets.MyListCombo;
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
	
 
	public EmailPanel() {
		userController.addObserver(this);
		setStyleName("barPanel", true);
		show(false);
		
		
		
//		addresseeListBox = new ListBox();
//		addresseeListBox.addStyleName("properWidth");
//		addresseeListBox.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				final Label label = new Label(addresseeListBox
//						.getItemText(addresseeListBox.getSelectedIndex()));
//				label.setTitle(addresseeListBox.getValue(addresseeListBox
//						.getSelectedIndex()));
//
//				label.addClickHandler(new ClickHandler() {
//
//					@Override
//					public void onClick(ClickEvent event) {
//						chosenEmailPanel.remove(label);
//						chosenEmailList.remove(label.getTitle());
//						
//						if(label.getTitle().equalsIgnoreCase(addresseeListBox.getValue(addresseeListBox
//						.getSelectedIndex()))){
//							addresseeListBox.setSelectedIndex(0);
//						}
//						
//						if (chosenEmailList.isEmpty()) {
//							chosenEmailPanel.removeStyleName("greenShadow");
//						}
//					}
//				});
//				clearChosenEmailList();
//				chosenEmailPanel.add(label);
//				chosenEmailPanel.setStyleName("greenShadow");
//				chosenEmailList.add(addresseeListBox.getValue(addresseeListBox
//						.getSelectedIndex()));
//			}
//		});

//		chosenEmailPanel = new FlowPanel();
//		chosenEmailList = new ArrayList<String>();
		
		
		
		
	
		
		
		
		
//		

		
		
//		JQMButton sendButton = new JQMButton(Customization.SEND);
//		sendButton.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				if (checkEmailValues()) {
//					PagesController.showWaitPanel();
////					userController.sendMail(chosenEmailList,
////							senderTextBox.getText(), subjectTextBox.getValue(),
////							messageTextArea.getValue());
//				}
//			}
//		});
		
		

		

		
//		add(toLabel);
//		add(addresseeListBox);
//		add(chosenEmailPanel);
//		add(fromLabel);
//		add(senderTextBox);
//		add(subjectLabel);
//		add(subjectTextBox);
//		add(messageLabel);
//		add(messageTextArea);
//		add(sendButton);
		
		senderTextBox = new TextBox();
		senderTextBox.addStyleName("myTextBox nameBox");
		senderTextBox.setEnabled(false);
		senderTextBox.setText(userController.getLoggedUser().getEmail());
		
		
		subjectTextBox = new TextBox();
		subjectTextBox.addStyleName("myTextBox nameBox");
		
		messageTextArea = new TextArea();
		messageTextArea.addStyleName("myTextBox nameBox");
		
		
		subjectTextBox.getElement().setAttribute("placeHolder",
		Customization.SUBJECT_PLACEHOLDER);

		messageTextArea.getElement().setAttribute("placeHolder",
		Customization.MESSAGE_PLACEHOLDER);
		
		container.setStyleName("containerPanelAddRestaurant", true);
		
		container.addItem(fromLabel, senderTextBox);
		container.addItem(toLabel, addressListCombo);
		container.addItem(subjectLabel, subjectTextBox);
		container.addItem(messageLabel, messageTextArea);
		container.add(send);
		add(container);
		
	}
	
	private boolean checkEmailValues() {

		boolean isValid = false;

		String alert = "";
//
//		if (chosenEmailList.isEmpty()) {
//			chosenEmailPanel.setStyleName("redShadow");
//			emptyMailList = new Label(Customization.EMPTY_MAIL_LIST);
//			clearChosenEmailList();
//			chosenEmailPanel.add(emptyMailList);
//		} else {
			if (subjectTextBox.getText().isEmpty()) {
				alert += Customization.CONFIRMATION_NO_SUBJECT + "\n";
			}
			if (messageTextArea.getText().isEmpty()) {
				alert += Customization.CONFIRMATION_NO_MESSAGE + "\n";
			}
//		}

//		if (!chosenEmailList.isEmpty() && !subjectTextBox.getText().isEmpty()
//				&& !messageTextArea.getText().isEmpty()) {
//			isValid = true;
//		}

		if (!alert.equalsIgnoreCase("")) {
			if (Window.confirm(alert)) {
				isValid = true;
			}
		}

		return isValid;
	}
	
//	private void clearChosenEmailList() {
//		if (chosenEmailList.isEmpty())
//			chosenEmailPanel.clear();
//	}
	
	@Override
	public void clearData() {
//		addresseeListBox.clear();
//
//		addresseeListBox.addItem(Customization.SELECT_ELEMENT);
//		addresseeListBox.getElement().getFirstChildElement()
//				.setAttribute("disabled", "disabled");
//
//		for (User user : userController.getUserList()) {
//			addresseeListBox.addItem( (user.getName() != null && !user.getName().isEmpty())  ? user.getName() + "(" + user.getEmail() + ")": "No Name" + " (" + user.getEmail() + ")" , user.getEmail() );
//		}
//
//		addresseeListBox.setSelectedIndex(0);

//		chosenEmailList.clear();
//		clearChosenEmailList();
//		chosenEmailPanel.removeStyleName("redShadow");
//		chosenEmailPanel.removeStyleName("greenShadow");
		messageTextArea.setText("");
		subjectTextBox.setText("");
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
		setMailPanel();
	}
	
	private void setMailPanel(){
			
		for (String email : emailList) {
			addressListCombo.addListItem(addressListCombo.getNewCheckBoxItem(email), emailList.indexOf(email));
		}
		PagesController.hideWaitPanel();
		
	}

}
