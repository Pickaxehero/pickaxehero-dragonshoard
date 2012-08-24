package com.pickaxehero.dragonshoard.dragonshoard.ores;


import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

import com.pickaxehero.dragonshoard.dragonshoard.strings.Strings;

public class SapphireOre extends GenericCubeCustomBlock {

	public SapphireOre(Plugin plugin) {
		super(
			plugin, 
			Strings.getString("SapphireOre.Name"),
			Strings.getString("SapphireOre.URL"), 
			16
		);
	}

    public boolean isProvidingPowerTo(World world, int x, int y, int z, BlockFace face) {
        return false;
    }
  
    public boolean isIndirectlyProvidingPowerTo(World world, int x, int y, int z, BlockFace face) {
        return false;
    }
    
    @Override
    public float getHardness() {
    	return MaterialData.diamondOre.getHardness();
    }
    
    @Override
    public int getLightLevel() {
    	// TODO Auto-generated method stub
    	return MaterialData.diamondOre.getLightLevel();
    }
}
