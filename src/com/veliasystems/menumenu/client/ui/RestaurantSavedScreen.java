package com.veliasystems.menumenu.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMContent;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.StoreService;
import com.veliasystems.menumenu.client.StoreServiceAsync;
import com.veliasystems.menumenu.client.entities.Restaurant;

class LoadingStartWidget extends JQMWidget {
	
	public LoadingStartWidget() {
		//initWidget( new HTML ("<h1 data-rel=\"dialog\" data-transition=\"pop\">Restaurant Saved</h1>") );
		
		VerticalPanel vp = new VerticalPanel();
		HTML savingHtml = new HTML("<center><h1>Saving ...</h1></center>");
		Image img = new Image("/img/loader.gif");
		vp.add(savingHtml);
		vp.add(img);
		
		initWidget(vp);
	}
	
}


class LoadingStopWidget extends JQMWidget {
	
	public LoadingStopWidget() {
		//initWidget( new HTML ("<h1 data-rel=\"dialog\" data-transition=\"pop\">Restaurant Saved</h1>") );
		
		VerticalPanel vp = new VerticalPanel();
		HTML savingHtml = new HTML("<center><h1>Saved</h1></center>");
		vp.add(savingHtml);
		
		initWidget(vp);
	}
	
}


class ErrorWidget extends JQMWidget {
	
	public ErrorWidget(String errorMessage) {
		//initWidget( new HTML ("<h1 data-rel=\"dialog\" data-transition=\"pop\">Restaurant Saved</h1>") );
		
		VerticalPanel vp = new VerticalPanel();
		HTML errorHtml = new HTML("<center><h1>" + errorMessage + "</h1></center>");
		vp.add(errorHtml);
		
		initWidget(vp);
	}
	
	
}

public class RestaurantSavedScreen extends JQMPage {
	
	JQMHeader header;
	JQMButton backButton;
	JQMButton saveButton;
	JQMContent content;
	
	
	private final StoreServiceAsync storeService = GWT.create(StoreService.class);
	
	private void init(){
		header = new JQMHeader(Customization.RESTAURANTSAVED);
		header.setFixed(true);
		
		backButton = new JQMButton(Customization.BACK);
		backButton.setBack(true);
		backButton.setInline();
		backButton.setIcon(DataIcon.LEFT);
		backButton.setIconPos(IconPos.LEFT);
	}
	
	public RestaurantSavedScreen(Restaurant r){
		init();
	}
	
	
	public RestaurantSavedScreen() {
		
		init();
		Restaurant restToSave = new Restaurant();
		
		final LoadingStartWidget loadingWidget  = new LoadingStartWidget();
		add(loadingWidget);
		
		final LoadingStopWidget loadedWidget  = new LoadingStopWidget();
		loadedWidget.setVisible(false);
		add(loadedWidget);
		
		/*
		storeService.saveRestaurant( restToSave, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				loadingWidget.setVisible(false);
				loadedWidget.setVisible(true);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				loadingWidget.setVisible(false);
				loadedWidget.setVisible(true);
				add( new ErrorWidget(caught.getMessage()) );
			}
		});
		*/
		
		header.setLeftButton(backButton);
		
		add(header);
	}
}

