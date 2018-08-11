package com.raphydaphy.arcanemagic.client.particle;


import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;

public class ParticleRenderer
{
    private static ParticleRenderer instance = null;
    private ArrayList<Particle> particles = new ArrayList<Particle>();
    public static int particleCounter = 0;

    public static ParticleRenderer getInstance()
    {
        if (instance == null)
        {
            instance = new ParticleRenderer();
        }
        return instance;
    }

    public void updateParticles()
    {
        ArrayList<Particle> newParticles = new ArrayList<Particle>();
        for (int i = 0; i < particles.size(); i++)
        {
            if (particles.size() > i)
            {
                Particle p = particles.get(i);
                if (p != null)
                {
                    if (p.isAlive())
                    {
                        p.onUpdate();
                        newParticles.add(p);
                    }

                }
            }
        }
        particles = newParticles;

    }

    public void renderParticles(float partialTicks)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        float lvt_3_1_ = ActiveRenderInfo.getRotationX();
        float lvt_4_1_ = ActiveRenderInfo.getRotationZ();
        float lvt_5_1_ = ActiveRenderInfo.getRotationYZ();
        float lvt_6_1_ = ActiveRenderInfo.getRotationXY();
        float lvt_7_1_ = ActiveRenderInfo.getRotationXZ();
        Particle.interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        Particle.interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        Particle.interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
        Particle.cameraViewDir = player.getLook(partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(516, 0.003921569F);

        for (int lvt_8_1_ = 0; lvt_8_1_ < 3; ++lvt_8_1_)
        {
            for (int lvt_9_1_ = 0; lvt_9_1_ < 2; ++lvt_9_1_)
            {
                GlStateManager.depthMask(true);

                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Tessellator lvt_10_1_ = Tessellator.getInstance();
                BufferBuilder lvt_11_1_ = lvt_10_1_.getBuffer();
                lvt_11_1_.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

                for (int i = 0; i < particles.size(); i++)
                {
                    if (i < particles.size())
                    {
                        Particle p = particles.get(i);

                        if (p != null)
                        {
                            p.renderParticle(lvt_11_1_, player, partialTicks, lvt_3_1_, lvt_7_1_, lvt_4_1_, lvt_5_1_, lvt_6_1_);

                        }
                    }
                }

                lvt_10_1_.draw();
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
    }

    public void addParticle(Particle particle)
    {
        particles.add(particle);
    }
}
