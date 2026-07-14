package net.cerealcamera.immersive_snow_reloaded;

import java.util.ArrayList;

public class ConfigurationData {
    /**
     * The minimum amount of chunks to be processed per tick. By default, Minecraft's "has time remaining to process" is
     * designed for I/O, and as such spuriously reports there's no time remaining to perform any tasks, when there is.
     * As such, this option specifies the minimum number of chunks to process regardless, even when Minecraft says that
     * there is no time remaining.
     */
    public int minChunksToProcessPerTick = 1;

    /**
     * The maximum amount of chunks to be processed per tick. Regardless if Minecraft says there's time remaining, this
     * is the maximum number of chunks that will be processed in a single tick. Once Minecraft reports no more time
     * remaining in that tick, chunk processing stops (unless fewer chunks have been processed than the minimum)
     */
    public int maxChunksToProcessPerTick = 10;

    /**
     * How long to wait before processing the same chunk (in seconds).
     */
    public long memoryDuration = 600;

    /**
     * Whether to only replace air, leaf litter, ice and Snow! Real Magic! compatible blocks.
     * (should have parity with the normal mode)
     */
    public boolean strictReplacement = false;

    /**
     * Whether biomeBlacklist should be a blacklist.
     */
    public boolean isBlacklist = true;

    /**
     * What biomes to skip when processing chunks.
     */
    public ArrayList<String> biomeBlacklist = new ArrayList<>();
}
