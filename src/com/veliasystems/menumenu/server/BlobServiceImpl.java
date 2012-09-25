package com.veliasystems.menumenu.server;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Query;
import com.veliasystems.menumenu.client.entities.ImageBlob;
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
        public String getBlobStoreUrl() {
                String url = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/blobUpload");
                return url;
        }
        
        @Override
        public String getBlobStoreUrl(String restId) {
            String url = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/blobUpload?restId=" + restId);
            return url;
        }
        
        @Override
        public List<ImageBlob> getImages( Restaurant r ) {
        	return getImages("" + r.getId());
        }
        
        @Override
        public List<ImageBlob> getImages(String restaurantId) {
        	List<ImageBlob> images = new ArrayList<ImageBlob>();
        	Query<ImageBlob> imgQuery = dao.ofy().query(ImageBlob.class);
    		if (imgQuery==null) return images;
    		
    		return imgQuery.filter("restId =", restaurantId).order("-dateCreated").list();
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
