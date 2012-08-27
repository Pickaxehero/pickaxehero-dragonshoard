package com.pickaxehero.dragonshoard.config;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

import com.pickaxehero.dragonshoard.DragonsHoardPlugin;

/**
 * This singleton manages config reading globally and enables different parts of 
 * the plugin to be customized
 * 
 * @author Samaryth Boltscale
 *
 */
public class ConfigManager {

	private FileConfiguration config = null;
	private static ConfigManager cmInstance = null;
	
	private ConfigManager() {
		this.config = DragonsHoardPlugin.instance().getConfig();
	}
	
	private static ConfigManager instance() {
		if(ConfigManager.cmInstance == null) {
			ConfigManager.cmInstance = new ConfigManager();
		}
		
		return cmInstance;
	}
	
	public static String getStringValue(String key) {
		Validate.isTrue(
			ConfigManager.instance().config.contains(key),
			"The config value " + key + " does not seem to exist!"
		);
		
		return ConfigManager.instance().config.getString(key);
	}
	
	public static boolean getBooleanValue(String key) {
		Validate.isTrue(
			ConfigManager.instance().config.contains(key),
			"The config value " + key + " does not seem to exist!"
		);
		return ConfigManager.instance().config.getBoolean(key);
	}
	
	public static int getIntegerValue(String key) {
		Validate.isTrue(
			ConfigManager.instance().config.contains(key),
			"The config value " + key + " does not seem to exist!"
		);
		
		return ConfigManager.instance().config.getInt(key);
	}
	
	/**
	 * Retrieves a config value but makes sure it is in the correct range
	 * 
	 * @param key The config value key 
	 * @param lowerBound The inclusive lower bound of the range
	 * @param upperBound The inclusive upper bound of the range
	 * @return
	 */
	public static int getIntegerValue(String key, int lowerBound, int upperBound) {
		Validate.isTrue((lowerBound <= upperBound), "lowerBound must be less or equal to upperbound!");
		
		int configValue = ConfigManager.getIntegerValue(key);
		
		Validate.isTrue(
			(configValue >= lowerBound) && (configValue <= upperBound), 
			"The config value " + key + " is not within the range " + lowerBound + "..." + upperBound
		);
		
		return configValue;
	}
}
