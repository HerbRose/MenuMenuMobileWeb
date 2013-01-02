package com.veliasystems.menumenu.client.services;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.objectify.Query;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;


@RemoteServiceRelativePath("blobstore")
public interface BlobService extends RemoteService {
        /**
         * 
         * @param restId - id of {@link Restaurant}
         * @param imageType - types of image, specified by {@link ImageType}
         * @return url of image
         */
        String getBlobStoreUrl(String restId, ImageType imageType);
        /**
         * 
         * @param r - {@link Restaurant} object
         * @return List<ImageBlob> which contains all logos images in specified {@link Restaurant}
         */
        List<ImageBlob> getHeaderImages( Restaurant r ); // loga/naglowki restauracji
        /**
         * 
         * @param restaurantId - id of {@link Restaurant}
         * @return List<ImageBlob> which contains all logos images in specified {@link Restaurant}
         */
        List<ImageBlob> getHeaderImages( String restaurantId );
        /**
         * 
         * @param r - {@link Restaurant} object
         * @return List<ImageBlob> which contains all boards images in specified {@link Restaurant}
         */
        List<ImageBlob> getBoardImages( Restaurant r );  // tablice/menu
        /**
         * 
         * @param restaurantId - id of {@link Restaurant}
         * @return List<ImageBlob> which contains all boards images in specified {@link Restaurant}
         */
        List<ImageBlob> getBoardImages( String restaurantId );
        /**
         * 
         * @param r - {@link Restaurant} object
         * @return List of {@link ImageBlob} which contains all profiles images in specified {@link Restaurant}
         */
        List<ImageBlob> getProfileImages( Restaurant r ); // zdjecia profilowe
        /**
         * 
         * @param restaurantId - id of restaurant
         * @return List of {@link ImageBlob} which contains all profiles images in specified {@link Restaurant}
         */
        List<ImageBlob> getProfileImages( String restaurantId );
        /**
         * 
         * @param r - {@link Restaurant} object
         * @return List {@link ImageType} of all images connected to {@link Restaurant}
         */
        List<ImageBlob> getAllImages( Restaurant r );
        /**
         * 
         * @param restaurantId - id of {@link Restaurant}
         * @return List of {@link ImageBlob} of all images connected to {@link Restaurant}
         */
        List<ImageBlob> getAllImages( String restaurantId );
        
        /**
         * 
         * @param restaurantId - id of {@link Restaurant}
         * @return Last uploaded image to specified {@link Restaurant}
         */
        ImageBlob getLastUploadedImage( String restaurantId );
        
        BlobData[] getBlobs(BlobDataFilter filter);
        /**
         *  
         * @param imageBlob - image to delete
         * @return true if image has been deleted or false if {@link ImageBlob} was null
         */
        boolean deleteBlob(ImageBlob imageBlob);
        /**
    	 * 
    	 * @param imageBlob - image to crop
    	 * @param leftX - percentage of left line, counting from left side
    	 * @param topY - percentage of top line, counting from top
    	 * @param rightX - percentage of right line, counting from left side
    	 * @param bottomY - percentage of bottom line, counting from top side
    	 * @return Map which contains two elements: old image blob and newly created image blob
    	 * <br/>
    	 * <br/>
    	 * <h1>Short description</h1>
    	 * <li>All values must be in range of 0.0 to 1.0, otherwise all incorrect values will be rounded to maximum</li>
    	 * <li>For example:if leftX < 0, then leftX is automatically rounded to 0.0;
    	 */
        Map<String, ImageBlob> cropImage(ImageBlob imageBlob, double leftX, double topY, double rightX, double bottomY);
        /**
         * 
         * @param restaurantId - id of {@link Restaurant}
         * @param imageType - type of image, specified in {@link ImageType}
         * @return List of images in restaurant 
         */
		List<ImageBlob> getImagesByType(Long restaurantId, ImageType imageType);
		/**
		 * 
		 * @param restaurantId - id of {@link Restaurant}
		 * @param imageType - type of image, specified in {@link ImageType}
		 * @return {@link ImageBlob} who was last uploaded to server
		 */
		ImageBlob getLastUploadedImage(Long restaurantId, ImageType imageType);
		/**
		 * 
		 * @return List of images which are set to default images of board, when none is set
		 */
		List<ImageBlob> getEmptyList();
        /**
         * 
         * @param imageBlob - image to set for default empty board
         * @return null if {@link Query} is null, else id of {@link ImageBlob}
         */
		String setEmptyBoard(ImageBlob imageBlob);
		/**
		 * 
		 * @return List of all images which were uploaded to server in last 24h
		 */
		List<ImageBlob> getLast24hImages();
}