package net.cerealcamera.immersivesnow_reloaded.fabric;

import net.cerealcamera.immersivesnow_reloaded.ImmersiveSnowReloaded;
import net.cerealcamera.immersivesnow_reloaded.ImmersiveSnowReloadedEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.cerealcamera.immersivesnow_reloaded.Command;
import net.cerealcamera.immersivesnow_reloaded.fabric.hook.FabricSeasonsHook;

public class Entrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ImmersiveSnowReloaded.init();

        ServerWorldEvents.LOAD.register((_server, level) -> ImmersiveSnowReloadedEvents.onLevelLoad(level));
        ServerWorldEvents.UNLOAD.register((_server, level) -> ImmersiveSnowReloadedEvents.onLevelUnload(level));
        ServerChunkEvents.CHUNK_LOAD.register(ImmersiveSnowReloadedEvents::onChunkLoad);
        ServerTickEvents.START_WORLD_TICK.register(ImmersiveSnowReloadedEvents::onWorldTick);

        if (FabricLoader.getInstance().isModLoaded("seasons")) {
            ServerTickEvents.END_WORLD_TICK.register(FabricSeasonsHook::onTick);
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, _1, _2) -> Command.register(dispatcher));
    }
}
