package net.cerealcamera.immersivesnow_reloaded.neoforge;

import net.neoforged.fml.loading.LoadingModList;

public class ModHooksImpl {
    private static final boolean SERENE_SEASONS = LoadingModList.get().getModFileById("sereneseasons") != null;
    private static final boolean SNOW_REAL_MAGIC = LoadingModList.get().getModFileById("snowrealmagic") != null;

    public static boolean sereneSeasonsLoaded() {
        return SERENE_SEASONS;
    }

    public static boolean snowRealMagicLoaded() {
        return SNOW_REAL_MAGIC;
    }
}
