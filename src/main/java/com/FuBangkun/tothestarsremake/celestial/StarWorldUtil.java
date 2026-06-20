package com.FuBangkun.tothestarsremake.celestial;

import com.FuBangkun.tothestarsremake.TTSR;
import com.FuBangkun.tothestarsremake.dimension.WorldProviderStar;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;

import java.util.*;

public class StarWorldUtil {
    private static final int AUTO_STAR_DIMENSION_BASE = -60000;

    private static final Map<String, Integer> autoStarDimensionIds = new HashMap<>();

    private static final Set<Integer> reservedAutoStarDimensions = new HashSet<>();

    public static void prepareLandableStar(Star star) {
        if (star == null) {
            return;
        }

        if (star.getDimensionID() == -1 || star.getWorldProvider() == null || !WorldProviderStar.class.equals(star.getWorldProvider())) {
            int dimensionId = getOrCreateDimensionId(star);
            star.setDimensionInfo(dimensionId, WorldProviderStar.class);
        }

        if (star.getTierRequirement() <= 0) {
            star.setTierRequired(3);
        }

        if (star.getDimensionSuffix() == null) {
            star.setDimensionSuffix("_" + star.getName());
        }

        if (star.getBodyIcon() == null) {
            star.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/sun.png"));
        }

        if (star.getBiomes() == null && TTSR.biomeSolFlat != null) {
            star.setBiomeInfo(TTSR.biomeSolFlat);
        }

        if (star.atmosphere.hasNoGases()) {
            star.atmosphereComponent(EnumAtmosphericGas.HELIUM).atmosphereComponent(EnumAtmosphericGas.HYDROGEN);
        }

        if (star.getChecklistKeys().isEmpty()) {
            star.addChecklistKeys("equip_oxygen_suit", "equip_shield_controller", "thermal_padding_t2");
        }
    }

    private static int getOrCreateDimensionId(Star star) {
        String key = star.getTranslationKey();
        Integer existing = autoStarDimensionIds.get(key);
        if (existing != null) {
            return existing;
        }

        int hash = Objects.hash(key, star.getParentSolarSystem() == null ? "" : star.getParentSolarSystem().getTranslationKey());
        int candidate = AUTO_STAR_DIMENSION_BASE - Math.floorMod(hash, 10000);
        int probe = Math.max(1, Math.abs(Objects.hash(key, "probe")) % 97);

        while (isDimensionIdOccupied(candidate)) {
            candidate -= probe;
        }

        autoStarDimensionIds.put(key, candidate);
        reservedAutoStarDimensions.add(candidate);
        return candidate;
    }

    private static boolean isDimensionIdOccupied(int id) {
        return reservedAutoStarDimensions.contains(id) || DimensionManager.isDimensionRegistered(id) || GalacticraftRegistry.isDimensionTypeIDRegistered(id);
    }
}
