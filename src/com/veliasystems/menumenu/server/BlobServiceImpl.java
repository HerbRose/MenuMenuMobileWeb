package com.veliasystems.menumenu.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
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
	
		private static final Logger log = Logger.getLogger(BlobServiceImpl.class.getName()); 
        
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
        
        @Override
        public ImageBlob getLastUploadedImage(String restaurantId) {
        	
        	List<ImageBlob> all = getAllImages(restaurantId);
        	if (!all.isEmpty()) return all.get(0);
        	else return null;
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
	        return getImages( ""+r.getId(), ImageType.MENU );
        }
        
        @Override
        public List<ImageBlob> getBoardImages(String restaurantId) {
        	return getImages( restaurantId, ImageType.MENU );
        }
        
        @Override
        public List<ImageBlob> getHeaderImages(Restaurant r) {
        	return getImages( ""+r.getId(), ImageType.LOGO );
        }
        
        @Override
        public List<ImageBlob> getHeaderImages(String restaurantId) {
        	return getImages( restaurantId, ImageType.LOGO );
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

		
		 @Override
		public void cropImage(ImageBlob imageBlob, double leftX, double topY,
			double rightX, double bottomY) {
			 		  	
			 	BlobKey blobKey = new BlobKey(imageBlob.getBlobKey());
			 	ImagesService imagesService = ImagesServiceFactory.getImagesService();
			 	Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
			 	Transform cropTransform = ImagesServiceFactory.makeCrop(leftX, topY, rightX, bottomY);
			 	Image newImage = imagesService.applyTransform(cropTransform, oldImage);
			 	
			 	Transform scaleTransform = null;
			 	switch(imageBlob.getImageType()){
			 	case PROFILE: 
			 		scaleTransform = ImagesServiceFactory.makeResize(420, 280);
			 		break;
			 	case LOGO:
			 		scaleTransform = ImagesServiceFactory.makeResize(220, newImage.getHeight());
			 		break;
			 	case MENU:
			 		scaleTransform = ImagesServiceFactory.makeResize(220, newImage.getHeight());
			 		break;
			 		
			 	}
			 	
			 	Image scaleImage = imagesService.applyTransform(scaleTransform, newImage);
			 	
			 	System.out.println("scaling: " + scaleImage.getHeight() + " " + scaleImage.getWidth());
			 	
			 	try {
					sendToBlobstore(imageBlob, "save", scaleImage.getImageData());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 	
			 	
		}
			  
    
		 private void sendToBlobstore(ImageBlob imageBlob, String cmd, byte[] imageBytes) throws IOException{
			 
			 String blobKeyToDelete = imageBlob.getBlobKey();
			  
			 System.out.println(imageBlob.getRestaurantId() + " crop " + imageBlob.getBlobKey());
			 
					 
			 ImageBlob tmpImage = imageBlob;
			 
			 dao.ofy().delete(imageBlob);
			 			 	 	 			 
			 BlobstoreServiceFactory.getBlobstoreService().delete(new BlobKey(blobKeyToDelete));	
			 		 
			 String url = BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/blobUpload?restId=" + tmpImage.getRestaurantId() + "&imageType=" + tmpImage.getImageType().name());
			 URLFetchService urlFetch = URLFetchServiceFactory.getURLFetchService();
			 try {
				String id = tmpImage.getId();
				HTTPRequest request = new HTTPRequest(new URL(url), HTTPMethod.POST, FetchOptions.Builder.withDeadline(20.0));
				String boundary = makeBoundary();
				
				request.setHeader(new HTTPHeader("Content-Type", "multipart/form-data; boundary=" + boundary));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				write(baos, "--"+boundary+"\r\n");
			    writeParameter(baos, "id", id);
			    write(baos, "--"+boundary+"\r\n");
			    writeImage(baos, cmd, imageBytes);
			    write(baos, "--"+boundary+"--\r\n");
			 
			    request.setPayload(baos.toByteArray());
			    try {
			        urlFetch.fetch(request);
			    } catch (IOException e) {
			        // Need a better way of handling Timeout exceptions here - 20 second deadline
			       
			    }        
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			
		 }
		 
		 private String makeBoundary() {
			    return "---------------------------" + randomString() + randomString() + randomString();
			} 
		 
		 private static String randomString() {
			    return Long.toString(random.nextLong(), 36);
			}
		 
		 private static Random random = new Random();
        
		 private void write(OutputStream os, String s) throws IOException {
			    os.write(s.getBytes());
			}
		 
		 private void writeParameter(OutputStream os, String name, String value) throws IOException {
			    write(os, "Content-Disposition: form-data; name=\""+name+"\"\r\n\r\n"+value+"\r\n");
			}
			 
			private void writeImage(OutputStream os, String name, byte[] bs) throws IOException {
			    write(os, "Content-Disposition: form-data; name=\""+name+"\"; filename=\"image.jpg\"\r\n");
			    write(os, "Content-Type: image/jpeg\r\n\r\n");
			    os.write(bs);
			    write(os, "\r\n");
			}
}
