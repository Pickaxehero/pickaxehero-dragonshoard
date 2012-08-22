package net.samarythdragon.minecraftplugins.dragonshoard;

import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.material.CustomBlock;

public class DragonsHoardBlockPopulator extends BlockPopulator {

	private static final int RUBY_CHANCE = 40;
	private static final int SAPPHIRE_CHANCE = 40;
	private static final int AMETHYST_CHANCE = 40;
	
	private Random randGen = null;
	private World currentWorld = null;
	private Chunk currentChunk = null;
	
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		Validate.notNull(world, "NULL world given!");
		Validate.notNull(random, "NULL random generator given!");
		Validate.notNull(chunk, "NULL chunk given!");
		
		this.currentWorld = world;
		this.randGen = random;
		this.currentChunk = chunk;
		
		this.generateBlockCluster(
			DragonsHoardPlugin.instance().rubyOreInstance(), 
			RUBY_CHANCE
		);
		
		this.generateBlockCluster(
			DragonsHoardPlugin.instance().sapphireOreInstance(), 
			SAPPHIRE_CHANCE
		);
		
		this.generateBlockCluster(
			DragonsHoardPlugin.instance().amethystOreInstance(), 
			AMETHYST_CHANCE
		);		
	}
	
	/**
	 * Generates a cluster of blocks using a certain material.
	 * The chance to spawn a cluster of this ore in the current chunk is 
	 * also adjustable.
	 * 
	 * @param customOreMaterial The material the new cluster should be made out of
	 * @param chancePercent A chance in percent (0...100%) in which a new cluster should be spawned
	 */
	private void generateBlockCluster(CustomBlock customOreMaterial, int chancePercent) {
		Validate.isTrue(
			(chancePercent >= 0) && (chancePercent <= 100), 
			"Chance percentage " + chancePercent + " is outside of 0% ... 100%"
		);
		
		if((this.randGen.nextInt(100) + 1) >= chancePercent) {
			return;
		}
		
		// Determine center where block generation starts
        int centerX = (this.currentChunk.getX() << 4) + this.randGen.nextInt(16);
        int centerZ = (this.currentChunk.getZ() << 4) + this.randGen.nextInt(16);
        
        // If world height at this point is lower than 21 blocks,
        // abort the generation
        if(this.currentWorld.getHighestBlockYAt(centerX, centerZ) < 21) {
        	return;
        }
        
        // Choose a starting point within 5 to 20 blocks height
        int blockY = this.randGen.nextInt(16) + 5;
        
        // Create a cluster of blocks, starting at this point
        Block oreBlock = this.currentWorld.getBlockAt(centerX, blockY, centerZ);
        SpoutManager.getMaterialManager().overrideBlock(oreBlock, customOreMaterial);
        SpoutBlock spoutOreBlock = (SpoutBlock) oreBlock;
        
        spoutOreBlock.setCustomBlock(customOreMaterial);
        
        for(int runNo = 0; runNo < 4; runNo++) {
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
