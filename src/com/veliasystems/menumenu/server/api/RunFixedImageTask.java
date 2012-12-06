package com.veliasystems.menumenu.server.api;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class RunFixedImageTask extends HttpServlet {
	
	private static final long serialVersionUID = 2566472678914274709L;
	
	private StoreService storeService = new StoreServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Queue queue = QueueFactory.getQueue(R.FIX_IMAGE_QUEUE_NAME);
		queue.add(withUrl("/fixedImagesTask").param("token", "a1b2c3"));

		//queue.add(withUrl("/fixedImagesTask?token=a1b2c3&restaurantId=5791926").param("token", "a1b2c3").param("cityIdFrom", cityIdFrom).param("cityIdTo", cityIdTo).param("emailAddress", email));
	    
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
}
