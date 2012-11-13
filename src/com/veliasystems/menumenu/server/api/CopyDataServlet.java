package com.veliasystems.menumenu.server.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.Util;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class CopyDataServlet extends HttpServlet {
	
	private static final long serialVersionUID = 2566472678914274709L;
	
	private StoreServiceImpl storeService = new StoreServiceImpl();
	private BlobServiceImpl blobService = new BlobServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String token = req.getParameter("token");
		
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			resp.getWriter().println("Wrong Token");
			resp.flushBuffer();
			return;
		}
		
		String cityIdFrom = req.getParameter("cityIdFrom");
		
		if (cityIdFrom==null || cityIdFrom.isEmpty()) {
			resp.getWriter().println("error1");
			resp.flushBuffer();
			return;
		}
		String cityIdTo = req.getParameter("cityIdTo");
		
		if (cityIdTo==null || cityIdTo.isEmpty()) {
			resp.getWriter().println("error2");
			resp.flushBuffer();
			return;
		}
		
		cityIdFrom = req.getParameter("cityIdFrom");
		cityIdTo = req.getParameter("cityIdTo");
		Long cityIdFromLong;
		Long cityIdToLong;
		
		try {
			cityIdFromLong = Long.parseLong(cityIdFrom);
		} catch (NumberFormatException e) {
			resp.getWriter().println("cityIdFrom must be Integer");
			resp.flushBuffer();
			return;
		}
		
		try {
			cityIdToLong = Long.parseLong(cityIdTo);
		} catch (NumberFormatException e) {
			resp.getWriter().println("cityIdTo must be Integer");
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
		
		City city = storeService.loadCitie(cityIdToLong);
		
		if(city == null){
			resp.getWriter().println("cityIdTo not found");
			resp.flushBuffer();
			return;
		}
		
		List<Restaurant> restaurants = storeService.loadRestaurants(cityIdFromLong);
		
		if(restaurants == null){
			resp.getWriter().println("error3 (no restaurant found)");
			resp.flushBuffer();
			return;
		}
		resp.getWriter().println("znalezione restauracje: \n" + restaurants);
		List<Restaurant> restaurantsToSave = new ArrayList<Restaurant>();
		
		for (Restaurant restaurant : restaurants) {
			Restaurant newRestaurant = new Restaurant(restaurant.getName()+"-tester", restaurant.getAddress(), city.getCity());
			newRestaurant.setCityId(cityIdToLong);
			newRestaurant.setDistrict(restaurant.getDistrict());
			newRestaurant.setLat(restaurant.getLat());
			newRestaurant.setLng(restaurant.getLng());
			newRestaurant.setMailRestaurant(restaurant.getMailRestaurant());
			newRestaurant.setMailUser(restaurant.getMailUser());
			newRestaurant.setNameUser(restaurant.getNameUser());
			newRestaurant.setPhoneRestaurant(restaurant.getPhoneRestaurant());
			newRestaurant.setPhoneUser(restaurant.getPhoneUser());
			newRestaurant.setSurnameUser(restaurant.getSurnameUser());
			
			copyImages(restaurant.getLogoImages(), newRestaurant.getId()+"");
			copyImages(restaurant.getMenuImages(), newRestaurant.getId()+"");
			copyImages(restaurant.getProfileImages(), newRestaurant.getId()+"");
			
			restaurantsToSave.add(newRestaurant);
			
		}
		
		storeService.saveRestaurants(restaurantsToSave);
		resp.getWriter().println("\n#######################################\n");
		resp.getWriter().println("zapisane restauracje: \n" + restaurantsToSave);
		if(jsonp != null) {
			resp.getWriter().print(")");
		}
		resp.flushBuffer();
		
	}
	
	
	private void copyImages(List<ImageBlob> logoImages, String newRestaurantId) {
		
		for (ImageBlob imageBlob : logoImages) {
			blobService.copyBlob(imageBlob, newRestaurantId);
		}
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
