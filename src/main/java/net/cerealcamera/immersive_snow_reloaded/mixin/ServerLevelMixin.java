package net.cerealcamera.immersive_snow_reloaded.mixin;

import net.cerealcamera.immersive_snow_reloaded.Queue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", shift = At.Shift.AFTER, ordinal = 0))
    private void snow$addToQueue(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        // Apparently injecting here is more reliable and faster than receiving chunk load events. Go figure.
        if (chunk.getLevel().dimension().identifier().toString().equals("minecraft:overworld")) Queue.tryAdd(chunk.getPos(), true);
    }
}
