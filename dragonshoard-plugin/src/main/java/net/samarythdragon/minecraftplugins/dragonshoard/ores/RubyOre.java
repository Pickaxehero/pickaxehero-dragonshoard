package net.samarythdragon.minecraftplugins.dragonshoard.ores;

import net.samarythdragon.minecraftplugins.dragonshoard.strings.Strings;

import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

public class RubyOre extends GenericCubeCustomBlock {
	
	public RubyOre(Plugin plugin) {
		super(
			plugin, 
			Strings.getString("RubyOre.Name"), 
			Strings.getString("RubyOre.TextureURL"),  
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
