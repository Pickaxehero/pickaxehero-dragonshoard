package net.samarythdragon.minecraftplugins.dragonshoard.ores;

import net.samarythdragon.minecraftplugins.dragonshoard.strings.Strings;

import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

public class AmethystOre extends GenericCubeCustomBlock {
	public AmethystOre(Plugin plugin) {
		super(
			plugin, 
			Strings.getString("AmethystOre.Name"), 
			Strings.getString("AmethystOre.URL"), 
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
