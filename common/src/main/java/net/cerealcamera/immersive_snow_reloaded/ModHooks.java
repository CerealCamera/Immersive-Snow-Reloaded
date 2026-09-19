package net.cerealcamera.immersive_snow_reloaded;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class ModHooks {
    private static final boolean SERENE_SEASONS = isModLoaded("sereneseasons");
    private static final boolean SNOW_REAL_MAGIC = isModLoaded("snowrealmagic");

    public static boolean sereneSeasonsLoaded() {
        return SERENE_SEASONS;
    }

    public static boolean snowRealMagicLoaded() {
        return SNOW_REAL_MAGIC;
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        return false;
    }
}
