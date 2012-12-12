package com.veliasystems.menumenu.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;

public interface BlobServiceAsync {

	void getBlobs(BlobDataFilter filter, AsyncCallback<BlobData[]> callback);

	void getBlobStoreUrl(String sectionKey, ImageType imageType,
			AsyncCallback<String> callback);

	void deleteBlob(ImageBlob imageBlob, AsyncCallback<Boolean> callback);

	void getHeaderImages(Restaurant r, AsyncCallback<List<ImageBlob>> callback);

	void getHeaderImages(String restaurantId,
			AsyncCallback<List<ImageBlob>> callback);

	void getBoardImages(Restaurant r, AsyncCallback<List<ImageBlob>> callback);

	void getBoardImages(String restaurantId,
			AsyncCallback<List<ImageBlob>> callback);

	void getProfileImages(Restaurant r, AsyncCallback<List<ImageBlob>> callback);

	void getProfileImages(String restaurantId,
			AsyncCallback<List<ImageBlob>> callback);

	void getAllImages(Restaurant r, AsyncCallback<List<ImageBlob>> callback);

	void getAllImages(String restaurantId,
			AsyncCallback<List<ImageBlob>> callback);

	void getLastUploadedImage(String restaurantId,
			AsyncCallback<ImageBlob> callback);

	void getLastUploadedImage(Long restaurantId, ImageType imageType,
			AsyncCallback<ImageBlob> callback);

	void cropImage(ImageBlob imageBlob, double leftX, double topY,
			double rightX, double bottomY, AsyncCallback<ImageBlob> callback);

	void getImagesByType(Long restaurantId, ImageType imageType,
			AsyncCallback<List<ImageBlob>> callback);

	void getEmptyList(AsyncCallback<List<ImageBlob>> callback);

	void setEmptyBoard(ImageBlob imageBlob, AsyncCallback<String> callback);

	void getLast24hImages(AsyncCallback<List<ImageBlob>> callback);

}
