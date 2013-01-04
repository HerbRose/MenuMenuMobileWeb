package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.User;

public class EmailPanel extends FlowPanel implements IManager, IObserver {

	
	
	private FlowPanel chosenEmailPanel;
	private TextBox senderTextBox;
	private ArrayList<String> chosenEmailList;
	private TextBox subjectTextBox;
	private TextArea messageTextArea;
	private ListBox addresseeListBox;
	private Label emptyMailList;
	private CheckBox toMeMail;
	private Label isToMeMail;

	private UserController userController = UserController.getInstance();
 
	public EmailPanel() {
		userController.addObserver(this);
		setStyleName("barPanel", true);
		show(false);
		
		Label toLabel = new Label(Customization.ADDRESSEE + ":");
		Label fromLabel = new Label(Customization.SENDER + ":");
		Label messageLabel = new Label(Customization.MESSAGE_TEXT + ":");
		Label subjectLabel = new Label(Customization.SUBJECT);
		isToMeMail = new Label(Customization.IS_TO_ME_COPY);

		addresseeListBox = new ListBox();
		addresseeListBox.addStyleName("properWidth");
		addresseeListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				final Label label = new Label(addresseeListBox
						.getItemText(addresseeListBox.getSelectedIndex()));
				label.setTitle(addresseeListBox.getValue(addresseeListBox
						.getSelectedIndex()));

				label.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						chosenEmailPanel.remove(label);
						chosenEmailList.remove(label.getTitle());
						
						if(label.getTitle().equalsIgnoreCase(addresseeListBox.getValue(addresseeListBox
						.getSelectedIndex()))){
							addresseeListBox.setSelectedIndex(0);
						}
						
						if (chosenEmailList.isEmpty()) {
							chosenEmailPanel.removeStyleName("greenShadow");
						}
					}
				});
				clearChosenEmailList();
				chosenEmailPanel.add(label);
				chosenEmailPanel.setStyleName("greenShadow");
				chosenEmailList.add(addresseeListBox.getValue(addresseeListBox
						.getSelectedIndex()));
			}
		});

		chosenEmailPanel = new FlowPanel();
		chosenEmailList = new ArrayList<String>();
		senderTextBox = new TextBox();
		senderTextBox.addStyleName("properWidth");
		senderTextBox.setEnabled(false);
		senderTextBox.setText(userController.getLoggedUser().getEmail());
		subjectTextBox = new TextBox();
		subjectTextBox.addStyleName("properWidth");
		subjectTextBox.getElement().setAttribute("placeHolder",
				Customization.SUBJECT_PLACEHOLDER);
		messageTextArea = new TextArea();
		messageTextArea.addStyleName("properWidth");
		messageTextArea.getElement().setAttribute("placeHolder",
				Customization.MESSAGE_PLACEHOLDER);
		JQMButton sendButton = new JQMButton(Customization.SEND);
		sendButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (checkEmailValues()) {
					PagesController.showWaitPanel();
					userController.sendMail(chosenEmailList,
							senderTextBox.getText(), subjectTextBox.getValue(),
							messageTextArea.getValue());
				}
			}
		});
		
		
//		toMeMail = new CheckBox();
//		toMeMail.addClickHandler(new  ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				if(toMeMail.getValue() == true){
//					Window.alert(senderTextBox.getText());
//					chosenEmailList.add(senderTextBox.getText());
//				} else{
//					chosenEmailList.remove(senderTextBox.getText());
//				}
//				
//			}
//		});
		
//		toMeMail.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//			
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				Window.alert(event.getValue() + "");
//				
//			}
//		});
		
		add(toLabel);
		add(addresseeListBox);
		add(chosenEmailPanel);
		add(fromLabel);
		add(senderTextBox);
		add(subjectLabel);
		add(subjectTextBox);
		add(messageLabel);
		add(messageTextArea);
//		add(isToMeMail);
//		add(toMeMail);
		add(sendButton);
		
	}
	
	private boolean checkEmailValues() {

		boolean isValid = false;

		String alert = "";

		if (chosenEmailList.isEmpty()) {
			chosenEmailPanel.setStyleName("redShadow");
			emptyMailList = new Label(Customization.EMPTY_MAIL_LIST);
			clearChosenEmailList();
			chosenEmailPanel.add(emptyMailList);
		} else {
			if (subjectTextBox.getText().isEmpty()) {
				alert += Customization.CONFIRMATION_NO_SUBJECT + "\n";
			}
			if (messageTextArea.getText().isEmpty()) {
				alert += Customization.CONFIRMATION_NO_MESSAGE + "\n";
			}
		}

		if (!chosenEmailList.isEmpty() && !subjectTextBox.getText().isEmpty()
				&& !messageTextArea.getText().isEmpty()) {
			isValid = true;
		}

		if (!alert.equalsIgnoreCase("")) {
			if (Window.confirm(alert)) {
				isValid = true;
			}
		}

		return isValid;
	}
	
	private void clearChosenEmailList() {
		if (chosenEmailList.isEmpty())
			chosenEmailPanel.clear();
	}
	
	@Override
	public void clearData() {
		addresseeListBox.clear();

		addresseeListBox.addItem(Customization.SELECT_ELEMENT);
		addresseeListBox.getElement().getFirstChildElement()
				.setAttribute("disabled", "disabled");

		for (User user : userController.getUserList()) {
			addresseeListBox.addItem( (user.getName() != null && !user.getName().isEmpty())  ? user.getName() + "(" + user.getEmail() + ")": "No Name" + " (" + user.getEmail() + ")" , user.getEmail() );
		}

		addresseeListBox.setSelectedIndex(0);

		chosenEmailList.clear();
		clearChosenEmailList();
		chosenEmailPanel.removeStyleName("redShadow");
		chosenEmailPanel.removeStyleName("greenShadow");
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
		
	}

	@Override
	public void onChange() {
		clearData();	
	}
	
	

}