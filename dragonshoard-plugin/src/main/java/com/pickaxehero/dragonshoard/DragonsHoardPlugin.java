package com.pickaxehero.dragonshoard;

import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;

import com.pickaxehero.dragonshoard.ores.AmethystGem;
import com.pickaxehero.dragonshoard.ores.AmethystOre;
import com.pickaxehero.dragonshoard.ores.RubyGem;
import com.pickaxehero.dragonshoard.ores.RubyOre;
import com.pickaxehero.dragonshoard.ores.SapphireGem;
import com.pickaxehero.dragonshoard.ores.SapphireOre;
import com.pickaxehero.dragonshoard.strings.Strings;

/**
 * The main class of the Dragon's Hoard plugin for 
 * Bukkit and Spout.
 * 
 * @author Pickaxehero
 *
 */
public class DragonsHoardPlugin extends JavaPlugin {

	private static Logger pluginLogger = null;
	
	// Block singletons
	private static RubyOre rubyOre = null;
	private static SapphireOre sapphireOre = null;
	private static AmethystOre amethystOre = null;
	
	// Item singletons
	private static RubyGem rubyGem = null;
	private static SapphireGem sapphireGem = null;
	private static AmethystGem amethystGem = null;
	
	// Self singelton
	private static DragonsHoardPlugin selfSingleton = null;
	
	/**
	 * Constructs this object and initializes its components
	 */
	public DragonsHoardPlugin() {
		super();
		DragonsHoardPlugin.pluginLogger = Bukkit.getLogger();
	}
	
	/**
	 * Singleton like static access method for other tool classes to access this plugin class
	 * WARNING: Wait until this plugin has been fully initialized or this method will return NULL!
	 *  
	 * @return The single instance of this plugin.
	 */
	public static DragonsHoardPlugin instance() {
		Validate.notNull(
			DragonsHoardPlugin.selfSingleton, 
			"DragonsHoardPlugin.instance() called before plugin was initialized!"
		);
		
		return DragonsHoardPlugin.selfSingleton;
	}
	
	/**
	 * Retrieves the current logger so that other plugin components
	 * can use it and log thru the correct plugin logger.
	 * @return The one logger instance to use.
	 */
	public static Logger logger() {
		return DragonsHoardPlugin.pluginLogger;
	}
	
	public RubyOre rubyOreInstance() {
		if(DragonsHoardPlugin.rubyOre == null) {
			DragonsHoardPlugin.rubyOre = new RubyOre(this);
		}
		
		return DragonsHoardPlugin.rubyOre;
	}
	
	public SapphireOre sapphireOreInstance() {
		if(DragonsHoardPlugin.sapphireOre == null) {
			DragonsHoardPlugin.sapphireOre = new SapphireOre(this);
		}
		
		return DragonsHoardPlugin.sapphireOre;		
	}
	
	public AmethystOre amethystOreInstance() {
		if(DragonsHoardPlugin.amethystOre == null) {
			DragonsHoardPlugin.amethystOre = new AmethystOre(this);
		}
		
		return DragonsHoardPlugin.amethystOre;		
	}
	
	public RubyGem rubyGemInstance() {
		if(DragonsHoardPlugin.rubyGem == null) {
			DragonsHoardPlugin.rubyGem = new RubyGem(this);
		}
		
		return DragonsHoardPlugin.rubyGem;
	}
	
	public SapphireGem sapphireGemInstance() {
		if(DragonsHoardPlugin.sapphireGem == null) {
			DragonsHoardPlugin.sapphireGem = new SapphireGem(this);
		}
		
		return DragonsHoardPlugin.sapphireGem;
	}
	
	public AmethystGem amethystGemInstance() {
		if(DragonsHoardPlugin.amethystGem == null) {
			DragonsHoardPlugin.amethystGem = new AmethystGem(this);
		}
		
		return DragonsHoardPlugin.amethystGem;
	}
	
	/**
	 * Call to init all the custom stuff at once
	 */
	public void initAllCustomItems() {
		this.rubyOreInstance();
		this.rubyGemInstance();
		
		this.sapphireOreInstance();
		this.sapphireGemInstance();
		
		this.amethystOreInstance();
		this.amethystGemInstance();
	}
	
	/**
	 * Enables this plugin and initializes blocks, recipes...
	 */
	@Override
	public void onEnable() {
		super.onEnable();
	
		// Set the singleton
		DragonsHoardPlugin.selfSingleton = this;
		
		// Set the plugin logger
		DragonsHoardPlugin.pluginLogger = getLogger();
		
		getServer().getPluginManager().registerEvents(
			new DragonsHoardEventListener(this), 
			this
		);
		
		// Add textures to login precache
		SpoutManager.getFileManager().addToPreLoginCache(
			this, 
			Strings.getString("RubyOre.TextureURL")
		);
		SpoutManager.getFileManager().addToPreLoginCache(
			this, 
			Strings.getString("SapphireOre.TextureURL")
		);
		SpoutManager.getFileManager().addToPreLoginCache(
			this, 
			Strings.getString("AmethystOre.TextureURL")
		);
		
		// Add the custom ore populator to the overworld
		List<World> worlds = getServer().getWorlds();
		for(World world : worlds) {
			pluginLogger.info("World: " + world.getName());
			
			if(world.getEnvironment() == Environment.NORMAL) {
				world.getPopulators().add(new DragonsHoardBlockPopulator());
				pluginLogger.info("Added populator to world '" + world.getName() + "'");
			}
		}
		
		// Init the ores and items. If this is not done during startup,
		// players might experience the already spawned ores being stone blocks at first
		this.initAllCustomItems();
		
	}
	
	/**
	 * Disables the plugin and causes it to 
	 * free its resources.
	 */
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
