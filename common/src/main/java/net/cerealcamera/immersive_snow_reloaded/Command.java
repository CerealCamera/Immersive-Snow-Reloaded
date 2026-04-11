package net.cerealcamera.immersive_snow_reloaded;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Objects;

public class Command {
    private static final Heightmap.Types[] HEIGHTMAPS = {Heightmap.Types.WORLD_SURFACE, Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES};

    private static void sendResponse(CommandContext<CommandSourceStack> context, String message) {
        context.getSource().sendSystemMessage(Component.literal(message));
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = LiteralArgumentBuilder.literal("snow");

        // Recalculate
        LiteralArgumentBuilder<CommandSourceStack> recalculate = LiteralArgumentBuilder.literal("recalculate");
        recalculate.executes(Command::recalculate);
        command.then(recalculate);

        // Forget
        LiteralArgumentBuilder<CommandSourceStack> forget = LiteralArgumentBuilder.literal("forget");
        forget.executes(Command::forget);
        command.then(forget);

        // Heightmaps
        LiteralArgumentBuilder<CommandSourceStack> heightmaps = LiteralArgumentBuilder.literal("heightmaps");
        heightmaps.executes(Command::heightmaps);
        command.then(heightmaps);

        dispatcher.register(command);
    }

    private static int recalculate(CommandContext<CommandSourceStack> context) {
        BlockPos blockPos = Objects.requireNonNull(context.getSource().getPlayer()).blockPosition();

        ChunkPos pos = new ChunkPos(blockPos);
        Queue.add(new Queue.Entry(pos, 0), true);
        sendResponse(context, String.format("Added chunk at %s %s to queue", pos.x, pos.z));
        return 1;
    }

    private static int forget(CommandContext<CommandSourceStack> context) {
        Queue.clear();
        Memory.erase();
        sendResponse(context, "Cleared queue and forgot all chunks");
        return 1;
    }

    private static int heightmaps(CommandContext<CommandSourceStack> context) {
        BlockPos playerPos = Objects.requireNonNull(context.getSource().getPlayer()).blockPosition();

        sendResponse(context, String.format("Block %s %s:", playerPos.getX(), playerPos.getZ()));

        for (Heightmap.Types heightmap : HEIGHTMAPS) {
            BlockPos blockPos = context.getSource().getLevel().getHeightmapPos(heightmap, playerPos);
            BlockState blockState = context.getSource().getLevel().getBlockState(blockPos);
            Block block = blockState.getBlock();

            BlockPos belowPos = blockPos.below();
            BlockState belowState = context.getSource().getLevel().getBlockState(belowPos);
            Block below = belowState.getBlock();

            String message = String.format("- %s: %s (%s); %s (%s)", heightmap.getSerializedName(), blockPos.toShortString(), block, belowPos.toShortString(), below);
            sendResponse(context, message);
        }

        return 1;
    }
}
