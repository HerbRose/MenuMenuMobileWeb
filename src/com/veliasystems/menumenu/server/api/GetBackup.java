package com.veliasystems.menumenu.server.api;


import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.util.resources.CalendarData;

import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.icu.util.Calendar;
import com.sun.tools.javac.util.Convert;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.BackUpBlobKey;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.ImageType;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.DAO;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class GetBackup extends HttpServlet {

	private static final Logger log = Logger.getLogger(GetBackup.class.getName());
	private StoreServiceImpl storService = new StoreServiceImpl();
	private DAO dao = new DAO();
	
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String token = req.getParameter("token");
		
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			resp.getWriter().println("Wrong Token");
			resp.flushBuffer();
			return;
		}
		
		BlobServiceImpl BSI = new BlobServiceImpl();
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
	
		if(jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
//		date using in name of backup file 
		long day = new Date().getDate();
		long hour = new Date().getHours()+1;
		long minut = new Date().getMinutes(); 
		long month = new Date().getMonth()+1;
		String today = day+"."+month+" "+hour+":"+minut;
		
//		List of enties
		List <Restaurant>rest = storService.loadbcRestaurants();
		List <User>user = storService.getUsers();
		List <City>cities = storService.loadCitiesEntity();
		List <ImageBlob> imgBlob = storService.loadImageBlob();
		List <BackUpBlobKey> Bubk= storService.loadBUBK();
		
//		map of list 
		HashMap hm = new HashMap();
			hm.put(User.class.getSimpleName(), user);
			hm.put(Restaurant.class.getSimpleName(),rest);
			hm.put(City.class.getSimpleName(),cities);
			hm.put(ImageBlob.class.getSimpleName(), imgBlob);
			hm.put(BackUpBlobKey.class.getSimpleName(), Bubk);

		
		BSI.writeBackupDB("tekst/txt", "Backup "+today, gson.toJson(hm).getBytes());
	

			if(jsonp != null) {
			resp.getWriter().print(")");
		}
		resp.flushBuffer();
				
		
	}
	
}
