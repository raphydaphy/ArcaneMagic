package com.raphydaphy.arcanemagic.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_295;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
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

	public void render(PlayerEntity fakePlayer, float partialTicks)
	{
		GlStateManager.pushMatrix();

		float rotationX = class_295.method_1375();
		float rotationZ = class_295.method_1380();
		float rotationYZ = class_295.method_1381();
		float rotationXY = class_295.method_1378();
		float rotationXZ = class_295.method_1377();
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null)
		{
			Particle.cameraX = player.prevX + (player.x - player.prevX) * partialTicks;
			Particle.cameraY = player.prevY + (player.y - player.prevY) * partialTicks;
			Particle.cameraZ = player.prevZ + (player.z - player.prevZ) * partialTicks;
			Particle.cameraRotation = ArcaneMagicUtils.interpPlayerLook(player, partialTicks);

			GlStateManager.enableAlphaTest(); // enableAlpha
			GlStateManager.enableBlend();
			GlStateManager.alphaFunc(516, 0.003921569F);
			GlStateManager.disableCull();

			GlStateManager.depthMask(false);

			MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX); // LOCATION_BLOCKS_TEXTURE
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder buffer = tess.getBufferBuilder();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR_UV_LMAP); //PARTICLE_POSITION_TEX_COLOR_LMAP
			for (Particle particle3 : particles)
			{
				if (particle3 instanceof ArcaneMagicParticle)
				{
					if (!((ArcaneMagicParticle) particle3).isAdditive())
					{
						particle3.buildGeometry(buffer, player, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
					}
				}
			}
			tess.draw();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_LMAP); //PARTICLE_POSITION_TEX_COLOR_LMAP
			for (Particle particle2 : particles)
			{
				if (particle2 != null)
				{
					if (((ArcaneMagicParticle) particle2).isAdditive())
					{
						particle2.buildGeometry(buffer, player, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
					}
				}
			}
			tess.draw();

			GlStateManager.disableDepthTest(); // disableDepth
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_LMAP);  //PARTICLE_POSITION_TEX_COLOR_LMAP
			for (Particle particle1 : particles)
			{
				if (particle1 instanceof ArcaneMagicParticle)
				{
					if (!((ArcaneMagicParticle) particle1).isAdditive() && ((ArcaneMagicParticle) particle1).renderThroughBlocks())
					{
						particle1.buildGeometry(buffer, player, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
					}
				}
			}
			tess.draw();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_LMAP);  //PARTICLE_POSITION_TEX_COLOR_LMAP
			for (Particle particle : particles)
			{
				if (particle != null)
				{
					if (((ArcaneMagicParticle) particle).isAdditive() && ((ArcaneMagicParticle) particle).renderThroughBlocks())
					{
						particle.buildGeometry(buffer, player, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
					}
				}
			}
			tess.draw();
			GlStateManager.enableDepthTest(); // enableDepth

			GlStateManager.enableCull();
			GlStateManager.depthMask(true);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableBlend();
			GlStateManager.alphaFunc(516, 0.1F);
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
