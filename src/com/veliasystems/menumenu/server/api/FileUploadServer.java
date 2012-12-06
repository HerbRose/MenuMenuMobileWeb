package com.veliasystems.menumenu.server.api;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.server.BlobServiceImpl;

public class FileUploadServer extends HttpServlet {

	private static final Logger log = Logger.getLogger(FileUploadServer.class
			.getName());

	private BlobServiceImpl blobService = new BlobServiceImpl();
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		
		String token = req.getParameter("token");
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			return;
		}

		String restId = req.getParameter("restaurantId");
    	String imageType = req.getParameter("imageType");
    	String imageLenghtString = req.getParameter("imageLenght");
		if (restId==null || restId.isEmpty() || imageType==null || imageType.isEmpty() || imageLenghtString==null || imageLenghtString.isEmpty() ){
			log.warning("FileUploadServer::doPost: restId or imageType is NULL||empty");
			return;
		}
		int imageLenght = -1;
		try{
			imageLenght = Integer.parseInt(imageLenghtString);
		}catch(NumberFormatException e){
			log.warning("FileUploadServer::doPost: NumberFormatException - imageLenght is not a Long" );
			return;
		}
		
		try {
			ServletFileUpload upload = new ServletFileUpload();
			res.setContentType("text/plain");

			FileItemIterator iterator = upload.getItemIterator(req);
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();

				if (item.isFormField()) {
					//log.warning("Got a form field: " + item.getFieldName());
				} else {
					//log.warning("Got an uploaded file: " + item.getFieldName()
					//		+ ", name = " + item.getName());

					// You now have the filename (item.getName() and the
					// contents (which you can read from stream). Here we just
					// print them back out to the servlet output stream, but you
					// will probably want to do something more interesting (for
					// example, wrap them in a Blob and commit them to the
					// datastore).
				
					byte[] fileBytes = new byte[imageLenght];
					if(imageLenght < 0){
						log.warning("FileUploadServer::doPost: imageLenght < 0 ????!!!!" );
						return;
					}else{
						fileBytes = new byte[imageLenght]; // filebytes.length MB buffers
					}
					
					stream.read(fileBytes);
					
					blobService.writeToBlobstore("image/jpeg", "fromIos.jpg", fileBytes);
					
//					while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
//						//res.getOutputStream().write(buffer, 0, len);
//						
//					}
					
					
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
}
