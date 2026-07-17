package net.cerealcamera.immersive_snow_reloaded.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Checks the config directory
     *
     * @return The path to the config directory
     */
    Path getConfigDir();
}
