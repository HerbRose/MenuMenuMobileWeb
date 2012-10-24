package com.veliasystems.menumenu.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.veliasystems.menumenu.client.R;


public class LanguageCombo extends ListBox {

	
	public LanguageCombo() {
		super();
		init();
	}
	
	
	private void init() {
		this.setVisibleItemCount(1);

	    Map<String,Integer> languagesOrder = new HashMap<String,Integer>();
	    languagesOrder.put(R.ENGLISH_CODE, 0);
	    languagesOrder.put(R.FRENCH_CODE, 1);
	    languagesOrder.put(R.POLISH_CODE, 2);

	    this.addItem(R.ENGLISH_NAME, R.ENGLISH_CODE);
	    this.addItem(R.FRENCH_NAME,  R.FRENCH_CODE);
	    this.addItem(R.POLISH_NAME,  R.POLISH_CODE);
	    
	    this.setItemSelected( languagesOrder.get(getCurrentLocale()).intValue() , true);
	    
	    //this.setStyleName("listBoxLang");
	    
	    this.addChangeHandler( new ChangeHandler() {

	        @Override
	        public void onChange(ChangeEvent event)
	        {
	            changeLanguage( getValue( getSelectedIndex() ));
	        }
	    });
	}
	
	
    private native void changeLanguage( String locale )
    /*-{
	    var currLocation = $wnd.location.toString().split("#");
	    currLocation = currLocation[0].split("?");
	    var currLocale = "";
	    if ( ! (locale == "EN" || locale == "en") )
	    {
	    	
	      currLocale = "?locale=" + locale;
	    }
	    
	    //$wnd.location.href = currLocation[0] + currLocale;
	    //$wnd.location.replace(currLocation[0] + currLocale);
	    var tmp = currLocation[0]+= currLocale ;
	    //$wnd.location.href = currLocation[0].replace("#",currLocale + "#");
	    $wnd.location.replace(tmp);
	   
     }-*/;


	public String getCurrentLocale()
	{
	    String ret = Window.Location.getParameter("locale");
	    if (ret==null || ret.isEmpty()) ret = R.ENGLISH_CODE; // default
	    return ret;
	}
	
}
