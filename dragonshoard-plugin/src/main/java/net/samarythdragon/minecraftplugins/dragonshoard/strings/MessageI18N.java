package net.samarythdragon.minecraftplugins.dragonshoard.strings;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageI18N {
	private static final String BUNDLE_NAME = "net.samarythdragon.minecraftplugins.dragonshoard.strings.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
		.getBundle(BUNDLE_NAME);

	private MessageI18N() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
