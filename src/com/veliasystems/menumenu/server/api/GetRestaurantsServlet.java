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
		String test = req.getParameter("test");
		boolean isTest = false;
		if (test!=null) isTest = true;
		
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			resp.getWriter().println("Wrong Token");
			resp.flushBuffer();
			return;
		}
		
		
		if (isTest) {
			doTest(req,resp);
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
			map.put( "logoImage", (r.getMainLogoImage()!=null) ? (r.getMainLogoImage().getImageUrl()) : "EMPTY");
			map.put( "menuImage", (r.getMainMenuImage()!=null) ? (r.getMainMenuImage().getImageUrl()) : "EMPTY");
			map.put( "profileImage", (r.getMainProfileImage()!=null) ? (r.getMainProfileImage().getImageUrl()) : "EMPTY");
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
	
	
	private void doTest( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		if(jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
		String limit = req.getParameter("limit");
		int n = 10;
		if (limit!= null) {
			try {
				n = Integer.parseInt(limit);
			} catch (NumberFormatException e) {
				n = 10;
			}
		}
		
		List< Map<String,String> > attributes = new ArrayList< Map<String,String>>();
		
		String[] names = { "Pimiento", "Warsztat", "Babcia Malina", "Cosa Nostra", "Ptaszyl", "La Scandale", "El Torro", "Rio Grande", "ChinTin" };
		String[] addresses = { "Ptasia 5", "Malinowa 45", "Borowkowa 2", "Zielona 55", "Lotnikow 4", "Debow 55" };
		
		String[] profileImages = { "http://www.shangrestaurant.com/wp-content/uploads/home-restaurant.jpg", 
							"http://alltopmovies.net/wp-content/uploads/2009/06/restaurant.jpg",
							"http://www.nuffy.net/pics/cool/bizarre-restaurants/restaurant_undersea.jpg",
							"http://www.hoteles-silken.com/hotel-berlaymont-brussels/files/2010/09/Restaurant-LObjectif.jpg",
							"http://s3.amazonaws.com/sfb111/story_xlimage_2010_08_R5362_Inwood_Restaurant_Grades.jpg",
							"http://parisparfait.typepad.com/paris_parfait/images/2008/08/31/chinese_restaurant_2.jpg" };
		
		String[] menuImages = { "http://graphixshare.com/uploads/posts/2011-05-07/1304774534_kedsbf6ujfqpmv5.jpeg",
								"http://www.mightysweet.com/mesohungry/wp-content/uploads/2010/08/02-Ukrainian-East-Village-Restaurant-menu-board.jpg",
								"http://www.mightysweet.com/mesohungry/wp-content/uploads/2011/05/05-La-Norte%C3%B1a-Restaurant-menu.jpg",
								"http://kookykitsch.com/Portals/0/productimages/2790_01c68.jpg",
								"http://www.tannery.ie/assets/images/tasting.JPG",
								"http://farm2.staticflickr.com/1329/665553384_6229ab7047_z.jpg?zz=1" };
								
		String[] logoImages = { "http://www.g-renderings.com/images/Logos/400X250/Corporate/BellavinoLogo.jpg",
								"http://www.hayashi.com.au/logo.gif",
								"http://www.freelanceforum.org/files/imagecache/screenshot_full/portfolio/Sheryl%20Nelson/1-ID-Zazu.gif",
								"http://m.artician.com/pu/OUHFIQYZPWRDLF3Q2CFR4UYGTUPDH3V5.preview.png",
								"http://eatmoredrinkmore.com/wp-content/uploads/2011/12/Toki-Underground-Logo.jpg",
								"http://www.cityhindusnetwork.org.uk/wp-content/uploads/2011/04/CHN-Networking-Dinner-logo-v2.jpg" };
		
		
		for (int i=0; i<n; i++) {
			Map<String,String> map = new HashMap<String,String>();
			map.put( "id", ""+ i);
			map.put( "name", names[(int) Util.getRandom(names.length)]);
			map.put( "city", "Krakow");
			map.put( "district", "District " + i);
			map.put( "address", addresses[(int) Util.getRandom(addresses.length)]);
			map.put( "logoImage", logoImages[(int) Util.getRandom(logoImages.length)]);
			map.put( "menuImage", menuImages[(int) Util.getRandom(menuImages.length)]);
			map.put( "profileImage", profileImages[(int) Util.getRandom(profileImages.length)]);
			map.put( "lat", "" + Util.getRandom(180));
			map.put( "lng", "" + Util.getRandom(180));
			
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
