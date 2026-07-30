package net.cerealcamera.immersive_snow_reloaded;

import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;

public class Queue {
    /** Queue for chunks to be processed in the current tick. */
    private static final ArrayList<Entry> currentTickChunks = new ArrayList<>();

    /** Queue for chunks to be processed in the next tick. */
    private static final ArrayList<Entry> nextTickChunks = new ArrayList<>();

    /** Tries to add a chunk to the queue, only if it has been forgotten. */
    public static void tryAdd(ChunkPos chunkPos, boolean currentTick) {
        if (Memory.hasForgotten(chunkPos)) {
            Memory.remember(chunkPos);
            add(new Entry(chunkPos, 0), currentTick);
        }
    }

    /** Immediately adds an entry to the queue. */
    public static void add(Entry entry, boolean currentTick) {
        if (currentTick) currentTickChunks.add(entry);
        else nextTickChunks.add(entry);
    }

    /** Returns the size of the current-tick queue. */
    public static int size() {
        return currentTickChunks.size();
    }

    /** Returns true if the current-tick queue is empty. */
    public static boolean isEmpty() {
        return currentTickChunks.isEmpty();
    }

    /** Removes the element at the front of the current-tick queue and returns it. */
    public static Entry pop() {
        return currentTickChunks.remove(0);
    }

    /** Shuffles items from the next-tick queue to the end of the current-tick queue. */
    public static void shuffle() {
        currentTickChunks.addAll(nextTickChunks);
        nextTickChunks.clear();
    }

    /** Clears all items in both the current-tick and next-tick queues. */
    public static void clear() {
        currentTickChunks.clear();
        nextTickChunks.clear();
    }

    public record Entry(ChunkPos pos, int sittingFor) {
    }
}
