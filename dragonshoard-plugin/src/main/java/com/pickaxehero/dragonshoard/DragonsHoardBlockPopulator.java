package com.pickaxehero.dragonshoard;

import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.material.CustomBlock;

import com.pickaxehero.dragonshoard.config.ConfigManager;

/**
 * The block populator creates the ores which can be mined in certain places.
 * 
 * @author Pickaxehero
 *
 */
public class DragonsHoardBlockPopulator extends BlockPopulator {
	private Random randGen = null;
	private World currentWorld = null;
	private Chunk currentChunk = null;

	// Messaging "markers"
	private boolean firstRun = true;
	
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		Validate.notNull(world, "NULL world given!");
		Validate.notNull(random, "NULL random generator given!");
		Validate.notNull(chunk, "NULL chunk given!");
		
		this.currentWorld = world;
		this.randGen = random;
		this.currentChunk = chunk;
		
		if(firstRun) {
			firstRun = false;
			DragonsHoardPlugin.logger().info("DragonsHoardBlockPopulator has started populating the ores!");
		}
		
		if(ConfigManager.getBooleanValue("RubyOre.Enabled")) {
			this.generateBlockCluster(
				DragonsHoardPlugin.instance().rubyOreInstance(), 
				ConfigManager.getIntegerValue("RubyOre.SpawnChancePercent", 1, 100),
				ConfigManager.getIntegerValue("RubyOre.MinClusterSize"),
				ConfigManager.getIntegerValue("RubyOre.MaxClusterSize")
			);			
		}
		
		if(ConfigManager.getBooleanValue("SapphireOre.Enabled")) {
			this.generateBlockCluster(
				DragonsHoardPlugin.instance().sapphireOreInstance(), 
				ConfigManager.getIntegerValue("SapphireOre.SpawnChancePercent", 1, 100),
				ConfigManager.getIntegerValue("SapphireOre.MinClusterSize"),
				ConfigManager.getIntegerValue("SapphireOre.MaxClusterSize")
			);
		}
		
		if(ConfigManager.getBooleanValue("AmethystOre.Enabled")) {
			this.generateBlockCluster(
				DragonsHoardPlugin.instance().amethystOreInstance(), 
				ConfigManager.getIntegerValue("AmethystOre.SpawnChancePercent", 1, 100),
				ConfigManager.getIntegerValue("AmethystOre.MinClusterSize"),
				ConfigManager.getIntegerValue("AmethystOre.MaxClusterSize")
			);		
		}
	}
	
	/**
	 * Generates a cluster of blocks using a certain material.
	 * The chance to spawn a cluster of this ore in the current chunk is 
	 * also adjustable.
	 * 
	 * @param customOreMaterial The material the new cluster should be made out of
	 * @param chancePercent A chance in percent (0...100%) in which a new cluster should be spawned
	 * @param minSize The cluster should contain at least minSize blocks
	 * @param maxSize ... and atmost maxsize blocks
	 */
	private void generateBlockCluster(CustomBlock customOreMaterial, int chancePercent, int minSize, int maxSize) {
		Validate.notNull(customOreMaterial, "CustomOreMaterial was NULL!");
		Validate.isTrue(
			(chancePercent >= 0) && (chancePercent <= 100), 
			"Chance percentage " + chancePercent + " is outside of 0% ... 100%"
		);
		Validate.isTrue(
			minSize <= maxSize, 
			"The minimum size of a block cluster cannot be greater than its maximum size!"
		);
		
		// Random gen. will return a value 0...99, adding +1 will make that 1...100
		// The higher chancePercent will be, the more likely it is for the
		// generated random value to be at least as large as chancePercent, thus running
		// the code after this if().
		if((this.randGen.nextInt(100) + 1) <= chancePercent) {
			return;
		}
		
		// Determine center where block generation starts
        int centerX = (this.currentChunk.getX() << 4) + this.randGen.nextInt(16);
        int centerZ = (this.currentChunk.getZ() << 4) + this.randGen.nextInt(16);
        
        // If world height at this point is lower than 21 blocks, abort the generation
        if(this.currentWorld.getHighestBlockYAt(centerX, centerZ) < 21) {
        	return;
        }
        
        // Choose a starting point within 5 to 20 blocks height
        int blockY = this.randGen.nextInt(16) + 5;
        
        // Create a cluster of blocks, starting at this point
        // Make sure to override the new block with the custom block via SpoutAPI
        Block oreBlock = this.currentWorld.getBlockAt(centerX, blockY, centerZ);
        
        // Don't replace AIR since it is likely to be in a cave!
        SpoutBlock spoutOreBlock;
        
        if(oreBlock.getType() != Material.AIR) {
        	// First set this block to AIR to avoid the block overlapping bug
        	oreBlock.setType(Material.AIR);
        	
            SpoutManager.getMaterialManager().overrideBlock(oreBlock, customOreMaterial);
            
            spoutOreBlock = (SpoutBlock) oreBlock;
            spoutOreBlock.setCustomBlock(customOreMaterial);
        }
        
        // Range is a random number between minSize and maxSize.
        // It will be at least minSize large, and can increase up to maxSize by the
        // difference between maxSize - minSize.
        // Adding +1 is not neccessary since we already have generated one block above.
        int numberOfBlockInCluster = this.randGen.nextInt(maxSize - minSize) + minSize;
        
        for(int runNo = 0; runNo < numberOfBlockInCluster; runNo++) {
        	// Direction is the direction which will be build to
        	// during this iteration. Either North, South, East or West
        	int direction = this.randGen.nextInt(4);
        	
        	if(direction == 0) {        
        		centerZ++;
        	} else if(direction == 1) {
        		centerX++;
        	} else if(direction == 2) {
        		centerZ--;
        	} else {
        		centerX--;
        	}
        	
        	// Place the currently generated block
        	oreBlock = this.currentWorld.getBlockAt(
        		centerX,
        		blockY,
        		centerZ
        	);
        	
        	// Again, don't replace AIR since it is likely to be in a cave!
        	if(oreBlock.getType() == Material.AIR) {
        		continue;
        	}

        	SpoutManager.getMaterialManager().overrideBlock(oreBlock, customOreMaterial);
        	spoutOreBlock = (SpoutBlock) oreBlock;
        	spoutOreBlock.setCustomBlock(customOreMaterial);
        	
        	// Randomly build a block on top or
        	// below the currently generated one
        	int generateUpper = this.randGen.nextInt(5);
        	int generateLower = this.randGen.nextInt(5);
            
        	// 40% chance of upper block
            if(generateUpper > 2) {
            	oreBlock = this.currentWorld.getBlockAt(
            		centerX,
            		blockY + 1,
            		centerZ
            	);
            	
            	// First set this block to AIR to avoid the block overlapping bug
            	oreBlock.setType(Material.AIR);
            	
            	SpoutManager.getMaterialManager().overrideBlock(oreBlock, customOreMaterial);
            	spoutOreBlock = (SpoutBlock) oreBlock;
            	spoutOreBlock.setCustomBlock(customOreMaterial);
            }
            
            // 20% chance of lower block
            if(generateLower > 3) {
            	oreBlock = this.currentWorld.getBlockAt(
            		centerX,
            		blockY - 1,
            		centerZ
            	);
            	
            	spoutOreBlock = (SpoutBlock) oreBlock;
            	spoutOreBlock.setCustomBlock(customOreMaterial);          	
            }
        }		
	}

}
