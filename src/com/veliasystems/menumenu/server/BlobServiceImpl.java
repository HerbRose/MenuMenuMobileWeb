package com.veliasystems.menumenu.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.FinalizationException;
import com.google.appengine.api.images.CompositeTransform;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesService.OutputEncoding;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.InputSettings;
import com.google.appengine.api.images.InputSettings.OrientationCorrection;
import com.google.appengine.api.images.OutputSettings;
import com.google.appengine.api.images.Transform;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Query;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.controllers.responseWrappers.BackupWrapper;
import com.veliasystems.menumenu.client.controllers.responseWrappers.BackupWrapperSimply;
import com.veliasystems.menumenu.client.entities.BackUpBlobKey;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.services.BlobData;
import com.veliasystems.menumenu.client.services.BlobDataFilter;
import com.veliasystems.menumenu.client.services.BlobService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BlobServiceImpl extends RemoteServiceServlet implements
		BlobService {

	private DAO dao = new DAO();

	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	
	private static final Logger log = Logger.getLogger(BlobServiceImpl.class
			.getName());

	
	@Override
	public String getBlobStoreUrl(String restId, ImageType imageType) {
		String url = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/blobUpload?restId=" + restId + "&imageType="+ imageType.name());
		return url;
	}

	
	public List<ImageBlob> getAllImages(Restaurant r) {
		return getAllImages("" + r.getId());
	}

	
	/**
	 * @param restaurantId = id of {@link Restaurant}
	 * @return List of images connected to given {@link Restaurant}
	 */
	public List<ImageBlob> getAllImages(String restaurantId) {
		List<ImageBlob> images = new ArrayList<ImageBlob>();
		Query<ImageBlob> imgQuery = dao.ofy().query(ImageBlob.class);
		if (imgQuery == null)
			return images;

		return imgQuery.filter("restId =", restaurantId).order("-dateCreated")
				.list();
	}

	@Override
	public ImageBlob getLastUploadedImage(String restaurantId) {

		List<ImageBlob> all = getAllImages(restaurantId);
		if (!all.isEmpty())
			return all.get(0);
		else
			return null;
	}

	@Override
	public ImageBlob getLastUploadedImage(Long restaurantId, ImageType imageType) {

		Query<ImageBlob> query = dao.ofy().query(ImageBlob.class);
		
		if(query == null) return null;
		
		List<ImageBlob> all = query.filter("restId", restaurantId + "")
				.filter("imageType", imageType).list();

		if (!all.isEmpty()) {
			Collections.sort(all, new MyComparator());
			return all.get(0);
		} else
			return null;
	}

	@Override
	public List<ImageBlob> getEmptyList() {
		List<ImageBlob> listBoards = new ArrayList<ImageBlob>();
		Query<ImageBlob> queryBoards = dao.ofy().query(ImageBlob.class);
		if (queryBoards == null)
			return listBoards;
		List<String> ids = new ArrayList<String>();
		ids.add("0");
		ids.add("1");
		listBoards = queryBoards.filter("restId IN", ids).list();
		
		return listBoards;
	}

	@Override
	public String setEmptyBoard(ImageBlob imageBlob) {

		Query<ImageBlob> imgQuery = dao.ofy().query(ImageBlob.class);
		if (imgQuery == null)
			return null;
		List<ImageBlob> imgList = imgQuery.filter("restId =", "0").list();
		for (ImageBlob imageBlob2 : imgList) {
			imageBlob2.setRestaurantId("1");
			dao.ofy().put(imageBlob2);
		}
		imageBlob.setRestaurantId("0");
		dao.ofy().put(imageBlob);
		return imageBlob.getId();
	}
	/**
	 * 
	 * @return List of {@link ImageBlob}, which id is set to 0, default empty board
	 */
	public List<ImageBlob> getDefaultEmptyMenu() {
		Query<ImageBlob> imgQuery = dao.ofy().query(ImageBlob.class);
		if (imgQuery == null)
			return new ArrayList<ImageBlob>();
		return imgQuery.filter("restId =", "0").list();
	}
	/**
	 * 
	 * @param restaurantId - id of {@link Restaurant}
	 * @param imageType - type of Image specified in {@link ImageType}
	 * @return List of images of the same type in given {@link Restaurant}
	 */
	private List<ImageBlob> getImages(String restaurantId, ImageType imageType) {
		List<ImageBlob> allImages = getAllImages(restaurantId);
		List<ImageBlob> ret = new ArrayList<ImageBlob>();

		for (ImageBlob blob : allImages) {
			if (blob.getImageType() == imageType) {
				ret.add(blob);
			}
		}

		return ret;
	}

	@Override
	public List<ImageBlob> getBoardImages(Restaurant r) {
		return getImages("" + r.getId(), ImageType.MENU);
	}

	@Override
	public List<ImageBlob> getBoardImages(String restaurantId) {
		return getImages(restaurantId, ImageType.MENU);
	}

	@Override
	public List<ImageBlob> getHeaderImages(Restaurant r) {
		return getImages("" + r.getId(), ImageType.LOGO);
	}

	@Override
	public List<ImageBlob> getHeaderImages(String restaurantId) {
		return getImages(restaurantId, ImageType.LOGO);
	}

	@Override
	public List<ImageBlob> getProfileImages(Restaurant r) {
		return getImages("" + r.getId(), ImageType.PROFILE);
	}

	@Override
	public List<ImageBlob> getProfileImages(String restaurantId) {
		return getImages(restaurantId, ImageType.PROFILE);
	}

	/**
	 * get blob info array
	 * 
	 * @param filter
	 * @return array of {@link BlobData} 
	 */
	public BlobData[] getBlobs(BlobDataFilter filter) {

		BlobInfoJdo db = new BlobInfoJdo();
		BlobData[] r = db.getBlobs(filter);

		return r;
	}


	
	/**
	 * 
	 * @param imageBlob
	 * @param isMinImage if true original blob will not be deleted, otherwise will
	 * @return
	 */
	@Override
	public boolean deleteBlob(ImageBlob imageBlob, boolean isMinImage) {
		if (imageBlob == null) {
			return false;
		}
		
		Long restaurantId = Long.valueOf(imageBlob.getRestaurantId());
		Restaurant restaurant;
		City city;
		
		switch (imageBlob.getImageType()) {
			case MENU:
				restaurant = dao.ofy().find(Restaurant.class, restaurantId);
				if(restaurant!= null && restaurant.getMainMenuImageString() != null && restaurant.getMainMenuImageString().equals("/blobServe?blob-key=" + imageBlob.getBlobKey())){
					restaurant.setMainMenuImageString("");
					restaurant.setMainMenuScaleSizeImageString("");
					restaurant.setMainMenuScreenSizeImageString("");
					dao.ofy().put(restaurant);
				}
				break;
			case PROFILE: 
				restaurant = dao.ofy().find(Restaurant.class, restaurantId);
				if(restaurant!= null && restaurant.getMainProfileImageString() != null && restaurant.getMainProfileImageString().equals("/blobServe?blob-key=" + imageBlob.getBlobKey())){
					restaurant.setMainProfileImageString("");
					dao.ofy().put(restaurant);
				}
				break;
			case LOGO:
				restaurant = dao.ofy().find(Restaurant.class, restaurantId);
				if(restaurant!= null && restaurant.getMainLogoImageString() != null && restaurant.getMainLogoImageString().equals("/blobServe?blob-key=" + imageBlob.getBlobKey())){
					restaurant.setMainLogoImageString("");
					dao.ofy().put(restaurant);
				}
				break;
			case CITY:
				city = dao.ofy().find(City.class, restaurantId);
				if( city!= null && city.getDistrictImageURL() != null && city.getDistrictImageURL().equals("/blobServe?blob-key=" + imageBlob.getBlobKey())){
					city.setDistrictImageURL("");
					dao.ofy().put(city);
				}
				break;
			default:
				break;
		}
		
		if(!isMinImage){
			if(imageBlob.getBlobKeyOriginalSize() != null && !imageBlob.getBlobKeyOriginalSize().isEmpty()){
				BlobstoreServiceFactory.getBlobstoreService().delete(
						new BlobKey(imageBlob.getBlobKeyOriginalSize()));
			}
		}
	
		BlobstoreServiceFactory.getBlobstoreService().delete(
				new BlobKey(imageBlob.getBlobKey()));
		
		
		if(imageBlob.getImageType() == ImageType.MENU){
			if(imageBlob.getBlobKeyScaleSize() != null){
				BlobstoreServiceFactory.getBlobstoreService().delete(
						new BlobKey(imageBlob.getBlobKeyScaleSize()));
			}
			if(imageBlob.getBlobKeyScreenSize() != null){
				BlobstoreServiceFactory.getBlobstoreService().delete(
						new BlobKey(imageBlob.getBlobKeyScreenSize()));
			}
			
		}
		dao.ofy().delete(imageBlob);
				
//		String blobKeyToDelete = imageBlob.getBlobKey();
//		dao.ofy().delete(imageBlob);
//		BlobstoreServiceFactory.getBlobstoreService().delete(
//				new BlobKey(blobKeyToDelete));
		return true;
	}

	// public boolean deleteBlob(BlobDataFilter filter) {
	//
	// String blobKeyString = filter.getBlobKey();
	//
	// return deleteBlob( blobKeyString );
	//
	// }

	/**
	 * <h1>Short description</h1>
	 * <ul>
	 * <li>All values must be in range of 0.0 to 1.0, otherwise all incorrect values will be rounded to maximum</li>
	 * <li>For example:if leftX < 0, then leftX is automatically rounded to 0.0 </li>
	 * </ul>
	 * @param imageBlob - image to crop
	 * @param leftX - percentage of left line, counting from left side
	 * @param topY - percentage of top line, counting from top
	 * @param rightX - percentage of right line, counting from left side
	 * @param bottomY - percentage of bottom line, counting from top side
	 * @param newName - name of new image after crop, old image will be deleted from blobstore and datastore
	 * @return ImageBlob which contains all informations about new image, like blobkey etc.
	 * 
	 */
	private ImageBlob cropImage(ImageBlob imageBlob, double leftX, double topY, double rightX, double bottomY, String newName) {
		
		Query<ImageBlob> query = dao.ofy().query(ImageBlob.class);
		ImageBlob imageBlob2;
		if (query == null) {
			imageBlob2 = new ImageBlob();
		}else{
			imageBlob2 = query.filter("blobKey", imageBlob.getBlobKey()).get();
		}
		
		if(imageBlob2 == null){ //second try
			Query<ImageBlob> query2 = dao.ofy().query(ImageBlob.class);
			if (query2 == null) {
				imageBlob2 = new ImageBlob();
			}else{
				imageBlob2 = query2.filter("blobKey", imageBlob.getBlobKey()).get();
			}
		}
		
		if(imageBlob2 == null){
			throw new NullPointerException() ;
		}
		
		int profileWidth = R.PROFILE_WIDTH;
		int profileHeight = R.PROFILE_HEIGHT;
		int logoWidth = 220;
		int menuWidth = 220;
		int menuScreenWidth = 440;
		int menuScaleWidth = 880;
		
//		//----------------------------------------
//		//--------- FOR TESTING ONLY -------------
//		//----------------------------------------
//		City cityToCheckDimensions = null;
////		log.info("imageBlob2.getRestaurantId() " + imageBlob2.getRestaurantId());
//		if(imageBlob2.getRestaurantId() != null || !imageBlob2.getRestaurantId().isEmpty() ){
//			restaurant = dao.ofy().find(Restaurant.class, Long.parseLong(imageBlob2.getRestaurantId()));
//			
//			if(restaurant!=null){
////				log.info("restaurant != null: ");
//				cityToCheckDimensions = dao.ofy().find(City.class, restaurant.getCityId());
//			}
//		}
//		String cityName = null;
//		if(cityToCheckDimensions != null){
//			cityName = cityToCheckDimensions.getCity();
////			log.info("cityToCheckDimensions != null: ");
//		}
//		if(cityName != null){
////			log.info("cityName != null: " + cityName);
//			if(cityName.endsWith("IOS")){
////				log.info("cityName.endsWith(\"IOS\") ");
//				newName += "_IOS_X2";
//				profileWidth *= 2;
//				profileHeight *= 2;
//				logoWidth *= 2;
//				menuWidth *= 2;
//			}else if(cityName.endsWith("RETINA")){
////				log.info("cityName.endsWith(\"RETINA\") ");
//				newName += "_RETINA_X4";
//				profileWidth *= 4;
//				profileHeight *= 4;
//				logoWidth *= 4;
//				menuWidth *= 4;
//			}
//		}
//		//----------------------------------------
//		//---------- END FORTESTING ONLY ---------
//		//----------------------------------------
		BlobKey blobKey ;
		if(imageBlob2.getBlobKeyOriginalSize() != null){
			blobKey = new BlobKey(imageBlob2.getBlobKeyOriginalSize());
			log.info("used getBlobKeyOriginalSize()");
		}else{ 
			blobKey = new BlobKey(imageBlob.getBlobKey());
			log.info("used getBlobKey()");
		}
		 
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		
		Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
		
		if(oldImage == null){
			log.severe("Picture not found in database:\n blobKey: " + blobKey);
			return null;
		}
		/**
		 * check if the range is ok
		 */
		
		
		if(leftX <= 0) leftX = 0;
		if(rightX >= 1) rightX = 1;
		if(bottomY >= 1) bottomY = 1;
		if(topY <= 0) topY = 0;
		
		if(leftX >=1) leftX = 1-0.1;
		if(rightX <=0) rightX = 0 + 0.1;
		if(bottomY <=0) bottomY = 0 + 0.1;
		if(topY >=1) topY = 1 -0.1;
		
		CompositeTransform compositeTransformForScaleImage = ImagesServiceFactory.makeCompositeTransform();
		CompositeTransform compositeTransformForScreenSize = ImagesServiceFactory.makeCompositeTransform();
		CompositeTransform compositeTransformForScaleSizeMenuImage = ImagesServiceFactory.makeCompositeTransform();
		
//		Transform cropTransform = ImagesServiceFactory.makeCrop(leftX, topY, rightX, bottomY);
		
		compositeTransformForScaleImage.concatenate(ImagesServiceFactory.makeCrop(leftX, topY, rightX, bottomY));
		compositeTransformForScreenSize.concatenate(ImagesServiceFactory.makeCrop(leftX, topY, rightX, bottomY));
		compositeTransformForScaleSizeMenuImage.concatenate(ImagesServiceFactory.makeCrop(leftX, topY, rightX, bottomY));
		
		InputSettings inputSettings = new InputSettings();
		inputSettings.setOrientationCorrection(OrientationCorrection.CORRECT_ORIENTATION);
		
		
		OutputSettings outputSettings = new OutputSettings(OutputEncoding.JPEG);
		outputSettings.setQuality(100);
		
		
//		Image newImage = imagesService.applyTransform(cropTransform, oldImage, OutputEncoding.JPEG);
//		Image newImage = null;
//		try{
//			newImage = imagesService.applyTransform(cropTransform, oldImage, inputSettings, outputSettings);
//		}catch(ImagesServiceFailureException e){
//			log.log(Level.SEVERE, "\noldImage: "+oldImage.getBlobKey()+"\n", e);
//			return null;
//		}
		Transform scaleTransform = null;
		
		//for only menu images
		Transform screenSizeTransform = null;
		Transform scaleSizeTransform = null;
		
		switch (imageBlob.getImageType()) {
		case PROFILE:
			scaleTransform = ImagesServiceFactory.makeResize(profileWidth, profileHeight);
			break;
		case LOGO:
			scaleTransform = ImagesServiceFactory.makeResize(logoWidth,4000);//newImage.getHeight());
			break;
		case MENU:
			scaleTransform = ImagesServiceFactory.makeResize(menuWidth,4000);//newImage.getHeight());
			break;
		case CITY:
			scaleTransform = ImagesServiceFactory.makeResize(246,290);//newImage.getHeight());
			break;
		default:
			log.severe("Unknow image type: " + imageBlob.getImageType());
			return null;
		}
		
		Image screenSizeMenuImage = null;
		Image scaleSizeMenuImage = null;
		BlobKey screenSizeBlobKey = null;
		BlobKey scaleSizeBlobKey = null;
		if(imageBlob.getImageType() == ImageType.MENU){

			screenSizeTransform = ImagesServiceFactory.makeResize(menuScreenWidth, 4000);
			scaleSizeTransform = ImagesServiceFactory.makeResize(menuScaleWidth, 4000);
//			Image tmp1 = newImage;
//			Image tmp2 = newImage;
			Image tmp1 = ImagesServiceFactory.makeImageFromBlob(blobKey);
			Image tmp2 = ImagesServiceFactory.makeImageFromBlob(blobKey);
		
			
			try {
				
				compositeTransformForScreenSize.concatenate(ImagesServiceFactory.makeResize(menuScreenWidth, 4000));
				
				
			//	screenSizeMenuImage = imagesService.applyTransform(screenSizeTransform, tmp1, inputSettings, outputSettings);
				screenSizeMenuImage = imagesService.applyTransform(compositeTransformForScreenSize, tmp1, inputSettings, outputSettings);
				screenSizeBlobKey = writeToBlobstore("image/jpeg", "screenSizeMenuImage.jpg", screenSizeMenuImage.getImageData());
				
				OutputSettings outScale = new OutputSettings(OutputEncoding.JPEG);
				outScale.setQuality(100);
				
				
				compositeTransformForScaleSizeMenuImage.concatenate(ImagesServiceFactory.makeResize(menuScaleWidth, 4000));
//				scaleSizeMenuImage = imagesService.applyTransform(scaleSizeTransform, tmp2, inputSettings, outScale);
				scaleSizeMenuImage = imagesService.applyTransform(compositeTransformForScaleSizeMenuImage, tmp2, inputSettings, outScale);
				scaleSizeBlobKey = writeToBlobstore("image/jpeg", "scaleSizeBlobKey.jpg", scaleSizeMenuImage.getImageData());
					
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println(scaleImage.getHeight() + "  " +scaleImage.getWidth());
		compositeTransformForScaleImage.concatenate(scaleTransform);
		Image newImage = oldImage;
//		Image scaleImage = imagesService.applyTransform(scaleTransform, newImage, inputSettings, outputSettings);
		Image scaleImage = imagesService.applyTransform(compositeTransformForScaleImage, newImage, inputSettings, outputSettings);
		BlobKey newBlobKey = null;
		ImageBlob newImageBlob = null;
		try {
			newBlobKey = writeToBlobstore("image/jpeg", imageBlob.getImageType().name()+newName+".jpg",
					scaleImage.getImageData());
			newImageBlob = new ImageBlob(imageBlob.getRestaurantId(),
					newBlobKey.getKeyString(), imageBlob.getDateCreated(),
					imageBlob.getImageType());
			newImageBlob.setWidth(scaleImage.getWidth());
			newImageBlob.setHeight(scaleImage.getHeight());
			newImageBlob.setBlobKeyOriginalSize(imageBlob2.getBlobKeyOriginalSize());
			
			if(imageBlob.getImageType() == ImageType.MENU){
				newImageBlob.setBlobKeyScreenSize(screenSizeBlobKey.getKeyString());
				newImageBlob.setBlobKeyScaleSize(scaleSizeBlobKey.getKeyString());	
			}
			dao.ofy().put(newImageBlob);
			
			if(newImageBlob.getImageType() == ImageType.CITY){
				String cityIdString = newImageBlob.getRestaurantId(); //in this case restaurantId is a cityId
				
				try {
					long cityId = Long.parseLong(cityIdString);
					City city = dao.ofy().find(City.class, cityId );
					if(city != null){
						city.setDistrictImageURL(newImageBlob.getImageUrl());
						dao.ofy().put(city);
					}
				} catch (NumberFormatException e) {
					log.severe("NumberFormatException when try find city to add image for this city");
				}
				
			}
			
			//remove old image and image's data
//			BlobstoreServiceFactory.getBlobstoreService().delete(new BlobKey(imageBlob.getBlobKey()));
			
			//this method will remove imageMin.jpg which was used to display to user in browser and will keep the original image in blobstore
			removeImageBlobByBlobKey(imageBlob.getBlobKey(), true);
			dao.ofy().delete(imageBlob);
			//END - remove old image and image's data
		} catch (IOException e) {
			log.severe("imageBlobKey: " + imageBlob.getBlobKey() + " \n"
					+ e.getStackTrace());
			// e.printStackTrace();
		}

//		try {
//			sendToBlobstore(imageBlob, "save", scaleImage.getImageData());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		return newImageBlob;
	}
	/**
	 * 
	 * @param blobKey
	 * @param isMinImage if true original blob will not be deleted
	 */
	@Override
	public void removeImageBlobByBlobKey(String blobKey, boolean isMinImage){
		Query<ImageBlob> blobQuery = dao.ofy().query(ImageBlob.class);
		if(blobQuery == null) return;
		ImageBlob imgBlob = blobQuery.filter("blobKey =", blobKey).get();
		if(imgBlob == null) return;
		deleteBlob(imgBlob, isMinImage);
		
	}

	@Override
	public Map<String, ImageBlob> cropImage(ImageBlob imageBlob, double leftX, double topY, double rightX, double bottomY){
		
		Map<String, ImageBlob> mapToReturn = new HashMap<String, ImageBlob>();
		
		ImageBlob newImageBlob = cropImage(imageBlob, leftX, topY, rightX, bottomY, "imageCrop");
		
		if(newImageBlob == null){
			throw new NullPointerException();
		}
		
		mapToReturn.put("old", imageBlob);
		mapToReturn.put("new", newImageBlob);
		
		
		
		return mapToReturn;
	}
	
	public void cropImageFromApp(ImageBlob imageBlob, double leftX, double topY, double rightX, double bottomY, String imageName){
		
		Image image = ImagesServiceFactory.makeImageFromBlob(new BlobKey(imageBlob.getBlobKey()));
		BlobKey blobKeyOriginalSize = null;
		try {
			blobKeyOriginalSize = writeToBlobstore("image/jpeg", imageName+"OrginalSize.jpg", image.getImageData());
		} catch (IOException e) {
			//log.severe("Error when try save image from app \n" );
			e.printStackTrace();
		}
		if(imageName != null){
			imageName.replaceAll(" ", "");
		}
		ImageBlob newImageBlob = cropImage(imageBlob, leftX, topY, rightX, bottomY, imageName+"ImageCrop");
		
		if(newImageBlob == null){
			throw new NullPointerException();
		}
		
		newImageBlob.setBlobKeyOriginalSize(blobKeyOriginalSize.getKeyString());
	}
	
	
	
	@Override
	public List<ImageBlob> getImagesByType(Long restaurantId,
			ImageType imageType) {
		return dao.ofy().query(ImageBlob.class)
				.filter("restId", restaurantId + "")
				.filter("imageType", imageType).list();
	}
	@Deprecated
	private void sendToBlobstore(ImageBlob imageBlob, String cmd,
			byte[] imageBytes) throws IOException {

		String blobKeyToDelete = imageBlob.getBlobKey();

		ImageBlob tmpImage = imageBlob;

		dao.ofy().delete(imageBlob);

		BlobstoreServiceFactory.getBlobstoreService().delete(
				new BlobKey(blobKeyToDelete));

		String url = BlobstoreServiceFactory.getBlobstoreService()
				.createUploadUrl(
						"/blobUpload?restId=" + tmpImage.getRestaurantId()
								+ "&imageType="
								+ tmpImage.getImageType().name());
		URLFetchService urlFetch = URLFetchServiceFactory.getURLFetchService();
		try {
			String id = tmpImage.getId();
			HTTPRequest request = new HTTPRequest(new URL(url),
					HTTPMethod.POST, FetchOptions.Builder.withDeadline(20.0));
			String boundary = makeBoundary();

			request.setHeader(new HTTPHeader("Content-Type",
					"multipart/form-data; boundary=" + boundary));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			write(baos, "--" + boundary + "\r\n");
			writeParameter(baos, "id", id);
			write(baos, "--" + boundary + "\r\n");
			writeImage(baos, cmd, imageBytes);
			write(baos, "--" + boundary + "--\r\n");

			request.setPayload(baos.toByteArray());
			try {
				urlFetch.fetch(request);
			} catch (IOException e) {
				// Need a better way of handling Timeout exceptions here - 20
				// second deadline

			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * @return boundary
	 */
	private String makeBoundary() {
		return "---------------------------" + randomString() + randomString()
				+ randomString();
	}
	/**
	 * 
	 * @return The resultant string is a maximum of ceil(ln(263) / ln(36)) = 13 characters long and is suitable for use as a temporary id or cookie name.
	 */
	private static String randomString() {
		return Long.toString(random.nextLong(), 36);
	}
	/**
	 * static object of Random class
	 */
	private static Random random = new Random();
	/**
	 * 
	 * @param os - OutputStream to write
	 * @param s - content to write
	 * @throws IOException
	 */
	private void write(OutputStream os, String s) throws IOException {
		os.write(s.getBytes());
	}
	/**
	 * 
	 * @param os - OutputStream
	 * @param name - name, e.g: id
	 * @param value - value of name
	 * @throws IOException
	 */
	private void writeParameter(OutputStream os, String name, String value)
			throws IOException {
		write(os, "Content-Disposition: form-data; name=\"" + name
				+ "\"\r\n\r\n" + value + "\r\n");
	}

	private void writeImage(OutputStream os, String name, byte[] bs)
			throws IOException {
		write(os, "Content-Disposition: form-data; name=\"" + name
				+ "\"; filename=\"image.jpg\"\r\n");
		write(os, "Content-Type: image/jpeg\r\n\r\n");
		os.write(bs);
		write(os, "\r\n");
	}
	/**
	 * 
	 * @param imageBlob - image to copy and delete old, used to resize images to smaller
	 */
	public void copyAndDeleteBlob(ImageBlob imageBlob) {
		cropImage(imageBlob, 0, 0, 1, 1, "imageResized");
	}
	/**
	 * 
	 * @param imageBlob - image to copy
	 * @param newRestaurantId - id of {@link Restaurant}, the image will be copied to this {@link Restaurant} 
	 * @return new image url or empty string
	 */
	public String copyBlob(ImageBlob imageBlob, String newRestaurantId) {

			if(imageBlob == null){
				log.severe("imageBlob is null.");
				return "";
			}
			if(newRestaurantId == null || newRestaurantId.isEmpty()){
				log.severe("newRestaurantId is null or empty: " + newRestaurantId);
				return "";
			}
			
			String blobKeyString = imageBlob.getBlobKey();
			String blobKeyOriginalSizeString = imageBlob.getBlobKeyOriginalSize();
			String blobKeyScaleSizeString = imageBlob.getBlobKeyScaleSize();
			String blobKeyScreenSizeString = imageBlob.getBlobKeyScreenSize();
			
			BlobKey blobKey =  blobKeyString!=null? new BlobKey(imageBlob.getBlobKey()):null;
			
			if(blobKey == null){
				log.severe("blobKey is null. newRestaurantId= "+ newRestaurantId);
				return "";
			}

		BlobKey blobKeyOriginalSize = blobKeyOriginalSizeString!=null ? new BlobKey(imageBlob.getBlobKeyOriginalSize()) : null;
		BlobKey blobKeyScaleSize = blobKeyScaleSizeString!=null ? new BlobKey(imageBlob.getBlobKeyScaleSize()) : null;
		BlobKey blobKeyScreenSize = blobKeyScreenSizeString!=null ? new BlobKey(imageBlob.getBlobKeyScreenSize()) : null;

		BlobInfo blobInfo = getBlobInfo(blobKey);
		BlobInfo blobInfoOriginalSize = getBlobInfo(blobKeyOriginalSize);
		BlobInfo blobInfoScaleSize = getBlobInfo(blobKeyScaleSize);
		BlobInfo blobInfoScreenSize = getBlobInfo(blobKeyScreenSize);


		BlobKey newBlobKey = null;
		BlobKey newBlobKeyOrginalSize = null;
		BlobKey newBlobKeyScaleSize = null;
		BlobKey newBlobKeyScreenSize = null;


		ImageBlob newImageBlob = null;
		try {
			newBlobKey = blobInfo!=null ? writeToBlobstore("image/jpeg", "CopyCity"+blobInfo.getFilename(), getImageBytes(blobKey, blobInfo.getSize())) : null;
			if(newBlobKey == null) return "";
			newBlobKeyOrginalSize = blobInfoOriginalSize!=null ? writeToBlobstore("image/jpeg", "CopyCity"+blobInfoOriginalSize.getFilename(), getImageBytes(blobKeyOriginalSize, blobInfoOriginalSize.getSize())) : null ;  
			newBlobKeyScaleSize = blobInfoScaleSize!=null ? writeToBlobstore("image/jpeg", "CopyCity"+blobInfoScaleSize.getFilename(), getImageBytes(blobKeyScaleSize, blobInfoScaleSize.getSize())) : null ; 
			newBlobKeyScreenSize = blobInfoScreenSize!=null ? writeToBlobstore("image/jpeg", "CopyCity"+blobInfoScreenSize.getFilename(), getImageBytes(blobKeyScreenSize, blobInfoScreenSize.getSize())) : null ; 
			
			newImageBlob = new ImageBlob(newRestaurantId, newBlobKey.getKeyString(), new Date(), imageBlob.getImageType());
			newImageBlob.setWidth(imageBlob.getWidth());
			newImageBlob.setHeight(imageBlob.getHeight());
			
			newImageBlob.setBlobKeyOriginalSize(newBlobKeyOrginalSize!=null ? newBlobKeyOrginalSize.getKeyString() : null);
			newImageBlob.setBlobKeyScaleSize(newBlobKeyScaleSize!=null ? newBlobKeyScaleSize.getKeyString() : null);
			newImageBlob.setBlobKeyScreenSize(newBlobKeyScreenSize!=null ? newBlobKeyScreenSize.getKeyString() : null);
			
			newImageBlob.setDateCreated(imageBlob.getDateCreated());
			
			dao.ofy().put(newImageBlob);
		} catch (IOException e) {
			log.severe("imageBlobKey: " + imageBlob.getBlobKey() + " \n" + e.getStackTrace());
		}
		return newImageBlob !=null?newImageBlob.getImageUrl():"";
	}

	private BlobInfo getBlobInfo(BlobKey blobKey) {
		
		if(blobKey == null) return null;
		
		BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
		BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);
		if(blobInfo == null){
			blobInfo = blobInfoFactory.loadBlobInfo(blobKey); //Second try ...
			if(blobInfo == null){
				blobInfo = blobInfoFactory.loadBlobInfo(blobKey);
				if(blobInfo == null){
					blobInfo = blobInfoFactory.loadBlobInfo(blobKey);
					if(blobInfo == null){
						log.severe("Could not loadBlobInfo() for blobKey: " + blobKey);
						return null;
					}
				}
			}
			
		}
		
		return blobInfo;
	}


	/**
	 * 
	 * @param blobKey - {@link BlobKey} of image
	 * @param filesize - size of file
	 * @return array of bytes
	 */
	private byte[] getImageBytes(BlobKey blobKey, long filesize) {

		if (blobKey == null) {
			return null;
		}

		long chunkSize = BlobstoreService.MAX_BLOB_FETCH_SIZE -1; // 1024;
		long startIndex = 0;
		long endIndex = chunkSize;

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (filesize > BlobstoreService.MAX_BLOB_FETCH_SIZE) {

			boolean theend = false;
			while (theend == false) {
				if (endIndex == filesize) {
					theend = true;
				}

				// System.out.println("startIndex=" + startIndex + " endIndex="
				// + endIndex);

				byte[] b = blobstoreService.fetchData(blobKey, startIndex,
						endIndex);
				try {
					out.write(b);
				} catch (IOException e) {
					e.printStackTrace();
				}

				startIndex = endIndex + 1;
				endIndex = startIndex + chunkSize;
				if (endIndex > filesize) {
					endIndex = filesize;
				}
			}

		} else {
			byte[] b = blobstoreService.fetchData(blobKey, 0, filesize);
			try {
				out.write(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		byte[] filebytes = out.toByteArray();

		// System.out.println("getImageBytes(): filebytes size: " +
		// filebytes.length + " blobData.size=" + blobData.getSize());

		return filebytes;
	}
	/**
	 * 
	 * @param contentType - content of file: "image/jpeg";
	 * @param fileName - name of file after save to restaurant
	 * @param filebytes - array of bytes, use getImageBytes() method
	 * @return BlobKey of newly created blob
	 * @throws IOException if fileService cannot create new BlobFile
	 */
	public BlobKey writeToBlobstore(String contentType, String fileName,
			byte[] filebytes) throws IOException {
		// Get a file service
		FileService fileService = FileServiceFactory.getFileService();
		AppEngineFile file = fileService.createNewBlobFile(contentType,
				fileName);
		// Open a channel to write to it
		boolean lock = true;
		FileWriteChannel writeChannel = null;
		writeChannel = fileService.openWriteChannel(file, lock);
		
		// lets buffer the bitch
		BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(filebytes));
		
		byte[] buffer;
		if(filebytes.length > 524288){
			buffer = new byte[524288]; // 0.5 MB buffers
		}else{
			buffer = new byte[filebytes.length]; // filebytes.length MB buffers
		}

		while (in.read(buffer) > 0) { // -1 means EndOfStream
			ByteBuffer bb = ByteBuffer.wrap(buffer);
			writeChannel.write(bb);
		}
		writeChannel.closeFinally();
		return fileService.getBlobKey(file);
	}
	
	
	
	

	public void  checkImageSize(){
		
	}

	@Override
	public List<ImageBlob> getLast24hImages() {
		List<ImageBlob> images = new ArrayList<ImageBlob>();
		Query<ImageBlob> imgQuery = dao.ofy().query(ImageBlob.class);
		if (imgQuery == null){
			log.severe("no images found");
			return images;
		}

		
		long oneDayInMiliseconds = 86400000;
		long today = new Date().getTime();
		long yesterday = today - oneDayInMiliseconds;

		List<ImageBlob> imageBlobs = imgQuery.filter("timeInMiliSec >", yesterday).list();
		
		List<ImageBlob> imageBlobList = new ArrayList<ImageBlob>();
		
		for (ImageBlob imageBlob : imageBlobs) {
			if(imageBlob.getImageType() != ImageType.CITY || imageBlob.getImageType() != ImageType.EMPTY_MENU) imageBlobList.add(imageBlob);
		}
		
//		log.info("imageBlobs.size = " + imageBlobs.size() +", today = " + today + ", yesterday = " + yesterday );
		
		return imageBlobList;
	}
	
//	/**
//	 * @deprecated
//	 */
//	private void changeTimeToMiliSec(){
//		List<ImageBlob> images = new ArrayList<ImageBlob>();
//		Query<ImageBlob> imgQuery = dao.ofy().query(ImageBlob.class);
//		if (imgQuery == null){
//			return;
//		}
//		
//		images = imgQuery.list();
//		
//		for (ImageBlob imageBlob : images) {
//			
//			imageBlob.setDateCreated(imageBlob.getDateCreated());
//			dao.ofy().put(imageBlob);
//		}
//		
//	}
	
// backup database
public BlobKey writeBackupDB(String contentType, String fileName,
		byte[] filebytes) throws IOException {
	// Get a file service
	BackUpBlobKey BUBK = new BackUpBlobKey();
	FileService fileService = FileServiceFactory.getFileService();
	AppEngineFile file = fileService.createNewBlobFile(contentType,
			fileName);
	// Open a channel to write to it
	boolean lock = true;
	FileWriteChannel writeChannel = null;
	writeChannel = fileService.openWriteChannel(file, lock);
	// lets buffer the bitch
	
	BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(filebytes));
	byte[] buffer;
	if(filebytes.length > 524288){
		buffer = new byte[524288]; // 0.5 MB buffers
	}else{
		buffer = new byte[filebytes.length]; // filebytes.length MB buffers
	}

	while (in.read(buffer) > 0) { // -1 means EndOfStream
		ByteBuffer bb = ByteBuffer.wrap(buffer);
		writeChannel.write(bb);
	}
	
	writeChannel.closeFinally();
	
	String path = file.getFullPath();
	BlobKey blobkey = fileService.getBlobKey(file);
	BUBK.setCreateDate(new Date());
	BUBK.setFileBlobKey(fileService.getBlobKey(file).getKeyString());
	BUBK.setAddress(path);
	dao.ofy().put(BUBK);
	return blobkey;
	
}

private void parseJSONToObjectSimply(String json){
	Gson gson = new Gson();
	BackupWrapperSimply backupWrapperSimply = gson.fromJson(json, BackupWrapperSimply.class);		
}


private void parseJSONStringToObjects(String json){


	JsonObject object = (JsonObject) new com.google.gson.JsonParser().parse(json);
	Set<Map.Entry<String, JsonElement>> set = object.entrySet(); 
	
	BackupWrapper backupWrapper = new BackupWrapper();

	for (Entry<String, JsonElement> entry : set) {

	if(entry.getKey().equalsIgnoreCase("User")){

	List<User> users = new ArrayList<User>();
	JsonArray usersArray = (JsonArray) entry.getValue();
	for (JsonElement user : usersArray) {
		User value = new Gson().fromJson(user.getAsJsonObject(), User.class);
		users.add(value);
	}
	backupWrapper.setUsers(users);
	dao.ofy().put(users);
	}
	if(entry.getKey().equalsIgnoreCase("Restaurant")){
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		JsonArray restaurantsArray = (JsonArray) entry.getValue();

		for (JsonElement restaurant : restaurantsArray) {

			Restaurant value = new Gson().fromJson(restaurant.getAsJsonObject(), Restaurant.class);
			restaurants.add(value);
		}
		backupWrapper.setRestaurants(restaurants);
		dao.ofy().put(restaurants);
		}
	if(entry.getKey().equalsIgnoreCase("City")){
		List<City> cities = new ArrayList<City>();
		JsonArray restaurantsArray = (JsonArray) entry.getValue();
	for (JsonElement city : restaurantsArray) {
			City value = new Gson().fromJson(city.getAsJsonObject(), City.class);
			cities.add(value);
				}
		backupWrapper.setCities(cities);
		dao.ofy().put(cities);
	}
	if(entry.getKey().equalsIgnoreCase("ImageBlob")){
		List<ImageBlob> images = new ArrayList<ImageBlob>();
		JsonArray restaurantsArray = (JsonArray) entry.getValue();
	for (JsonElement image : restaurantsArray) {
		ImageBlob value = new Gson().fromJson(image.getAsJsonObject(),ImageBlob.class);
		images.add(value);
		dao.ofy().put(images);
	}
	backupWrapper.setImageBlobs(images);
	
	}
	if(entry.getKey().equalsIgnoreCase("BackUpBlobKey")){
		List<BackUpBlobKey> backups = new ArrayList<BackUpBlobKey>();
		JsonArray restaurantsArray = (JsonArray) entry.getValue();
	for (JsonElement backup : restaurantsArray) {
		BackUpBlobKey value = new Gson().fromJson(backup.getAsJsonObject(),BackUpBlobKey.class);
		backups.add(value);
		dao.ofy().put(value);
	}
	backupWrapper.setBackups(backups);
		dao.ofy().put(backups);

	}
}	 

}


// restore database
public 	String restoreBackuoDB(List<String> JSON,List<BackUpBlobKey> blobkey) throws IOException, FileNotFoundException {
		
		String jsonString = "";
		FileService fileservice = FileServiceFactory.getFileService();
		AppEngineFile file ;
		FileReadChannel readChannel = null;
//		for (String json :JSON){
			for(BackUpBlobKey bubk : blobkey){
			
			file = new AppEngineFile(bubk.getAddress());
			
		boolean lock = true;
		try{
			readChannel = fileservice.openReadChannel(file, lock);	
			if (readChannel !=null) break;
		}catch(FileNotFoundException ex){
			log.severe("file not found");
			jsonString+="file not found";
			dao.ofy().delete(bubk);
		}catch (FinalizationException exp) {
			log.severe("FinalizationException");
			jsonString+="FinalizationException";
			dao.ofy().delete(bubk);

		}catch (Exception e) {
			log.severe(e.getMessage());
			jsonString+=e.getMessage();
			dao.ofy().delete(bubk);
		}
		
}if(readChannel == null) return jsonString;
		BufferedReader reader = new BufferedReader(Channels.newReader(readChannel,"UTF-8"));
		String line = "";
		jsonString="";
		
			
			while ( (line = reader.readLine())!=null) {
				jsonString += line+ "\n";	
			}
			readChannel.close();
			
			parseJSONStringToObjects(jsonString);
			return jsonString;
}
/***
 * 
 * Comparator class used to compare image blobs by creation date
 *
 */
class MyComparator implements Comparator<ImageBlob> {
	public int compare(ImageBlob o1, ImageBlob o2) {
		if (o1.getDateCreated().getTime() > o2.getDateCreated().getTime()) {
			return -1;
		} else if (o1.getDateCreated().getTime() < o2.getDateCreated()
				.getTime()) {
			return 1;
		}
		return 0;
	}

}

}
