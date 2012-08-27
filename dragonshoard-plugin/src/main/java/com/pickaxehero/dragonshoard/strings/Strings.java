package com.pickaxehero.dragonshoard.strings;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class provides string which need NOT to be internationalized
 * 
 * @author Pickaxehero
 *
 */
public class Strings {
	private static final String BUNDLE_NAME = 
		"com.pickaxehero.dragonshoard.strings.strings";

	private static final ResourceBundle RESOURCE_BUNDLE = 
		ResourceBundle.getBundle(BUNDLE_NAME);

	private Strings() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
