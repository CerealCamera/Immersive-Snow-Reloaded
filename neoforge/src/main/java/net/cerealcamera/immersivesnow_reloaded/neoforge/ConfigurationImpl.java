package net.cerealcamera.immersivesnow_reloaded.neoforge;

import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ConfigurationImpl {
    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}