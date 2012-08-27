package com.pickaxehero.dragonshoard;

import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.CustomItem;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.pickaxehero.dragonshoard.ores.AmethystOre;
import com.pickaxehero.dragonshoard.ores.RubyOre;
import com.pickaxehero.dragonshoard.ores.SapphireOre;
import com.pickaxehero.dragonshoard.strings.MessageI18N;
import com.pickaxehero.dragonshoard.strings.Strings;

/**
 * Listens for and handles various events needed in this plugin.
 * 
 * @author Pickaxehero
 *
 */
public class DragonsHoardEventListener implements Listener {

	private Plugin parent = null; 
	
	public DragonsHoardEventListener(Plugin parent) {
		Validate.notNull(parent, MessageI18N.getString("DragonsHoardEventListener.ErrorPluginHasNoParent")); //$NON-NLS-1$
		
		this.parent = parent;
		
		DragonsHoardPlugin.logger().info(MessageI18N.getString("DragonsHoardEventListener.InfoEventListenerCreated")); //$NON-NLS-1$
	}
	
	/**
	 * This handler overrides the default behaviour for the ores.
	 * Normally, they'd just drop as themselves (ore block), not giving any gems.
	 * This is addressed and handled here.
	 * 
	 * @param blockBreakEvent The event triggered by a breaking block
	 */
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent blockBreakEvent) {
		Validate.notNull(
			blockBreakEvent, 
			MessageI18N.getString("DragonsHoardEventListener.ErrorBlockBreakEventNull")
		); //$NON-NLS-1$
		
		// Check if the player is in creative mode, so nothing will be dropped when 
		// players break blocks then.
		Player player = blockBreakEvent.getPlayer();
		if((player != null) && (player.getGameMode() == GameMode.CREATIVE)) {
			return;
		}
		
		// Get the block and cast it to a Spout block
		SpoutBlock broken = (SpoutBlock) blockBreakEvent.getBlock();
		CustomItem minedGem = null;
		
		// Check if the block is an instance of one of out ores
		if(broken.getCustomBlock() instanceof RubyOre) {
			minedGem = DragonsHoardPlugin.instance().rubyGemInstance();
		} else if(broken.getCustomBlock() instanceof SapphireOre) {
			minedGem = DragonsHoardPlugin.instance().sapphireGemInstance();
		} else if(broken.getCustomBlock() instanceof AmethystOre) {
			minedGem = DragonsHoardPlugin.instance().amethystGemInstance();
		}
		
		// If one of our ores was broken, handle dropping the appropriate gems
		if(minedGem != null) {
			blockBreakEvent.setCancelled(true);
			broken.setType(Material.AIR);
			broken.setCustomBlock(null);
			
			int gemCount = 1;
			
			SpoutItemStack gemStack = new SpoutItemStack(minedGem);
			gemStack.setAmount(gemCount);
			
			broken.getWorld().dropItemNaturally(
				broken.getLocation(), 
				gemStack
			);
		}		
	}
	
	/**
	 * This handler handles the creeper explosion override feature.
	 * @param entityExplodeEvent
	 */
	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent entityExplodeEvent) {
		Validate.notNull(entityExplodeEvent, "EntityExplodeEvent is NULL");
		if(entityExplodeEvent.getEntity().getType() == EntityType.CREEPER) {
			entityExplodeEvent.setCancelled(true);
			Bukkit.getServer().broadcastMessage(MessageI18N.getString("DragonsHoardEventListener.InfoCreeperNeutered")); //$NON-NLS-1$
		}
	}
	
	/**
	 * This handler handles the mob override feature.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Validate.notNull(event, "EntityDamageByEntityEvent is NULL");
		
		// Creepers should not do any more damage, so override this behaviour
		if(event.getDamager().getType() == EntityType.CREEPER) {
			event.setCancelled(true);
			
			// If a player was damaged, play a sound
			if(event.getEntityType() == EntityType.PLAYER) {
				SpoutManager.getSoundManager().playCustomSoundEffect(
					DragonsHoardPlugin.instance(), 
					(SpoutPlayer) event.getEntity(), 
					Strings.getString("DragonsHoardEventListener.CreeperSoundURL"),  //$NON-NLS-1$
					false,
					event.getDamager().getLocation()
				);
			}
		} 
		
		// If a spider attacked a player, ignite it and "toss" it away
		else if(event.getDamager().getType() == EntityType.SPIDER) {
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
	
	/**
	 * New players are informed that this server runs the plugin
	 * @param playerJoinEvent
	 */
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent playerJoinEvent) {
		playerJoinEvent.getPlayer().sendMessage(
			MessageI18N.getString("DragonsHoardEventListener.InfoPlayerJoinWelcomeMessage") //$NON-NLS-1$
		);
	}
}
