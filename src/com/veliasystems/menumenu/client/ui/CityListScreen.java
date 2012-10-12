package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.CityController;
import com.veliasystems.menumenu.client.entities.City;




public class CityListScreen extends JQMPage{
	
	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMList list;
	JQMButton backButton;
	
	Label label = new Label();
	
//	private final StoreServiceAsync storeService = GWT.create(StoreService.class);	
	private List<City> cityList;
	
	public CityListScreen() {
		
		header = new JQMHeader(Customization.CITY);
		header.setFixed(true);
		header.setText(Customization.CITY);
		
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
		
		header.setBackButton(backButton);
		
		add(header);
		
		list = new JQMList();
	    list.setInset(false);
	    
//	    storeService.loadCities(new AsyncCallback<List<String>>() {
//			
//			@Override
//			public void onSuccess(List<String> result) {
//				// TODO Auto-generated method stub
//				cityList = new ArrayList<String>();
//				cityList = result;
//				addCities(cityList);
//				add(list);	
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//				showError();
//			}
//		});
	    cityList = CityController.getInstance().getCitiesList();
	    addCities(cityList);
	    add(list);
	    
	    footer = new JQMFooter();
	    footer.setFixed(true);
	    footer.setWidth("100%");
	    add(footer);
	   
	    addButton = new JQMButton(Customization.ADD_CITY, new AddCity(), Transition.SLIDE);
	    addButton.setWidth("100%");
	    addButton.setIcon(DataIcon.PLUS);
	    addButton.setIconPos(IconPos.TOP);
	    
	    footer.add(addButton);
	        
	}
	
	private void addCities(List<City> cities){
		
		for(City city: cities){
			this.list.addItem(city.getCity(), new CityInfoScreen(city.getCity()));
		}
	}
	
//	private void showError(){
//		label.setText(Customization.LOADERROR);
//		this.add(label);
//	}
	@Override
	protected void onPageShow() {
		// TODO Auto-generated method stub
		super.onPageShow();
		if(Cookies.getCookie(R.lastPage) != null){
			Cookies.removeCookie(R.lastPage);
		}
	}

}
