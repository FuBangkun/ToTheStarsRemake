package com.fubangkun.tothestarsremake.client;

import com.fubangkun.tothestarsremake.Reference;
import com.fubangkun.tothestarsremake.TTSR;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Side.CLIENT)
public class ClientHandler {
    private static final ModelResourceLocation FLUID_MODEL = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "sol_plasma"), "fluid");

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        Item item = Item.getItemFromBlock(TTSR.blockSolPlasma);
        ModelBakery.registerItemVariants(item);
        ModelLoader.setCustomMeshDefinition(item, stack -> FLUID_MODEL);
        ModelLoader.setCustomStateMapper(TTSR.blockSolPlasma,
                new StateMapperBase() {
                    @Nonnull
                    @Override
                    protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                        return FLUID_MODEL;
                    }
                }
        );
    }
}