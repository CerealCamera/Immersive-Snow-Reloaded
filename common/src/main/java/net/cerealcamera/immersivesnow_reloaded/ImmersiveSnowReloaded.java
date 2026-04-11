package net.cerealcamera.immersivesnow_reloaded;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.cerealcamera.immersivesnow_reloaded.hook.SereneSeasonsHook;

public class ImmersiveSnowReloaded {
    public static final String MOD_ID = "immersivesnow_reloaded";
    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        Configuration.load();
        if (ModHooks.sereneSeasonsLoaded()) SereneSeasonsHook.init();
    }
}
