package com.FuBangkun.tothestarsremake.asm.api;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.core.util.list.CelestialList;
import micdoodle8.mods.galacticraft.core.util.list.ImmutableCelestialList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StarRegistry {
    private static final CelestialList<LandableStar> landableStars = CelestialList.create();

    private static final Map<SolarSystem, CelestialList<LandableStar>> solarSystemLandableStarList = new HashMap<>();

    public static void refreshLandableStarsInGalaxies() {
        solarSystemLandableStarList.clear();

        for (LandableStar landableStar : getLandableStars()) {
            SolarSystem solarSystem = landableStar.getParentSolarSystem();
            CelestialList<LandableStar> list = solarSystemLandableStarList.get(solarSystem);
            if (list == null) {
                list = CelestialList.create();
            }
            list.add(landableStar);
            solarSystemLandableStarList.put(solarSystem, list);
        }
    }

    public static CelestialBody getLandableStarFromTranslationkey(String translationKey) {
        for (LandableStar landableStar : landableStars) {
            if (landableStar.getTranslationKey().equals(translationKey)) {
                return landableStar;
            }
        }
        return null;
    }

    public static void registerLandableStar(LandableStar star)
    {
        landableStars.add(star);
    }

    public static CelestialBody getLandableStarFromDimensionID(int dimensionID) {
        for (LandableStar landableStar : landableStars) {
            if (landableStar.getDimensionID() == dimensionID) {
                return landableStar;
            }
        }

        return null;
    }

    public static List<LandableStar> getLandableStarsForSolarSystem(SolarSystem solarSystem) {
        if (solarSystemLandableStarList.get(solarSystem) == null) {
            return new ArrayList<>();
        }
        return solarSystemLandableStarList.get(solarSystem);
    }

    /**
     * Returns a read-only list containing all registered Landable Stars
     */
    public static ImmutableCelestialList<LandableStar> getLandableStars() {
        return ImmutableCelestialList.of(landableStars);
    }
}