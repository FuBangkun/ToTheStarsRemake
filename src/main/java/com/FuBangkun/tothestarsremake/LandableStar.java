package com.FuBangkun.tothestarsremake;

import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;

public class LandableStar extends Star {
    public LandableStar(String bodyName) {
        super(bodyName);
    }

    public LandableStar setParentSolarSystem(SolarSystem galaxy) {
        return (LandableStar) super.setParentSolarSystem(galaxy);
    }
}
