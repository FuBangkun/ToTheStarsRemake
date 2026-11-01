package com.fubangkun.tothestarsremake.mixin;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = GuiCelestialSelection.class, remap = false)
public abstract class GuiCelestialSelectionMixin {
    @Inject(method = "getChildren", at = @At("RETURN"), cancellable = true)
    private void getChildren(Object object, CallbackInfoReturnable<List<Object>> cir) {
        if (object instanceof SolarSystem) {
            SolarSystem solarSystem = (SolarSystem) object;
            List<Object> originalList = cir.getReturnValue();
            List<Object> modifiedList = originalList != null ? new ArrayList<>(originalList) : new ArrayList<>();
            CelestialBody mainStar = solarSystem.getMainStar();
            if (mainStar != null && !modifiedList.contains(mainStar)) {
                modifiedList.add(0, mainStar);
                cir.setReturnValue(modifiedList);
            }
        }
    }

    @Inject(method = "getSiblings", at = @At("HEAD"), cancellable = true)
    private void getSiblings(CelestialBody celestialBody, CallbackInfoReturnable<List<CelestialBody>> cir) {
        for (SolarSystem solarSystem : GalaxyRegistry.getSolarSystems()) {
            if (solarSystem.getMainStar() == celestialBody) {
                List<CelestialBody> siblings = new ArrayList<>();

                siblings.add(solarSystem.getMainStar());

                List<Planet> planets = GalaxyRegistry.getPlanetsForSolarSystem(solarSystem);
                if (planets != null) {
                    siblings.addAll(planets);
                }

                cir.setReturnValue(siblings);
                return;
            }
        }
    }
}