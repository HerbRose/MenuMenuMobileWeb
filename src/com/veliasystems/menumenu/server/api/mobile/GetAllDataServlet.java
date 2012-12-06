package com.veliasystems.menumenu.server.api.mobile;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class GetAllDataServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(GetAllDataServlet.class.getName());
	private StoreServiceImpl storeService = new StoreServiceImpl();
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

		String login = req.getParameter("login");
    	String password = req.getParameter("password");
		if (login==null || login.isEmpty() ){
			return;
		}
		if (password==null || password.isEmpty() ){
			return;
		}
		
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		if(jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
		 Map<String, Object> tesponseMap = storeService.getAllData(login, password);

		resp.getWriter().print(gson.toJson(tesponseMap));
		
		if(jsonp != null) {
			resp.getWriter().print(")");
		}
		resp.flushBuffer();
	}
}
