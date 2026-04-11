package net.cerealcamera.immersivesnow_reloaded.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftServer.class)
public interface MinecraftServerInvoker {
    @Invoker("haveTime")
    boolean snow$hasTimeRemaining();
}
