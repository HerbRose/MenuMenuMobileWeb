package com.veliasystems.menumenu.server.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.googlecode.objectify.Query;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.client.entities.BackUpBlobKey;
import com.veliasystems.menumenu.server.BlobServiceImpl;
import com.veliasystems.menumenu.server.DAO;
import com.veliasystems.menumenu.server.EmailServiceImpl;


public class RestoreTask extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2317294957266920644L;
	private static final Logger log = Logger.getLogger(RestoreTask.class.getName()); 
	private EmailServiceImpl emailService = new EmailServiceImpl();
	private DAO dao = new DAO();


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String token = req.getParameter("token");
		
		if (token==null || token.isEmpty() || !token.equalsIgnoreCase(R.TOKEN)) {
			resp.getWriter().println("Wrong Token");
			resp.flushBuffer();
			return;
		}
		
		BlobServiceImpl BSI = new BlobServiceImpl();

		
		if (getBackUpAddress()!=null||getLastListOfBUBK()!=null){BSI.restoreBackuoDB(getBackUpAddress(), getLastListOfBUBK());}
			else resp.getWriter().print("Brak wpisu w bazie danych");	
	}
	
	private List<BackUpBlobKey> getLastListOfBUBK(){
		Query <BackUpBlobKey>bubkQuery = dao.ofy().query(BackUpBlobKey.class);
			List<BackUpBlobKey> BUBK = bubkQuery.order("-timeInMiliSec").list();
		try{	
		return BUBK;
		}catch(IndexOutOfBoundsException e){
			log.severe("Entie empty ");
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
			log.severe("Illegal Argument ");
		}
		return null;
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
