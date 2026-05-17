package com.FuBangkun.tothestarsremake.mixin;

import micdoodle8.mods.galacticraft.api.galaxies.Star;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

@Pseudo
@Mixin(targets = "asmodeuscore.core.astronomy.gui.screen.NewGuiCelestialSelection", remap = false)
public class NewGuiCelestialSelectionMixin {
    @ModifyConstant(
            method = "mouseClicked(III)V",
            slice = @Slice(
                    to = @At(
                            value = "INVOKE",
                            target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"
                    )
            ),
            constant = @Constant(
                    classValue = Star.class,
                    ordinal = 2
            ),
            remap = false
    )
    private Class<?> redirectOnlyTargetStarClass(Object instance, Class<?> type) {
        return NewGuiCelestialSelectionMixin.class;
    }
}