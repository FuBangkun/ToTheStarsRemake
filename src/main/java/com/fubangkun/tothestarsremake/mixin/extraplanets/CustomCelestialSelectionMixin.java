package com.fubangkun.tothestarsremake.mixin.extraplanets;

import com.mjr.extraplanets.client.gui.screen.CustomCelestialSelection;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = CustomCelestialSelection.class, remap = false)
public abstract class CustomCelestialSelectionMixin {
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
}
