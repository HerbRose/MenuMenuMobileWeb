package com.veliasystems.menumenu.client.ui.administration;

import java.util.Map;

import javax.servlet.http.HttpServlet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class CityManagerPanel extends FlowPanel implements IManager {

	
	private TextBox cityIdFrom;
	private TextBox cityIdTo;
	private JQMButton send;
	private CityController cityController = CityController.getInstance();

	public CityManagerPanel() {
		
		setStyleName("barPanel", true);
		show(false);
		
		cityIdFrom = new TextBox();
		cityIdFrom.addStyleName("properWidth");
		setPlaceHolder(cityIdFrom, "doNotUseThis");
		
		cityIdTo = new TextBox();
		cityIdTo.addStyleName("properWidth");
		setPlaceHolder(cityIdTo, "doNotUseThis");
		
		send = new JQMButton(Customization.SAVE);
		send.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String pass = Window.prompt("Password", "");
				if(pass.equals("wdo67pla")){
					cityController.copyAllDataFromCity(cityIdFrom.getText(), cityIdTo.getText());
				}
				
			}

		});
		
		add(cityIdFrom);
		
		add(cityIdTo);
		
		add(send);

	}
	
	private void setPlaceHolder(Widget element, String placeHolder){
		element.getElement().setAttribute("placeHolder", placeHolder);
	}
	
	private boolean validData() {
		boolean isCorrect = false;
		

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
	
	@Override
	public void clearData() {
		cityIdFrom.setValue("");
		setValidDataStyle(null, cityIdFrom);
		cityIdTo.setValue("");
		setValidDataStyle(null, cityIdTo);
		
	}

	@Override
	public String getName() {
		return "City manager";
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
	}

}
