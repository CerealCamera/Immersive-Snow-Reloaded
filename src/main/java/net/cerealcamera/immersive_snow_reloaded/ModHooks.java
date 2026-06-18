package net.cerealcamera.immersive_snow_reloaded;

import net.neoforged.fml.ModList;

public class ModHooks {
    private static final boolean SERENE_SEASONS = ModList.get().getModFileById("sereneseasons") != null;

    public static boolean sereneSeasonsLoaded() {
        return SERENE_SEASONS;
    }
}
