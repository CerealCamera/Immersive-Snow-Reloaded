package net.cerealcamera.immersive_snow_reloaded.hook;

import net.cerealcamera.immersive_snow_reloaded.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import snownee.snow.CoreModule;
import snownee.snow.Hooks;
import snownee.snow.block.*;
import snownee.snow.compat.sereneseasons.SereneSeasonsCompat;

public class SnowRealMagicHook {
    public static boolean canReplaceBlock(BlockState state) {
        return Hooks.canContainState(state);
    }

    public static void replaceBlock(ServerLevel world, BlockPos pos, BlockState state) {
        Hooks.convert(world, pos, state, 1, 2, true);
    }

    public static boolean canMelt(BlockState state) {
        Block block = state.getBlock();
        return state.is(CoreModule.SNOW_TAG) || block instanceof SnowSlabBlock || block instanceof SnowStairsBlock || block instanceof SnowWallBlock || block instanceof SnowFenceBlock || block instanceof SnowFenceGateBlock;
    }

    public static void melt(ServerLevel level, BlockPos pos, BlockState state) {
        SnowVariant snow = (SnowVariant) state.getBlock();
        Utils.setBlock(level, pos, snow.srm$getRaw(state, level, pos));
    }

    public static boolean shouldMelt(Level level, BlockPos pos) {
        return !coldEnoughToSnow(level, pos);
    }

    public static boolean coldEnoughToSnow(Level level, BlockPos pos) {
        return SereneSeasonsCompat.coldEnoughToSnow(level, pos, level.getBiome(pos));
    }
}
