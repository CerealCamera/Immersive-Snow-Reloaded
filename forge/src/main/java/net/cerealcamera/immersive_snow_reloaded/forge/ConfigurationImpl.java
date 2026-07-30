package net.cerealcamera.immersive_snow_reloaded.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ConfigurationImpl {
    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
