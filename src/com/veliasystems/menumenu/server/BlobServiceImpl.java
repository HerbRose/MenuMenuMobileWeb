package com.veliasystems.menumenu.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesService.OutputEncoding;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ImagesServiceFailureException;
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
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Query;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
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
		String url = BlobstoreServiceFactory.getBlobstoreService()
				.createUploadUrl(
						"/blobUpload?restId=" + restId + "&imageType="
								+ imageType.name());
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
			return null;
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

	@Override
	public boolean deleteBlob(ImageBlob imageBlob) {
		if (imageBlob == null) {
			return false;
		}
		if(imageBlob.getBlobKeyOriginalSize() != null && !imageBlob.getBlobKeyOriginalSize().isEmpty()){
			BlobstoreServiceFactory.getBlobstoreService().delete(
					new BlobKey(imageBlob.getBlobKeyOriginalSize()));
		}
		BlobstoreServiceFactory.getBlobstoreService().delete(
				new BlobKey(imageBlob.getBlobKey()));
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
	private ImageBlob cropImage(ImageBlob imageBlob, double leftX, double topY,
			double rightX, double bottomY, String newName) {

		Query<ImageBlob> query = dao.ofy().query(ImageBlob.class);
		ImageBlob imageBlob2;
		if (query == null) {
			imageBlob2 =  new ImageBlob();
		}else{
			imageBlob2 = query.filter("blobKey", imageBlob.getBlobKey()).get();
		}
		
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
		

		Transform cropTransform = ImagesServiceFactory.makeCrop(leftX, topY,
				rightX, bottomY);
		
		InputSettings inputSettings = new InputSettings();
		inputSettings.setOrientationCorrection(OrientationCorrection.CORRECT_ORIENTATION);
		
		OutputSettings outputSettings = new OutputSettings(OutputEncoding.JPEG);
//		outputSettings.setQuality(100);
		
		
//		Image newImage = imagesService.applyTransform(cropTransform, oldImage, OutputEncoding.JPEG);
		Image newImage;
		try{
			newImage = imagesService.applyTransform(cropTransform, oldImage, inputSettings, outputSettings);
		}catch(ImagesServiceFailureException e){
			log.log(Level.SEVERE, "\noldImage: "
					+oldImage 
					+"\n", e);
			return null;
		}
		Transform scaleTransform = null;
		switch (imageBlob.getImageType()) {
		case PROFILE:
			scaleTransform = ImagesServiceFactory.makeResize(450, 260);
			break;
		case LOGO:
			scaleTransform = ImagesServiceFactory.makeResize(220,
					4000);//newImage.getHeight());
			break;
		case MENU:
			scaleTransform = ImagesServiceFactory.makeResize(220,
					4000);//newImage.getHeight());
			break;
		default:
			log.severe("Unknow image type: " + imageBlob.getImageType());
		}

		Image scaleImage = imagesService.applyTransform(scaleTransform,
				newImage, inputSettings, outputSettings);
		
		//System.out.println(scaleImage.getHeight() + "  " +scaleImage.getWidth());
		
		BlobKey newBlobKey = null;
		ImageBlob newImageBlob = null;
		try {
			newBlobKey = writeToBlobstore("image/jpeg", imageBlob.getImageType().name()+newName,
					scaleImage.getImageData());
			newImageBlob = new ImageBlob(imageBlob.getRestaurantId(),
					newBlobKey.getKeyString(), imageBlob.getDateCreated(),
					imageBlob.getImageType());
			newImageBlob.setWidth(scaleImage.getWidth());
			newImageBlob.setHeight(scaleImage.getHeight());
			newImageBlob.setBlobKeyOriginalSize(imageBlob2.getBlobKeyOriginalSize());
			dao.ofy().put(newImageBlob);
			
			//remove old image and image's data
			BlobstoreServiceFactory.getBlobstoreService().delete(
					new BlobKey(imageBlob.getBlobKey()));
			removeImageBlobByBlobKey(imageBlob.getBlobKey());
			//dao.ofy().delete(imageBlob);
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
	@Override
	public void removeImageBlobByBlobKey(String blobKey){
		Query<ImageBlob> blobQuery = dao.ofy().query(ImageBlob.class);
		if(blobQuery == null) return;
		ImageBlob imgBlob = blobQuery.filter("blobKey =", blobKey).get();
		if(imgBlob == null) return;
		System.out.println("delete blob");
		deleteBlob(imgBlob);
		
	}

	@Override
	public Map<String, ImageBlob> cropImage(ImageBlob imageBlob, double leftX, double topY,
			double rightX, double bottomY){
		
		Map<String, ImageBlob> mapToReturn = new HashMap<String, ImageBlob>();
		
		mapToReturn.put("old", imageBlob);
		mapToReturn.put("new", cropImage(imageBlob, leftX, topY, rightX, bottomY, "imageCrop.jpg") );
		return mapToReturn;
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
		cropImage(imageBlob, 0, 0, 1, 1, "imageResized.jpg");
	}
	/**
	 * 
	 * @param imageBlob - image to copy
	 * @param newRestaurantId - id of {@link Restaurant}, the image will be copied to this {@link Restaurant} 
	 */
	public void copyBlob(ImageBlob imageBlob, String newRestaurantId) {

		BlobKey blobKey = new BlobKey(imageBlob.getBlobKey());
		BlobKey newBlobKey = null;

		BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
		BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);
		// Transform cropTransform = ImagesServiceFactory.makeRotate(0);
		// Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);

		// Image newImage = imagesService.applyTransform(cropTransform,
		// oldImage);

		// byte[] newImageData = getImageBytes(newBlobKey, blobInfo.getSize());
		log.info("Start copy imageBlob: " + imageBlob.getBlobKey() + " blobSize: " + blobInfo.getSize());
		try {
			newBlobKey = writeToBlobstore("image/jpeg", "imageCopy.jpg",
					getImageBytes(blobKey, blobInfo.getSize()));
			ImageBlob newImageBlob = new ImageBlob(newRestaurantId,
					newBlobKey.getKeyString(), new Date(),
					imageBlob.getImageType());
			newImageBlob.setWidth(imageBlob.getWidth());
			newImageBlob.setHeight(imageBlob.getHeight());
			dao.ofy().put(newImageBlob);
		} catch (IOException e) {
			log.severe("imageBlobKey: " + imageBlob.getBlobKey() + " \n"
					+ e.getStackTrace());
			// e.printStackTrace();
		}

		// BlobKey blobKey = new BlobKey(imageBlob.getBlobKey());
		// ImagesService imagesService =
		// ImagesServiceFactory.getImagesService();
		// Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
		//
		// // Transform cropTransform = ImagesServiceFactory.makeRotate(0);
		// // Image newImage = imagesService.applyTransform(cropTransform,
		// // oldImage);
		//
		// byte[] newImageData = oldImage.getImageData();
		//
		// if (newImageData == null) {
		// return;
		// }
		//
		// String url = BlobstoreServiceFactory.getBlobstoreService()
		// .createUploadUrl(
		// "/blobUpload?restId=" + newRestaurantId + "&imageType="
		// + imageBlob.getImageType().name());
		// URLFetchService urlFetch =
		// URLFetchServiceFactory.getURLFetchService();
		// try {
		// String id = imageBlob.getId();
		// HTTPRequest request = new HTTPRequest(new URL(url),
		// HTTPMethod.POST, FetchOptions.Builder.withDeadline(20.0));
		// String boundary = makeBoundary();
		//
		// request.setHeader(new HTTPHeader("Content-Type",
		// "multipart/form-data; boundary=" + boundary));
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//
		// try {
		// write(baos, "--" + boundary + "\r\n");
		// writeParameter(baos, "id", id);
		// write(baos, "--" + boundary + "\r\n");
		// writeImage(baos, "save", newImageData);
		// write(baos, "--" + boundary + "--\r\n");
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		// request.setPayload(baos.toByteArray());
		// try {
		// urlFetch.fetch(request);
		// } catch (RequestTooLargeException e) {
		// log.log(Level.SEVERE,
		// "RequestTooLargeException- ImageBlobKey: "
		// + imageBlob.getBlobKey()
		// + " newImageData length: "
		// + newImageData.length + "\n" + e);
		// } catch (IOException e) {
		// // Need a better way of handling Timeout exceptions here - 20
		// // second deadline
		// }
		//
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
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
		BufferedInputStream in = new BufferedInputStream(
				new ByteArrayInputStream(filebytes));
		
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
		//changeTimeToMiliSec();
		List<ImageBlob> images = new ArrayList<ImageBlob>();
		Query<ImageBlob> imgQuery = dao.ofy().query(ImageBlob.class);
		if (imgQuery == null){
			return images;
		}

		Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, -1);
		
		Date yesterday = new Date(today.getTimeInMillis());

		return imgQuery.filter("timeInMiliSec >", yesterday.getTime()).list();
	}
	
	/**
	 * @deprecated
	 */
	private void changeTimeToMiliSec(){
		List<ImageBlob> images = new ArrayList<ImageBlob>();
		Query<ImageBlob> imgQuery = dao.ofy().query(ImageBlob.class);
		if (imgQuery == null){
			return;
		}
		
		images = imgQuery.list();
		
		for (ImageBlob imageBlob : images) {
			
			imageBlob.setDateCreated(imageBlob.getDateCreated());
			dao.ofy().put(imageBlob);
		}
		
	}
	
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
