package com.FuBangkun.tothestarsremake.sol;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenObsidianPlatform extends WorldGenerator {
    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {

        for (int i = -4; i <= 4; i++) {
            for (int j = -(4 - Math.abs(i)); j <= (4 - Math.abs(i)); j++) {
                BlockPos pos1 = pos.add(i, 0, j);
                world.setBlockState(pos1, Blocks.OBSIDIAN.getDefaultState(), 1);
            }
        }
        return true;
    }
}
