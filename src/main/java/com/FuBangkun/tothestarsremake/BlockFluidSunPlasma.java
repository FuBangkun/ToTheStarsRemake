package com.FuBangkun.tothestarsremake;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockFluidSunPlasma extends BlockFluidClassic {
    private static final List<Integer> blacklist = new ArrayList<>();

    public BlockFluidSunPlasma(Fluid fluid, Material material) {
        super(fluid, material);
    }

    /**
     * To prevent lag in dimensions.
     *
     * @return Itself
     */
    public BlockFluidSunPlasma initBlackList() {
        blacklist.addAll(Stream.of(Blocks.AIR, Blocks.BEDROCK, Blocks.OBSIDIAN).map(Block::getIdFromBlock).collect(Collectors.toSet()));
        return this;
    }

    protected boolean canFlowInto(IBlockAccess world, BlockPos pos) {
        return (checkOkSurroundings((World) world, pos) && world.getBlockState(pos).getBlock() == this) || canDisplace(world, pos);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos) {
        return ((world.getBlockState(pos).getBlock().blockResistance < 1000.0F) && (checkOkSurroundings((World) world, pos) && world.getBlockState(pos).getBlock() == this)) || super.canDisplace(world, pos);
    }

    @Override
    public void onEntityCollision(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, Entity entityIn) {
        entityIn.attackEntityFrom((new DamageSource("sol_plasma")).setFireDamage(), 100.0F);
        super.onEntityCollision(worldIn, pos, state, entityIn);
    }

    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        for (EnumFacing rotation1 : EnumFacing.VALUES) {
            for (EnumFacing rotation2 : EnumFacing.VALUES) {
                if (rotation1 == rotation2) continue;
                BlockPos pos1 = pos.offset(rotation1);
                BlockPos pos2 = pos1.offset(rotation2);
                if (checkOk(world, pos1)) world.setBlockToAir(pos1);
                if (checkOk(world, pos2)) world.setBlockToAir(pos2);
            }
        }

        super.updateTick(world, pos, state, rand);
    }

    public boolean checkOkSurroundings(World world, BlockPos pos) {
        return checkOk(world, pos.north()) && checkOk(world, pos.east()) && checkOk(world, pos.south()) && checkOk(world, pos.west()) && checkOk(world, pos.north()) && checkOk(world, pos.south());
    }

    public boolean checkOk(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return !blacklist.contains(Block.getIdFromBlock(block)) && (block != this) && !world.isUpdateScheduled(pos, block) && pos.getY() >= 0;
    }
}
