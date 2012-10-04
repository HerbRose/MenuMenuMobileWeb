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
import com.veliasystems.menumenu.client.services.StoreService;
import com.veliasystems.menumenu.server.StoreServiceImpl;

public class ServiceServlet extends HttpServlet {
	
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
		
		
		String fillWithData = req.getParameter("fillWithData");
		if (fillWithData!=null) {
			storeService.fillWithData();
			resp.getWriter().println("DB filled with data");
		}
		
		String clearStore = req.getParameter("clearStore");
		if (clearStore!=null) {
			storeService.clearStore();
			resp.getWriter().println("All restaurants removed from DB");
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
