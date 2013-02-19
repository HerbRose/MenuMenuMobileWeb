package com.veliasystems.menumenu.server.api;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.log.*;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;


// Get request logs along with their app log lines and display them 5 at		
// a time, using a Next link to cycle through to the next 5.
public class GetLogsServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		// We use this to break out of our iteration loop, limiting record
		// display to 5 request logs at a time.
		int limit = 50;

		// This retrieves the offset from the Next link upon user click.
		String offset = req.getParameter("offset");
		
		
		String timeStart = req.getParameter("timeStart");
		long secondsStart = 0;
		if(timeStart!=null && !timeStart.isEmpty()){
			try{
				secondsStart = getSeconds(timeStart);
			}catch(NumberFormatException e){
				secondsStart = -1;
			}
		}
		
		
		String timeEnd = req.getParameter("timeEnd");
		long secondsEnd = 0;
		if(timeEnd!=null && !timeEnd.isEmpty()){
			try {
				secondsEnd = getSeconds(timeEnd);
			} catch (NumberFormatException e) {
				secondsEnd = -1;
			}
		}
		// We want the App logs for each request log
		LogQuery query = LogQuery.Builder.withDefaults();
//		query.includeAppLogs(true);

		// Set the offset value retrieved from the Next link click.
		if (offset != null) {
			query.offset(offset);
		}
		
		Calendar cal = Calendar.getInstance();
		if(secondsEnd > 0){
			query.endTimeMillis(secondsEnd * 1000); //secondsEnd is the earliest request completion or last-update time that results should be fetched for, in SECONDS since the Unix epoch. If null then no requests will be excluded for ending too long ago.
			cal.setTimeInMillis(new Date().getTime() - (secondsEnd*1000) );
			writer.println("<br />START TIME: "+ cal.getTime().toString() + " ("+secondsEnd+")<br />");
		}
		
		if(secondsStart > 0){
			query.startTimeMillis(secondsStart * 1000);//secondsStart is the latest request completion or last-update time that results should be fetched for, in SECONDS since the Unix epoch. If null then no requests will be excluded for ending too recently.
			cal.setTimeInMillis(new Date().getTime() - (secondsStart*1000) );
			writer.println("<br />END TIME: "+  cal.getTime().toString() + " ("+secondsStart+")<br />");
		}
		
		int i = 0;

		// Display a few properties of each request log.
		for (RequestLogs record : LogServiceFactory.getLogService().fetch(query)) {
			
			String resurce =  record.getResource();
			String regex = ".*getAllData.*";
//			if(!resurce.matches(".*getAllData.*")){
//				continue;
//			}
			
			writer.println("<br />REQUEST LOG <br />");
			//Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(record.getStartTimeUsec() / 1000);

			writer.println("IP: " + record.getIp() + "<br />");
			writer.println("Method: " + record.getMethod() + "<br />");
			writer.println("Resource " + resurce);
			writer.println(String.format("<br />Date: %s", cal.getTime().toString()));
			writer.println("<br />");

//			// Display all the app logs for each request log.
//			for (AppLogLine appLog : record.getAppLogLines()) {
//				writer.println("<br />" + "APPLICATION LOG" + "<br />");
//				Calendar appCal = Calendar.getInstance();
//				appCal.setTimeInMillis(appLog.getTimeUsec() / 1000);
//				writer.println(String.format("<br />Date: %s", appCal.getTime()
//						.toString()));
//				writer.println("<br />Level: " + appLog.getLogLevel()
//						+ "<br />");
//				writer.println("Message: " + appLog.getLogMessage()
//						+ "<br /> <br />");
//			} // for each log line

			if (++i >= limit) {
				break;
			}
		} // for each record

	} // end doGet

	int[] timeMultiplier = {60, 60*60, 60*60*24, 60*60*24*7}; //0 - minutes to seconds, 1 - hours to seconds, days to seconds, weeks to seconds,
	
	private long getSeconds(String timeStart) throws NumberFormatException{
		
		long timeSeconds = 0;
		String[] timeArray = timeStart.split(":");
		
		int arraySize = timeArray.length;
		int currentMultiplerPosition = 0; 
		for (int i = arraySize-1; i >= 0 ; i--, currentMultiplerPosition++) {
			String currentValueString = timeArray[i];
			int currentTimeValue = Integer.parseInt(currentValueString);
			int currentMultiplier = currentMultiplerPosition > timeMultiplier.length-1 ? -1 : timeMultiplier[currentMultiplerPosition] ;
			if(currentMultiplier > 0)
				timeSeconds += currentTimeValue*currentMultiplier;
			
		}
		
		return timeSeconds;
	}
} // end class