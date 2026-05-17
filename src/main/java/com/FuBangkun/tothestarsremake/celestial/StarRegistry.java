package com.FuBangkun.tothestarsremake.celestial;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.core.util.list.CelestialList;
import micdoodle8.mods.galacticraft.core.util.list.ImmutableCelestialList;

import java.util.HashMap;
import java.util.Map;

public class StarRegistry {
    private static final CelestialList<Star> landableStars = CelestialList.create();

    private static final Map<SolarSystem, CelestialList<Star>> solarSystemLandableStarList = new HashMap<>();

    public static void refreshLandableStarsInGalaxies() {
        solarSystemLandableStarList.clear();

        for (Star landableStar : getLandableStars()) {
            SolarSystem solarSystem = landableStar.getParentSolarSystem();
            CelestialList<Star> list = solarSystemLandableStarList.get(solarSystem);
            if (list == null) list = CelestialList.create();
            list.add(landableStar);
            solarSystemLandableStarList.put(solarSystem, list);
        }
    }

    public static void registerLandableStar(Star star) {
        if (star == null) return;

        StarWorldUtil.prepareLandableStar(star);
        if (!landableStars.contains(star)) {
            landableStars.add(star);
        }
    }

    public static CelestialBody getLandableStarFromDimensionID(int dimensionID) {
        for (Star landableStar : landableStars) {
            if (landableStar.getDimensionID() == dimensionID) {
                return landableStar;
            }
        }

        return null;
    }

    /**
     * Returns a read-only list containing all registered Landable Stars
     */
    public static ImmutableCelestialList<Star> getLandableStars() {
        return ImmutableCelestialList.of(landableStars);
    }
}