package net.samarythdragon.minecraftplugins.dragonshoard;

import java.util.Random;

import net.samarythdragon.minecraftplugins.dragonshoard.ores.RubyOre;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.player.SpoutPlayer;

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
	
	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent entityExplodeEvent) {
		if(entityExplodeEvent.getEntity().getType() == EntityType.CREEPER) {
			entityExplodeEvent.setCancelled(true);
			Bukkit.getServer().broadcastMessage("A creeper has been neutered!");
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(event.getDamager().getType() == EntityType.CREEPER) {
			event.setCancelled(true);
			
			if(event.getEntityType() == EntityType.PLAYER) {
				SpoutManager.getSoundManager().playCustomSoundEffect(
					DragonsHoardPlugin.instance(), 
					(SpoutPlayer) event.getEntity(), 
					"http://www.pickaxehero.com/18848.mp3", 
					false,
					event.getDamager().getLocation()
				);
			}
		}
		
		if(event.getDamager().getType() == EntityType.SPIDER) {
			Random random = new Random(System.currentTimeMillis());
			
			event.getDamager().setFireTicks(500);
			event.getDamager().teleport(
				event.getEntity().getLocation().add(
					new Vector(
						random.nextInt(15),
						random.nextInt(15),
						random.nextInt(15)
					)
				)
			);
		}

	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent playerJoinEvent) {
		playerJoinEvent.getPlayer().sendMessage(
			"Hey there! This Server is running Dragon's Hoard *rawr* ^.=.^"
		);
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
