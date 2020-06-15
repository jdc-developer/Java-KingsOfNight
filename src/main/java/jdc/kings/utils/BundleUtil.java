package jdc.kings.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class BundleUtil {
	
	private static final String bundleName = "kings-of-night";

	protected static ClassLoader getCurrentClassLoader(Object defaultObject) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = defaultObject.getClass().getClassLoader();
		}
		return loader;
	}

	public static String getMessageResourceString(String key, Locale locale) {
		String text = null;
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
		try {
			text = bundle.getString(key);
		} catch (MissingResourceException e) {
			text = "key " + key + " not found";
		}
		return text;
	}
}
