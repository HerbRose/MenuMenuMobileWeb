package com.veliasystems.menumenu.server.api;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.veliasystems.menumenu.client.R;

public class GetUploadUrl extends HttpServlet {

	private static final Logger log = Logger.getLogger(GetUploadUrl.class.getName());
	
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

		String restId = req.getParameter("restaurantId");
    	String imageType = req.getParameter("imageType");
		if (restId==null || restId.isEmpty() || imageType==null || imageType.isEmpty()){
			//System.out.println("BlobUploadServlet::doPost: restId or imageType is NULL||empty");
			log.warning("BlobUploadServlet::doPost: restId or imageType is NULL||empty");
//			res.getWriter().println("BlobUploadServlet::doPost: restId is NULL");
//			res.getWriter().flush();
			return;
		}
		
		String url = BlobstoreServiceFactory.getBlobstoreService()
				.createUploadUrl("/blobUpload?restId=" + restId + "&imageType=" + imageType);
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String jsonp = req.getParameter("jsonp");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		if(jsonp != null) {
			resp.getWriter().print(jsonp + "(");
		}
		
		

		resp.getWriter().print(gson.toJson(url));
		
		if(jsonp != null) {
			resp.getWriter().print(")");
		}
		resp.flushBuffer();
	}
}
