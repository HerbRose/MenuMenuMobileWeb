package com.veliasystems.menumenu.server.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.apphosting.api.DeadlineExceededException;
import com.googlecode.objectify.Key;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.EmailServiceImpl;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class FixedImagesTask extends HttpServlet{

	private static final Logger log = Logger.getLogger(FixedImagesTask.class.getName());
	private BlobServiceImpl blobService = new BlobServiceImpl();
	private EmailServiceImpl emailService = new EmailServiceImpl();
    private StoreServiceImpl storeService = new StoreServiceImpl();
	private String emailMessageBody = "";
	private String emailAddress = "mateusz@velia-systems.com";
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String token = req.getParameter("token");
		
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			log.warning("Wrong token");
			return;
		}
		
		int copyCount = 0;
		try{
			
			List<Restaurant> restaurants = new ArrayList<Restaurant>();
			restaurants.addAll(storeService.loadRestaurants());
			List<Key<Restaurant>> keys = storeService.loadRestaurantsId();
			
			if(keys == null){
				emailMessageBody = "FixedImagesTask::doGet - no restaurants keys found";
				sendMail(emailAddress);
				return;
			}
			for (Key<Restaurant> key : keys) {
				List<ImageBlob> list = new ArrayList<ImageBlob>();
				list = blobService.getAllImages(key.getId()+"");
				copyImages(list);
				copyCount+=1;
			}
			
			emailMessageBody = "FixedImagesTask::doGet - finish (copied images in "+ copyCount +" restaurants).";
			sendMail(emailAddress);
		}catch( DeadlineExceededException e){//10 minutes deadline (if used as task)
			Queue queue = QueueFactory.getQueue(R.FIX_IMAGE_QUEUE_NAME);
			queue.purge();
			e.printStackTrace();
			emailMessageBody = "FixedImagesTask::doGet - DeadlineExceededException (copied images in "+ copyCount +" restaurants).";
			sendMail(emailAddress);
			
		}catch (NullPointerException e) { //another
			Queue queue = QueueFactory.getQueue(R.FIX_IMAGE_QUEUE_NAME);
			queue.purge();
			
			emailMessageBody = "FixedImagesTask::doGet - NullPointerException\n .getCause().getMessage():\n"
					+e==null || e.getCause()==null?"-- null.getMessage() --":e.getCause().getMessage()
					+"\ngetLocalizedMessage()\n"
					+e.getLocalizedMessage()	
					+"\n\n(copied images in "+ copyCount +" restaurants)..";
			sendMail(emailAddress);
		}catch (Exception e) { //another
			Queue queue = QueueFactory.getQueue(R.FIX_IMAGE_QUEUE_NAME);
			queue.purge();
			log.log(Level.SEVERE, "", e);
			emailMessageBody = "FixedImagesTask::doGet - Exception\n "
					+"\ngetLocalizedMessage()\n"
					+e.getLocalizedMessage()	
					+"\n\n(copied images in "+ copyCount +" restaurants)..";
			sendMail(emailAddress);
		}

	}

	private void copyImages(List<ImageBlob> logoImages) {		
		for (ImageBlob imageBlob : logoImages) {
			blobService.copyAndDeleteBlob(imageBlob);
		}
	}
	
	private void sendMail( String emailAddress ){
		String subject = "Message from MenuMenuWeb site (menumenu-cms)" ;
		List<String> emailAddressList = new ArrayList<String>();
		emailAddressList.add(emailAddress);
		
		emailService.sendEmail(emailAddressList, "", emailMessageBody, subject);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		doGet(req, resp);
	}
}
