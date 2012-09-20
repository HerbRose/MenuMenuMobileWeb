package com.veliasystems.menumenu.client;

import java.io.UnsupportedEncodingException;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Focusable;


public class Util
{
	
	public static long getRandom() {
	   return (long) (Math.random() * 999999999);
	}
	
	public static long getRandom(long range) {
		   return (long) (Math.random() * range);
		}
	
    public static boolean isValidEmail(String email)
    {
        if (email == null || email.isEmpty()) return false;
        if (email.matches("[^\\s@]+@[^\\s@]+\\.[a-zA-Z]+"))
                return true;
        else
                return false;
    }

    private static Boolean ie6 = null;
    public static native String getUserAgent()
    /*-{
        return navigator.userAgent.toLowerCase();
    }-*/;

    public static boolean isIE6()
    {
        if(ie6 == null){
            ie6 = getUserAgent().contains("msie 6");
        }
        return ie6;
    }

    public static String escape(String in)
    {
        return in.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
    }

    private static String capitalizeWord(String s)
    {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String capitalize(String s)
    {
        return capitalizeWord(s);
    }

    /**
     * Adds style specified by cssStr to the page style, for example:
     * <pre>
     * Util.refreshStyle(".button{color: red}");
     * </pre>
     * @param cssStr raw CSS text
     */
    public static native void refreshStyle(String cssStr) /*-{
        var style = $wnd.document.createElement("style");
        style.setAttribute("type", "text/css");
        if(style.styleSheet){
                style.styleSheet.cssText = cssStr;
        } else {
                var cssText = $wnd.document.createTextNode(cssStr);
                style.appendChild(cssText);
        }
        var head = $wnd.document.getElementsByTagName("head")[0];
        if(head){
                head.appendChild(style);
        }
    }-*/;

    /**
     * This method solves problem with focusing widgets in getext windows (i hope)
     * @param w
     */
    public static void focusWidgetWorkaround(final Focusable w){
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                Timer t = new Timer(){
                    @Override
                    public void run() {
                        w.setFocus(true);
                    }
                };
                t.schedule(1);
            }
        });
    }

    public static String fixUrl( String url )
    {
        String prefix = "http://";
        if(!url.toLowerCase().startsWith(prefix)) {
            return prefix + url;
        }
        return url;
    }


    public static String toUtf8( String s ) {
		try {
			byte ptext[] = s.getBytes("ISO-8859-1");
			return new String(ptext, "UTF-8"); 
		} catch (UnsupportedEncodingException e) {
			return s;
		} 
    }
    
}
