package com.FuBangkun.tothestarsremake.sol.proxy;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class HSTProxy {
    public void construction(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void preInit(FMLPreInitializationEvent event) {}

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}

    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData) {}
}
