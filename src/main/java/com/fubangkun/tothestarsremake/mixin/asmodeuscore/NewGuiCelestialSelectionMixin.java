package com.fubangkun.tothestarsremake.mixin.asmodeuscore;

import asmodeuscore.core.astronomy.gui.screen.NewGuiCelestialSelection;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import net.minecraft.world.WorldProviderSurface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(value = NewGuiCelestialSelection.class, remap = false)
public abstract class NewGuiCelestialSelectionMixin extends GuiCelestialSelection {
    @Shadow
    private int tierneed;
    @Shadow
    private double traveltime;
    @Shadow
    private boolean canTravel;
    @Shadow
    private int fuelSet;
    @Shadow
    private int fuelRocketLevel;
    @Shadow
    private int currenttier;
    @Shadow
    private asmodeuscore.core.astronomy.SpaceData.Engine_Type engine;
    @Shadow
    private Map<CelestialBody, Integer> celestialBodyTiers;

    public NewGuiCelestialSelectionMixin(boolean mapMode, List<CelestialBody> possibleBodies, boolean canCreateStations) {
        super(mapMode, possibleBodies, canCreateStations);
    }

    @ModifyConstant(method = "mouseClicked", constant = @Constant(classValue = Star.class, ordinal = 2))
    private Class<?> mouseClicked(Object instance, Class<?> type) {
        return NewGuiCelestialSelectionMixin.class;
    }

    @Inject(method = "getChildren", at = @At("RETURN"), cancellable = true)
    private void getChildren(Object object, int start, int size, CallbackInfoReturnable<List<Object>> cir) {
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

    @Inject(method = "drawButtons", at = @At("HEAD"))
    private void drawButtons(int mousePosX, int mousePosY, CallbackInfo ci) {
        if (this.selectedBody instanceof Star) {
            int travel_modifier = 200;
            double dist = 0.0;
            SolarSystem starSource = GalacticraftCore.solarSystemSol;
            this.canTravel = true;
            if (!(this.mc.player.world.provider instanceof WorldProviderSurface) && this.mc.player.world.provider instanceof IGalacticraftWorldProvider) {
                CelestialBody currentBody = ((IGalacticraftWorldProvider) this.mc.player.world.provider).getCelestialBody();
                if (currentBody instanceof Planet) {
                    starSource = ((Planet) currentBody).getParentSolarSystem();
                    dist = currentBody.getRelativeDistanceFromCenter().unScaledDistance;
                } else if (currentBody instanceof IChildBody) {
                    starSource = ((IChildBody) currentBody).getParentPlanet().getParentSolarSystem();
                    dist = ((IChildBody) currentBody).getParentPlanet().getRelativeDistanceFromCenter().unScaledDistance;
                } else if (currentBody instanceof Star) {
                    starSource = ((Star) currentBody).getParentSolarSystem();
                    dist = 0.0;
                }
                if (this.selectedBody == currentBody) {
                    dist = 0.0;
                }
            } else {
                dist = GalacticraftCore.planetOverworld.getRelativeDistanceFromCenter().unScaledDistance;
                starSource = GalacticraftCore.solarSystemSol;
            }
            SolarSystem starTarget = ((Star) this.selectedBody).getParentSolarSystem();
            double time;
            if (starSource.equals(starTarget)) {
                time = Math.round(dist * 70.0);
            } else {
                double distOthersystem = Math.floor(Vector3.distance(starSource.getMapPosition(), starTarget.getMapPosition()) / 100.0) * 100.0;
                time = distOthersystem * 10.0;
                if (this.engine.getID() < asmodeuscore.core.astronomy.SpaceData.Engine_Type.SUBLIGHT_ENGINE.getID()) {
                    this.canTravel = false;
                }
            }
            this.traveltime = (time <= 0 ? 10 : (int) time) * travel_modifier;
            for (int i = 0; i <= asmodeuscore.core.astronomy.BodiesRegistry.getMaxTier(); ++i) {
                if (dist >= 0.2 * i) {
                    this.tierneed = (starSource.equals(starTarget)) ? Math.min(i + 1, asmodeuscore.core.astronomy.BodiesRegistry.getMaxTier()) : asmodeuscore.core.astronomy.BodiesRegistry.getMaxTier();
                }
            }
            this.fuelSet = (int) Math.round((double) this.fuelRocketLevel - (double) this.fuelRocketLevel * (dist / (double) this.currenttier));
            if (this.celestialBodyTiers.containsKey(this.selectedBody)) {
                this.tierneed = this.celestialBodyTiers.get(this.selectedBody);
            }
            if (this.mc.player.capabilities.isCreativeMode) {
                this.canTravel = true;
            }
            if (this.fuelSet < 0 && !this.mc.player.capabilities.isCreativeMode) {
                this.canTravel = false;
            }
        }
    }
}