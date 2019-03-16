package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.entity.MixerBlockEntity;
import com.raphydaphy.arcanemagic.util.ArcaneMagicUtils;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.lwjgl.opengl.GL11;

public class MixerRenderer extends BlockEntityRenderer<MixerBlockEntity>
{
	private static Identifier tex = new Identifier(ArcaneMagic.DOMAIN, "textures/block/mixer_tanks.png");
	private static RenderUtils.TextureBounds[] outer = {
			new RenderUtils.TextureBounds(0, 0, 6, 6), // Bottom
			new RenderUtils.TextureBounds(0, 0, 6, 6), // Top
			new RenderUtils.TextureBounds(0, 6, 6, 13), // North
			new RenderUtils.TextureBounds(0, 6, 6, 13), // South
			new RenderUtils.TextureBounds(0, 6, 6, 13), // West
			new RenderUtils.TextureBounds(0, 6, 6, 13)}; // East

	private static RenderUtils.TextureBounds[] inner = {
			new RenderUtils.TextureBounds(11, 0, 16, 5), // Bottom
			new RenderUtils.TextureBounds(11, 0, 16, 5), // Top
			new RenderUtils.TextureBounds(11, 5, 16, 11), // North
			new RenderUtils.TextureBounds(11, 5, 16, 11), // South
			new RenderUtils.TextureBounds(11, 5, 16, 11), // West
			new RenderUtils.TextureBounds(11, 5, 16, 11)}; // East

	public void render(MixerBlockEntity entity, double renderX, double renderY, double renderZ, float partialTicks, int destroyStage)
	{
		super.render(entity, renderX, renderY, renderZ, partialTicks, destroyStage);

		if (entity != null)
		{
			if (entity.bottom)
			{
				ItemStack stack = entity.getInvStack(0);
				float ticks = ArcaneMagicUtils.lerp(entity.ticks - 1, entity.ticks, partialTicks);

				if (!stack.isEmpty())
				{
					GlStateManager.pushMatrix();

					GuiLighting.enable();
					GlStateManager.enableLighting();
					GlStateManager.disableRescaleNormal();
					GlStateManager.translated(renderX + .5, renderY + 0.35 + Math.sin((Math.PI / 180) * (ticks * 4)) / 30, renderZ + .5);
					GlStateManager.rotated(2 * ticks, 0, 1, 0);
					GlStateManager.scaled(0.7, 0.7, 0.7);
					MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Type.GROUND);

					GlStateManager.popMatrix();
				}
			} else
			{
				int water = 0;
				FluidInstance[] fluids = entity.getFluids(Direction.UP);
				if (fluids.length >= 1 && fluids[0] != null && fluids[0].getFluid() == Fluids.WATER)
				{
					water = (int) (fluids[0].getAmount() / (float) MixerBlockEntity.MAX_FLUID * 12);
				}

				GlStateManager.pushMatrix();
				MinecraftClient.getInstance().getTextureManager().bindTexture(tex);
				GlStateManager.enableAlphaTest();
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
				GlStateManager.depthMask(false);
				GlStateManager.disableCull();
				Tessellator tess = Tessellator.getInstance();
				BufferBuilder builder = tess.getBufferBuilder();

				double pixel = 1d / 16d;

				builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_NORMAL);
				RenderUtils.renderBox(builder, renderX + pixel * 2, renderY, renderZ + pixel * 2, renderX + pixel * 14, renderY + pixel * 14, renderZ + pixel * 14, outer, new int[]{1, 1, 1, 1, 1, 1});

				if (water > 0)
				{
					RenderUtils.TextureBounds[] waterTextures = {
							new RenderUtils.TextureBounds(22, 0, 32, 10, 32, 32), // Bottom
							new RenderUtils.TextureBounds(22, 0, 32, 10, 32, 32), // Top
							new RenderUtils.TextureBounds(22, 10, 32, 10 + water, 32, 32), // North
							new RenderUtils.TextureBounds(22, 10, 32, 10 + water, 32, 32), // South
							new RenderUtils.TextureBounds(22, 10, 32, 10 + water, 32, 32), // West
							new RenderUtils.TextureBounds(22, 10, 32, 10 + water, 32, 32)}; // East
					RenderUtils.renderBox(builder, renderX + pixel * 3, renderY + pixel * 1, renderZ + pixel * 3, renderX + pixel * 13, renderY + pixel * (water + 1), renderZ + pixel * 13, 28, 120, 206, waterTextures, new int[]{1, 1, 1, 1, 1, 1});
				}
				tess.draw();

				GlStateManager.popMatrix();
			}
		}
	}
}
