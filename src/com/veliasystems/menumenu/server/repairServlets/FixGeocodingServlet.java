package com.veliasystems.menumenu.server.repairServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.veliasystems.menumenu.client.R;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class FixGeocodingServlet extends HttpServlet {
	
	private static final long serialVersionUID = 2566472678914274709L;
	
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
		
		storeService.fixGeocoding();
		
		resp.getWriter().write("Done");
		resp.flushBuffer();
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
}
