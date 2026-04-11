package net.cerealcamera.immersive_snow_reloaded.neoforge;

import net.cerealcamera.immersive_snow_reloaded.ImmersiveSnowReloaded;
import net.neoforged.fml.common.Mod;

@Mod(ImmersiveSnowReloaded.MOD_ID)
public class Entrypoint {
    public Entrypoint() {
        ImmersiveSnowReloaded.init();
    }
}
