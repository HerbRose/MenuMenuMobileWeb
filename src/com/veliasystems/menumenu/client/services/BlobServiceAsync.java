package com.veliasystems.menumenu.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;

public interface BlobServiceAsync {

	void getBlobStoreUrl(AsyncCallback<String> callback);

	void getBlobs(BlobDataFilter filter, AsyncCallback<BlobData[]> callback);

	void getBlobStoreUrl(String sectionKey, AsyncCallback<String> callback);

	void deleteBlob(String blobKeyString, AsyncCallback<Boolean> callback);

	void getImages(Restaurant r, AsyncCallback<List<ImageBlob>> callback);

	void getImages(String restaurantId, AsyncCallback<List<ImageBlob>> callback);

}
