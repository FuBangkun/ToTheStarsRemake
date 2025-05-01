package com.FuBangkun.tothestarsremake;

import com.FuBangkun.tothestarsremake.mixin.TTSREarlyInit;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;

public class StarWorldUtil {
    public static List<Integer> registeredStars;

    public static boolean registerStar(int id, boolean initialiseDimensionAtServerInit, int defaultID) {
        if (StarWorldUtil.registeredStars == null) {
            StarWorldUtil.registeredStars = new ArrayList<>();
        }

        if (initialiseDimensionAtServerInit) {
            if (!DimensionManager.isDimensionRegistered(id)) {
                DimensionManager.registerDimension(id, WorldUtil.getDimensionTypeById(id));
                TTSREarlyInit.logger.info("Registered Dimension: {}", id);
                StarWorldUtil.registeredStars.add(id);
            } else if (DimensionManager.getProviderType(id).getId() == id && GalacticraftRegistry.isDimensionTypeIDRegistered(id)) {
                TTSREarlyInit.logger.info("Re-registered dimension: {}", id);
                StarWorldUtil.registeredStars.add(id);
            } else {
                TTSREarlyInit.logger.error("Dimension already registered: unable to register planet dimension {}", id);
                // Add 0 to the list to preserve the correct order of the other planets (e.g. if server/client initialise with different dimension IDs in configs, the order becomes important for figuring out what is going on)
                registeredStars.add(defaultID);
                return false;
            }
            DimensionType dt = WorldUtil.getDimensionTypeById(id);
            WorldProvider wp = dt.createDimension();
            WorldUtil.dimNames.put(id, WorldUtil.getDimensionName(wp));
            return true;
        }

        // Not to be initialised - still add to the registered stars list (for hotloading later?)
        StarWorldUtil.registeredStars.add(id);
        return true;
    }
}
