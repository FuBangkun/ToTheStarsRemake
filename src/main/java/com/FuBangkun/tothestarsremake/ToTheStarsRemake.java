package com.FuBangkun.tothestarsremake;

import com.FuBangkun.tothestarsremake.asm.api.LandableStar;
import com.FuBangkun.tothestarsremake.asm.api.StarRegistry;
import com.FuBangkun.tothestarsremake.asm.api.StarWorldUtil;
import com.FuBangkun.tothestarsremake.haveasoltime.*;
import com.FuBangkun.tothestarsremake.haveasoltime.proxy.HSTProxy;
import com.FuBangkun.tothestarsremake.tothestarsremake.Tags;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemBucketGC;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Arrays;

@Mod(modid = Tags.MOD_ID, version = Tags.VERSION, dependencies = "required-after:galacticraftcore;required-after:galacticraftplanets")
public class ToTheStarsRemake {

    public static Logger logger;

    @Mod.Instance(Tags.MOD_ID)
    public static ToTheStarsRemake instance;

    @SidedProxy(clientSide = "com.FuBangkun.tothestarsremake.haveasoltime.proxy.HSTProxyClient", serverSide = "com.FuBangkun.tothestarsremake.haveasoltime.proxy.HSTProxy")
    public static HSTProxy proxy;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    public static Fluid solPlasma;
    public static Fluid solPlasmaHST;
    public static final MaterialLiquid materialSolPlasma = new MaterialLiquid(MapColor.YELLOW);
    public static LandableStar starSol;

    public static int dimSolId = -5430;
    public static DimensionType dimSol;
    public static Biome biomeSolFlat;

    @Mod.EventHandler
    public void contruction(FMLConstructionEvent event) {
        proxy.construction(event);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        ToTheStarsRemake.starSol = new LandableStar("sol").setParentSolarSystem(GalacticraftCore.solarSystemSol);

        MinecraftForge.EVENT_BUS.register(new EventHandlerSol());

        if (!FluidRegistry.isFluidRegistered("sol_plasma")) {
            solPlasmaHST = new Fluid("sol_plasma",
                    new ResourceLocation("tothestarsremake:blocks/fluids/sol_plasma_still"),
                    new ResourceLocation("tothestarsremake:blocks/fluids/sol_plasma_flow"),
                    Color.YELLOW)
                    .setDensity((int) Math.round(HSTHelper.realDensityToForgeDensity(1410.0))) // 1410.0 kg/(m^3)
                    .setLuminosity(15)
                    .setViscosity((int) Math.round(HSTHelper.realViscosityToForgeViscosity(0.000041))) // 4.1 x 10^-5 Pa-s
                    .setTemperature(5778) //5778 K
                    .setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA)
                    .setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
                    .setRarity(EnumRarity.RARE);
            FluidRegistry.registerFluid(solPlasmaHST);
        } else {
            ToTheStarsRemake.logger.info("ToTheStarsRemake Sol Plasma Fluid is not default, issues may occur.");
        }

        solPlasma = FluidRegistry.getFluid("sol_plasma");

        if (solPlasma.getBlock() == null) {
            SolBlocks.blockSolPlasma = new BlockFluidSunPlasma(solPlasma, materialSolPlasma).initBlackList()
                    .setTranslationKey("sol_plasma");
            ((BlockFluidSunPlasma) SolBlocks.blockSolPlasma).setQuantaPerBlock(3);
            SolBlocks.registerBlock(SolBlocks.blockSolPlasma, ItemBlockDesc.class);
            solPlasma.setBlock(SolBlocks.blockSolPlasma);
        } else {
            ToTheStarsRemake.logger.info("ToTheStarsRemake Sol Plasma Block is not default, issues may occur.");
            SolBlocks.blockSolPlasma = solPlasma.getBlock();
        }

        if (SolBlocks.blockSolPlasma != null) {
            FluidRegistry.addBucketForFluid(solPlasma); // Create a Universal
            // Bucket AS WELL AS our
            // type, this is needed to
            // pull fluids out of other
            // mods tanks
            SolItems.bucketSunPlasma = new ItemBucketGC(SolBlocks.blockSolPlasma, solPlasma).setTranslationKey("bucket_sol_plasma");
            SolItems.registerItem(SolItems.bucketSunPlasma);
            EventHandlerGC.bucketList.put(SolBlocks.blockSolPlasma, SolItems.bucketSunPlasma);
        }

        SolBlocks.initBlocks();
        SolItems.initItems();

        biomeSolFlat = new BiomeSunGenBaseGC(new Biome.BiomeProperties("sol")
                .setBaseHeight(1.5F)
                .setHeightVariation(0.4F)
                .setRainfall(0.0F)
                .setRainDisabled()
                .setWaterColor(0xFFFF00)
                .setTemperature((float) HSTHelper.realTemperatureToMinecraftTemperature(((double) solPlasma.getTemperature()) - 273.15)));

        ToTheStarsRemake.starSol.setBiomeInfo(biomeSolFlat);

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SolBlocks.oreDictRegistration();

        ToTheStarsRemake.starSol.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/sun.png"));
        ToTheStarsRemake.starSol.setDimensionInfo(dimSolId, WorldProviderSol.class).setTierRequired(3);
        ToTheStarsRemake.starSol.atmosphereComponent(EnumAtmosphericGas.HELIUM)
                .atmosphereComponent(EnumAtmosphericGas.HYDROGEN);

        GalaxyRegistry.register(ToTheStarsRemake.starSol);
        GalacticraftRegistry.registerRocketGui(WorldProviderSol.class, new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/overworld_rocket_gui.png"));
        GalacticraftRegistry.registerTeleportType(WorldProviderSol.class, new TeleportTypeSol());

        //Biomes
        ForgeRegistries.BIOMES.register(biomeSolFlat);

        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        for (CelestialBody body : StarRegistry.getLandableStars()) {
            if (body.shouldAutoRegister()) {
                int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, body.getDimensionID());
                DimensionType type = GalacticraftRegistry.registerDimension(body.getTranslationKey(), body.getDimensionSuffix(), body.getDimensionID(), body.getWorldProvider(), body.getForceStaticLoad() || id < 0);
                if (type != null) {
                    body.initialiseMobSpawns();
                } else {
                    body.setUnreachable();
                    ToTheStarsRemake.logger.error("Tried to register dimension for body: {} hit conflict with ID {}", body.getTranslationKey(), body.getDimensionID());
                }
            }

            if (body.getSurfaceBlocks() != null) {
                TransformerHooks.spawnListAE2_GC.addAll(body.getSurfaceBlocks());
            }
        }
        ToTheStarsRemake.dimSol = WorldUtil.getDimensionTypeById(dimSolId);
        GalacticraftCore.solarSystemSol.setMainStar(ToTheStarsRemake.starSol);

        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        for (CelestialBody body : StarRegistry.getLandableStars()) {
            if (body.shouldAutoRegister()) {
                if (!StarWorldUtil.registerStar(body.getDimensionID(), body.isReachable(), 0)) {
                    body.setUnreachable();
                }
            }
        }
    }
}
