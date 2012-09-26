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

public class GetRestaurantsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 2566472678914274709L;
	
	private StoreService storeService = new StoreServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String token = req.getParameter("token");
		
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			resp.getWriter().println("Wrong Token");
			resp.flushBuffer();
			return;
		}
		
		String cityIdString = req.getParameter("cityId");
		Integer cityId = 0;
		
		try {
			cityId = Integer.parseInt(cityIdString);
		} catch (NumberFormatException e) {
			resp.getWriter().println("cityId must be Integer");
			resp.flushBuffer();
			return;
		}
		
		
		List<String> cities = storeService.loadCities();
		if (cityId>=cities.size() || cityId<0) {
			resp.getWriter().println("Wrong cityId");
			resp.flushBuffer();
			return;
		}
		
		String city = cities.get(cityId);
		
		List<Restaurant> rests = storeService.loadRestaurants(city);
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		if(jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
		
		List< Map<String,String> > attributes = new ArrayList< Map<String,String>>();
		
		
		for (Restaurant r : rests) {
			Map<String,String> map = new HashMap<String,String>();
			map.put( "id", ""+ r.getId());
			map.put( "name", r.getName());
			map.put( "city", r.getCity());
			map.put( "district", r.getDistrict());
			map.put( "address", r.getAddress());
			map.put( "logoImage", r.getMainBoardImage());
			map.put( "menuImage", r.getMainImage());
			map.put( "profileImage", r.getMainProfileImage());
			map.put( "lat", "" + r.getLat());
			map.put( "lng", "" + r.getLng());
			
			attributes.add(map);
		}
		
		resp.getWriter().print(gson.toJson(attributes));
		
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
