package com.veliasystems.menumenu.client.controllers;

import com.veliasystems.menumenu.client.ui.MyImage;

public class ImagesController {

	
	
	private MyImage selectedImage = null;
	
	private MyImage selectedFlowPanel = null;
	
	public ImagesController() {
		
	}
	
	public void selectImage(MyImage image){
		if(selectedImage != null){
			selectedImage.unselectImage();
			
		}
		image.setSelected();
		selectedImage = image;
	}
	
	public void showFlowPanel(MyImage flowPanel){
		if(selectedFlowPanel != null){
			
			selectedFlowPanel.hideFlowPanelButtons();
			
			if(selectedFlowPanel.equals(flowPanel)){
				selectedFlowPanel =  null;
				return;
			}
			
		}

		flowPanel.showFLowPanelButtons();
		selectedFlowPanel = flowPanel;
	}
	
}
