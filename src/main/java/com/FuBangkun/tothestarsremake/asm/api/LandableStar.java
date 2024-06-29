package com.FuBangkun.tothestarsremake.asm.api;

import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import net.minecraft.world.biome.Biome;

import java.util.Objects;

public class LandableStar extends Star {
	public LandableStar(String bodyName) {
		super(bodyName);
	}

	public LandableStar setParentSolarSystem(SolarSystem galaxy) {
		return (LandableStar) super.setParentSolarSystem(galaxy);
	}

	public static void addMobToSpawn(String starName, Biome.SpawnListEntry mobData) {
		Objects.requireNonNull(StarRegistry.getLandableStarFromTranslationkey("star." + starName)).addMobInfo(mobData);
	}
}
