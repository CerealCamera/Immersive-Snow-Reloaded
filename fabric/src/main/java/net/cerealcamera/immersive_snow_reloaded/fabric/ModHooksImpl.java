package net.cerealcamera.immersive_snow_reloaded.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class ModHooksImpl {
    private static final boolean SERENE_SEASONS = FabricLoader.getInstance().isModLoaded("sereneseasons");

    public static boolean sereneSeasonsLoaded() {
        return SERENE_SEASONS;
    }
}
