package com.veliasystems.menumenu.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.FinalizationException;
import com.google.appengine.api.files.LockException;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.apphosting.api.ApiProxy.RequestTooLargeException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Query;
import com.veliasystems.menumenu.client.entities.City;
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

		List<ImageBlob> all = dao.ofy().query(ImageBlob.class)
				.filter("restId", restaurantId + "")
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

	public List<ImageBlob> getDefaultEmptyProfil() {
		Query<ImageBlob> imgQuery = dao.ofy().query(ImageBlob.class);
		if (imgQuery == null)
			return null;
		return imgQuery.filter("restId =", "0").list();
	}

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
	 * get blob info list
	 * 
	 * @param filter
	 * @return
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
		String blobKeyToDelete = imageBlob.getBlobKey();
		dao.ofy().delete(imageBlob);
		BlobstoreServiceFactory.getBlobstoreService().delete(
				new BlobKey(blobKeyToDelete));
		return true;
	}

	// public boolean deleteBlob(BlobDataFilter filter) {
	//
	// String blobKeyString = filter.getBlobKey();
	//
	// return deleteBlob( blobKeyString );
	//
	// }

	@Override
	public void cropImage(ImageBlob imageBlob, double leftX, double topY,
			double rightX, double bottomY) {

		BlobKey blobKey = new BlobKey(imageBlob.getBlobKey());
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
		if (leftX < 0)
			leftX = 0;
		if (topY < 0)
			topY = 0;
		if (rightX > 1)
			rightX = 1;
		if (bottomY > 1)
			bottomY = 1;
		Transform cropTransform = ImagesServiceFactory.makeCrop(leftX, topY,
				rightX, bottomY);
		Image newImage = imagesService.applyTransform(cropTransform, oldImage);

		Transform scaleTransform = null;
		switch (imageBlob.getImageType()) {
		case PROFILE:
			scaleTransform = ImagesServiceFactory.makeResize(420, 280);
			break;
		case LOGO:
			scaleTransform = ImagesServiceFactory.makeResize(220,
					newImage.getHeight());
			break;
		case MENU:
			scaleTransform = ImagesServiceFactory.makeResize(220,
					newImage.getHeight());
			break;

		}

		Image scaleImage = imagesService.applyTransform(scaleTransform,
				newImage);
		try {
			sendToBlobstore(imageBlob, "save", scaleImage.getImageData());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<ImageBlob> getImagesByType(Long restaurantId,
			ImageType imageType) {
		return dao.ofy().query(ImageBlob.class)
				.filter("restId", restaurantId + "")
				.filter("imageType", imageType).list();
	}

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

	private String makeBoundary() {
		return "---------------------------" + randomString() + randomString()
				+ randomString();
	}

	private static String randomString() {
		return Long.toString(random.nextLong(), 36);
	}

	private static Random random = new Random();

	private void write(OutputStream os, String s) throws IOException {
		os.write(s.getBytes());
	}

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

	private BlobKey writeToBlobstore(String contentType, String fileName,
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
		byte[] buffer = new byte[524288]; // 0.5 MB buffers
		while (in.read(buffer) > 0) { // -1 means EndOfStream
			ByteBuffer bb = ByteBuffer.wrap(buffer);
			writeChannel.write(bb);
		}
		writeChannel.closeFinally();
		return fileService.getBlobKey(file);
	}

}

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
