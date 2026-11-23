package net.cerealcamera.immersive_snow_reloaded.hook;

import dev.imb11.snow.snowlogging.Snowlogging;
import net.cerealcamera.immersive_snow_reloaded.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SnowHook {
    public static boolean canReplaceBlock(BlockState state) {
        return Snowlogging.canSnowlog(state);
    }

    public static void replaceBlock(ServerLevel level, BlockPos pos) {
        Snowlogging.snowlog(level, pos);
        Utils.setBlock(level, pos, Blocks.SNOW.defaultBlockState());
    }
}
