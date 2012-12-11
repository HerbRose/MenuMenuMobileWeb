package com.veliasystems.menumenu.client.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;
import com.veliasystems.menumenu.client.ui.MyImage;
import com.veliasystems.menumenu.client.userInterface.RestaurantImageView;


public class ImagesController {

	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
	
	private Map<String, ImageBlob> defoultEmptyProfilImageBlobMap;
	
	/**
	 * used in SwipeView to select main image
	 */
	private Map<RestaurantImageView, Map<ImageType, MyImage>> selectedImageControler = new HashMap<RestaurantImageView, Map<ImageType,MyImage>>();
	private static ImagesController instance;
	private static List<IObserver> observers = new ArrayList<IObserver>();
	
	private MyImage selectedImage = null;
	private MyImage selectedFlowPanel = null;
	
	private ImagesController() {
	}
	
	public static ImagesController getInstance(){
		if(instance == null){
			instance = new ImagesController();
		}
		
		return instance;
	}
	
	public void addObserver(IObserver iObserver){
		observers.add(iObserver);
	}
	
	private void notifyAllObservers(){
		for (IObserver observer : observers) {
			observer.onChange();
		}
	}
	
	public void setEmptyBoard(ImageBlob imageBlob){
		PagesController.showWaitPanel();
		blobService.setEmptyBoard(imageBlob, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				PagesController.hideWaitPanel();
				notifyAllObservers();
				Window.alert(Customization.CONNECTION_ERROR);
			}

			@Override
			public void onSuccess(String imageBlobId) {
				changeDefoultEmptyProfile(imageBlobId);
				PagesController.hideWaitPanel();
				notifyAllObservers();
			}
		});
	}

	public void setDefoultEmptyProfilImageBlobMap(Map<String, ImageBlob> defoultEmptyProfilImageBlobMap){
		this.defoultEmptyProfilImageBlobMap = defoultEmptyProfilImageBlobMap;
	}
	
	private void changeDefoultEmptyProfile(String imageBlobId){
		for (String key : defoultEmptyProfilImageBlobMap.keySet() ) {
			defoultEmptyProfilImageBlobMap.get(key).setRestaurantId("1");
		}

		defoultEmptyProfilImageBlobMap.get(imageBlobId).setRestaurantId("0");
	}
	
	
	private MyImage isInSelectedImageControler(MyImage image){
		if(!selectedImageControler.containsKey(image.getMyParent())){
			Map<ImageType,MyImage> map = new HashMap<ImageType, MyImage>();
			map.put(image.getImageType(), image);
			selectedImageControler.put(image.getMyParent(), map);
			return null;
		}else{
			Map<ImageType,MyImage> map = selectedImageControler.get(image.getMyParent());
			if(map.containsKey(image.getImageType())){
				return map.get(image.getImageType());
			}else{
				map.put(image.getImageType(), image);
				return null;
			}
		}
	}
	
	public void selectImage(MyImage image){	
		
		if(image!=null){
			selectedImage = isInSelectedImageControler(image);
			if(selectedImage != null) selectedImage.unselectImage();
			image.setSelected();
		}
	
		selectedImage = image;
	}
	
	public void clear(RestaurantImageView parent) {
		Map<ImageType,MyImage> map = selectedImageControler.get(parent);
		if(map==null) return;
		Set<ImageType> keys = map.keySet();
		for (ImageType imageType : keys) {
			map.put(imageType, null);
		}
	}

	public Map<String, ImageBlob> getEmptyProfileImages(){
		if(defoultEmptyProfilImageBlobMap == null){
			return null;
		}else {
			return defoultEmptyProfilImageBlobMap;
		}
	}
	
	public ImageBlob getDefoultEmptyProfile(){
		if(defoultEmptyProfilImageBlobMap == null){
			//Window.alert("defoultEmptyProfilImageBlobMap == null");
			return new ImageBlob();
		}
		for (String key : defoultEmptyProfilImageBlobMap.keySet()) {
			if(defoultEmptyProfilImageBlobMap.get(key).getRestaurantId().equals("0")) return defoultEmptyProfilImageBlobMap.get(key);
		}
		return new ImageBlob();
	}
}
