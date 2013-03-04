package com.veliasystems.menumenu.server.tasks;

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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.BackUpBlobKey;
import com.veliasystems.menumenu.client.entities.City;
import com.veliasystems.menumenu.client.entities.ImageBlob;
import com.veliasystems.menumenu.client.entities.Restaurant;
import com.veliasystems.menumenu.client.entities.User;
import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.EmailServiceImpl;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class BackUpTask extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2317294957266920644L;
	private static final Logger log = Logger.getLogger(BackUpTask.class.getName()); 
	private StoreServiceImpl storeService = new StoreServiceImpl();
	private BlobServiceImpl blobService = new BlobServiceImpl();
	private EmailServiceImpl emailService = new EmailServiceImpl();

	private String postscript = "-tester";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String token = req.getParameter("token");
		
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			log.severe("Wrong Token");
			return;
		}

		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		System.out.println("TUTAJ");
		
//		date using in name of backup file 
		Date date = new Date();
		
//		List of enties
		List <Restaurant>rest = storeService.loadbcRestaurants();
		List <User>user = storeService.getUsers();
		List <City>cities = storeService.loadCitiesEntity();
		List <ImageBlob> imgBlob = storeService.loadImageBlob();
		List <BackUpBlobKey> Bubk= storeService.loadBUBK();
		
//		map of list 
		Map<Object, Object> hm = new HashMap<Object, Object>();
			hm.put(User.class.getSimpleName(), user);
			hm.put(Restaurant.class.getSimpleName(),rest);
			hm.put(City.class.getSimpleName(),cities);
			hm.put(ImageBlob.class.getSimpleName(), imgBlob);
			hm.put(BackUpBlobKey.class.getSimpleName(), Bubk);

		
		blobService.writeBackupDB("tekst/txt", "Backup"+date, gson.toJson(hm).getBytes());
		sendMail("matus.1989@homtial.com");
		}		
		



	private void sendMail( String emailAddress ){
		
		String subject = "Message from MenuMenuWeb site" ;
		List<String> emailAddressList = new ArrayList<String>();
		emailAddressList.add(emailAddress);
		
		emailService.sendEmail(emailAddressList, "", "nic", subject);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		doGet(req, resp);
	}
}
