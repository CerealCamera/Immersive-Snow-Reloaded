package net.cerealcamera.immersive_snow_reloaded.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class ModHooksImpl {
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
