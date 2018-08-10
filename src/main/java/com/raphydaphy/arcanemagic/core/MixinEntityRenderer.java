package com.raphydaphy.arcanemagic.core;

import com.raphydaphy.arcanemagic.client.particle.ParticleRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer
{
    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V"))
    public void updateCameraAndRender(float partialTicks, long finishTimeNano, boolean randomBoolean, CallbackInfo info) // RenderWorldLastEvent
    {
        World world = Minecraft.getMinecraft().world;

        if (world != null)
        {
            GlStateManager.pushMatrix();
            ParticleRenderer.getInstance().renderParticles(partialTicks);
            GlStateManager.popMatrix();
        }
    }
}
