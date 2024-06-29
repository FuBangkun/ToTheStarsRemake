package com.FuBangkun.tothestarsremake.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zone.rong.mixinbooter.ILateMixinLoader;

@IFMLLoadingPlugin.SortingIndex(TTSCoremod.SORTINGINDEX)
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions("com.FuBangkun.tothestarsremake.asm")
public class TTSCoremod implements IFMLLoadingPlugin, ILateMixinLoader {
	public static final int SORTINGINDEX = 3;
	public static final Logger logger = LogManager.getLogger("ToTheStarsRemakeCore");

	public TTSCoremod() {
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.tothestarsremake.json");
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return "com.FuBangkun.tothestarsremake.asm.TTSModContainer";
	}

	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	@Override
	public List<String> getMixinConfigs() {
		return Collections.singletonList("mixins.tothestarsremake.json");
	}
}