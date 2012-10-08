package com.veliasystems.menumenu.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
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
import com.googlecode.objectify.Key;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;

public class BlobUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 6876072793481198419L;

	private DAO dao = new DAO();
	
	// init the blog store service
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private static final Logger log = Logger.getLogger(BlobUploadServlet.class.getName()); 

    public void doPost(HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {

    	
    	String restId = request.getParameter("restId");
    	String imageType = request.getParameter("imageType");
    	
    	
		if (restId==null || restId.isEmpty() || imageType==null || imageType.isEmpty()){
			//System.out.println("BlobUploadServlet::doPost: restId or imageType is NULL||empty");
			log.warning("BlobUploadServlet::doPost: restId or imageType is NULL||empty");
//			res.getWriter().println("BlobUploadServlet::doPost: restId is NULL");
//			res.getWriter().flush();
			return;
		}
		
//		System.out.println("BlobUploadServlet::doPost: restId is: " + restId);
    	
            // this has to be used in an rpc call to get the url to be used with this request
            //String url = blobstoreService.createUploadUrl("/upload");
            
            int len = request.getContentLength();
            int mb = (1024 * 1024) * 1;
            if (len > mb) { 
              throw new RuntimeException("Sorry that file is too large. Try < 1024 or 1MB file"); }
            
            Map<String, List<BlobKey>> uploads = blobstoreService.getUploads(request);
            

            
            BlobKey blobKey = null;
            
            Set<String> keys = uploads.keySet();
//            System.out.println("BlobUploadServlet, list uploads:");
            for (String key : keys) {
            	List<BlobKey> blobkeys = uploads.get(key);
            	for (BlobKey bkey : blobkeys) {
            		System.out.println(key + " - " + bkey);
            		if (blobKey==null) blobKey = bkey; // just pick the 1st one
            	}
            }

            
            if (blobKey == null) {
//            	System.out.println("BlobUploadServlet, previous method returned shit, trying deprecated...");
            	Map blobs = blobstoreService.getUploadedBlobs(request);
            	blobKey = (BlobKey) blobs.get("image"); 
            }
            	
            

            if (blobKey == null) {
//            		System.out.println("BlobUploadServlet::doPost: blobKey is null");
                    res.sendRedirect("/");
            } else {
//            	  System.out.println("BlobUploadServlet::doPost: blobKey is " + blobKey.getKeyString() + ", storing.."); 
            		storeImageBlob( restId, imageType, blobKey );
                    res.sendRedirect("/blobServe?blob-key=" + blobKey.getKeyString());
            }
            
    }
    
    
    
    
    void storeImageBlob( String restId, String imageType, BlobKey blobKey ) {
		
 //   	System.out.println("BlobUploadServlet::storeImageBlob");
    	
		ImageBlob imageBlob = new ImageBlob(restId, blobKey.getKeyString(), new Date(), ImageType.valueOf(imageType) );
			
        Image image = ImagesServiceFactory.makeImageFromBlob(blobKey);
        ImagesService imageService = ImagesServiceFactory.getImagesService();
        
        Transform dummy = ImagesServiceFactory.makeRotate(360);
        Image newImage = imageService.applyTransform( dummy, image );
        
//        int ratio = image.getWidth() / 220;
//        
//        Transform resize;
//        
//        if (imageType.equalsIgnoreCase(ImageType.LOGO.name())) {
//        	resize = ImagesServiceFactory.makeResize(220, newImage.getHeight() / ratio);
//        } 
//        else if (imageType.equalsIgnoreCase(ImageType.MENU.name())) {
//            	resize = ImagesServiceFactory.makeResize(220, newImage.getHeight() / ratio);
//            }	
//        else {
//        	resize = ImagesServiceFactory.makeResize(420, 280);
//        }
//    
//        Image resizeImage = imageService.applyTransform( resize, image, ImagesService.OutputEncoding.JPEG );
//       
    
		imageBlob.setWidth(newImage.getWidth());
		imageBlob.setHeight(newImage.getHeight());
		
		Key<ImageBlob> key = dao.ofy().put(imageBlob);
		
//		System.out.println("Blob saved: " + key.getKind() + ", " + key.getName() + ", blobKey: " + blobKey.getKeyString());
		
		
	}
    
  
}
