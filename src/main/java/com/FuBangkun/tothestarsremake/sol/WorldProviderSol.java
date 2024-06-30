package com.FuBangkun.tothestarsremake.sol;

import com.FuBangkun.tothestarsremake.ToTheStarsRemake;
import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.List;

public class WorldProviderSol extends WorldProviderSpace {

    @Override
    public DimensionType getDimensionType() {
        return ToTheStarsRemake.dimSol;
    }

    @Override
    public Vector3 getFogColor() {
        return new Vector3(1.0f, 0.647f, 0.0f);
    }

    @Override
    public Vector3 getSkyColor() {
        return new Vector3(0.0f, 0.0f, 0.0f);
    }

    @Override
    public boolean hasSunset() {
        return false;
    }

    @Override
    public long getDayLength() {
        return 588000;
    }

    @Override
    public Class<? extends IChunkGenerator> getChunkProviderClass() {
        return ChunkProviderTestSun.class;
    }

    @Override
    public float getGravity() {
        return (float) HSTHelper.realGravityToGCGravity(28.0);
    }

    @Override
    public double getFuelUsageMultiplier() {
        return 0;
    }

    @Override
    public boolean canSpaceshipTierPass(int tier) {
        return tier > 2;
    }

    @Override
    public float getFallDamageModifier() {
        return 0;
    }

    @Override
    public CelestialBody getCelestialBody() {
        return ToTheStarsRemake.starSol;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2) {
        return var1 == 0 && var2 == 0;
    }

    @Nullable
    @Override
    public BlockPos getSpawnCoordinate() {
        return new BlockPos(0, 66, 0);
    }

    @Override
    public int getDungeonSpacing() {
        return 700;
    }

    @Override
    public ResourceLocation getDungeonChestType() {
        return new ResourceLocation("galacticraftcore:dungeon_tier_3");
    }

    @Override
    public List<Block> getSurfaceBlocks() {
        return Lists.newArrayList(SolBlocks.blockSolPlasma);
    }

    @Override
    public boolean canRespawnHere() {
        if (EventHandlerGC.bedActivated) {
            EventHandlerGC.bedActivated = false;
            return true;
        }
        return false;
    }
}
