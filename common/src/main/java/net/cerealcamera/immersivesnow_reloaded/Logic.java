package net.cerealcamera.immersivesnow_reloaded;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.cerealcamera.immersivesnow_reloaded.hook.SereneSeasonsHook;
import net.cerealcamera.immersivesnow_reloaded.hook.SnowRealMagicHook;

public class Logic {
    private static final boolean SERENE_SEASONS = ModHooks.sereneSeasonsLoaded();
    private static final boolean SNOW_REAL_MAGIC = ModHooks.snowRealMagicLoaded();

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
        // Heightmaps always return the block they are looking for... plus one.
        // `topPos` and `topState` are the position and state of the *non-collision block* above (air, snow, etc.)
        // `blockPos` and `blockState` are the actual motion-blocking block (grass, ice, water, leaves, etc.)

        BlockPos blockPos = topPos.below();

        BlockState topState = level.getBlockState(topPos);
        BlockState blockState = level.getBlockState(blockPos);

        Biome biome = level.getBiome(topPos).value();

        /* Snowing and Freezing */
        if (biome.shouldSnow(level, topPos) && !topState.is(Blocks.SNOW) && Blocks.SNOW.defaultBlockState().canSurvive(level, topPos)) {
            Utils.setBlock(level, topPos, Blocks.SNOW.defaultBlockState());
        } else if (biome.shouldFreeze(level, blockPos, false) && !blockState.is(Blocks.ICE)) {
            Utils.setBlock(level, blockPos, Blocks.ICE.defaultBlockState());
        } else if (SNOW_REAL_MAGIC && coldEnoughToSnow(level, biome, topPos)) {
            if (SnowRealMagicHook.canReplaceBlock(topState) && !SnowRealMagicHook.canMelt(topState))
                SnowRealMagicHook.replaceBlock(level, topPos, topState);
            else if (SnowRealMagicHook.canReplaceBlock(blockState) && !SnowRealMagicHook.canMelt(blockState))
                SnowRealMagicHook.replaceBlock(level, blockPos, blockState);
        }

        /* Melting */
        else if (blockState.is(Blocks.ICE) && shouldMelt(level, biome, topPos)) {
            Utils.setBlock(level, blockPos, IceBlock.meltsInto());
            level.neighborChanged(blockPos, IceBlock.meltsInto().getBlock(), blockPos);
        } else if (topState.is(Blocks.SNOW) && shouldMelt(level, biome, topPos)) {
            Utils.setBlock(level, topPos, Blocks.AIR.defaultBlockState());
        } else if (SNOW_REAL_MAGIC && shouldMelt(level, biome, topPos)) {
            if (SnowRealMagicHook.canMelt(topState))
                SnowRealMagicHook.melt(level, topPos, topState);
            else if (SnowRealMagicHook.canMelt(blockState))
                SnowRealMagicHook.melt(level, blockPos, blockState);
        }
    }

    private static boolean shouldMelt(Level level, Biome biome, BlockPos pos) {
        // TODO: seems like light level 11 is one block too much for SRM, but works fine for vanilla?
        boolean brightEnough = level.getBrightness(LightLayer.BLOCK, pos) > 11;
        if (SERENE_SEASONS) return SereneSeasonsHook.shouldMelt(level, biome, pos) || brightEnough;
        return biome.warmEnoughToRain(pos) || brightEnough;
    }

    private static boolean coldEnoughToSnow(Level level, Biome biome, BlockPos pos) {
        boolean darkEnough = level.getBrightness(LightLayer.BLOCK, pos) <= 11;
        if (SERENE_SEASONS) return SereneSeasonsHook.coldEnoughToSnow(level, biome, pos) && darkEnough;
        return biome.coldEnoughToSnow(pos) && darkEnough;
    }
}
