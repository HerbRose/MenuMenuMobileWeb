package com.veliasystems.menumenu.client.ui.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

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
import com.veliasystems.menumenu.client.controllers.IObserver;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;

public class DefaultEmptyProfilePanel extends FlowPanel implements IManager, IObserver {

	private FormPanel formPanel;
	private FileUpload fileUpload = new FileUpload();
	private List<ImageBlob> emptyProfileList;
	private CellTable<ImageBlob> cellTable;
	private Column<ImageBlob, String> imageColumn;
	private Column<ImageBlob, Boolean> isMain;
	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
	private ImagesController imagesController = ImagesController.getInstance();

	public DefaultEmptyProfilePanel() {
		
		imagesController.addObserver(this);
		
		setStyleName("barPanel", true);
		show(false);
		
		cellTable = new CellTable<ImageBlob>();
		
		emptyProfileList = new ArrayList<ImageBlob>();
		
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
		setImagesFlowPanel();
		
	}
	
	private void refresh(){
		
		Map<String, ImageBlob> imageMap = imagesController.getEmptyProfileImages();
		
		if(imageMap == null)return;
		
		emptyProfileList.clear();
		for (String	key : imageMap.keySet()) {
			emptyProfileList.add(imagesController.getEmptyProfileImages().get(key));
		}
		
		cellTable.setRowData(emptyProfileList);
		cellTable.redraw();
		
	}
		 
	private void setImagesFlowPanel() {
		
			
		imageColumn = new Column<ImageBlob, String>(new ImageCell()) {
			
			@Override
			public String getValue(ImageBlob object) {
				return object.getImageUrl();
			}
		};
		
		final SingleSelectionModel<ImageBlob> selectionModel = new SingleSelectionModel<ImageBlob>();
		isMain = new Column<ImageBlob, Boolean>(new CheckboxCell()) {
			
			@Override
			public Boolean getValue(ImageBlob imageBlob) {	
				
				return (imageBlob.getRestaurantId().equals("0"))?true:false; //selectionModel.isSelected(object);
			}
		};
		
		isMain.setFieldUpdater(new FieldUpdater<ImageBlob, Boolean>() {

			@Override
			public void update(int index, ImageBlob object, Boolean value) {
				imagesController.setEmptyBoard(object);
			}
		});
		
		cellTable.setSelectionModel(selectionModel, DefaultSelectionEventManager.<ImageBlob> createCheckboxManager());	
		cellTable.addStyleName("emptyTable");	
		cellTable.addColumn(isMain, "Set As default");
		cellTable.setColumnWidth(isMain, "20px");
		cellTable.addColumn(imageColumn, "Preview");	
		cellTable.setRowData(emptyProfileList);	
		add(cellTable);
		
		for (ImageBlob imgBlob : emptyProfileList) {
			if(imgBlob.getRestaurantId().equals("0")) selectionModel.setSelected(imgBlob, true);
		}

	}
	

	@Override
	public void clearData() {
		if(formPanel != null){
			formPanel.reset(); 
		}
		refresh();
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
	
	@Override
	public void onChange() {
		refresh();
	}

}
