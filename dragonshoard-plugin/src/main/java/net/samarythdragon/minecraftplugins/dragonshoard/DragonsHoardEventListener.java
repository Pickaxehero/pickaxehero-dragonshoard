package net.samarythdragon.minecraftplugins.dragonshoard;

import net.samarythdragon.minecraftplugins.dragonshoard.ores.RubyOre;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.MaterialManager;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

/**
 * Listens for and handles various events needed in this plugin.
 * 
 * @author samaryth
 *
 */
public class DragonsHoardEventListener implements Listener {

	private Plugin parent = null; 
	
	public DragonsHoardEventListener(Plugin parent) {
		Validate.notNull(parent, "This instance must have a parent plugin! (parent == null)");
		
		this.parent = parent;
		
		DragonsHoardPlugin.logger().info("Event listener created!");
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent chunkLoadEvent) {
		Validate.notNull(chunkLoadEvent, "Chunk load event is null!");
		
	
	}
	
	@EventHandler
	public void onChunkPopulateEvent(ChunkPopulateEvent chunkPopulateEvent) {
		
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent blockBreakEvent) {
		Validate.notNull(blockBreakEvent, "BlockBreakEvent ist NULL!");
		
		SpoutBlock broken = (SpoutBlock) blockBreakEvent.getBlock();
		
		if(broken.isCustomBlock() == false) {
			return;
		}
		
		if(broken.getCustomBlock() instanceof RubyOre) {
			blockBreakEvent.setCancelled(true);
			broken.setType(Material.AIR);
			
			int gemCount = 1;
			
			SpoutItemStack rubyGems = new SpoutItemStack(DragonsHoardPlugin.instance().rubyGemInstance());
			rubyGems.setAmount(gemCount);
			
			broken.getWorld().dropItemNaturally(
				broken.getLocation(), 
				rubyGems
			);
		}
	}
	
//	@EventHandler
//	public void onWorldInitEvent(WorldInitEvent worldInitEvent) {
//		World world = worldInitEvent.getWorld();
//		
//		DragonsHoardPlugin.logger().info("World initiated: " + world.getName());
//		
//		if(world.getEnvironment() == Environment.NORMAL) {
//			world.getPopulators().add(new DragonsHoardBlockPopulator());
//		}
//	}
//	
//	@EventHandler
//	public void onWorldLoadEvent(WorldLoadEvent worldLoadEvent) {
//		World world = worldLoadEvent.getWorld();
//		
//		DragonsHoardPlugin.logger().info("World loaded: " + world.getName());
//	}
	
}
