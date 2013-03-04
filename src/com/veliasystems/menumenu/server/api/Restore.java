package com.veliasystems.menumenu.server.api;


import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;


import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.veliasystems.menumenu.client.R;
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
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		Queue queue = QueueFactory.getDefaultQueue();
	    queue.add(withUrl("/tasks/RestoreTask").param("token", "a1b2c3"));
		
		
		
		String jsonp = req.getParameter("jsonp");
		
		
	
		if(jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		resp.getWriter().print("Restore Complited");
		if(jsonp != null) {
			resp.getWriter().print(")");
		}
		resp.flushBuffer();
	
	
}
}