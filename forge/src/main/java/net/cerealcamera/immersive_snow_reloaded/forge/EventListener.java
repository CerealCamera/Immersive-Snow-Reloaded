package net.cerealcamera.immersive_snow_reloaded.forge;

import net.cerealcamera.immersive_snow_reloaded.Command;
import net.cerealcamera.immersive_snow_reloaded.ImmersiveSnowReloadedEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
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
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.side.isClient() || event.phase != TickEvent.Phase.END || !event.haveTime()) return;
        ImmersiveSnowReloadedEvents.onWorldTick((ServerLevel) event.level);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        Command.register(event.getDispatcher());
    }
}
