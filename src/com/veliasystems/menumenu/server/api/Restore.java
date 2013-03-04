package com.veliasystems.menumenu.server.api;


import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.util.resources.CalendarData;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.objectify.Query;
import com.ibm.icu.util.Calendar;
import com.sun.tools.javac.util.Convert;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.BackUpBlobKey;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.DAO;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class Restore extends HttpServlet {

	private static final Logger log = Logger.getLogger(Restore.class.getName());
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
//		
//		resp.setCharacterEncoding("UTF-8");
//		resp.setContentType("application/json");
//		
//		String jsonp = req.getParameter("jsonp");
		
		if (getBackUpAddress()!=null||getLastListOfBUBK()!=null){BSI.restoreBackuoDB(getBackUpAddress(), getLastListOfBUBK());}
			else resp.getWriter().print("Brak wpisu w bazie danych");

//			
//		if(jsonp != null) {
//			resp.getWriter().print(")");
//		}
//		resp.flushBuffer();
			
		
	}
	private List<BackUpBlobKey> getLastListOfBUBK(){
		Query <BackUpBlobKey>bubkQuery = dao.ofy().query(BackUpBlobKey.class);
			List<BackUpBlobKey> BUBK = bubkQuery.order("-timeInMiliSec").list();
		try{	
		return BUBK;
		}catch(IndexOutOfBoundsException e){
			log.severe("Null");
		}return null;
	}
	private List<String> getBackUpAddress(){
		Query<BackUpBlobKey> query = dao.ofy().query(BackUpBlobKey.class);
		List <String> getBackUpAddress = new ArrayList <String>();
		
		List <BackUpBlobKey> BUBK = query.order("-timeInMiliSec").list();
		for (BackUpBlobKey backUp : BUBK) {
			
			getBackUpAddress.add(backUp.getAddress());
		}
		
		try {
		return getBackUpAddress;
		}catch (IndexOutOfBoundsException e){
			log.severe("Entie is null");
		}catch (IllegalArgumentException ex){
			log.severe("cos");
		}
		return null;
	}	
	
}
