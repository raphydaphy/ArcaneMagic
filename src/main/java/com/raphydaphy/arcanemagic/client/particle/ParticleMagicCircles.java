package com.raphydaphy.arcanemagic.client.particle;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.raphydaphy.arcanemagic.entity.EntityItemFancy;
import com.raphydaphy.arcanemagic.handler.ArcaneMagicSoundHandler;
import com.raphydaphy.arcanemagic.init.ModRegistry;
import com.raphydaphy.arcanemagic.util.GLHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleMagicCircles extends Particle
{
	private EntityPlayer player;
	private double constantRot = 0;
	private double edgeRot = 0;
	private Vec3d centerItemPos;
	private Vec3d circlePos;

	public ParticleMagicCircles(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, EntityPlayer player)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0);
		this.player = player;
		this.posX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.particleMaxAge = 800;

		circlePos = new Vec3d(this.posX + 0.5, this.posY, this.posZ + 0.5);
		centerItemPos = circlePos;
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
		
		if (rand.nextInt(30) == 1)
		{
			world.playSound(posX, posY, posZ, ArcaneMagicSoundHandler.randomCreakSound(),
					SoundCategory.MASTER, 1f, 1f, false);
		}
		this.constantRot += 1;
		
		if (constantRot >= 360)
		{
			if (edgeRot < 90)
			{
				this.edgeRot += 0.5;
				if (edgeRot == 89)
				{
					world.spawnEntity(new EntityItemFancy(world, circlePos.x, circlePos.y + 0.9 + (edgeRot == 0 ? 0 : (edgeRot / 90)),circlePos.z, new ItemStack(ModRegistry.NOTEBOOK), 270));
				}
			}
		}

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setExpired();
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
		GlStateManager.rotate((float)constantRot, 0, 1, 0);

		// Main Circle
		GLHelper.drawCircle(1.28, 1.24, 0, 2.8, 0, color);
		
		// Middle Triangle #1
		GLHelper.drawTriangle(2.15, 0, 0, -1.23, color);
		
		// Rotated middle triangle
		GlStateManager.pushMatrix();
		GlStateManager.rotate(60, 0, 1, 0);
		GLHelper.drawTriangle(2.15, 0, 0, -1.23, color);
		GlStateManager.popMatrix();
		
		// Middle semi-circle #1
		GlStateManager.pushMatrix();
		GlStateManager.translate(-1, 2.8, -0.55);
		GlStateManager.rotate(62, 0, 1 ,0);
		GLHelper.drawCircle(0.5, 0.48, 0, 0, 0, color, 180);
		GlStateManager.popMatrix();
		
		// Middle semi-circle #2
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.97, 2.8, -0.6);
		GlStateManager.rotate(-58, 0, 1 ,0);
		GLHelper.drawCircle(0.5, 0.48, 0, 0, 0, color, 180);
		GlStateManager.popMatrix();
		
		// Middle semi-circle #3
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 2.8, 1.14);
		GlStateManager.rotate(-180, 0, 1 ,0);
		GLHelper.drawCircle(0.5, 0.48, 0, 0, 0, color, 180);
		GlStateManager.popMatrix();
		
		// Middle center circle
		GLHelper.drawCircle(0.64, 0.62, 0, 2.8, 0, color);
		
		color = Color.cyan;
		
		// Outer circles
		GlStateManager.pushMatrix();
		GlStateManager.translate(1.9, 2.8 + (edgeRot == 0 ? 0 : (edgeRot / 90)), 1.1);
		GlStateManager.rotate(90, 0, 1, 0);
		GlStateManager.rotate(-30, 0, 1, 0);
		GlStateManager.rotate(-(float)edgeRot, 1, 0, 0);
		GLHelper.drawCircle(0.6, 0.58, 0, 0, 0, color);
		GLHelper.drawCircle(0.64, 0.58, 0, 0, 0, color);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(-1.9, 2.8 + (edgeRot == 0 ? 0 : (edgeRot / 90)), 1.1);
		GlStateManager.rotate(90, 0, 1, 0);
		GlStateManager.rotate(30 + 180, 0, 1, 0);
		GlStateManager.rotate(-(float)edgeRot, 1, 0, 0);
		GLHelper.drawCircle(0.6, 0.58, 0, 0, 0, color);
		GLHelper.drawCircle(0.64, 0.58, 0, 0, 0, color);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 2.8 + (edgeRot == 0 ? 0 : (edgeRot / 90)), -2.2);
		GlStateManager.rotate(90, 0, 1, 0);
		GlStateManager.rotate(90, 0, 1, 0);
		GlStateManager.rotate(-(float)edgeRot, 1, 0, 0);
		GLHelper.drawCircle(0.6, 0.58, 0, 0, 0, color);
		GLHelper.drawCircle(0.64, 0.58, 0, 0, 0, color);
		GlStateManager.popMatrix();
		
		RenderHelper.enableGUIStandardItemLighting();

		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
		
		// Center Item
		if (edgeRot < 89)
		{
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90, 1, 0, 0);
			GlStateManager.translate(0, 0, -2.8  + (edgeRot == 0 ? 0 : -(edgeRot / 90)));
			GlStateManager.rotate(-(float)constantRot * 2, 0, 0, 1);
			
			Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModRegistry.ANCIENT_PARCHMENT),
					ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}
		// Outer Item #1
		GlStateManager.pushMatrix();
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.translate(1.9, 1.1, -2.8 + (edgeRot == 0 ? 0 : -(edgeRot / 90)));
		float angle1 = ((float)Math.atan2(-1.9, 1.1) * (180f / (float)Math.PI)) + 180;
		
		GlStateManager.rotate(angle1, 0, 0, 1);
		GlStateManager.rotate((float)edgeRot, 1, 0, 0);
		Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.BOOK),
				ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
				
		// Outer Item #2
		GlStateManager.pushMatrix();
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.translate(-1.9, 1.1, -2.8 + (edgeRot == 0 ? 0 : -(edgeRot / 90)));
		float angle2 = ((float)Math.atan2(1.9, 1.1) * (180f / (float)Math.PI)) + 180;
		GlStateManager.rotate(angle2, 0, 0, 1);
		GlStateManager.rotate((float)edgeRot, 1, 0, 0);
		Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.BOOK),
				ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
		
		// Outer Item #3
		GlStateManager.pushMatrix();
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.translate(0, -2.2, -2.8 + (edgeRot == 0 ? 0 : -(edgeRot / 90)));
		float angle3 = ((float)Math.atan2(0, 2.2) * (180f / (float)Math.PI));
		GlStateManager.rotate(angle3, 0, 0, 1);
		GlStateManager.rotate((float)edgeRot, 1, 0, 0);
		Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.BOOK),
				ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();

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
