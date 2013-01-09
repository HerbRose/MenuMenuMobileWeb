package com.veliasystems.menumenu.server.api.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class PublishRestaurantServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(PublishRestaurantServlet.class.getName());
	
	private StoreServiceImpl storeService = new StoreServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		
		String token = req.getParameter("token");
		
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			resp.getWriter().println("Wrong Token");
			resp.flushBuffer();
			return;
		}
		
		String restaurantIdString = req.getParameter("restaurantId");
    	String isPublishString = req.getParameter("isPublish");

		if (restaurantIdString==null || restaurantIdString.isEmpty() || isPublishString==null || isPublishString.isEmpty() ){
			log.warning("PublishRestaurant::doGet: restaurantIdString or isPublishString parameter is NULL or empty");
			resp.getWriter().println("Wrong parameter");
			resp.flushBuffer();
			return;
		}
		long restaurantId = 0;
		boolean isPublish = false;
		
		try {
			restaurantId = Long.parseLong(restaurantIdString);
		} catch (NumberFormatException e) {
			log.warning("PublishRestaurant::doGet: restaurantIdString parameter is not a number (long required)");
			resp.getWriter().println("Parameter data error");
			resp.flushBuffer();
			return;
		}
		
		if(isPublishString.equalsIgnoreCase("true")){
			isPublish = true;
		}else if(!isPublishString.equalsIgnoreCase("false")){
			log.warning("PublishRestaurant::doGet: isPublishString parameter have to be 'true' or 'false'");
			resp.getWriter().println("Parameter data error");
			resp.flushBuffer();
			return;
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		if(jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
		resp.getWriter().println(storeService.publishRestaurant(restaurantId, isPublish));
		
		
		if(jsonp != null) {
			resp.getWriter().print(")");
		}
		resp.flushBuffer();
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
}
