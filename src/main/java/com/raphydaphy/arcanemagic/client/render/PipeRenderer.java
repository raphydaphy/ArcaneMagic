package com.raphydaphy.arcanemagic.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.block.entity.PipeBlockEntity;
import com.raphydaphy.arcanemagic.util.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class PipeRenderer extends BlockEntityRenderer<PipeBlockEntity>
{
	private static Identifier tex = new Identifier(ArcaneMagic.DOMAIN, "textures/block/pipe.png");

	public void render(PipeBlockEntity entity, double renderX, double renderY, double renderZ, float partialTicks, int destroyStage)
	{
		super.render(entity, renderX, renderY, renderZ, partialTicks, destroyStage);

		RenderUtils.TextureBounds[] centerBig = {
				new RenderUtils.TextureBounds(7,7,13,13), // Bottom
				new RenderUtils.TextureBounds(7,7,13,13), // Top
				new RenderUtils.TextureBounds(7,7,13,13), // North
				new RenderUtils.TextureBounds(7,7,13,13), // South
				new RenderUtils.TextureBounds(7,7,13,13), // West
				new RenderUtils.TextureBounds(7,7,13,13)}; // East

		RenderUtils.TextureBounds[] up = {
				new RenderUtils.TextureBounds(0, 0, 0, 0), // Bottom
				new RenderUtils.TextureBounds(0, 0, 0, 0), // Top
				new RenderUtils.TextureBounds(0, 4, 4, 12), // North
				new RenderUtils.TextureBounds(0, 4, 4, 12), // South
				new RenderUtils.TextureBounds(0, 4, 4, 12), // West
				new RenderUtils.TextureBounds(0, 4, 4, 12)}; // East

		RenderUtils.TextureBounds[] down = {
				new RenderUtils.TextureBounds(0, 0, 0, 0), // Bottom
				new RenderUtils.TextureBounds(0, 0, 0, 0), // Top
				new RenderUtils.TextureBounds(0, 4, 4, 12), // North
				new RenderUtils.TextureBounds(0, 4, 4, 12), // South
				new RenderUtils.TextureBounds(0, 4, 4, 12), // West
				new RenderUtils.TextureBounds(0, 4, 4, 12)}; // East

		if (entity != null)
		{
			GlStateManager.pushMatrix();
			MinecraftClient.getInstance().getTextureManager().bindTexture(tex);
			GlStateManager.disableCull();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder builder = tess.getBufferBuilder();

			builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR_NORMAL);
			double pixel = 1d / 16d;

			int connections = 0;
			BlockPos centerPos = entity.getPos();
			BlockState upState = getWorld().getBlockState(centerPos.add(0, 1, 0));
			BlockState downState = getWorld().getBlockState(centerPos.add(0, -1, 0));

			if (!upState.isAir())
			{
				connections++;
				RenderUtils.renderBox(builder, renderX + pixel * 6, renderY + pixel * 8, renderZ + pixel * 6, renderX + pixel * 10, renderY + 1, renderZ + pixel * 10, up, new int[]{1, 1, 1, 1, 1, 1});
			}
			if (!downState.isAir())
			{
				connections++;
				RenderUtils.renderBox(builder, renderX + pixel * 6, renderY, renderZ + pixel * 6, renderX + pixel * 10, renderY + pixel * 8, renderZ + pixel * 10, down, new int[]{-1, -1, 1, 1, 1, 1});
			}
			if (connections < 2)
			{
				RenderUtils.renderBox(builder, renderX + pixel * 5, renderY + pixel * 5, renderZ + pixel * 5, renderX + pixel * 11, renderY + pixel * 11, renderZ + pixel * 11, centerBig, new int[]{1, 1, 1, 1, 1, 1});
			} else
			{

			}
			tess.draw();

			GlStateManager.popMatrix();
		}
	}
}
