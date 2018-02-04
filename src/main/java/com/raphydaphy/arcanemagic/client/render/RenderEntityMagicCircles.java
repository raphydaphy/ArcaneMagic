package com.raphydaphy.arcanemagic.client.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.raphydaphy.arcanemagic.api.anima.Anima;
import com.raphydaphy.arcanemagic.common.entity.EntityMagicCircles;
import com.raphydaphy.arcanemagic.common.init.ModRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderEntityMagicCircles extends Render<EntityMagicCircles> {

	public RenderEntityMagicCircles(RenderManager renderManager) {
		super(renderManager);

	}

	@Override
	public void doRender(EntityMagicCircles entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();

		GlStateManager.translate(x, y + 1.1, z);
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

		boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
		GlStateManager.depthMask(false);

		GlStateManager.pushMatrix();

		RenderHelper.disableStandardItemLighting();

		Color color = Color.blue;

		GlStateManager.translate(entity.circlePos.x, entity.circlePos.y + 0, entity.circlePos.z);
		GlStateManager.rotate((float) entity.constantRot, 0, 1, 0);

		// Main Circle
		GLHelper.drawCircle(1.28, 1.24, 0, 0, 0, color);

		// Middle Triangle #1
		GLHelper.drawTriangle(2.15, 0, 0, -1.23, color);

		// Rotated middle triangle
		GlStateManager.pushMatrix();
		GlStateManager.rotate(60, 0, 1, 0);
		GLHelper.drawTriangle(2.15, 0, 0, -1.23, color);
		GlStateManager.popMatrix();

		// Middle semi-circle #1
		GlStateManager.pushMatrix();
		GlStateManager.translate(-1, 0, -0.55);
		GlStateManager.rotate(62, 0, 1, 0);
		GLHelper.drawCircle(0.5, 0.48, 0, 0, 0, color, 180);
		GlStateManager.popMatrix();

		// Middle semi-circle #2
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.97, 0, -0.6);
		GlStateManager.rotate(-58, 0, 1, 0);
		GLHelper.drawCircle(0.5, 0.48, 0, 0, 0, color, 180);
		GlStateManager.popMatrix();

		// Middle semi-circle #3
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, 1.14);
		GlStateManager.rotate(-180, 0, 1, 0);
		GLHelper.drawCircle(0.5, 0.48, 0, 0, 0, color, 180);
		GlStateManager.popMatrix();

		// Middle center circle
		GLHelper.drawCircle(0.64, 0.62, 0, 0, 0, color);

		color = Color.cyan;

		// Outer circles
		GlStateManager.pushMatrix();
		GlStateManager.translate(1.9, (entity.edgeRot == 0 ? 0 : (entity.edgeRot / 90)), 1.1);
		GlStateManager.rotate(90, 0, 1, 0);
		GlStateManager.rotate(-30, 0, 1, 0);
		GlStateManager.rotate(-(float) entity.edgeRot, 1, 0, 0);
		GLHelper.drawCircle(0.6, 0.58, 0, 0, 0, color);
		GLHelper.drawCircle(0.64, 0.58, 0, 0, 0, color);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(-1.9, (entity.edgeRot == 0 ? 0 : (entity.edgeRot / 90)), 1.1);
		GlStateManager.rotate(90, 0, 1, 0);
		GlStateManager.rotate(30 + 180, 0, 1, 0);
		GlStateManager.rotate(-(float) entity.edgeRot, 1, 0, 0);
		GLHelper.drawCircle(0.6, 0.58, 0, 0, 0, color);
		GLHelper.drawCircle(0.64, 0.58, 0, 0, 0, color);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, (entity.edgeRot == 0 ? 0 : (entity.edgeRot / 90)), -2.2);
		GlStateManager.rotate(90, 0, 1, 0);
		GlStateManager.rotate(90, 0, 1, 0);
		GlStateManager.rotate(-(float) entity.edgeRot, 1, 0, 0);
		GLHelper.drawCircle(0.6, 0.58, 0, 0, 0, color);
		GLHelper.drawCircle(0.64, 0.58, 0, 0, 0, color);
		GlStateManager.popMatrix();

		RenderHelper.enableGUIStandardItemLighting();

		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();

		// Center Item
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.translate(0, (entity.edgeRot == 0 ? 0 : (entity.edgeRot / 90)), 0);
		if (entity.edgeRot >= 89) {
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();
			GLHelper.renderFancyBeams(0, 0 + 0.1, 0,
					Anima.getFromBiome(entity.world.getBiome(new BlockPos(x, y, z))).getColor(), entity.world.getSeed(),
					4748342, 16, 0.7f, 30, 10);
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		}
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GLHelper.renderItemWithTransform(entity.world, new ItemStack(ModRegistry.ANCIENT_PARCHMENT),
				ItemCameraTransforms.TransformType.GROUND);
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();

		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		if (entity.hasBook) {
			// Outer Item #1
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();
			GlStateManager.rotate(90, 1, 0, 0);
			GlStateManager.translate(1.9, 1.1, (entity.edgeRot == 0 ? 0 : -(entity.edgeRot / 90)));
			float angle1 = ((float) Math.atan2(-1.9, 1.1) * (180f / (float) Math.PI)) + 180;

			GlStateManager.rotate(angle1, 0, 0, 1);
			GlStateManager.rotate((float) entity.edgeRot, 1, 0, 0);
			GlStateManager.rotate(90, 0, 0, 1);

			Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.BOOK),
					ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();

			// Outer Item #2
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90, 1, 0, 0);
			GlStateManager.translate(-1.9, 1.1, (entity.edgeRot == 0 ? 0 : -(entity.edgeRot / 90)));
			float angle2 = ((float) Math.atan2(1.9, 1.1) * (180f / (float) Math.PI)) + 180;
			GlStateManager.rotate(angle2, 0, 0, 1);
			GlStateManager.rotate((float) entity.edgeRot, 1, 0, 0);
			GlStateManager.rotate(90, 0, 0, 1);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.BOOK),
					ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();

			// Outer Item #3
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90, 1, 0, 0);
			GlStateManager.translate(0, -2.2, (entity.edgeRot == 0 ? 0 : -(entity.edgeRot / 90)));
			float angle3 = ((float) Math.atan2(0, 2.2) * (180f / (float) Math.PI));
			GlStateManager.rotate(angle3, 0, 0, 1);
			GlStateManager.rotate((float) entity.edgeRot, 1, 0, 0);
			GlStateManager.rotate(90, 0, 0, 1);
			Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.BOOK),
					ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}

		if (lighting) {
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

	public static class Factory implements IRenderFactory<EntityMagicCircles> {

		@Override
		public Render<? super EntityMagicCircles> createRenderFor(RenderManager manager) {
			return new RenderEntityMagicCircles(manager);
		}

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMagicCircles entity) {
		return null;
	}
}