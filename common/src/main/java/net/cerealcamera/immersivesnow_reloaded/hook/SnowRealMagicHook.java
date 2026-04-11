package net.cerealcamera.immersivesnow_reloaded.hook;

import net.cerealcamera.immersivesnow_reloaded.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import snownee.snow.CoreModule;
import snownee.snow.Hooks;
import snownee.snow.block.*;

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
}
