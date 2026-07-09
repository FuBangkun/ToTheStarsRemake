package com.fubangkun.tothestarsremake.mixin;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;
import zone.rong.mixinbooter.service.ModDiscoverer;

@SuppressWarnings("unused")
public class TTSRMixinConnector implements IMixinConnector {
	@Override
	public void connect() {
		Mixins.addConfiguration("mixins.tothestarsremake.json");
		if (ModDiscoverer.isModPresent("asmodeuscore")) {
			Mixins.addConfiguration("mixins.tothestarsremake.asmodeuscore.json");
		}
		if (ModDiscoverer.isModPresent("extraplanets")) {
			Mixins.addConfiguration("mixins.tothestarsremake.extraplanets.json");
		}
	}
}