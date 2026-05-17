package com.FuBangkun.tothestarsremake;

import com.FuBangkun.tothestarsremake.block.BlockFluidSunPlasma;
import com.FuBangkun.tothestarsremake.celestial.StarRegistry;
import com.FuBangkun.tothestarsremake.dimension.StarSkyProvider;
import com.FuBangkun.tothestarsremake.dimension.StarTeleportType;
import com.FuBangkun.tothestarsremake.dimension.WorldProviderStar;
import com.FuBangkun.tothestarsremake.helper.TTSRHelper;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Arrays;

@Mod(modid = Tags.MOD_ID, version = Tags.VERSION, dependencies = "required-after:galacticraftcore;required-after:galacticraftplanets;required-after:mixinbooter@[10.0,);before:asmodeuscore")
public class TTSR {
    public static final MaterialLiquid materialSolPlasma = new MaterialLiquid(MapColor.YELLOW);
    public static Block blockSolPlasma;
    public static Logger logger;
    public static Fluid solPlasma;
    public static Fluid solPlasmaTTSR;
    public static Biome biomeSolFlat;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        if (!FluidRegistry.isFluidRegistered("sol_plasma")) {
            solPlasmaTTSR = new Fluid("sol_plasma",
                    new ResourceLocation("tothestarsremake:blocks/fluids/sol_plasma_still"),
                    new ResourceLocation("tothestarsremake:blocks/fluids/sol_plasma_flow"),
                    Color.YELLOW)
                    .setDensity((int) Math.round(TTSRHelper.realDensityToForgeDensity(1410.0))) // 1410.0 kg/(m^3)
                    .setLuminosity(15)
                    .setViscosity((int) Math.round(TTSRHelper.realViscosityToForgeViscosity(0.000041))) // 4.1 x 10^-5 Pa-s
                    .setTemperature(5778) //5778 K
                    .setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA)
                    .setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
                    .setRarity(EnumRarity.RARE);
            FluidRegistry.registerFluid(solPlasmaTTSR);
        } else {
            TTSR.logger.info("ToTheStarsRemake Sol Plasma Fluid is not default, issues may occur.");
        }

        solPlasma = FluidRegistry.getFluid("sol_plasma");

        if (solPlasma.getBlock() == null) {
            blockSolPlasma = new BlockFluidSunPlasma(solPlasma, materialSolPlasma).setTranslationKey("sol_plasma");
            ((BlockFluidSunPlasma) blockSolPlasma).setQuantaPerBlock(3);
            GCBlocks.register(Tags.MOD_ID, blockSolPlasma, ItemBlockDesc.class);
            solPlasma.setBlock(blockSolPlasma);
        } else {
            TTSR.logger.info("ToTheStarsRemake Sol Plasma Block is not default, issues may occur.");
            blockSolPlasma = solPlasma.getBlock();
        }

        if (blockSolPlasma != null) {
            FluidRegistry.addBucketForFluid(solPlasma);
        }

        biomeSolFlat = new BiomeSolGen(new Biome.BiomeProperties("sol")
                .setBaseHeight(1.5F)
                .setHeightVariation(0.4F)
                .setRainfall(0.0F)
                .setRainDisabled()
                .setWaterColor(0xFFFF00)
                .setTemperature((float) TTSRHelper.realTemperatureToMinecraftTemperature(((double) solPlasma.getTemperature()) - 273.15)));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        GalacticraftRegistry.registerRocketGui(WorldProviderStar.class, new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/overworld_rocket_gui.png"));
        GalacticraftRegistry.registerTeleportType(WorldProviderStar.class, new StarTeleportType());

        ForgeRegistries.BIOMES.register(biomeSolFlat);

        MinecraftForge.EVENT_BUS.register(new TickHandlerClient());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        for (Star body : StarRegistry.getLandableStars()) {
            if (body.shouldAutoRegister()) {
                int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, body.getDimensionID());
                DimensionType type = GalacticraftRegistry.registerDimension(body.getTranslationKey(), body.getDimensionSuffix(), body.getDimensionID(), body.getWorldProvider(), body.getForceStaticLoad() || id < 0);
                if (type != null) body.initialiseMobSpawns();
                if (type != null && !DimensionManager.isDimensionRegistered(body.getDimensionID())) {
                    try {
                        DimensionManager.registerDimension(body.getDimensionID(), type);
                        WorldUtil.dimNames.put(body.getDimensionID(), body.getTranslationKey());
                    } catch (IllegalArgumentException e) {
                        TTSR.logger.error("Failed to register Forge dimension mapping for {} ({})", body.getTranslationKey(), body.getDimensionID(), e);
                    }
                }
                else {
                    body.setUnreachable();
                    TTSR.logger.error("Tried to register dimension for body: {} hit conflict with ID {}", body.getTranslationKey(), body.getDimensionID());
                }
            }

            if (body.getSurfaceBlocks() != null) {
                TransformerHooks.spawnListAE2_GC.addAll(body.getSurfaceBlocks());
            }
        }
    }


    public static class TickHandlerClient {
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void onClientTick(TickEvent.ClientTickEvent event) {
            final Minecraft minecraft = FMLClientHandler.instance().getClient();

            final WorldClient world = minecraft.world;

            if (world != null) {
                if (world.provider instanceof WorldProviderStar) {
                    if (world.provider.getSkyRenderer() == null) {
                        world.provider.setSkyRenderer(new StarSkyProvider());
                    }

                    if (world.provider.getCloudRenderer() == null) {
                        world.provider.setCloudRenderer(new CloudRenderer());
                    }
                }
            }
        }
    }

    private static class BiomeSolGen extends BiomeGenBaseGC {
        public BiomeSolGen(BiomeProperties var1) {
            super(var1, true);
        }

        @Override
        public void registerTypes(Biome b) {
            BiomeDictionary.addTypes(b, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DEAD);
            super.registerTypes(b);
        }
    }
}
