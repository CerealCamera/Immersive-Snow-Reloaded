package net.cerealcamera.immersive_snow_reloaded;

import net.minecraft.world.level.ChunkPos;

import java.util.HashMap;

public class Memory {
    private static final HashMap<ChunkPos, Double> data = new HashMap<>();

    public static void remember(ChunkPos chunkPos) {
        data.put(chunkPos, Logic.getProcessChance());
    }

    public static void forget(ChunkPos chunkPos) {
        data.remove(chunkPos);
    }

    public static boolean hasForgotten(ChunkPos chunkPos) {
        double rememberedProcessChance = data.getOrDefault(chunkPos, -Configuration.data.memoryThreshold);
        double difference = Logic.getProcessChance() - rememberedProcessChance;

        boolean hasForgotten = difference >= Configuration.data.memoryThreshold;
        if (hasForgotten) data.remove(chunkPos); // Clear out so memory doesn't grow forever
        return hasForgotten;
    }

    public static void erase() {
        data.clear();
    }
}
