package com.veliasystems.menumenu.client.controllers;

import com.veliasystems.menumenu.client.ui.MyImage;

public class ImagesController {

	
	private MyImage selectedImage = null;
	
	public ImagesController() {
		
	}
	
	public void selectImage(MyImage image){
		if(selectedImage != null){
			selectedImage.unselect();
		}
		image.setSelected();
		selectedImage = image;
	}
	
}
