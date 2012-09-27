package com.veliasystems.menumenu.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.form.elements.JQMTextArea;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;



class ToRemove {
	public static String REST_ID = "666";  // TODO: to musi byc dynamiczne
	public static ImageType imgType = ImageType.MENU;
}


class UploadForm extends FormPanel {
	private VerticalPanel mainPanel = new VerticalPanel();
	private FileUpload fileUpload = new FileUpload();
	private JQMButton uploadButton = new JQMButton("Upload");
	JQMTextArea textArea = new JQMTextArea();
	
	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
	
	public UploadForm() {
				
		mainPanel.add(textArea);
		mainPanel.add(fileUpload);
		mainPanel.add(uploadButton);
		
		fileUpload.setName("image");
		
		uploadButton.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				blobService.getBlobStoreUrl( ToRemove.REST_ID, ToRemove.imgType, new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						setAction(result);
						textArea.setText(result);
						submit();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						textArea.setText(caught.getMessage());
					}
				});
				
			}
		});
		
		setWidget(mainPanel);
	}
	
}


public class UploadImageScreen extends JQMPage {

	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
	
	UploadForm form;
	
	FlowPanel imagesPanel = new FlowPanel();
	JQMButton showImagesButton = new JQMButton("Show uploads"); 
	
	
	public UploadImageScreen() {
		
		form = new UploadForm();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		
		
		
	//	startNewBlobstoreSession();
		
		form.addSubmitCompleteHandler( new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				form.reset();
			//	startNewBlobstoreSession();
			}
		});
		
		
		add(form);
		
		add(showImagesButton);
		
		showImagesButton.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				imagesPanel.clear();
				
				blobService.getBoardImages(ToRemove.REST_ID, new AsyncCallback<List<ImageBlob>>() {
					
					@Override
					public void onSuccess(List<ImageBlob> result) {
						if (result == null || result.isEmpty()) {
							imagesPanel.add(new HTML("No uploads found."));
						} else {
							for ( ImageBlob blob : result ) {
								//imagesPanel.add( new Image("/blobServe?blob-key=" + blob.getBlobKey() ) );
								imagesPanel.add( new Image( blob.getImageUrl() ) );
							}
						}
					};
					
					@Override
					public void onFailure(Throwable caught) {
						imagesPanel.add(new HTML(caught.getMessage()));
					}
				});
				
			}
		});
		
		
		add(imagesPanel);
		
	}
	
	
	
	
}
