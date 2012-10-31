package com.veliasystems.menumenu.client.ui.administration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;

public class DefaultEmptyProfilePanel extends FlowPanel implements IManager {

	private FormPanel formPanel;
	private FileUpload fileUpload = new FileUpload();
	
	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
	
	
	public DefaultEmptyProfilePanel() {
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
				// clickOnInputFile(formPanel.getUploadButton().getElement());
				blobService.getBlobStoreUrl("0", ImageType.EMPTY_PROFILE,
						new AsyncCallback<String>() {

							@Override
							public void onSuccess(String result) {
								formPanel.setAction(result);
								formPanel.submit();
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

	}
	 
	@Override
	public void clearData() {
		if(formPanel != null){
			formPanel.clear(); 
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
