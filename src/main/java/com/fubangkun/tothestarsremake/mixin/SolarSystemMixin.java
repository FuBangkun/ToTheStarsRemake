package com.fubangkun.tothestarsremake.mixin;

import com.fubangkun.tothestarsremake.celestial.StarRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SolarSystem.class, remap = false)
public abstract class SolarSystemMixin {
    @Inject(method = "setMainStar", at = @At("RETURN"))
    private void setMainStar(Star star, CallbackInfoReturnable<SolarSystem> cir) {
        if (star == null) {
            return;
        }
        StarRegistry.registerLandableStar(star);
    }
}