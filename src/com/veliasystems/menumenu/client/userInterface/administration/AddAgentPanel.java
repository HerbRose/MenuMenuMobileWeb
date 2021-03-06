package com.veliasystems.menumenu.client.userInterface.administration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.controllers.UserController;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.User;

public class AddAgentPanel extends FlowPanel implements IManager {

	
	private TextBox inputEmailAgent;
	private PasswordTextBox passwordAgent;
	private PasswordTextBox passwordAgent2;
	private MultiWordSuggestOracle citySuggest;
	private SuggestBox citySuggestBox;
	private JQMButton addCityToCityTextBox;
	private CellList<String> citiesCellList;
	private SingleSelectionModel<String> selectionModelCities;
	private ArrayList<String> addedCities;
	private JQMButton saveAgentButton;
	private Label mailLabel;
	private Label passwordLabe;
	private Label repeatPasswordLabe;
	
	private CityController cityController = CityController.getInstance();
	private UserController userController = UserController.getInstance();
	
	public AddAgentPanel() {
		setStyleName("barPanel", true);
		show(false);
		
		mailLabel = new Label(Customization.INPUT_EMAIL);
		passwordLabe = new Label(Customization.INPUT_PASSWORD);
		repeatPasswordLabe = new Label(Customization.REPEAT_PASSWORD);
		
		inputEmailAgent = new TextBox();
		inputEmailAgent.addStyleName("properWidth");
		inputEmailAgent.getElement().setAttribute("placeHolder", Customization.EMAIL_PLACEHOLDER);
		passwordAgent = new PasswordTextBox();
		passwordAgent.addStyleName("properWidth");
		passwordAgent.getElement().setAttribute("placeHolder", Customization.PASSWORD_PLACEHOLDER);
		passwordAgent2 = new PasswordTextBox();
		passwordAgent2.addStyleName("properWidth");
		passwordAgent2.getElement().setAttribute("placeHolder", Customization.REPEAT_PASSWORD_PLACEHOLDER);
		citySuggest = new MultiWordSuggestOracle("-");
		citySuggestBox = new SuggestBox(citySuggest);
		citySuggestBox.getElement().setAttribute("placeHolder", Customization.CITY_PLACEHOLDER);
		citySuggestBox.addStyleName("properWidth");

		addCityToCityTextBox = new JQMButton("");
		addCityToCityTextBox.setIcon(DataIcon.PLUS);
		addCityToCityTextBox.setIconPos(IconPos.NOTEXT);
		addCityToCityTextBox.setStyleName("addButtonCity");
		addCityToCityTextBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (checkCity(citySuggestBox.getValue().trim())) {
					// addedCities = new ArrayList<String>();
					addedCities.add(citySuggestBox.getValue());
					citiesCellList.setRowData(addedCities);
					citiesCellList.setRowCount(addedCities.size());
					citiesCellList.redraw();
					citySuggestBox.setText("");
				}

			}
		});

		TextCell cityTextCell = new TextCell();
		addedCities = new ArrayList<String>();
		citiesCellList = new CellList<String>(cityTextCell);
		selectionModelCities = new SingleSelectionModel<String>();
		citiesCellList.setSelectionModel(selectionModelCities);
		selectionModelCities
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						String selected = selectionModelCities
								.getSelectedObject();
						if (selected != null) {
							addedCities.remove(selected);
							selectionModelCities.setSelected(
									selectionModelCities
											.getSelectedObject(), false);
							citiesCellList.setRowData(addedCities);
							citiesCellList.setRowCount(addedCities.size());
							citiesCellList.redraw();
						}

					}
				});
		citiesCellList.setRowData(addedCities);
		citiesCellList.setRowCount(addedCities.size());
		saveAgentButton = new JQMButton(Customization.SAVE);
		saveAgentButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (validData()) {
					User agent = new User(inputEmailAgent.getValue().trim());
					agent.setPassword(passwordAgent.getValue().trim());
					agent.setAgent(true);
					List<Long> cityId = new ArrayList<Long>();
					for (String cityName : addedCities) {
						for (City city : cityController.getCitiesList()) {
							if (city.getCity().equals(cityName)) {
								if (!cityId.contains(city.getId())) {
									cityId.add(city.getId());
								}
							}
						}
					}
					agent.setCitiesId(cityId);
					addUser(agent);
				}

			}


		});

		add(mailLabel);
		add(inputEmailAgent);
		add(passwordLabe);
		add(passwordAgent);
		add(repeatPasswordLabe);
		add(passwordAgent2);
		add(citySuggestBox);
		add(addCityToCityTextBox);
		add(citiesCellList);
		add(saveAgentButton);
	}
	
	private boolean checkCity(String CityName) {
		List<City> cities = cityController.getCitiesList();
		List<String> citiesName = new ArrayList<String>();
		for (City city : cities) {
			citiesName.add(city.getCity());
		}
		if (citiesName.contains(CityName)) {
			if (!addedCities.contains(CityName))
				return true;
		}
		return false;
	}

	private void addUser(User user) {
		userController.addUser(user);
	}
	
	private boolean validData() {
		boolean isCorrect = false;
		
		if (userController.isUserInStor(inputEmailAgent.getValue().trim())
				|| inputEmailAgent.getValue().trim().equals("")
				|| !Util.isValidEmail(inputEmailAgent.getValue())) {
			setValidDataStyle(false, inputEmailAgent);
		} else {
			setValidDataStyle(true, inputEmailAgent);
			isCorrect = true;
		}
		if (passwordAgent.getValue().trim().equals("")
				|| passwordAgent2.getValue().trim().equals("")
				|| !passwordAgent.getValue().equals(
						passwordAgent2.getValue())) {
			setValidDataStyle(false, passwordAgent);
			setValidDataStyle(false, passwordAgent2);
			isCorrect = false;
		} else {
			setValidDataStyle(true, passwordAgent);
			setValidDataStyle(true, passwordAgent2);
		}
		if (addedCities.size() < 1) {
			setValidDataStyle(false, citySuggestBox);
			isCorrect = false;
		} else {
			setValidDataStyle(true, citySuggestBox);
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
	
	@Override
	public void clearData() {
		
		inputEmailAgent.setValue("");
		passwordAgent.setValue("");
		passwordAgent2.setValue("");
		
		setValidDataStyle(null, inputEmailAgent);
		setValidDataStyle(null, passwordAgent);
		setValidDataStyle(null, passwordAgent2);
		setValidDataStyle(null, citySuggestBox);
		
		addedCities.clear();
		citiesCellList.setRowData(addedCities);
		citiesCellList.redraw();
		citySuggestBox.setText("");
		citySuggest.clear();
		for (City city : cityController.getCitiesList()) {
			citySuggest.add(city.getCity());
		}
	}

	@Override
	public String getName() {
		return Customization.ADD_AGENT;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
	}

}
