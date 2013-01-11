package com.veliasystems.menumenu.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.veliasystems.menumenu.client.Customization;
import com.veliasystems.menumenu.client.controllers.ImagesController;
import com.veliasystems.menumenu.client.controllers.PagesController;
import com.veliasystems.menumenu.client.controllers.RestaurantController;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.client.services.StoreServiceAsync;

public class MyImage extends FlowPanel {

	private ImagesController imagesController;

	Image image = new Image();

	private ImageBlob imgBlob;
	
	private Label txtLabel;
	private Label mainLAbel;
	private Label editLabel;
	
	private String url;

	private FlowPanel detailsPanel;
	private FlowPanel editPanel;
	
	private HTML detailsContent;

	private boolean mainImage = true;
	private ImageType imageType;

	private StoreServiceAsync storeService = GWT.create(StoreService.class);
	private RestaurantImageView parent;
	private RestaurantController restaurantController = RestaurantController
			.getInstance();
	private boolean isEdit = false;

	
	private FocusPanel deletePanel;
	
	/**
	 * zdjęcie z pustą tablicą
	 * 
	 * @param imagesController
	 *            - controler do zarzadzania zdjeciami w danej kliszy
	 * @param image
	 *            - wyswietlane zdjecie
	 * @param myParent
	 *            - widok restauracji w ktorej znajdule sie to zdjecie
	 * @param myImageType
	 *            - typ zdjecia
	 */
	public MyImage(ImagesController imagesController, Image image,
		 RestaurantImageView myParent, ImageType myImageType) {
		this.image = image;
		imageType = myImageType;
		image.setStyleName("image");
		parent = myParent;
		setStyleName("imagePanel");

		image.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(!parent.isEdit()){
					PagesController.showWaitPanel();
					restaurantController.setEmptyBoard(parent.getRestaurant(),imageType);
					getImagesController().selectImage(getMe());
				}

			}
		});
		this.imagesController = imagesController;
		add(image);
	}

	/**
	 * tworzy nowe zdjecie, po kliknieciu na zdjecie, zdjecie ustawiane jest
	 * jako glowne
	 * 
	 * @param imagesController
	 *            - controler do zarzadzania zdjeciami w danej kliszy
	 * @param imageBlob
	 *            - wyswietlane zdjecie
	 * @param myParent
	 *            - widok restauracji w ktorej znajdule sie to zdjecie
	 * @param myImageType
	 *            - typ zdjecia
	 */
	public MyImage(ImagesController imagesController, ImageBlob imageBlob,
			RestaurantImageView myParent, ImageType myImageType) {
		
		editPanel = new FlowPanel();
		editPanel.addStyleName("editImagePanel myImageEditPanel" + imageBlob.getRestaurantId());
		
		editLabel = new Label(Customization.EDIT.toUpperCase());
		editLabel.addStyleName("editLabelImage");
		
		deletePanel = new FocusPanel();
		deletePanel.addStyleName("deletePanelImages");
		
		
		url = imageBlob.getImageUrl();
		parent = myParent;
		imageType = myImageType;
		imgBlob = imageBlob;
		setMyImage(url);

		image.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if(!parent.isEdit()){
					PagesController.showWaitPanel();
					restaurantController.setMainImage(imgBlob);
//					JQMContext.changePage(new Test(imgBlob));
					getImagesController().selectImage(getMe());
				}
				else{
					parent.showDeletePanelForImages(imgBlob);
				}
			
			}
		});


		String dateTimeFormat = DateTimeFormat.getFormat(
						DateTimeFormat.PredefinedFormat.MONTH_NUM_DAY).format(
						imageBlob.getDateCreated());
		String day = DateTimeFormat.getFormat(
				DateTimeFormat.PredefinedFormat.DAY).format(
				imageBlob.getDateCreated());
		String month = DateTimeFormat.getFormat(
				DateTimeFormat.PredefinedFormat.MONTH_ABBR).format(
				imageBlob.getDateCreated());
		add(setDetailsPanel(day +" " +  month));
		//add(setDetailsPanel(day + " " + Util.getMonthShortName(monthCounter)));
		setStyleName("imagePanel");
		this.imagesController = imagesController;
		add(image);
		
		editPanel.add(editLabel);
		add(editPanel);
//		add(deletePanel);
//		parent.add(deletePanel);
	}

	public MyImage(int emptyImageNumber, ImageType imageType) {
		setStyleName("imagePanel");
	
		switch (imageType) {
		case PROFILE:
			url = "img/layout/klisza_piktogram_knajpa.png";
			break;
		case MENU:
			url = "img/layout/klisza_piktogram_tablica.png";
		}
		setMyImage(url);

		image.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			}
		});

		add(setDetailsPanel(emptyImageNumber + ""));
		add(image);
	}
	
//	private void hideEditPanel(){
//		deletePanel.getElement().getStyle().setHeight(0.00, Unit.PX);
//	}
//	private void showEditPanel(){
//		deletePanel.getElement().getStyle().setHeight(205.00, Unit.PX);
//		deletePanel.getElement().getStyle().setBottom(0, Unit.PX);	
//	}

	private FlowPanel setDetailsPanel(String details) {
		detailsPanel = new FlowPanel();
		detailsPanel.setStyleName("details");

		detailsContent = new HTML(details);
		detailsPanel.add(detailsContent);
		detailsContent.setStyleName("detailsContent");
		// flowPanelButtons.addStyleName("hiddenPanel");

		return detailsPanel;
	}

	private void setMyImage(String url) {
		image.setUrl(url);
		image.setStyleName("image");
	}

	private MyImage getMe() {
		return this;
	}

	public ImagesController getImagesController() {
		return imagesController;
	}

	public void setSelected() {
		image.setStyleName("imageSelected", true);
		setMain(true);
	}

	public void unselectImage() {
		image.setStyleName("imageSelected", false);
		setMain(false);
	}

	public String getUrl() {
		return url;
	}

	public RestaurantImageView getMyParent() {
		return parent;
	}

	public void setMain(Boolean isMain) {
		mainImage = isMain;
	}

	public boolean isMainImage() {
		return mainImage;
	}

	public ImageType getImageType() {
		return imageType;
	}
	public boolean isEdit() {
		return isEdit;
	}
	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
}
