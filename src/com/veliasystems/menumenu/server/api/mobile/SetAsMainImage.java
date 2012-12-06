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

public class SetAsMainImage extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(SetAsMainImage.class.getName());
	
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
		
		String restId = req.getParameter("restaurantId");
    	String imageType = req.getParameter("imageType");
    	String blobKey = req.getParameter("blobKey");
		if (restId==null || restId.isEmpty() || imageType==null || imageType.isEmpty() || blobKey==null || blobKey.isEmpty() ){
			log.warning("SetAsMainImage::doGet: restId or imageType is NULL or empty");
			resp.getWriter().println("Wrong parametr");
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
		ImageType myImageType = null;
		try {
			myImageType = ImageType.valueOf(imageType);
        } catch (IllegalArgumentException e) {
        	log.warning("SetAsMainImage::doGet: Invalid value for imageType: " + imageType);
        	resp.getWriter().print(gson.toJson("Invalid value for imageType: " + imageType));
    		if(jsonp != null) {
    			resp.getWriter().print(")");
    		}
    		resp.flushBuffer();
        }
		
		
		ImageBlob imageBlob = new ImageBlob(restId, blobKey, new Date(), myImageType); // created temporary
		
		Restaurant restaurant = storeService.setMainImage(imageBlob);
		
		resp.getWriter().print(gson.toJson(restaurant));
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
