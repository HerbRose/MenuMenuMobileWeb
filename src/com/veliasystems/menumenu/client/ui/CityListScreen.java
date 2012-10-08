package com.veliasystems.menumenu.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;




public class CityListScreen extends JQMPage{
	
	JQMHeader header;
	JQMFooter footer;
	JQMButton addButton;
	JQMList list;
	JQMButton backButton;

	
	Label label = new Label();
	
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);	
	private List<String> cityList;
	
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
	    
	    storeService.loadCities(new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> result) {
				// TODO Auto-generated method stub
				cityList = new ArrayList<String>();
				cityList = result;
				addCities(cityList);
				add(list);	
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				showError();
			}
		});
	    
	   
	        
	}
	
	private void addCities(List<String> list){
		
		for(String item: list){
			this.list.addItem(item, new CityInfoScreen(item));
		}
	}
	
	private void showError(){
		label.setText(Customization.LOADERROR);
		this.add(label);
	}

}
