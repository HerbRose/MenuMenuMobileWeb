package com.veliasystems.menumenu.server.repairServlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class SelectAgainAllMenuImages extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5641741819577631401L;
	
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
		
		
		List<Restaurant> rests = storeService.loadRestaurants();
		
		for (Restaurant restaurant : rests) {	
			if(restaurant.getMainMenuImageString() != null && !restaurant.getMainMenuImageString().isEmpty()){
				if(restaurant.getMainMenuImageString().contains("=")){
					String blobKey = restaurant.getMainMenuImageString().split("=")[1];
					if(blobKey!=null){
						ImageBlob img = storeService.getImageBlobByBlobKey(blobKey);
						if(img!=null){
							storeService.setMainImage(img);
						}
					}
				}
			}
		}
		
	}
	
	
	
}
