package com.FuBangkun.tothestarsremake.mixin;

import com.FuBangkun.tothestarsremake.celestial.StarRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SolarSystem.class)
public abstract class SolarSystemMixin {
    @Inject(method = "setMainStar(Lmicdoodle8/mods/galacticraft/api/galaxies/Star;)Lmicdoodle8/mods/galacticraft/api/galaxies/SolarSystem;", at = @At("RETURN"), remap = false)
    private void setMainStar(Star star, CallbackInfoReturnable<SolarSystem> cir) {
        if (star == null) {
            return;
        }


        StarRegistry.registerLandableStar(star);
    }
}


