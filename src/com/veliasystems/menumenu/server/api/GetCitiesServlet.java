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
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.server.StoreServiceImpl;
/**
 * 
 * @author velia-systems
 * if parameter <code>production</code> is not specified or is empty or is not set to false then servlet returns productions cities
 *
 */
public class GetCitiesServlet extends HttpServlet {
	
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
		
		List<City> cities = storeService.loadCitiesEntity();
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		if(jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
		String isProductionString = req.getParameter("production");
		
		boolean isProduction = true;
			
		if (isProductionString!=null && isProductionString.equalsIgnoreCase("false")) {
				isProduction = false;
		}
		
		List< Map<String,String> > attributes = new ArrayList< Map<String,String>>();
		
		
		for (City city : cities) {
			if( city.isVisable(isProduction)){
				String cityImage = "";
				if( city.getDistrictImageURL() != null && !city.getDistrictImageURL().isEmpty()){
					cityImage = city.getDistrictImageURL();
				}
				Map<String,String> cityPair = new HashMap<String,String>();
				cityPair.put( "name", city.getCity());
				cityPair.put( "id", "" + city.getId());
				cityPair.put("districtImageURL", cityImage.isEmpty()?"":addHostToUrl( city.getDistrictImageURL()) );
				attributes.add(cityPair);
			}
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
