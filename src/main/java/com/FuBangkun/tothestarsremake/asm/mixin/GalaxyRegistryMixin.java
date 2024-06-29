package com.FuBangkun.tothestarsremake.asm.mixin;

import com.FuBangkun.tothestarsremake.asm.api.LandableStar;
import com.FuBangkun.tothestarsremake.asm.api.StarRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.util.stream.CelestialCollector;
import micdoodle8.mods.galacticraft.core.util.list.ImmutableCelestialList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GalaxyRegistry.class)
public abstract class GalaxyRegistryMixin {


    @Shadow(remap = false)
    public static ImmutableCelestialList<Planet> getPlanets() {
        return null;
    }

    @Shadow(remap = false)
    public static ImmutableCelestialList<Moon> getMoons() {
        return null;
    }

    @Inject(method = "refreshGalaxies()V", at = @At("HEAD"), remap = false)
    private static void refreshGalaxies(CallbackInfo ci) {
        StarRegistry.refreshLandableStarsInGalaxies();
    }

    @Inject(method = "getCelestialBodyFromDimensionID(I)Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;", at = @At("RETURN"), remap = false, cancellable = true)
    private static void getCelestialBodyFromDimensionID(int dimensionID, CallbackInfoReturnable<CelestialBody> cir) {
        if (cir.getReturnValue() != null) {
            return;
        }

        cir.setReturnValue(StarRegistry.getLandableStarFromDimensionID(dimensionID));
    }

    @Inject(method = "register(Ljava/lang/Object;)V", at = @At("RETURN"), remap = false)
    private static <T> void register(T object, CallbackInfo cir) {
        if (object instanceof LandableStar) {
            StarRegistry.registerLandableStar((LandableStar) object);
        }
    }

    /**
     * @author FuBangkun and Team Galacticraft
     * @reason List is immutable, so it must be replaced.
     */
    @Overwrite(remap = false)
    public static ImmutableCelestialList<CelestialBody> getAllReachableBodies() {
        //@noformat
        assert getPlanets() != null;
        assert getMoons() != null;
        return ImmutableCelestialList.from(
                getPlanets().stream().filter(CelestialBody.filterReachable()).collect(CelestialCollector.toList()),
                getMoons().stream().filter(CelestialBody.filterReachable()).collect(CelestialCollector.toList()),
                StarRegistry.getLandableStars().stream().filter(CelestialBody.filterReachable()).collect(CelestialCollector.toList())
        );
    }
}
