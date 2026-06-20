package net.cerealcamera.immersive_snow_reloaded;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class ModHooks {
    @ExpectPlatform
    public static boolean sereneSeasonsLoaded() {
        return false;
    }

    @ExpectPlatform
    public static boolean snowRealMagicLoaded() {
        return false;
    }

    @ExpectPlatform
    public static boolean vanillaBackportLoaded() {
        return false;
    }
}
