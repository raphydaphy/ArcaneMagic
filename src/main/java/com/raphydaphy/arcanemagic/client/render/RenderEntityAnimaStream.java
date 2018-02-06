package com.raphydaphy.arcanemagic.client.render;

import org.lwjgl.opengl.GL11;

import com.raphydaphy.arcanemagic.common.entity.EntityAnimaStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityAnimaStream extends Render<EntityAnimaStream>
{

	public ResourceLocation texture = new ResourceLocation("arcanemagic:misc/plus");

	protected RenderEntityAnimaStream(RenderManager renderManager)
	{
		super(renderManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAnimaStream entity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doRender(EntityAnimaStream entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		/*
		TextureAtlasSprite particleTexture = Minecraft.getMinecraft().getTextureMapBlocks()
				.getAtlasSprite(texture.toString());

		EntityPlayerSP player = Minecraft.getMinecraft().player;
		double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
		double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
		double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
		
		float rotX = ActiveRenderInfo.getRotationX();
		float rotZ = ActiveRenderInfo.getRotationZ();
		float rotYZ = ActiveRenderInfo.getRotationYZ();
		float rotXY = ActiveRenderInfo.getRotationXY();
		float rotXZ = ActiveRenderInfo.getRotationXZ();
		
		
		Vec3d cameraViewDir = player.getLook(partialTicks);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.alphaFunc(516, 0.003921569F);
		GlStateManager.disableCull();

		GlStateManager.depthMask(false);

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buffer = tess.getBuffer();

		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

		float f4 = 0.1F * entity.getScale();

		float f = particleTexture.getMinU();
		float f1 = particleTexture.getMaxU();
		float f2 = particleTexture.getMinV();
		float f3 = particleTexture.getMaxV();

		float f5 = (float) (entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks - interpPosX);
		float f6 = (float) (entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks - interpPosY);
		float f7 = (float) (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks - interpPosZ);
		int i = 255; // render brightness
		int j = i >> 16 & 65535;
		int k = i & 65535;
		Vec3d[] avec3d = new Vec3d[] {
				new Vec3d((double) (-rotX * f4 - rotXY * f4), (double) (-rotZ * f4),
						(double) (-rotYZ * f4 - rotXZ * f4)),
				new Vec3d((double) (-rotX * f4 + rotXY * f4), (double) (rotZ * f4),
						(double) (-rotYZ * f4 + rotXZ * f4)),
				new Vec3d((double) (rotX * f4 + rotXY * f4), (double) (rotZ * f4),
						(double) (rotYZ * f4 + rotXZ * f4)),
				new Vec3d((double) (rotX * f4 - rotXY * f4), (double) (-rotZ * f4),
						(double) (rotYZ * f4 - rotXZ * f4)) };

		if (entity.getAngle() != 0.0F)
		{
			float f8 = entity.getAngle() + (entity.getAngle() - entity.getPrevAngle()) * partialTicks;
			float f9 = MathHelper.cos(f8 * 0.5F);
			float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
			float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
			float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
			Vec3d vec3d = new Vec3d((double) f10, (double) f11, (double) f12);

			for (int l = 0; l < 4; ++l)
			{
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d))
						.add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d)))
						.add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
			}
		}

		buffer.pos((double) f5 + avec3d[0].x, (double) f6 + avec3d[0].y, (double) f7 + avec3d[0].z)
				.tex((double) f1, (double) f3).color(entity.getColorR(), entity.getColorG(), entity.getColorB(), entity.getAlpha())
				.lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[1].x, (double) f6 + avec3d[1].y, (double) f7 + avec3d[1].z)
				.tex((double) f1, (double) f2).color(entity.getColorR(), entity.getColorG(), entity.getColorB(), entity.getAlpha())
				.lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[2].x, (double) f6 + avec3d[2].y, (double) f7 + avec3d[2].z)
				.tex((double) f, (double) f2).color(entity.getColorR(), entity.getColorG(), entity.getColorB(), entity.getAlpha())
				.lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[3].x, (double) f6 + avec3d[3].y, (double) f7 + avec3d[3].z)
				.tex((double) f, (double) f3).color(entity.getColorR(), entity.getColorG(), entity.getColorB(), entity.getAlpha())
				.lightmap(j, k).endVertex();

		tess.draw();

		GlStateManager.enableCull();
		GlStateManager.depthMask(true);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		GlStateManager.alphaFunc(516, 0.1F);*/
	}

	public static class Factory implements IRenderFactory<EntityAnimaStream>
	{

		@Override
		public Render<? super EntityAnimaStream> createRenderFor(RenderManager manager)
		{
			return new RenderEntityAnimaStream(manager);
		}

	}

}
