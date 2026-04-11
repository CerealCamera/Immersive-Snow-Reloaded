package net.cerealcamera.immersivesnow_reloaded.neoforge;

import net.cerealcamera.immersivesnow_reloaded.ImmersiveSnowReloadedEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.cerealcamera.immersivesnow_reloaded.Command;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class EventListener {
    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        LevelAccessor level = event.getLevel();
        if (level.isClientSide()) return;
        ImmersiveSnowReloadedEvents.onLevelLoad((ServerLevel) level);
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        LevelAccessor level = event.getLevel();
        if (level.isClientSide()) return;
        ImmersiveSnowReloadedEvents.onLevelUnload((ServerLevel) level);
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        LevelAccessor level = event.getLevel();
        if (level.isClientSide()) return;
        ImmersiveSnowReloadedEvents.onChunkLoad((ServerLevel) level, event.getChunk());
    }

    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;
        ImmersiveSnowReloadedEvents.onWorldTick((ServerLevel) level);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        Command.register(event.getDispatcher());
    }
}
