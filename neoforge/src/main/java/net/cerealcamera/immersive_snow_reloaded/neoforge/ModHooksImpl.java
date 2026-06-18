package net.cerealcamera.immersive_snow_reloaded.neoforge;

import net.neoforged.fml.ModList;

public class ModHooksImpl {
    private static final boolean SERENE_SEASONS = ModList.get().getModFileById("sereneseasons") != null;

    public static boolean sereneSeasonsLoaded() {
        return SERENE_SEASONS;
    }
}
