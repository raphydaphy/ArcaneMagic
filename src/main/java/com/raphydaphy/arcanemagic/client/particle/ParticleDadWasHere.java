package com.raphydaphy.arcanemagic.client.particle;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleDadWasHere extends Particle
{

	final BlockPos original;
	double factor = 1;
	boolean first = true;

	public ParticleDadWasHere(BlockPos pos, World world)
	{
		super(world, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
		this.setMaxAge(300);
		original = pos;
	}

	int color = getRandomColor(Minecraft.getMinecraft().world.rand);
	int color2 = getRandomColor(Minecraft.getMinecraft().world.rand);

	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX,
			float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{

		EntityPlayer player = Minecraft.getMinecraft().player;

		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.translate(-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).x,
				-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).y,
				-player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()).z);
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.enableCull();
		GlStateManager.disableAlpha();
		// pre-alpha
		GlStateManager.blendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
		boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
		GlStateManager.depthMask(false);

		GlStateManager.pushMatrix();
		Tessellator tes = Tessellator.getInstance();
		RenderHelper.disableStandardItemLighting();

		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = (color >> 0) & 0xFF;
		int r2 = (color2 >> 16) & 0xFF;
		int g2 = (color2 >> 8) & 0xFF;
		int b2 = (color2 >> 0) & 0xFF;
		double d = Math.abs(factor);
		buffer.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
		for (float i = 0; i < 1; i += 0.1)
			buffer.pos(d * .5 + this.posX * (i * d) * (rand.nextBoolean() ? 1 : -1),
					d * .5 + this.posY * (i * d) * (rand.nextBoolean() ? 1 : -1),
					d * .5 + this.posZ * (i * d) * (rand.nextBoolean() ? 1 : -1)).color(r2, g2, b2, 1).endVertex();
		tes.draw();

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(posX, posY, posZ).color(r, g, b, 1).endVertex();
		buffer.pos(posX + d, posY, posZ).color(r, g, b, 0).endVertex();
		buffer.pos(posX + d, posY, posZ + d).color(r, g, b, 0).endVertex();
		buffer.pos(posX, posY, posZ + d).color(r, g, b, 0).endVertex();
		tes.draw();

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(posX, posY + d, posZ).color(r, g, b, 1).endVertex();
		buffer.pos(posX + d, posY + d, posZ).color(r, g, b, 0).endVertex();
		buffer.pos(posX + d, posY + d, posZ + d).color(r, g, b, 0).endVertex();
		buffer.pos(posX, posY + d, posZ + d).color(r, g, b, 0).endVertex();
		tes.draw();

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(posX, posY, posZ + d).color(r, g, b, 1).endVertex();
		buffer.pos(posX + d, posY, posZ + d).color(r, g, b, 0).endVertex();
		buffer.pos(posX + d, posY + d, posZ + d).color(r, g, b, 0).endVertex();
		buffer.pos(posX, posY + d, posZ + d).color(r, g, b, 0).endVertex();
		tes.draw();

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(posX + d, posY, posZ).color(r, g, b, 1).endVertex();
		buffer.pos(posX + d, posY + d, posZ).color(r, g, b, 0).endVertex();
		buffer.pos(posX + d, posY + d, posZ + d).color(r, g, b, 0).endVertex();
		buffer.pos(posX + d, posY, posZ + d).color(r, g, b, 0).endVertex();
		tes.draw();

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(posX, posY, posZ).color(r, g, b, 1).endVertex();
		buffer.pos(posX, posY + d, posZ).color(r, g, b, 0).endVertex();
		buffer.pos(posX, posY + d, posZ + d).color(r, g, b, 0).endVertex();
		buffer.pos(posX, posY, posZ + d).color(r, g, b, 0).endVertex();
		tes.draw();

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(posX, posY, posZ).color(r, g, b, 1).endVertex();
		buffer.pos(posX, posY + d, posZ).color(r, g, b, 0).endVertex();
		buffer.pos(posX + d, posY + d, posZ).color(r, g, b, 0).endVertex();
		buffer.pos(posX + d, posY, posZ).color(r, g, b, 0).endVertex();
		tes.draw();

		if (lighting)
			GL11.glEnable(GL11.GL_LIGHTING);

		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();

		RenderHelper.enableStandardItemLighting();

		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}

	public int getFXLayer()
	{
		return 3;
	}

	private double destX = 0;
	private double destY = 0;
	private double destZ = 0;

	public void onUpdate()
	{
		Random rand = Minecraft.getMinecraft().world.rand;

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		double x = original.getX() - posX - 0.5 + destX;
		double y = original.getY() - posY + 0.5 + destY;
		double z = original.getZ() - posZ - 0.5 + destZ;

		if (Math.abs(x) <= .05D)
			destX = rand.nextInt(10) * (rand.nextBoolean() ? 1 : -1);
		if (Math.abs(y) <= .05D)
			destY = rand.nextInt(10) * (rand.nextBoolean() ? 1 : -1);
		if (Math.abs(z) <= .05D)
			destZ = rand.nextInt(10) * (rand.nextBoolean() ? 1 : -1);

		if (Math.abs(x) <= .05D)
		{
			color = getRandomColor(Minecraft.getMinecraft().world.rand);
			factor = rand.nextDouble() * 4;
			color2 = getRandomColor(Minecraft.getMinecraft().world.rand);
		}

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setExpired();
		}
		this.move(x * 0.05, y * 0.08, z * 0.05);

	}

	private static int getRandomColor(Random rand)
	{
		int r = rand.nextInt(256);
		int g = rand.nextInt(256);
		int b = rand.nextInt(256);
		int value = ((255 & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
		return value;
	}

	public void setExpired()
	{
		super.setExpired();
	}

}
