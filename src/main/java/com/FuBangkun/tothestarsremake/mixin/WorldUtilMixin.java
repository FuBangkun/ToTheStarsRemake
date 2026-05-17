package com.FuBangkun.tothestarsremake.mixin;

import com.FuBangkun.tothestarsremake.celestial.StarRegistry;
import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.Satellite;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;

@Mixin(WorldUtil.class)
public abstract class WorldUtilMixin {
    @Inject(method = "getReachableCelestialBodiesForDimensionID(I)Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;", at = @At("RETURN"), remap = false, cancellable = true)
    private static void getReachableCelestialBodiesForDimensionID(int id, CallbackInfoReturnable<CelestialBody> cir) {
        for (Star cBody : StarRegistry.getLandableStars()) {
            if (cBody.isReachable()) {
                if (cBody.getDimensionID() == id) {
                    cir.setReturnValue(cBody);
                }
            }
        }
    }

    @Inject(method = "getReachableCelestialBodiesForName(Ljava/lang/String;)Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;", at = @At("RETURN"), remap = false, cancellable = true)
    private static void getReachableCelestialBodiesForName(String name, CallbackInfoReturnable<CelestialBody> cir) {
        for (CelestialBody cBody : StarRegistry.getLandableStars()) {
            if (cBody.isReachable()) {
                if (cBody.getName().equals(name)) {
                    cir.setReturnValue(cBody);
                }
            }
        }
    }

    @Inject(method = "getArrayOfPossibleDimensions(ILnet/minecraft/entity/player/EntityPlayerMP;)Ljava/util/HashMap;", at = @At("RETURN"), remap = false)
    private static void getArrayOfPossibleDimensions(int tier, EntityPlayerMP playerBase, CallbackInfoReturnable<HashMap<String, Integer>> cir) {
        for (CelestialBody body : StarRegistry.getLandableStars()) {
            if (!body.isReachable()) {
                cir.getReturnValue().put(body.getTranslatedName() + "*", body.getDimensionID());
            }
        }
    }

    @Inject(method = "getPossibleDimensionsForSpaceshipTier", at = @At("RETURN"), remap = false)
    private static void getPossibleDimensionsForSpaceshipTier(int tier, EntityPlayerMP playerBase, CallbackInfoReturnable<List<Integer>> cir) {
        List<Integer> temp = cir.getReturnValue();
        for (Star star : StarRegistry.getLandableStars()) {
            WorldProvider provider = WorldUtil.getProviderForDimensionServer(star.getDimensionID());

            if (provider != null) {
                if (provider instanceof IGalacticraftWorldProvider) {
                    if (((IGalacticraftWorldProvider) provider).canSpaceshipTierPass(tier)) {
                        temp.add(star.getDimensionID());
                    }
                } else {
                    temp.add(star.getDimensionID());
                }
            }
        }
    }

    @Shadow(remap = false)
    private static void insertChecklistEntries(CelestialBody body, List<CelestialBody> bodiesDone, List<List<String>> checklistValues) {
    }

    /**
     * @author FuBangkun and Team Galacticraft
     * @reason Keep GC's checklist ordering, but also include addon stars.
     */
    @Overwrite(remap = false)
    public static List<List<String>> getAllChecklistKeys() {
        List<List<String>> checklistValues = Lists.newArrayList();
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        List<CelestialBody> bodiesDone = Lists.newArrayList();

        for (Planet planet : GalaxyRegistry.getPlanets()) {
            insertChecklistEntries(planet, bodiesDone, checklistValues);
        }

        for (Moon moon : GalaxyRegistry.getMoons()) {
            insertChecklistEntries(moon, bodiesDone, checklistValues);
        }

        for (Satellite satellite : GalaxyRegistry.getSatellites()) {
            insertChecklistEntries(satellite, bodiesDone, checklistValues);
        }

        for (Star star : StarRegistry.getLandableStars()) {
            insertChecklistEntries(star, bodiesDone, checklistValues);
        }

        return checklistValues;
    }
}
