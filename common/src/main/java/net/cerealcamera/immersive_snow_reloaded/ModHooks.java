package net.cerealcamera.immersive_snow_reloaded;

import net.cerealcamera.immersive_snow_reloaded.platform.Services;

public class ModHooks {
    private static final boolean SERENE_SEASONS = Services.PLATFORM.isModLoaded("sereneseasons");
    private static final boolean SNOW_REAL_MAGIC = Services.PLATFORM.isModLoaded("snowrealmagic");

    public static boolean sereneSeasonsLoaded() {
        return SERENE_SEASONS;
    }

    public static boolean snowRealMagicLoaded() {
        return SNOW_REAL_MAGIC;
    }
}
