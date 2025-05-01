package com.FuBangkun.tothestarsremake;

import micdoodle8.mods.galacticraft.api.world.ChunkProviderBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class SolChunkProvider extends ChunkProviderBase {

    private static final FlatGeneratorInfo flatGeneratorInfo;

    static {
        flatGeneratorInfo = new FlatGeneratorInfo();
        flatGeneratorInfo.setBiome(Biome.getIdForBiome(Biomes.VOID));
        List<FlatLayerInfo> info = flatGeneratorInfo.getFlatLayers();
        info.add(new FlatLayerInfo(1, Blocks.BEDROCK));
        info.add(new FlatLayerInfo(64, TTSR.blockSolPlasma));
        flatGeneratorInfo.updateLayers();
    }

    private final World world;
    private final Random rand;
    private final ChunkGeneratorFlat flat;

    public SolChunkProvider(World worldIn, long seed, boolean mapFeaturesEnabled) {
        this.world = worldIn;
        this.rand = new Random(seed);
        this.flat = new ChunkGeneratorFlat(worldIn, seed, mapFeaturesEnabled, flatGeneratorInfo.toString());
    }

    @Nonnull
    @Override
    public Chunk generateChunk(int x, int z) {
        return flat.generateChunk(x, z);
    }

    @Override
    public void populate(int x, int z) {
        flat.populate(x, z);
        if (x == 0 && z == 0) new WorldGenObsidianPlatform().generate(this.world, this.rand, new BlockPos(0, 64, 0));
    }

    @Override
    public boolean generateStructures(@Nonnull Chunk chunkIn, int x, int z) {
        return false;
    }

    @Nonnull
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(@Nonnull EnumCreatureType creatureType, @Nonnull BlockPos pos) {
        return flat.getPossibleCreatures(creatureType, pos);
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(@Nonnull World worldIn, @Nonnull String structureName, @Nonnull BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public void recreateStructures(@Nonnull Chunk chunkIn, int x, int z) {
        flat.recreateStructures(chunkIn, x, z);
    }

    @Override
    public boolean isInsideStructure(@Nonnull World worldIn, @Nonnull String structureName, @Nonnull BlockPos pos) {
        return false;
    }

    private static class WorldGenObsidianPlatform extends WorldGenerator {
        @Override
        public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos) {
            for (int i = -4; i <= 4; i++) {
                for (int j = -(4 - Math.abs(i)); j <= (4 - Math.abs(i)); j++) {
                    BlockPos pos1 = pos.add(i, 0, j);
                    world.setBlockState(pos1, Blocks.OBSIDIAN.getDefaultState(), 1);
                }
            }
            return true;
        }
    }
}
