package net.cerealcamera.immersive_snow_reloaded;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class Entrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ImmersiveSnowReloaded.init();

        ServerLevelEvents.LOAD.register((_, level) -> ImmersiveSnowReloadedEvents.onLevelLoad(level));
        ServerLevelEvents.UNLOAD.register((_, level) -> ImmersiveSnowReloadedEvents.onLevelUnload(level));
        ServerChunkEvents.CHUNK_LOAD.register((level, chunk, _) -> ImmersiveSnowReloadedEvents.onChunkLoad(level, chunk));
        ServerTickEvents.START_LEVEL_TICK.register(ImmersiveSnowReloadedEvents::onWorldTick);

        CommandRegistrationCallback.EVENT.register((dispatcher, _1, _2) -> Command.register(dispatcher));
    }
}
