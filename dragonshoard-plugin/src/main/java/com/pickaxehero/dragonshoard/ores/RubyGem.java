package com.pickaxehero.dragonshoard.ores;


import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import com.pickaxehero.dragonshoard.strings.Strings;

public class RubyGem extends GenericCustomItem {

	public RubyGem(Plugin plugin) {
		super(
			plugin, 
			Strings.getString("RubyGem.Name"), //$NON-NLS-1$
			Strings.getString("RubyGem.URL") //$NON-NLS-1$
		);
	}

	
}
