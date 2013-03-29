package com.veliasystems.menumenu.server.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class GetRestaurantsInArea extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(GetRestaurantsInArea.class.getName());
	private BlobServiceImpl blobService = new BlobServiceImpl();
	private StoreServiceImpl storeService = new StoreServiceImpl();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doGet(req, resp);

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String token = req.getParameter("token");

		if (token == null || token.isEmpty()
				|| !token.equalsIgnoreCase(R.TOKEN)) {
			resp.getWriter().println("Wrong Token");
			resp.flushBuffer();
			return;
		}

		String latDeviceString = req.getParameter("lat");
		String lonDeviceString = req.getParameter("lon");

		if (latDeviceString == null || latDeviceString.isEmpty()) {
			resp.getWriter().println("Wrong latitude");
			resp.flushBuffer();
			return;
		}

		if (lonDeviceString == null || lonDeviceString.isEmpty()) {
			resp.getWriter().println("Wrong lon");
			resp.flushBuffer();
			return;
		}
		
		String isProductionString = req.getParameter("production");
		
		boolean isProduction = true;
			
		if (isProductionString!=null && isProductionString.equalsIgnoreCase("false")) {
				isProduction = false;
		}
		

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");

		double latDevice;
		double lonDevice;

		try {
			latDevice = Double.parseDouble(latDeviceString); // Double.valueOf(latDeviceString);
			lonDevice = Double.parseDouble(lonDeviceString);// Double.valueOf(lonDeviceString);
		} catch (NumberFormatException e) {
			resp.getWriter().println("latitude and lon... must be a number");
			resp.getWriter().println(e.getMessage());
			log.severe(e.getMessage());
			resp.flushBuffer();
			return;
		}
		
		List<Restaurant> restaurantListAll = storeService.getRestaurantsInArea(latDevice, lonDevice);
		List<Restaurant> restaurantList = new ArrayList<Restaurant>();
		
		for (Restaurant restaurant : restaurantListAll) {
			if(restaurant.getLatitude() != 0 && restaurant.getLongitude() != 0){
				
				boolean tmp = isVisible(restaurant.getCityId(), isProduction);
	
				if( tmp && restaurant.isVisibleForApp()){
					restaurantList.add(restaurant);
				}
			}
			
		}

	
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");

		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		if (jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
		List< Map<String,Object> > attributes = new ArrayList< Map<String,Object>>();
		ImageBlob emptyDefoultMenu;
		
		if(blobService.getDefaultEmptyMenu().isEmpty()){
			emptyDefoultMenu = new ImageBlob();
		}else{
			emptyDefoultMenu = blobService.getDefaultEmptyMenu().get(0);
		}
		String emptyDefaultMenuString = emptyDefoultMenu.getImageUrl();
		boolean emptyDefaultMenuExist;
		if(emptyDefoultMenu == null) emptyDefaultMenuExist = false;
		else {
			emptyDefaultMenuString = getBlobKey(emptyDefaultMenuString);
			emptyDefaultMenuExist = checkIfBlobExist(emptyDefaultMenuString);
		}

		for (Restaurant r : restaurantList) {
			if(r.isVisibleForApp()){
					Map<String, Object> map = new HashMap<String, Object>();
					
					map.put("id", "" + r.getId());
					map.put("name", r.getName());
					map.put("city", getCityName(r.getCityId()));
					map.put("district", r.getDistrict());
					map.put("address", r.getAddress());
					map.put("lat", "" + r.getLat());
					map.put("lng", "" + r.getLng());
					map.put("phoneRestaurant", r.getPhoneRestaurant());
					map.put("openHours", r.getOpenHours());
					map.put("menuPublishTime", r.getMenuPublishTimeInMiliSec());
					
					boolean logoImageExist = true;
					boolean menuImageExist = true;
					boolean profileImageExist = true;
					boolean menuImageScreenSizeExist = true;
					boolean menuImageScaleSizeExist = true;
					
					String logoImageString = (r.getMainLogoImageString()!=null) ? r.getMainLogoImageString() : null;
					String menuImageString = (r.getMainMenuImageString()!=null) ?r.getMainMenuImageString() : null;
					String profileImageString = (r.getMainProfileImageString()!=null) ? r.getMainProfileImageString() : null;
					String menuImageScreenString = (r.getMainMenuScreenSizeImageString()!=null) ? r.getMainMenuScreenSizeImageString() : null;
					String menuImageScaleString = (r.getMainMenuScaleSizeImageString()!=null) ? r.getMainMenuScaleSizeImageString() : null;

					logoImageExist = checkIfStringExist(logoImageString); 
					if(logoImageExist){
						logoImageString = getBlobKey(logoImageString);
						logoImageExist = checkIfBlobExist(logoImageString);
					}	
					
					profileImageExist = checkIfStringExist(profileImageString);	
					if(profileImageExist){
						profileImageString = getBlobKey(profileImageString);
						profileImageExist = checkIfBlobExist(profileImageString);
					}
					
					menuImageExist = checkIfStringExist(menuImageString);
					if(menuImageExist){
						menuImageString = getBlobKey(menuImageString);
						menuImageExist = checkIfBlobExist(menuImageString);
					}
					
					menuImageScreenSizeExist = checkIfStringExist(menuImageScreenString);
					if(menuImageScreenSizeExist){
						menuImageScreenString = getBlobKey(menuImageScreenString);
						menuImageScreenSizeExist = checkIfBlobExist(menuImageScreenString);
					}
					
					menuImageScaleSizeExist = checkIfStringExist(menuImageScaleString);
					if(menuImageScaleSizeExist){
						menuImageScaleString = getBlobKey(menuImageScaleString);
						menuImageScaleSizeExist = checkIfBlobExist(menuImageScaleString);
					}

					if(logoImageExist){
						ImageBlob imgBlob = storeService.getImageBlobByBlobKey(logoImageString);
						map.put("logoImage", addHostToUrl("blobServe?blob-key=" + logoImageString));	
						addToMapImageDeatails(map, imgBlob);	
					} else {
						map.put("logoImage", "EMPTY");
						addDefaultImageDetails(map, ImageType.LOGO);
					}
					
					if(profileImageExist){
						ImageBlob imgBlob = storeService.getImageBlobByBlobKey(profileImageString);
						map.put("profileImage", addHostToUrl("blobServe?blob-key=" + profileImageString));
						addToMapImageDeatails(map, imgBlob);
					} else {
						map.put("profileImage", "EMPTY");
						addDefaultImageDetails(map, ImageType.PROFILE);
					}
					
					if(menuImageExist){	
						ImageBlob imgBlob = storeService.getImageBlobByBlobKey(menuImageString);
						map.put("menuImage", addHostToUrl("blobServe?blob-key=" + menuImageString));
						map.put("menuImageDefault", false);
						addToMapImageDeatails(map, imgBlob);
						
						map.put("menuImageScreenSize", (menuImageScreenSizeExist ? addHostToUrl("blobServe?blob-key=" + menuImageScreenString) : 
							addHostToUrl("blobServe?blob-key=" + menuImageString)));
						
						map.put("menuImageScaleSize", (menuImageScaleSizeExist ? addHostToUrl("blobServe?blob-key=" + menuImageScaleString) : 
							addHostToUrl("blobServe?blob-key=" + menuImageString)));

					} else {
						if(emptyDefaultMenuExist){
							map.put("menuImage", addHostToUrl("blobServe?blob-key=" + emptyDefaultMenuString));
							map.put("menuImageScreenSize", addHostToUrl("blobServe?blob-key=" + emptyDefaultMenuString));
							map.put("menuImageScaleSize", addHostToUrl("blobServe?blob-key=" + emptyDefaultMenuString));
							map.put("menuImageDefault", true);
							addToMapImageDeatails(map, emptyDefoultMenu);
						} else {
							map.put("menuImage", "EMPTY");
							map.put("menuImageScreenSize", "EMPTY");
							map.put("menuImageScaleSize", "EMPTY");
							map.put("menuImageDefault", true);
							addDefaultImageDetails(map, ImageType.MENU);
						}
					}	
					attributes.add(map);
				}
			
			}
		
			cityMap.clear();


		resp.getWriter().print(gson.toJson(attributes));
		if (jsonp != null) {
			resp.getWriter().print(")");
		}
		resp.flushBuffer();

	}
	
	private String getBlobKey(String url){
		String[] urlArray = url.split("=");
		if(urlArray.length > 1){
			url = urlArray[1];
		}
		return url;
	}
	private boolean checkIfBlobExist(String url){
		BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
		BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(new BlobKey(url));
		if(blobInfo == null) return false;
		return true;
	}

	private boolean checkIfStringExist(String url){
		if(url == null || url.isEmpty()) return false;
		return true;
	}
	
	private void addToMapImageDeatails(Map<String, Object> map, ImageBlob imageBlob){
		if(imageBlob != null) {
			if(imageBlob.getImageType() != ImageType.EMPTY_MENU){
				map.put(imageBlob.getImageType()+"DateCreate", imageBlob.getDateCreated()+"");
				map.put(imageBlob.getImageType()+"DateCreateInMiliS", imageBlob.getDateCreated().getTime()+"");
				map.put(imageBlob.getImageType()+"Height", imageBlob.getHeight()+"");
				map.put(imageBlob.getImageType()+"Width", imageBlob.getWidth()+"");
			} else {
				map.put(ImageType.MENU+"DateCreate", imageBlob.getDateCreated()+"");
				map.put(ImageType.MENU+"DateCreateInMiliS", imageBlob.getDateCreated().getTime()+"");
				map.put(ImageType.MENU+"Height", imageBlob.getHeight()+"");
				map.put(ImageType.MENU+"Width", imageBlob.getWidth()+"");
			}		
		} else {
			log.warning("image blob is null");
		}
	}
	
	private void addDefaultImageDetails(Map<String, Object> map, ImageType imgType){
		map.put(imgType+"DateCreate", "");
		map.put(imgType+"DateCreateInMiliS", "");
		map.put(imgType+"Height", "");
		map.put(imgType+"Width", "");
	}

	private Map<Long, City> cityMap = new HashMap<Long, City>();
	
	private String getCityName(long cityId){
		
		if(cityMap.containsKey(cityId)){
			return cityMap.get(cityId).getCity();
		}else{
			City city = storeService.loadCity(cityId);
			if(city == null) return "";
			
			cityMap.put(city.getId(), city);
			return cityMap.get(cityId).getCity();
		}
		
	}
	
	private boolean isVisible(long cityId, boolean isProduction){
		
		if(cityMap.containsKey(cityId)){
			return cityMap.get(cityId).isVisable(isProduction);
		}else{
			City city = storeService.loadCity(cityId);
			if(city == null) return false;
			
			cityMap.put(city.getId(), city);
			return cityMap.get(cityId).isVisable(isProduction);
		}
	}
	
	private String addHostToUrl(String url) {
		if (url.startsWith("http://"))
			return url;
		return getHostName() + url;
	}

	public static final String getHostName() {
		String hostUrl;
		String environment = System
				.getProperty("com.google.appengine.runtime.environment");
		if (environment.equalsIgnoreCase("Production")) {
			String applicationId = System
					.getProperty("com.google.appengine.application.id");
			String version = System
					.getProperty("com.google.appengine.application.version");
			// hostUrl = "http://"+version+"."+applicationId+".appspot.com/";
			hostUrl = "http://" + applicationId + ".appspot.com/"; // without
																	// version
		} else {
			hostUrl = "http://localhost:8888";
		}
		return hostUrl;
	}

	public static double distFrom(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 6371;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;
		return dist;
	}

	private double distFromByStreets(double lat1, double lng1, double lat2,
			double lng2) {
		
		String urlString = R.DIRECTIONS_MAPS_URL + "&mode=walking&origin=" + lat1 + ","
				+ lng1 + "&destination=" + lat2 + "," + lng2;

		String json = getJsonFromGoogleMaps(urlString);

		
		Gson gson = new Gson();
		
		/* converting json string to Map*/
	    Type mapOfStringObjectType = new TypeToken<Map<String, Object>>() {}.getType();
	    Map<String,Object> decodedJson = gson.fromJson(json, mapOfStringObjectType);
	   
	    /* getting routes from converted json*/
	    Object routes = decodedJson.get("routes");
	    if(routes == null) return 100000D;
	    
	    /* converting routes Object to List*/	    
	    if(!(routes instanceof List)) return 100000D;
	    List<Object> routesList = (List<Object>) routes;
	    
	    /* getting legs from converted routes*/
	    Object mapObject = findInList("legs", routesList);
	    if(mapObject == null) return 100000D;
	    Object legs = ((Map)mapObject).get("legs");
	    /* converting legs Object to List*/
	    if(legs == null || !(legs instanceof List)) return 100000D;
	    List<Object> legsList = (List<Object>) legs;
	    
	    /* getting distance from converted legsList*/
	    Object distanceObject = findInList("distance", legsList);
	    if(distanceObject == null) return 100000D;
	    Object distance = ((Map) distanceObject).get("distance");
	    
	    /* converting distance Object to Map*/
	    if(distance == null || !(distance instanceof Map)) return 100000D;
	    Map<Object, Object> distanceMap = (Map<Object, Object>) distance;
	    
	    Object distanceValue = distanceMap.get("value");
	    
	    if(distanceValue == null || !(distanceValue instanceof Integer)) return 100000D;

		return (Double) distanceValue;
	}

	private Object findInList(String text, List<Object> list){
		for (Object object : list) {
			if(object instanceof Map && ((Map) object).containsKey(text)){
				return object;
			}
		}
		return null;
	}
	
	private String getJsonFromGoogleMaps(String urlString) {

		URLFetchService urlFetchService = URLFetchServiceFactory
				.getURLFetchService();

		String response = "";
		URL url;
		try {
			url = new URL(urlString);

			FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
			fetchOptions.doNotValidateCertificate();
			fetchOptions.setDeadline(60D);

			HTTPRequest request = new HTTPRequest(url, HTTPMethod.GET,
					fetchOptions);

			HTTPResponse httpResponse = urlFetchService.fetch(request);

			if (httpResponse.getResponseCode() == 200) {
				response = new String(httpResponse.getContent());
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;

	}
}
