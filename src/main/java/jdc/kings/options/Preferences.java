package jdc.kings.options;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import jdc.kings.input.Key;

public class Preferences implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<Key> keys;
	private Locale locale;
	
	public List<Key> getKeys() {
		return keys;
	}
	
	public void setKeys(List<Key> keys) {
		this.keys = keys;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
