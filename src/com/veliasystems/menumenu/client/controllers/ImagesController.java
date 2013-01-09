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
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.BlobService;
import com.veliasystems.menumenu.client.services.BlobServiceAsync;
import com.veliasystems.menumenu.client.userInterface.MyImage;
import com.veliasystems.menumenu.client.userInterface.RestaurantImageView;


public class ImagesController {
	
	public static String imageUrl = "";

	private final BlobServiceAsync blobService = GWT.create(BlobService.class);
	
	private Map<String, ImageBlob> defoultEmptyMenuImageBlobMap;
	private List<ImageBlob> defoultEmptyMenuImageBlobList = new ArrayList<ImageBlob>();
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
	/**
	 * 
	 * @return Single instance of {@link ImagesController} object
	 */
	public static ImagesController getInstance(){
		if(instance == null){
			instance = new ImagesController();
		}
		
		return instance;
	}
	/**
	 * add observer to list only if observer != null and list doesn't contains this observer.
	 * @param iObserver
	 */
	public void addObserver(IObserver iObserver){
		observers.add(iObserver);
	}
	
	private void notifyAllObservers(){
		for (IObserver observer : observers) {
			observer.onChange();
		}
	}
	/**
	 * 
	 * @param imageBlob - {@link ImageBlob} to set as default empty board
	 */
	public void setEmptyMenuBoard(ImageBlob imageBlob){
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
				changeDefoultEmptyMenu(imageBlobId);
				PagesController.hideWaitPanel();
				notifyAllObservers();
			}
		});
	}
	
	/**
	 * 
	 * @param defoultEmptyMenuImageBlobMap - Map of {@link ImageBlob} which may be default empty menu Image
	 */
	public void setDefoultEmptyMenuImageBlobMap(Map<String, ImageBlob> defoultEmptyMenuImageBlobMap){
		this.defoultEmptyMenuImageBlobMap = defoultEmptyMenuImageBlobMap;
		
		for (String imageBlobString : defoultEmptyMenuImageBlobMap.keySet()) {
			defoultEmptyMenuImageBlobList.add(defoultEmptyMenuImageBlobMap.get(imageBlobString));
		}
	}
	
	private void changeDefoultEmptyMenu(String imageBlobId){
		for (String key : defoultEmptyMenuImageBlobMap.keySet() ) {
			defoultEmptyMenuImageBlobMap.get(key).setRestaurantId("1");
		}

		defoultEmptyMenuImageBlobMap.get(imageBlobId).setRestaurantId("0");
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
	/**
	 * Set image as main on server to {@link Restaurant} 
	 * @param image - {@link MyImage} image to select as main
	 */
	public void selectImage(MyImage image){	
		
		if(image!=null){
			selectedImage = isInSelectedImageControler(image);
			if(selectedImage != null) selectedImage.unselectImage();
			image.setSelected();
		}
	
		selectedImage = image;
	}
	/**
	 * Used to secure before add duplicate Images after refresh page
	 * @param parent - {@link RestaurantImageView} is parent
	 */
	public void clear(RestaurantImageView parent) {
		Map<ImageType,MyImage> map = selectedImageControler.get(parent);
		if(map==null) return;
		Set<ImageType> keys = map.keySet();
		for (ImageType imageType : keys) {
			map.put(imageType, null);
		}
	}
	/**
	 * 
	 * @return Map of {@link ImageBlob} of empty menu images
	 */
	public Map<String, ImageBlob> getEmptyMenuImages(){
		if(defoultEmptyMenuImageBlobMap == null){
			return null;
		}else {
			return defoultEmptyMenuImageBlobMap;
		}
	}
	/**
	 * Get empty menu images from server and add to local list
	 */
	public void getEmptyMenuImagesFromServer(){
		blobService.getEmptyList(new AsyncCallback<List<ImageBlob>>() {
			
			@Override
			public void onSuccess(List<ImageBlob> result) {
				defoultEmptyMenuImageBlobList.clear();
				defoultEmptyMenuImageBlobList.addAll(result);
				notifyAllObservers();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				PagesController.hideWaitPanel();
				Window.alert(Customization.CONNECTION_ERROR);
				
			}
		});
	}
	/**
	 * 
	 * @return {@link ImageBlob} - default empty menu
	 */
	public ImageBlob getDefoultEmptyMenu(){
		if(defoultEmptyMenuImageBlobMap == null){
			return new ImageBlob();
		}
		for (String key : defoultEmptyMenuImageBlobMap.keySet()) {
			if(defoultEmptyMenuImageBlobMap.get(key).getRestaurantId().equals("0")) return defoultEmptyMenuImageBlobMap.get(key);
		}
		return new ImageBlob();
	}
	/**
	 * 
	 * @return List of {@link ImageBlob} - default empty menu images
	 */
	public List<ImageBlob> getDefoultEmptyMenuImageBlobList() {
		return defoultEmptyMenuImageBlobList;
	}
}
