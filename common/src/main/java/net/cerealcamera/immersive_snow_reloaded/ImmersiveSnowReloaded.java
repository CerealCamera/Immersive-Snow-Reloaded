package net.cerealcamera.immersive_snow_reloaded;

import net.cerealcamera.immersive_snow_reloaded.hook.SereneSeasonsHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImmersiveSnowReloaded {
    public static final String MOD_ID = "immersive_snow_reloaded";
    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        Configuration.load();
        if (ModHooks.sereneSeasonsLoaded()) SereneSeasonsHook.init();
    }
}
