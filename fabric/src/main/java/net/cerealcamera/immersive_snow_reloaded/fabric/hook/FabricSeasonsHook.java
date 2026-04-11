package net.cerealcamera.immersive_snow_reloaded.fabric.hook;

import io.github.lucaargolo.seasons.FabricSeasons;
import io.github.lucaargolo.seasons.utils.Season;
import net.cerealcamera.immersive_snow_reloaded.ImmersiveSnowReloadedEvents;
import net.minecraft.server.level.ServerLevel;

public class FabricSeasonsHook {
    public enum SeasonProgress {
        EARLY,
        MID,
        LATE;
    }

    private static Season season;
    private static SeasonProgress progress;


    public static void onTick(ServerLevel level) {
        if (!level.dimension().location().toString().equals("minecraft:overworld")) return;
        Season currentSeason = FabricSeasons.getCurrentSeason(level);

        // Fabric Seasons doesn't have "sub-seasons", so split each season into thirds and figure it out from there.
        // TODO: change from hardcoded speed factors to real-time calculation, to make transitions smoother?
        long seasonLength = currentSeason.getSeasonLength();
        long timeToNextSeason = FabricSeasons.getTimeToNextSeason(level);
        long rawProgress = seasonLength - timeToNextSeason;

        SeasonProgress currentProgress;
        if (FabricSeasons.CONFIG.isSeasonTiedWithSystemTime()) currentProgress = SeasonProgress.MID;

        // To avoid floating point bugs
        else if (rawProgress < (seasonLength/3)) currentProgress = SeasonProgress.EARLY;
        else if (rawProgress < (seasonLength*2/3)) currentProgress = SeasonProgress.MID;
        else currentProgress = SeasonProgress.LATE;

        if (currentSeason != season || currentProgress != progress) {
            season = currentSeason;
            progress = currentProgress;
            ImmersiveSnowReloadedEvents.onSeasonChange(level);
        }
    }
}
