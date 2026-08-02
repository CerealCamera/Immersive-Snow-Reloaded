package net.cerealcamera.immersive_snow_reloaded.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class ModHooksImpl {
    private static final boolean SERENE_SEASONS = FabricLoader.getInstance().isModLoaded("sereneseasons");
    private static final boolean SNOW_REAL_MAGIC = FabricLoader.getInstance().isModLoaded("snowrealmagic");
    private static final boolean VANILLA_BACKPORT = FabricLoader.getInstance().isModLoaded("vanillabackport");

    public static boolean sereneSeasonsLoaded() {
        return SERENE_SEASONS;
    }

    public static boolean snowRealMagicLoaded() {
        return SNOW_REAL_MAGIC;
    }
}
