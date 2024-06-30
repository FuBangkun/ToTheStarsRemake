package com.FuBangkun.tothestarsremake.sol;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.Item;

public class SolItems {
	public static Item bucketSunPlasma;

	public static void initItems() {
		SolItems.registerItems();
		SolItems.registerHarvestLevels();
	}

	public static void registerHarvestLevels() {}

	private static void registerItems() {}

	public static void registerItem(Item item) {
		String name = item.getTranslationKey().substring(5);
		GCCoreUtil.registerGalacticraftItem(name, item);
		GalacticraftCore.itemListTrue.add(item);
		item.setRegistryName(name);
	}
}
