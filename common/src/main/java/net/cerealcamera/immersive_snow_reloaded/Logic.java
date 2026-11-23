package net.cerealcamera.immersive_snow_reloaded;

import net.cerealcamera.immersive_snow_reloaded.hook.SereneSeasonsHook;
import net.cerealcamera.immersive_snow_reloaded.hook.SnowRealMagicHook;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import sereneseasons.api.season.Season;

import java.util.ArrayList;
import java.util.Set;

public class Logic {
    private static final boolean SERENE_SEASONS = ModHooks.sereneSeasonsLoaded();
    private static final boolean SNOW_REAL_MAGIC = ModHooks.snowRealMagicLoaded();

    private static final ArrayList<String> strictReplacementWhitelist = new ArrayList<>(Set.of("minecraft:air", "minecraft:water", "minecraft:ice", "minecraft:snow", "minecraft:leaf_litter"));

    private static int seasonalTicks = 0;

    /**
     * Iterates over all (X, Z) combinations within a chunk and runs snow recalculation logic on them.
     *
     * @param level The level in which to perform the snow recalculation.
     * @param chunkPos The position of the chunk to recalculate.
     */
    public static void processChunk(ServerLevel level, ChunkPos chunkPos) {
        for (int x = chunkPos.getMinBlockX(); x <= chunkPos.getMaxBlockX(); x++) {
            for (int z = chunkPos.getMinBlockZ(); z <= chunkPos.getMaxBlockZ(); z++) {
                BlockPos column = new BlockPos(x, 0, z);

                BlockPos motionBlockingPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, column);
                Logic.checkAndUpdateBlock(level, motionBlockingPos);

                // This lets us generate & melt snow under trees.
                BlockPos noLeavesPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, column);
                if (motionBlockingPos != noLeavesPos) Logic.checkAndUpdateBlock(level, noLeavesPos);
            }
        }
    }

    /**
     * This is the core logic of the mod. This method is called for every (x, z) combination in a chunk when running a
     * recalculation of the snow.
     *
     * @param level  The level in which to run the calculation.
     * @param topPos The block position given as a result of [ServerLevel::getHeightmapPos].
     */
    public static void checkAndUpdateBlock(ServerLevel level, BlockPos topPos) {
        if (isBiomeBlacklisted(level.getBiome(topPos).getRegisteredName())) return;

        // Heightmaps always return the block they are looking for... plus one.
        // `topPos` and `topState` are the position and state of the *non-collision block* above (air, snow, etc.)
        // `blockPos` and `blockState` are the actual motion-blocking block (grass, ice, water, leaves, etc.)
        BlockPos blockPos = topPos.below();

        BlockState topState = level.getBlockState(topPos);
        BlockState blockState = level.getBlockState(blockPos);

        Biome biome = level.getBiome(topPos).value();
        String topId = BuiltInRegistries.BLOCK.getKey(topState.getBlock()).toString();

        boolean topWhitelisted = isBlockWhitelisted(topId) && isBlockStrictWhitelisted(topId);

        if (SERENE_SEASONS) {
            Season season = SereneSeasonsHook.getCurrentSeason(level);

            if (Math.random() < getProcessChance()) {
                removeLeafLitter(level, biome, topPos, topState, topWhitelisted, season == Season.WINTER || season == Season.SPRING);
                if (snowAndFreeze(level, biome, blockPos, topPos, blockState, topState, topWhitelisted))
                    melt(level, biome, blockPos, topPos, blockState, topState, topWhitelisted);
            } else if (!biome.coldEnoughToSnow(topPos, level.getSeaLevel())) {
                if (season == Season.WINTER) {
                    if (blockState.is(Blocks.ICE)) {
                        Utils.setBlock(level, blockPos, IceBlock.meltsInto());
                        level.neighborChanged(blockPos, IceBlock.meltsInto().getBlock(), null);
                    } else if (!topState.isAir() && (topState.is(Blocks.SNOW) || topState.is(Blocks.LEAF_LITTER))) {
                        Utils.setBlock(level, topPos, Blocks.AIR.defaultBlockState());
                    } else if (SNOW_REAL_MAGIC) {
                        if (SnowRealMagicHook.canMelt(topState))
                            SnowRealMagicHook.melt(level, topPos, topState);
                        else if (SnowRealMagicHook.canMelt(blockState))
                            SnowRealMagicHook.melt(level, blockPos, blockState);
                    }
                } else {
                    if (season == Season.SPRING && SereneSeasonsHook.coldEnoughToSnowInSeason(level, biome, topPos, Season.SubSeason.MID_WINTER)) {
                        if (!topState.is(Blocks.SNOW) && Blocks.SNOW.defaultBlockState().canSurvive(level, topPos) && topWhitelisted) {
                            Utils.setBlock(level, topPos, Blocks.SNOW.defaultBlockState());
                        } else if (SNOW_REAL_MAGIC) {
                            if (SnowRealMagicHook.canReplaceBlock(topState) && !SnowRealMagicHook.canMelt(topState))
                                SnowRealMagicHook.replaceBlock(level, topPos, topState);
                            else if (SnowRealMagicHook.canReplaceBlock(blockState) && !SnowRealMagicHook.canMelt(blockState))
                                SnowRealMagicHook.replaceBlock(level, blockPos, blockState);
                        }
                    } else {
                        melt(level, biome, blockPos, topPos, blockState, topState, topWhitelisted);
                    }
                }
            }
        } else {
            removeLeafLitter(level, biome, topPos, topState, topWhitelisted, false);
            if (snowAndFreeze(level, biome, blockPos, topPos, blockState, topState, topWhitelisted))
                melt(level, biome, blockPos, topPos, blockState, topState, topWhitelisted);
        }
    }

    private static void removeLeafLitter(ServerLevel level, Biome biome, BlockPos blockPos, BlockState blockState, boolean whitelisted, boolean forceRemove) {
        if (blockState.is(Blocks.LEAF_LITTER) && whitelisted) {
            Utils.setBlock(level, blockPos, Blocks.AIR.defaultBlockState());
            if (biome.shouldSnow(level, blockPos) && Blocks.SNOW.defaultBlockState().canSurvive(level, blockPos))
                Utils.setBlock(level, blockPos, Blocks.SNOW.defaultBlockState());
            else if (forceRemove && Blocks.SNOW.defaultBlockState().canSurvive(level, blockPos))
                Utils.setBlock(level, blockPos, Blocks.AIR.defaultBlockState());
            else
                Utils.setBlock(level, blockPos, blockState);
        }
    }

    /** Returns true if no blocks were processed */
    private static boolean snowAndFreeze(ServerLevel level, Biome biome, BlockPos blockPos, BlockPos topPos, BlockState blockState, BlockState topState, boolean topWhitelisted) {
        if (biome.shouldSnow(level, topPos) && !topState.is(Blocks.SNOW) && Blocks.SNOW.defaultBlockState().canSurvive(level, topPos) && topWhitelisted) {
            Utils.setBlock(level, topPos, Blocks.SNOW.defaultBlockState());
        } else if (biome.shouldFreeze(level, blockPos, false) && !blockState.is(Blocks.ICE)) {
            Utils.setBlock(level, blockPos, Blocks.ICE.defaultBlockState());
        } else if (SNOW_REAL_MAGIC && coldEnoughToSnow(level, biome, topPos)) {
            if (SnowRealMagicHook.canReplaceBlock(topState) && !SnowRealMagicHook.canMelt(topState))
                SnowRealMagicHook.replaceBlock(level, topPos, topState);
            else if (SnowRealMagicHook.canReplaceBlock(blockState) && !SnowRealMagicHook.canMelt(blockState))
                SnowRealMagicHook.replaceBlock(level, blockPos, blockState);
            else return true;
        } else return true;
        return false;
    }

    private static void melt(ServerLevel level, Biome biome, BlockPos blockPos, BlockPos topPos, BlockState blockState, BlockState topState, boolean topWhitelisted) {
        if (blockState.is(Blocks.ICE) && shouldMelt(level, biome, topPos)) {
            Utils.setBlock(level, blockPos, IceBlock.meltsInto());
            level.neighborChanged(blockPos, IceBlock.meltsInto().getBlock(), null);
        } else if (topState.is(Blocks.SNOW) && shouldMelt(level, biome, topPos) && topWhitelisted) {
            Utils.setBlock(level, topPos, Blocks.AIR.defaultBlockState());
        } else if (SNOW_REAL_MAGIC && shouldMelt(level, biome, topPos)) {
            if (SnowRealMagicHook.canMelt(topState))
                SnowRealMagicHook.melt(level, topPos, topState);
            else if (SnowRealMagicHook.canMelt(blockState))
                SnowRealMagicHook.melt(level, blockPos, blockState);
        }
    }

    private static boolean isBiomeBlacklisted(String biomeId) {
        if (Configuration.data.isBiomeBlacklist) return Configuration.data.biomeBlacklist.contains(biomeId);
        return !Configuration.data.biomeBlacklist.contains(biomeId);
    }

    private static boolean isBlockWhitelisted(String blockId) {
        if (Configuration.data.isBlockBlacklist) return !Configuration.data.blockBlacklist.contains(blockId);
        return Configuration.data.blockBlacklist.contains(blockId);
    }

    private static boolean isBlockStrictWhitelisted(String blockId) {
        return !Configuration.data.strictReplacement || strictReplacementWhitelist.contains(blockId);
    }

    private static boolean shouldMelt(Level level, Biome biome, BlockPos pos) {
        // TODO: seems like light level 11 is one block too much for SRM, but works fine for vanilla?
        if (level.getBrightness(LightLayer.BLOCK, pos) > 11) return true;
        if (SERENE_SEASONS) {
            if (SNOW_REAL_MAGIC) return SnowRealMagicHook.shouldMelt(level, pos);
            return SereneSeasonsHook.shouldMelt(level, biome, pos);
        }
        return biome.warmEnoughToRain(pos, level.getSeaLevel());
    }

    private static boolean coldEnoughToSnow(Level level, Biome biome, BlockPos pos) {
        if (level.getBrightness(LightLayer.BLOCK, pos) > 11) return false;
        if (SERENE_SEASONS) {
            if (SNOW_REAL_MAGIC) return SnowRealMagicHook.coldEnoughToSnow(level, pos);
            return SereneSeasonsHook.coldEnoughToSnow(level, biome, pos);
        }
        return biome.coldEnoughToSnow(pos, level.getSeaLevel());
    }

    public static double getProcessChance() {
        return seasonalTicks / 2000.0;
    }

    public static void incrementSeasonalTicks(int seasonalTicks) {
        Logic.seasonalTicks = Math.clamp(Logic.seasonalTicks + seasonalTicks, 0, 2000);
    }

    public static void setSeasonalTicks(int seasonalTicks) {
        Logic.seasonalTicks = Math.clamp(seasonalTicks, 0, 2000);
    }
}
