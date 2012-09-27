package com.veliasystems.menumenu.server;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
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
public class BlobServiceImpl extends RemoteServiceServlet implements BlobService {

    
		private DAO dao = new DAO();
	
        
        @Override
        public String getBlobStoreUrl(String restId, ImageType imageType) {
            String url = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/blobUpload?restId=" + restId + "&imageType=" + imageType.name());
            return url;
        }
        
        public List<ImageBlob> getAllImages( Restaurant r ) {
        	return getAllImages("" + r.getId());
        }
        
        public List<ImageBlob> getAllImages(String restaurantId) {
        	List<ImageBlob> images = new ArrayList<ImageBlob>();
        	Query<ImageBlob> imgQuery = dao.ofy().query(ImageBlob.class);
    		if (imgQuery==null) return images;
    		
    		return imgQuery.filter("restId =", restaurantId).order("-dateCreated").list();
        }
        
        private List<ImageBlob> getImages(String restaurantId, ImageType imageType) {
        	List<ImageBlob> allImages = getAllImages(restaurantId);
        	List<ImageBlob> ret = new ArrayList<ImageBlob>();
        	
        	for ( ImageBlob blob : allImages ) {
        		if (blob.getImageType() == imageType) {
        			ret.add(blob);
        		}
        	}
        	
        	return ret;
        }
        
        
        @Override
        public List<ImageBlob> getBoardImages(Restaurant r) {
	        return getImages( ""+r.getId(), ImageType.BOARD );
        }
        
        @Override
        public List<ImageBlob> getBoardImages(String restaurantId) {
        	return getImages( restaurantId, ImageType.BOARD );
        }
        
        @Override
        public List<ImageBlob> getHeaderImages(Restaurant r) {
        	return getImages( ""+r.getId(), ImageType.HEADER );
        }
        
        @Override
        public List<ImageBlob> getHeaderImages(String restaurantId) {
        	return getImages( restaurantId, ImageType.HEADER );
        }
        
        @Override
        public List<ImageBlob> getProfileImages(Restaurant r) {
        	return getImages( ""+r.getId(), ImageType.PROFILE );
        }
        
        @Override
        public List<ImageBlob> getProfileImages(String restaurantId) {
        	return getImages( restaurantId, ImageType.PROFILE );
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
        public boolean deleteBlob( String blobKeyString ) {
        	 if (blobKeyString == null || blobKeyString.isEmpty()) {
                 return false;
               }
               
        	 BlobKey blobKey = new BlobKey(blobKeyString);
        	 BlobstoreServiceFactory.getBlobstoreService().delete(blobKey);
               
               return true;
        }
        
        
        public boolean deleteBlob(BlobDataFilter filter) {
          
          String blobKeyString = filter.getBlobKey();
          
          return deleteBlob( blobKeyString );
         
        }

       
        
        
}
