package com.raphydaphy.arcanemagic.client.particle;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.util.GLHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleMagicCircles extends Particle
{
	private EntityPlayer player;
	private Vec3d circlePos;
	private Vec3d circleRot;

	public ParticleMagicCircles(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, EntityPlayer player)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0);
		this.player = player;
		this.posX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.particleMaxAge = 500;

		circlePos = new Vec3d(this.posX + 0.5, this.posY, this.posZ + 0.5);
		circleRot = new Vec3d(0, 0, 0);
	}

	@Override
	public int getFXLayer()
	{
		return 3;
	}

	@Override
	public void onUpdate()
	{
		motionX = 0;
		motionY = 0;
		motionZ = 0;

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		this.circleRot = new Vec3d(circleRot.x, circleRot.y + 1, circleRot.z);

		if (this.particleAge++ >= this.particleMaxAge)
		{
			//this.setExpired();
		}

		this.move(motionX, motionY, motionZ);
	}

	@Override
	public void renderParticle(BufferBuilder vb, Entity entity, float partialTicks, float rotationX, float rotationZ,
			float rotationYZ, float rotationXY, float rotationXZ)
	{
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
		GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

		boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
		GlStateManager.depthMask(false);

		GlStateManager.pushMatrix();

		GL11.glLineWidth(10);
		Tessellator tes = Tessellator.getInstance();

		RenderHelper.disableStandardItemLighting();

		Color color = Color.blue;

		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();

		GlStateManager.translate(circlePos.x, circlePos.y + 0, circlePos.z);
		GlStateManager.rotate((float)circleRot.y, 0, 1, 0);

		GLHelper.drawCircle(1.25, 1.235, 0, 0, 0, color);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(-1.14, 0, 0);
		GlStateManager.rotate(90, 0, 1 ,0);
		GLHelper.drawCircle(0.5, 0.48, 0, 0, 0, color, 180);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(1.14, 0, 0);
		GlStateManager.rotate(-90, 0, 1 ,0);
		GLHelper.drawCircle(0.5, 0.48, 0, 0, 0, color, 180);
		GlStateManager.popMatrix();

		RenderHelper.enableGUIStandardItemLighting();

		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.translate(0, 0, -2.8);
		Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.BOOK),
				ItemCameraTransforms.TransformType.NONE);

		if (lighting)
		{
			GL11.glEnable(GL11.GL_LIGHTING);
		}

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
}
