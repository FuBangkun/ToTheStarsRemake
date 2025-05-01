package com.FuBangkun.tothestarsremake.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zone.rong.mixinbooter.ILateMixinLoader;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class TTSREarlyInit implements IFMLLoadingPlugin, ILateMixinLoader {
    public static final Logger logger = LogManager.getLogger("ToTheStarsRemakeCore");

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.tothestarsremake.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}