package net.cerealcamera.immersive_snow_reloaded.hook;

import glitchcore.event.EventManager;
import net.cerealcamera.immersive_snow_reloaded.ImmersiveSnowReloadedEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import sereneseasons.api.season.SeasonChangedEvent;
import sereneseasons.season.SeasonHooks;

// Code adapted from Snow Real Magic mod
// https://github.com/Snownee/SnowRealMagic/blob/d6ea96f5a2eefc4bf25f233417e33f1e0690eef2/src/main/java/snownee/snow/compat/sereneseasons/SereneSeasonsCompat.java

public class SereneSeasonsHook {

    public static void init() {
        EventManager.addListener((SeasonChangedEvent.Standard event) -> {
            Level level = event.getLevel();
            if (level.isClientSide() || !level.dimension().identifier().toString().equals("minecraft:overworld")) return;
            ImmersiveSnowReloadedEvents.onSeasonChange((ServerLevel) level);
        });
    }

    public static boolean shouldMelt(Level level, Biome biome, BlockPos pos) {
        return !coldEnoughToSnow(level, biome, pos);
    }

    public static boolean coldEnoughToSnow(Level level, Biome biome, BlockPos pos) {
        return SeasonHooks.getBiomeTemperature(level, Holder.direct(biome), pos, level.getSeaLevel()) < 0.15F;
    }
}
