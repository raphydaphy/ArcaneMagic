package com.raphydaphy.arcanemagic.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4184;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

// Derived from Elucent's particle system in Embers
// Source (no longer available)
@Environment(EnvType.CLIENT)
public class ParticleRenderer
{
	public static ParticleRenderer INSTANCE = new ParticleRenderer();
	private List<Particle> particles = new ArrayList<>();

	public void update()
	{
		if (!MinecraftClient.getInstance().isPaused())
		{
			boolean doRemove;
			for (int i = 0; i < particles.size(); i++)
			{
				doRemove = true;
				if (particles.get(i) != null)
				{
					if (particles.get(i) instanceof ArcaneMagicParticle)
					{
						if (((ArcaneMagicParticle) particles.get(i)).alive())
						{
							particles.get(i).update();
							doRemove = false;
						}
					}
				}
				if (doRemove)
				{
					particles.remove(i);
				}
			}
		}
	}

	public void render(float partialTicks, class_4184 camera)
	{
		GlStateManager.pushMatrix();

		float rotationX = MathHelper.cos(camera.method_19330() * 0.017453292F);
		float rotationZ = MathHelper.sin(camera.method_19330() * 0.017453292F);
		float rotationYZ = -rotationZ * MathHelper.sin(camera.method_19329() * 0.017453292F);
		float rotationXY = rotationX * MathHelper.sin(camera.method_19329() * 0.017453292F);
		float rotationXZ = MathHelper.cos(camera.method_19329() * 0.017453292F);

		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null)
		{
			Particle.cameraX = camera.method_19326().x;
			Particle.cameraY = camera.method_19326().y;
			Particle.cameraZ = camera.method_19326().z;

			GlStateManager.enableAlphaTest();
			GlStateManager.enableBlend();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
			GlStateManager.disableCull();

			GlStateManager.depthMask(false);

			MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);

			Tessellator tess = Tessellator.getInstance();
			BufferBuilder buffer = tess.getBufferBuilder();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_LMAP);
			for (Particle particle : particles)
			{
				if (particle instanceof ArcaneMagicParticle)
				{
					if (!((ArcaneMagicParticle) particle).isAdditive())
					{
						particle.buildGeometry(buffer, camera, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
					}
				}
			}
			tess.draw();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_LMAP);
			for (Particle particle : particles)
			{
				if (particle != null)
				{
					if (((ArcaneMagicParticle) particle).isAdditive())
					{
						particle.buildGeometry(buffer, camera, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
					}
				}
			}
			tess.draw();

			GlStateManager.disableDepthTest(); // disableDepth
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_LMAP);
			for (Particle particle : particles)
			{
				if (particle instanceof ArcaneMagicParticle)
				{
					if (!((ArcaneMagicParticle) particle).isAdditive() && ((ArcaneMagicParticle) particle).renderThroughBlocks())
					{
						particle.buildGeometry(buffer, camera, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
					}
				}
			}
			tess.draw();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_LMAP);
			for (Particle particle : particles)
			{
				if (particle != null)
				{
					if (((ArcaneMagicParticle) particle).isAdditive() && ((ArcaneMagicParticle) particle).renderThroughBlocks())
					{
						particle.buildGeometry(buffer, camera, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
					}
				}
			}
			tess.draw();
			GlStateManager.enableDepthTest();

			GlStateManager.enableCull();
			GlStateManager.depthMask(true);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableBlend();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		}

		GlStateManager.popMatrix();
	}

	public void add(Particle particle)
	{
		if (!MinecraftClient.getInstance().isPaused())
		{
			particles.add(particle);
		}
	}
}
