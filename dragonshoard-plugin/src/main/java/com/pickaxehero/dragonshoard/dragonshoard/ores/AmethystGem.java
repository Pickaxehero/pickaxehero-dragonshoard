package com.pickaxehero.dragonshoard.dragonshoard.ores;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import com.pickaxehero.dragonshoard.dragonshoard.strings.Strings;

public class AmethystGem extends GenericCustomItem {
	
	public AmethystGem(Plugin plugin) {
		super(
			plugin, 
			Strings.getString("AmethystGem.Name"), //$NON-NLS-1$
			Strings.getString("AmethystGem.URL") //$NON-NLS-1$
		);
	}
}
