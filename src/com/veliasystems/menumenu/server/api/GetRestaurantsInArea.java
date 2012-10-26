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
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class GetRestaurantsInArea extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private StoreService storeService = new StoreServiceImpl();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		doGet(req, resp);
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		String token = req.getParameter("token");

		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			resp.getWriter().println("Wrong Token");
			resp.flushBuffer();
			return;
		}
		
		List<Restaurant> restaurantList = storeService.loadRestaurants();
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		double latDevice = Double.valueOf(req.getParameter("lat"));
		double lonDevice = Double.valueOf(req.getParameter("lon"));
	
		double distance = 1;
		

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		if(jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
		List< Map<String,String> > attributes = new ArrayList< Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		
		for (Restaurant restaurant : restaurantList) {
			double restLat = Double.valueOf(restaurant.getLat());
			double restLon = Double.valueOf(restaurant.getLng());			
			if(restaurant.isVisibleForApp()){
				if(distFrom(latDevice, lonDevice, restLat, restLon) <= distance){				
					map.put( "id", ""+ restaurant.getId());
					map.put( "name", restaurant.getName());
					map.put( "city", restaurant.getCity());
					map.put( "district", restaurant.getDistrict());
					map.put( "address", restaurant.getAddress());
					map.put( "logoImage", (restaurant.getMainLogoImageString()!=null) ? addHostToUrl(restaurant.getMainLogoImageString()) : "EMPTY");
					map.put( "menuImage", (restaurant.getMainMenuImageString()!=null) ? addHostToUrl(restaurant.getMainMenuImageString()) : "EMPTY");
					map.put( "profileImage", (restaurant.getMainProfileImageString()!=null) ? addHostToUrl(restaurant.getMainProfileImageString()) : "EMPTY");
					map.put( "lat", "" + restaurant.getLat());
					map.put( "lng", "" + restaurant.getLng());
					attributes.add(map);	
				}
				
			}
		}
				
			resp.getWriter().print(gson.toJson(attributes));
			if(jsonp != null) {
				resp.getWriter().print(")");
			}
			resp.flushBuffer();
		
	}
	
	private String addHostToUrl( String url ) {
		if (url.startsWith("http://")) return url;
		return getHostName() + url;
	}
	
	public static final String getHostName() {
    	String hostUrl; 
        String environment = System.getProperty("com.google.appengine.runtime.environment");
        if (environment.equalsIgnoreCase("Production")) {
            String applicationId = System.getProperty("com.google.appengine.application.id");
            String version = System.getProperty("com.google.appengine.application.version");
            //hostUrl = "http://"+version+"."+applicationId+".appspot.com/";
            hostUrl = "http://"+applicationId+".appspot.com/"; // without version
        } else {
            hostUrl = "http://localhost:8888";
        }
        return hostUrl;
    }
	
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 6371.0072;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    return dist;
	    }
}