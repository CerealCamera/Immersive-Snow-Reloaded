package net.cerealcamera.immersive_snow_reloaded;

import net.neoforged.fml.ModList;

public class ModHooks {
    private static final boolean SERENE_SEASONS = ModList.get().isLoaded("sereneseasons");
    //private static final boolean SNOW_REAL_MAGIC = ModList.get().isLoaded("snowrealmagic");

    public static boolean sereneSeasonsLoaded() {
        return SERENE_SEASONS;
    }

    public static boolean snowRealMagicLoaded() {
        return false /*SNOW_REAL_MAGIC*/;
    }
}
