package net.cerealcamera.immersivesnow_reloaded;

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
}
