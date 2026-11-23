package net.cerealcamera.immersive_snow_reloaded.hook;

import glitchcore.event.EventManager;
import net.cerealcamera.immersive_snow_reloaded.ImmersiveSnowReloadedEvents;
import net.cerealcamera.immersive_snow_reloaded.Logic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonChangedEvent;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.season.SeasonHooks;

import java.util.ArrayList;
import java.util.Set;

// Code adapted from Snow Real Magic mod
// https://github.com/Snownee/SnowRealMagic/blob/d6ea96f5a2eefc4bf25f233417e33f1e0690eef2/src/main/java/snownee/snow/compat/sereneseasons/SereneSeasonsCompat.java

public class SereneSeasonsHook {
    private static final ArrayList<Season.SubSeason> earlySeasons = new ArrayList<>(Set.of(Season.SubSeason.EARLY_SPRING, Season.SubSeason.EARLY_SUMMER, Season.SubSeason.EARLY_AUTUMN, Season.SubSeason.EARLY_WINTER));

    private static Season.SubSeason getPrevSeason(Season.SubSeason season) {
        return switch (season) {
            case EARLY_SPRING ->  Season.SubSeason.LATE_WINTER;
            case MID_SPRING -> Season.SubSeason.EARLY_SPRING;
            case LATE_SPRING -> Season.SubSeason.MID_SPRING;
            case EARLY_SUMMER -> Season.SubSeason.LATE_SPRING;
            case MID_SUMMER -> Season.SubSeason.EARLY_SUMMER;
            case LATE_SUMMER -> Season.SubSeason.MID_SUMMER;
            case EARLY_AUTUMN -> Season.SubSeason.LATE_SUMMER;
            case MID_AUTUMN -> Season.SubSeason.EARLY_AUTUMN;
            case LATE_AUTUMN -> Season.SubSeason.MID_AUTUMN;
            case EARLY_WINTER -> Season.SubSeason.LATE_AUTUMN;
            case MID_WINTER -> Season.SubSeason.EARLY_WINTER;
            case LATE_WINTER -> Season.SubSeason.MID_WINTER;
        };
    };

    public static void init() {
        EventManager.addListener((SeasonChangedEvent.Standard event) -> {
            Level level = event.getLevel();
            if (level.isClientSide() || !level.dimension().identifier().toString().equals("minecraft:overworld")) return;

            Season.SubSeason prevSeason = event.getPrevSeason();
            Season.SubSeason newSeason = event.getNewSeason();

            if (prevSeason != getPrevSeason(newSeason) && (newSeason.getSeason() == Season.WINTER || newSeason.getSeason() == Season.SPRING)) {
                Logic.setSeasonalTicks(earlySeasons.contains(newSeason) ? 1500 : 4000);
            } else if (prevSeason.getSeason() != newSeason.getSeason()) {
                if (newSeason.getSeason() == Season.WINTER || newSeason.getSeason() == Season.SPRING)
                    Logic.setSeasonalTicks(0);
                else if (newSeason.getSeason() == Season.SUMMER)
                    Logic.setSeasonalTicks(1500);
                else
                    Logic.setSeasonalTicks(3000);
            }

            ImmersiveSnowReloadedEvents.onSeasonChange((ServerLevel) level);
        });
    }

    public static boolean shouldMelt(Level level, Biome biome, BlockPos pos) {
        return !coldEnoughToSnow(level, biome, pos);
    }

    public static boolean coldEnoughToSnow(Level level, Biome biome, BlockPos pos) {
        return SeasonHooks.getBiomeTemperature(level, Holder.direct(biome), pos, level.getSeaLevel()) < 0.15F;
    }

    public static boolean coldEnoughToSnowInSeason(Level level, Biome biome, BlockPos pos, Season.SubSeason season) {
        return SeasonHooks.getBiomeTemperatureInSeason(season, Holder.direct(biome), pos, level.getSeaLevel()) < 0.15F;
    }

    public static Season getCurrentSeason(Level level) {
        return SeasonHelper.getSeasonState(level).getSeason();
    }
}
