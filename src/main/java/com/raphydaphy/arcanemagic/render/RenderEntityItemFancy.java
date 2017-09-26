package com.raphydaphy.arcanemagic.render;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.raphydaphy.arcanemagic.entity.EntityItemFancy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityItemFancy extends Render<EntityItemFancy>
{

	private final RenderEntityItem renderItem;

	public RenderEntityItemFancy(RenderManager renderManager)
	{
		super(renderManager);
		renderItem = new RenderEntityItem(renderManager, Minecraft.getMinecraft().getRenderItem());
	}

	@Override
	public void doRender(EntityItemFancy entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		renderFancyBeams(x, y + 0.5, z, Color.MAGENTA, entity.world.getSeed(), entity.getAge(), 16, 0.7f, 10, 5);
		GL11.glPushMatrix();
		ItemStack stack = entity.getItem();
		if (!stack.isEmpty())
		{
			EntityItem ei = new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, stack);
			ei.age = entity.getAge();
			ei.hoverStart = entity.hoverStart;

			renderItem.doRender(ei, x, y, z, entityYaw, partialTicks);
		}
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityItemFancy entity)
	{
		return null;
	}

	public static class Factory implements IRenderFactory<EntityItemFancy>
	{

		@Override
		public Render<? super EntityItemFancy> createRenderFor(RenderManager manager)
		{
			return new RenderEntityItemFancy(manager);
		}

	}

	public static void renderFancyBeams(double x, double y, double z, Color effectColor, long seed, long continuousTick,
			int dstJump, float scale, int countFancy, int countNormal)
	{
		Random rand = new Random(seed);
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);

		int fancy_count = !FMLClientHandler.instance().getClient().gameSettings.fancyGraphics ? countNormal
				: countFancy;

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder vb = tes.getBuffer();

		RenderHelper.disableStandardItemLighting();
		float rotateSpeed = continuousTick / 1000.0F;
		float beamSize = 0.4F;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDepthMask(false);
		GL11.glPushMatrix();
		for (int i = 0; i < fancy_count; i++)
		{
			GL11.glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F + rotateSpeed * 360.0F, 0.0F, 0.0F, 1.0F);
			vb.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
			float fa = rand.nextFloat() * 20.0F + 5.0F + beamSize * 10.0F;
			float f4 = rand.nextFloat() * 2.0F + 1.0F + beamSize * 2.0F;
			fa /= 30.0F / (Math.min(dstJump, 10 * scale) / 10.0F);
			f4 /= 30.0F / (Math.min(dstJump, 10 * scale) / 10.0F);
			vb.pos(0, 0, 0).color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(),
					(int) (255.0F * (1.0F - beamSize))).endVertex();
			vb.pos(-0.7D * f4, fa, -0.5F * f4)
					.color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), 0).endVertex();
			vb.pos(0.7D * f4, fa, -0.5F * f4)
					.color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), 0).endVertex();
			vb.pos(0.0D, fa, 1.0F * f4).color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), 0)
					.endVertex();
			vb.pos(-0.7D * f4, fa, -0.5F * f4)
					.color(effectColor.getRed(), effectColor.getGreen(), effectColor.getBlue(), 0).endVertex();
			tes.draw();
		}
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		RenderHelper.enableStandardItemLighting();

		GL11.glPopMatrix();
	}

}