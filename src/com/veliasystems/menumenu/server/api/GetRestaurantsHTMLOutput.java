package com.veliasystems.menumenu.server.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class GetRestaurantsHTMLOutput extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3943140271845652503L;
	
	private StoreServiceImpl storeService = new StoreServiceImpl();
	private BlobServiceImpl blobService = new BlobServiceImpl();

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
		
		String cityIdString = req.getParameter("cityId");
		Long cityId;
		
		try {
			cityId = Long.parseLong(cityIdString);
		} catch (NumberFormatException e) {
			resp.getWriter().println("cityId must be Integer");
			resp.flushBuffer();
			return;
		}
		
		City city = storeService.findCity(cityId);
		
		if (city == null) {
			resp.getWriter().println("Wrong cityId");
			resp.flushBuffer();
			return;
		}
		
		resp.setContentType("text/html; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		resp.setDateHeader("Expires", 0);
		resp.addHeader("Cache-Control", "no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0");
		//resp.addHeader("Keep-Alive", "timeout=3, max=993");
		
		List<Restaurant> rests = storeService.loadRestaurantsForWeb(cityId);
		
		String output = "";
		
		String slider = "<ul id='slider'>";
		String sliderEnd = "</ul>";
		
		String thumb = "<ul id=\'thumb'>";
		String thumbEnd = "</ul>";
		
		String liStart = "<li class='liItem'";
		String liStartLastImage = "<li class='liItem lastImage'";
		String liIdStart = " id=";
		String liIdEnd = ">";
		String liEnd = "</li>";
		
		String imgStart = "<img src='";
		String imgEnd = "' />";
		
		String imgLogoEnd = "' class='logoImage' />";
		String imgProfileEnd = "' class='profileImage' />";
		String imgMenuEnd = "' class='menuImage' />";
		
		String liThumbStart = "<li class='thumbLi'>";
		String liThumbEnd = "</li>";
		
		String aHrefStart = "<a href='";
		String aHrefEnd1 = "'>";
		String aHrefEnd2 = "</a>";
		
		 String imageContent = "";
		 String thumbContent = "";
		
		 int counter = 1;
		 
		 ImageBlob emptyDefoultMenu;
			if(blobService.getDefaultEmptyMenu().isEmpty()){
				emptyDefoultMenu = new ImageBlob();
			}else{
				emptyDefoultMenu = blobService.getDefaultEmptyMenu().get(0);
			}
		 
			
			List<Restaurant> properRests = new ArrayList<Restaurant>();
			
			for (Restaurant restaurant : rests) {
				if(restaurant.isVisibleForApp()){
					
					String menuImage = "";
			    	String logoImage = "";
			    	String profileImage = "";
					
					if(restaurant.getMainMenuImageString()!=null && !restaurant.getMainMenuImageString().isEmpty()){
						menuImage = addHostToUrl(restaurant.getMainMenuImageString());					
					}else{
						menuImage = (emptyDefoultMenu != null ? addHostToUrl(emptyDefoultMenu.getImageUrl()):"EMPTY" );
					}
			    	
					if(restaurant.getMainLogoImageString()!=null && !restaurant.getMainLogoImageString().isEmpty()){
						logoImage = addHostToUrl(restaurant.getMainLogoImageString());
					} else {
						logoImage = "EMPTY";
					} 
					
					if(restaurant.getMainProfileImageString()!=null && !restaurant.getMainProfileImageString().isEmpty()){
						profileImage = addHostToUrl(restaurant.getMainProfileImageString());
					} else {
						profileImage = "EMPTY";
					}
					
					if(menuImage.equalsIgnoreCase("EMPTY") || logoImage.equalsIgnoreCase("EMPTY") || profileImage.equalsIgnoreCase("EMPTY")){
						continue;
					}
					properRests.add(restaurant);
				}
			}
			
		for (Restaurant restaurant : properRests) {
//			if(restaurant.isVisibleForApp()){
				String restuarantName = restaurant.getName();
		    	String address = restaurant.getAddress();
		    	String phone = restaurant.getPhoneRestaurant();
		    	List<String> openHours = restaurant.getOpenHours();
		    	String MONDAY = "";
		    	String TUESDAY = "";
		    	String WEDNESDAY = "";
		    	String THURSDAY = "";
		    	String FRIDAY = "";
		    	String SATURDAY = "";
		    	String SUNDAY = "";
		    	String opeString ="";
		    	if(openHours != null){
		    		if(!openHours.isEmpty()){
			    		MONDAY = "Monday: " + openHours.get(0);
			    		TUESDAY = " | Tuesday: " + openHours.get(1);
			    		WEDNESDAY = " | Wednesday " + openHours.get(2);
			    		THURSDAY = " | Thursday " + openHours.get(3);
			    		FRIDAY = " | Friday " + openHours.get(4);
			    		SATURDAY = " | Saturday " + openHours.get(5);
			    		SUNDAY = " | Sunday " + openHours.get(6);
			    		opeString +=MONDAY + TUESDAY + WEDNESDAY + THURSDAY + FRIDAY + SATURDAY + SUNDAY;
			    	}
		    	}
		    	
		     	
		    	if (!phone.isEmpty()) {
					String phoneCopy = phone;
					phone = "<br /> Phone number: " + phoneCopy;
				}
				
		    	String menuImage = "";
		    	String logoImage = "";
		    	String profileImage = "";
		    	
				if(restaurant.getMainMenuImageString()!=null && !restaurant.getMainMenuImageString().isEmpty()){
					menuImage = addHostToUrl(restaurant.getMainMenuImageString());					
				}else{
					menuImage = (emptyDefoultMenu != null ? addHostToUrl(emptyDefoultMenu.getImageUrl()):"EMPTY" );
				}
		    	
				if(restaurant.getMainLogoImageString()!=null && !restaurant.getMainLogoImageString().isEmpty()){
					logoImage = addHostToUrl(restaurant.getMainLogoImageString());
				} else {
					logoImage = "EMPTY";
				} 
				
				if(restaurant.getMainProfileImageString()!=null && !restaurant.getMainProfileImageString().isEmpty()){
					profileImage = addHostToUrl(restaurant.getMainProfileImageString());
				} else {
					profileImage = "EMPTY";
				}
				
//				if(menuImage.equalsIgnoreCase("EMPTY") || logoImage.equalsIgnoreCase("EMPTY") || profileImage.equalsIgnoreCase("EMPTY")){
//					continue;
//				}
			
				
				if(!(counter < properRests.size())){
					imageContent += liStartLastImage;
				} else {
					imageContent += liStart;
				}
				imageContent += liIdStart + counter + liIdEnd;
				imageContent += "<div class='wrapper'>";
				
				imageContent += "<div class='titlePanel'>" + restuarantName + "<div class='address'>" + address + " " + city.getCity() + phone + "<br />" + opeString +  "</div>";
				imageContent += "</div>";
				imageContent+= imgStart + profileImage + imgProfileEnd;
				imageContent+= imgStart + logoImage +  imgLogoEnd;
				imageContent+= imgStart + menuImage + imgMenuEnd;
				imageContent += liEnd;
			
				imageContent += "</div>";
//				thumbContent += liThumbStart + aHrefStart + "#" + counter + aHrefEnd1 + imgStart + logoImage + imgEnd + aHrefEnd2 + liThumbEnd;
				
//				if(!(counter < properRests.size())){
//					thumbContent += liThumbStart + "<div class='imgThumbWrapper' id='lastImage'>" +  aHrefStart + "#" + counter + aHrefEnd1 + imgStart + logoImage + imgEnd + aHrefEnd2 +"</div>" +liThumbEnd;
//				} else {
					thumbContent += liThumbStart + "<div class='imgThumbWrapper'>" +  aHrefStart + "#" + counter + aHrefEnd1 + imgStart + logoImage + imgEnd + aHrefEnd2 +"</div>" +liThumbEnd;
//				}
				
				
				counter++;
		
//			}
		}
		
		slider += imageContent + sliderEnd;
		thumb += thumbContent + thumbEnd;

		output += slider + thumb;
		
		resp.getWriter().write(output);
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
}
