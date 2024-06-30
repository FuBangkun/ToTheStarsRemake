package com.FuBangkun.tothestarsremake.sol;

import com.FuBangkun.tothestarsremake.tothestarsremake.Tags;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class SolBlocks {
	public static Block blockSolPlasma;

	public static void initBlocks() {}

	private static void setHarvestLevel(Block block, String toolClass, int level, int meta) {
		block.setHarvestLevel(toolClass, level, block.getStateFromMeta(meta));
	}

	private static void setHarvestLevel(Block block, String toolClass, int level) {
		block.setHarvestLevel(toolClass, level);
	}

	public static void setHarvestLevels() {}

	public static void registerBlock(Block block, Class<? extends ItemBlock> itemClass) {
		GCBlocks.register(Tags.MOD_ID, block, itemClass);
	}

	public static void registerBlocks() {}

	public static void oreDictRegistration() {}
}
