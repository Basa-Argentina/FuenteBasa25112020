package lib;

import java.util.Locale;
import java.util.ResourceBundle;

public class Constants {
	public static final String URL_CONFIG_INI;
	public static final String PATH_CONFIG_INI;

	static{
		URL_CONFIG_INI = displayValue("URL_CONFIG_INI");	
		PATH_CONFIG_INI = displayValue("PATH_CONFIG_INI");	
	}

	static String displayValue(String key) {	
	    ResourceBundle labels = 
	       ResourceBundle.getBundle("lib.Constants",Locale.FRENCH);
	    String value  = labels.getString(key);
	    return value;	
	}

}

