package com.pickaxehero.dragonshoard.dragonshoard.ores;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import com.pickaxehero.dragonshoard.dragonshoard.strings.Strings;

public class SapphireGem extends GenericCustomItem {

	public SapphireGem(Plugin plugin) {
		super(
			plugin, 
			Strings.getString("SapphireGem.Name"), //$NON-NLS-1$
			Strings.getString("SapphireGem.URL") //$NON-NLS-1$
		);
	}

}
