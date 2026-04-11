package net.cerealcamera.immersivesnow_reloaded.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class ModHooksImpl {
    private static final boolean SERENE_SEASONS = FabricLoader.getInstance().isModLoaded("sereneseasons");
    private static final boolean SNOW_REAL_MAGIC = FabricLoader.getInstance().isModLoaded("snowrealmagic");

    public static boolean sereneSeasonsLoaded() {
        return SERENE_SEASONS;
    }

    public static boolean snowRealMagicLoaded() {
        return SNOW_REAL_MAGIC;
    }
}
