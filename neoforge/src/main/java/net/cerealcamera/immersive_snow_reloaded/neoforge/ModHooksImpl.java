package net.cerealcamera.immersive_snow_reloaded.neoforge;

import net.neoforged.fml.ModList;

public class ModHooksImpl {
    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
