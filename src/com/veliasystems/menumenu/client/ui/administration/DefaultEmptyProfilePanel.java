package com.veliasystems.menumenu.client.ui.administration;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;

public class DefaultEmptyProfilePanel extends FlowPanel implements IManager {

	private FormPanel formPanel;
	private FileUpload fileUpload = new FileUpload();
	private List<ImageBlob> emptyList;
	private CellTable<ImageBlob> cellTable;
	private Column<ImageBlob, String> imageColumn;
	private Column<ImageBlob, Boolean> isMain;
	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
	private RestaurantController restaurantController = RestaurantController.getInstance();
	private JQMButton refreshButton;
	
	public DefaultEmptyProfilePanel() {
		
		cellTable= new CellTable<ImageBlob>();
		
		emptyList = new ArrayList<ImageBlob>();
		setStyleName("barPanel", true);
		
		formPanel = new FormPanel();
		formPanel.setVisible(true);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.add(fileUpload);
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				Document.get().getElementById("load").setClassName("loaded");
				formPanel.reset();
			}
		});
		fileUpload.setName("defaultEmptyImage");
		fileUpload.addStyleName("properWidth");
		
		fileUpload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				Document.get().getElementById("load").setClassName("loading");
				blobService.getBlobStoreUrl("1", ImageType.EMPTY_PROFILE,
						new AsyncCallback<String>() {

							@Override
							public void onSuccess(String result) {
								formPanel.setAction(result);
								formPanel.submit();
								refresh();
							}

							@Override
							public void onFailure(Throwable caught) {
								Document.get().getElementById("load")
										.setClassName("loaded");
								Window.alert("Problem with upload. Try again");
								
							}
						});
			}
		});
		
		add(formPanel);
		setList();
		
		refreshButton = new JQMButton("refresh");
		add(refreshButton);
		refreshButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				refresh();
				
			}
		});
		
		
	}
	
	private void refresh(){
		blobService.getEmptyList(new AsyncCallback<List<ImageBlob>>() {

			@Override
			public void onFailure(Throwable caught) {
					
			}

			@Override
			public void onSuccess(List<ImageBlob> result) {
				emptyList = result;
				cellTable.setRowData(emptyList);
				cellTable.redraw();
			}
			
		});
	}
		 
	private void setImagesFlowPanel(int size) {
		
			
		imageColumn = new Column<ImageBlob, String>(new ImageCell()) {
			
			@Override
			public String getValue(ImageBlob object) {
				// TODO Auto-generated method stub
				return object.getImageUrl();
			}
		};
		final SingleSelectionModel<ImageBlob> selectionModel = new SingleSelectionModel<ImageBlob>();
		isMain = new Column<ImageBlob, Boolean>(new CheckboxCell()) {
			
			@Override
			public Boolean getValue(ImageBlob object) {	
				
				return selectionModel.isSelected(object);
			}
		};
		
		isMain.setFieldUpdater(new FieldUpdater<ImageBlob, Boolean>() {

			@Override
			public void update(int index, ImageBlob object, Boolean value) {
				restaurantController.setEmptyBoard(object);
			}
		});
		cellTable.setSelectionModel(selectionModel, DefaultSelectionEventManager.<ImageBlob> createCheckboxManager());	
		cellTable.addStyleName("emptyTable");	
		cellTable.addColumn(isMain, "Set As default");
		cellTable.setColumnWidth(isMain, "20px");
		cellTable.addColumn(imageColumn, "Preview");	
		cellTable.setRowData(emptyList);	
		add(cellTable);
		
		for (ImageBlob imgBlob : emptyList) {
			if(imgBlob.getRestaurantId().equals("0")) selectionModel.setSelected(imgBlob, true);
		}

	}
	
	private void setList(){
		blobService.getEmptyList(new AsyncCallback<List<ImageBlob>>() {
			@Override
			public void onFailure(Throwable caught) {	
			}

			@Override
			public void onSuccess(List<ImageBlob> result) {
				if(result == null) return;
				emptyList = result;
				setImagesFlowPanel(emptyList.size());
				
			}
		});
		
	}
	
	@Override
	public void clearData() {
		if(formPanel != null){
			formPanel.reset(); 
		}
	}

	@Override
	public String getName() {
		return Customization.SET_DEFAULT_EMPTY_PROFIL;
	}

	@Override
	public void show(boolean isVisable) {
		setStyleName("show", isVisable);
		setStyleName("hide", !isVisable);
	}

}
