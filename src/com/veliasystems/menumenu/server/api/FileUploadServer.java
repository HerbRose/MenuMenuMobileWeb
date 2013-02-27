package com.veliasystems.menumenu.server.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Blob;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class FileUploadServer extends HttpServlet {

	private static final Logger log = Logger.getLogger(FileUploadServer.class.getName());

	private BlobServiceImpl blobService = new BlobServiceImpl();
	private StoreServiceImpl storService = new StoreServiceImpl();
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String token = req.getParameter("token");
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			return;
		}

		String restId = req.getParameter("restaurantId");
    	String imageTypeString = req.getParameter("imageType");
		if (restId==null || restId.isEmpty() || imageTypeString==null || imageTypeString.isEmpty() ){
			log.warning("FileUploadServer::doPost: restId or imageType is NULL||empty");
			return;
		}

		String fileName = req.getParameter("filename");
		if(fileName == null || fileName.isEmpty() ){
			fileName = "fileNameNotFound";
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");

		String jsonp = req.getParameter("jsonp");
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		if (jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
		ImageType imageType= null;
		
		try{
			imageType = ImageType.valueOf(imageTypeString);
		}catch( IllegalArgumentException e){
			log.warning("FileUploadServer::doPost: IllegalArgumentException - imageTypeString is not a ImageType. imageTypeString: " +imageTypeString );
			return;
		}
		ImageBlob newImageBlob = null;
		try {
			ServletFileUpload upload = new ServletFileUpload();
			//res.setContentType("text/plain");

			FileItemIterator iterator = upload.getItemIterator(req);
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();
				
				if (!item.isFormField()) {
				
					Blob imageBlob = new Blob(IOUtils.toByteArray(stream));
					BlobKey blobKey = blobService.writeToBlobstore("image/jpeg", fileName+".jpg", imageBlob.getBytes());
					BlobKey blobKeyOrginalSize = blobService.writeToBlobstore("image/jpeg", fileName+"MENUOrginalSize.jpg", imageBlob.getBytes());
					
					newImageBlob = new ImageBlob(restId, blobKey.getKeyString(), new Date(), imageType);
					newImageBlob.setBlobKeyOriginalSize(blobKeyOrginalSize.getKeyString());
					
					storService.saveImageBlob(newImageBlob);
					Map<String, ImageBlob> returnedMap =  blobService.cropImage(newImageBlob, 0, 0, 1, 1);
					ImageBlob tmpImageBlob = returnedMap.get("new");
					if(tmpImageBlob != null) newImageBlob.setBlobKey(tmpImageBlob.getBlobKey());
					else{
						log.warning("FileUploadServer::doPost: new blobKey not defined" );
						return;
					}
					
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
		
		resp.getWriter().print(gson.toJson(newImageBlob.getBlobKey()));
		if (jsonp != null) {
			resp.getWriter().print(")");
		}
		resp.flushBuffer();
		
	}
}
